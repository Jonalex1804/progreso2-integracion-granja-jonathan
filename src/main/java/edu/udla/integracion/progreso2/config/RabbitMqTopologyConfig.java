package edu.udla.integracion.progreso2.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqTopologyConfig {

    @Bean
    Queue billingQueue() {
        return new Queue("billing.queue", true);
    }

    @Bean
    DirectExchange billingExchange() {
        return new DirectExchange("billing.exchange", true, false);
    }

    @Bean
    Binding billingBinding(Queue billingQueue, DirectExchange billingExchange) {
        return BindingBuilder.bind(billingQueue).to(billingExchange).with("billing.queue");
    }

    @Bean
    Queue notificationsQueue() {
        return new Queue("notifications.queue", true);
    }

    @Bean
    Queue analyticsQueue() {
        return new Queue("analytics.queue", true);
    }

    @Bean
    FanoutExchange appointmentsEventsExchange() {
        return new FanoutExchange("appointments.events", true, false);
    }

    @Bean
    Binding notificationsBinding(Queue notificationsQueue, FanoutExchange appointmentsEventsExchange) {
        return BindingBuilder.bind(notificationsQueue).to(appointmentsEventsExchange);
    }

    @Bean
    Binding analyticsBinding(Queue analyticsQueue, FanoutExchange appointmentsEventsExchange) {
        return BindingBuilder.bind(analyticsQueue).to(appointmentsEventsExchange);
    }
}
