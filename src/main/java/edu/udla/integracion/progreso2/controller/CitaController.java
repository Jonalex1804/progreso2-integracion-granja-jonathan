package edu.udla.integracion.progreso2.controller;

import edu.udla.integracion.progreso2.model.CitaRequest;
import edu.udla.integracion.progreso2.service.CitaErrorLogger;
import edu.udla.integracion.progreso2.service.CitaValidationService;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final ProducerTemplate producerTemplate;
    private final CitaValidationService validationService;
    private final CitaErrorLogger errorLogger;

    public CitaController(
            ProducerTemplate producerTemplate,
            CitaValidationService validationService,
            CitaErrorLogger errorLogger
    ) {
        this.producerTemplate = producerTemplate;
        this.validationService = validationService;
        this.errorLogger = errorLogger;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarCita(@RequestBody CitaRequest request) {
        List<String> errors = validationService.validate(request);
        if (!errors.isEmpty()) {
            String reason = String.join("; ", errors);
            errorLogger.logRejected(request, reason);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "estado", "RECHAZADA",
                    "idCita", request == null ? "SIN_ID" : request.getIdCita(),
                    "errores", errors
            ));
        }

        producerTemplate.sendBody("direct:procesarCita", request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "estado", "ACEPTADA",
                "idCita", request.getIdCita(),
                "mensaje", "Cita recibida e integrada correctamente"
        ));
    }
}
