package edu.udla.integracion.progreso2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.udla.integracion.progreso2.model.CitaRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

@Service
public class CitaErrorLogger {

    private final ObjectMapper objectMapper;
    private final Path errorLogPath;

    public CitaErrorLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.errorLogPath = Path.of("data", "errors", "citas-rechazadas.log");
    }

    public void logRejected(CitaRequest request, String reason) {
        try {
            Files.createDirectories(errorLogPath.getParent());
            String idCita = request == null || request.getIdCita() == null ? "SIN_ID" : request.getIdCita();
            String payload = objectMapper.writeValueAsString(request);
            String line = "%s | idCita=%s | motivo=%s | payload=%s%n"
                    .formatted(LocalDateTime.now(), idCita, reason, payload);
            Files.writeString(
                    errorLogPath,
                    line,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo registrar la cita rechazada", e);
        }
    }
}
