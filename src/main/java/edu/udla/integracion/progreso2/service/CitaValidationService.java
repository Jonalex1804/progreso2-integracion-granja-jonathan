package edu.udla.integracion.progreso2.service;

import edu.udla.integracion.progreso2.model.CitaRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CitaValidationService {

    public List<String> validate(CitaRequest request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("payload es obligatorio");
            return errors;
        }
        if (isBlank(request.getIdCita())) {
            errors.add("idCita es obligatorio");
        }
        if (isBlank(request.getPaciente())) {
            errors.add("paciente es obligatorio");
        }
        if (isBlank(request.getCorreo())) {
            errors.add("correo es obligatorio");
        }
        if (isBlank(request.getEspecialidad())) {
            errors.add("especialidad es obligatoria");
        }
        if (isBlank(request.getFechaCita())) {
            errors.add("fechaCita es obligatoria");
        }
        if (isBlank(request.getSede())) {
            errors.add("sede es obligatoria");
        }
        if (request.getValor() == null || request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("valor debe ser mayor a 0");
        }

        return errors;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
