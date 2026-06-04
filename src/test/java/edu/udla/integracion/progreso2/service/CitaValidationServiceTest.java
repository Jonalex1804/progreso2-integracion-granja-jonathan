package edu.udla.integracion.progreso2.service;

import edu.udla.integracion.progreso2.model.CitaRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CitaValidationServiceTest {

    private final CitaValidationService service = new CitaValidationService();

    @Test
    void acceptsCompleteAppointmentWithPositiveValue() {
        CitaRequest request = validRequest();

        List<String> errors = service.validate(request);

        assertThat(errors).isEmpty();
    }

    @Test
    void rejectsMissingRequiredFields() {
        CitaRequest request = new CitaRequest();

        List<String> errors = service.validate(request);

        assertThat(errors).contains(
                "idCita es obligatorio",
                "paciente es obligatorio",
                "correo es obligatorio",
                "especialidad es obligatoria",
                "fechaCita es obligatoria",
                "sede es obligatoria",
                "valor debe ser mayor a 0"
        );
    }

    @Test
    void rejectsValueLessThanOrEqualToZero() {
        CitaRequest request = validRequest();
        request.setValor(BigDecimal.ZERO);

        List<String> errors = service.validate(request);

        assertThat(errors).containsExactly("valor debe ser mayor a 0");
    }

    private CitaRequest validRequest() {
        CitaRequest request = new CitaRequest();
        request.setIdCita("CITA-1001");
        request.setPaciente("Ana Torres");
        request.setCorreo("ana.torres@email.com");
        request.setEspecialidad("Cardiologia");
        request.setFechaCita("2026-06-15");
        request.setSede("Centro Norte");
        request.setValor(new BigDecimal("45.50"));
        return request;
    }
}
