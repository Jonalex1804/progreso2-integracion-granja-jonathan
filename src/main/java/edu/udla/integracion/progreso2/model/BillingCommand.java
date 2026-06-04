package edu.udla.integracion.progreso2.model;

import java.math.BigDecimal;

public record BillingCommand(
        String idCita,
        String paciente,
        String especialidad,
        BigDecimal valor,
        String tipoMensaje
) {
}
