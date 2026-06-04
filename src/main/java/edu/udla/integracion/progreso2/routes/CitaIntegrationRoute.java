package edu.udla.integracion.progreso2.routes;

import edu.udla.integracion.progreso2.model.CitaRequest;
import edu.udla.integracion.progreso2.service.CitaCsvFormatter;
import edu.udla.integracion.progreso2.service.CitaErrorLogger;
import edu.udla.integracion.progreso2.service.CitaMessageFactory;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class CitaIntegrationRoute extends RouteBuilder {

    private final CitaMessageFactory messageFactory;
    private final CitaCsvFormatter csvFormatter;
    private final CitaErrorLogger errorLogger;

    public CitaIntegrationRoute(
            CitaMessageFactory messageFactory,
            CitaCsvFormatter csvFormatter,
            CitaErrorLogger errorLogger
    ) {
        this.messageFactory = messageFactory;
        this.csvFormatter = csvFormatter;
        this.errorLogger = errorLogger;
    }

    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .process(this::logProcessingError);

        from("direct:procesarCita")
                .routeId("cita-integration-route")
                .setProperty("citaOriginal", body())
                .process(exchange -> {
                    CitaRequest cita = exchange.getMessage().getBody(CitaRequest.class);
                    exchange.getMessage().setBody(messageFactory.toBillingCommand(cita));
                })
                .marshal().json(JsonLibrary.Jackson)
                .to("spring-rabbitmq:billing.exchange"
                        + "?routingKey=billing.queue"
                        + "&exchangeType=direct"
                        + "&autoDeclare=false")
                .setBody(exchangeProperty("citaOriginal"))
                .process(exchange -> {
                    CitaRequest cita = exchange.getMessage().getBody(CitaRequest.class);
                    exchange.getMessage().setBody(messageFactory.toConfirmedEvent(cita));
                })
                .marshal().json(JsonLibrary.Jackson)
                .to("spring-rabbitmq:appointments.events"
                        + "?exchangeType=fanout"
                        + "&autoDeclare=false")
                .setBody(exchangeProperty("citaOriginal"))
                .process(exchange -> {
                    CitaRequest cita = exchange.getMessage().getBody(CitaRequest.class);
                    exchange.getMessage().setBody(csvFormatter.toCsvLine(cita));
                })
                .to("file:data/outbox?fileName=auditoria-citas.csv&fileExist=Append&appendChars=%0A")
                .log("Cita ${exchangeProperty.citaOriginal.idCita} procesada correctamente");

        from("spring-rabbitmq:appointments.events"
                + "?queues=notifications.queue"
                + "&exchangeType=fanout"
                + "&autoDeclare=false")
                .routeId("notifications-subscriber-route")
                .log("Notificaciones recibio evento: ${body}");

        from("spring-rabbitmq:appointments.events"
                + "?queues=analytics.queue"
                + "&exchangeType=fanout"
                + "&autoDeclare=false")
                .routeId("analytics-subscriber-route")
                .log("Analitica recibio evento: ${body}");
    }

    private void logProcessingError(Exchange exchange) {
        CitaRequest cita = exchange.getProperty("citaOriginal", CitaRequest.class);
        Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        String reason = exception == null ? "Error al procesar el mensaje" : exception.getMessage();
        errorLogger.logRejected(cita, "Error al procesar el mensaje: " + reason);
    }
}
