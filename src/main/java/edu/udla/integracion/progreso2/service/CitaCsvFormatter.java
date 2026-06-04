package edu.udla.integracion.progreso2.service;

import edu.udla.integracion.progreso2.model.CitaRequest;
import org.springframework.stereotype.Service;

@Service
public class CitaCsvFormatter {

    public String toCsvLine(CitaRequest cita) {
        return String.join(",",
                escape(cita.getIdCita()),
                escape(cita.getPaciente()),
                escape(cita.getCorreo()),
                escape(cita.getEspecialidad()),
                escape(cita.getFechaCita()),
                escape(cita.getSede()),
                cita.getValor().toPlainString()
        );
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
