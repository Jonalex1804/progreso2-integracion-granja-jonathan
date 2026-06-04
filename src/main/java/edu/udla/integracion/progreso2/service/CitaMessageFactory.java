package edu.udla.integracion.progreso2.service;

import edu.udla.integracion.progreso2.model.BillingCommand;
import edu.udla.integracion.progreso2.model.CitaConfirmedEvent;
import edu.udla.integracion.progreso2.model.CitaRequest;
import org.springframework.stereotype.Service;

@Service
public class CitaMessageFactory {

    public BillingCommand toBillingCommand(CitaRequest cita) {
        return new BillingCommand(
                cita.getIdCita(),
                cita.getPaciente(),
                cita.getEspecialidad(),
                cita.getValor(),
                "COMANDO_FACTURAR_CITA"
        );
    }

    public CitaConfirmedEvent toConfirmedEvent(CitaRequest cita) {
        return new CitaConfirmedEvent(
                cita.getIdCita(),
                cita.getPaciente(),
                cita.getCorreo(),
                cita.getEspecialidad(),
                cita.getFechaCita(),
                cita.getSede(),
                "CITA_CONFIRMADA"
        );
    }
}
