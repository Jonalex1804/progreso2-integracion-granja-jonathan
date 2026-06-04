package edu.udla.integracion.progreso2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.udla.integracion.progreso2.model.CitaRequest;
import edu.udla.integracion.progreso2.service.CitaErrorLogger;
import edu.udla.integracion.progreso2.service.CitaValidationService;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CitaController.class)
@Import(CitaValidationService.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProducerTemplate producerTemplate;

    @MockBean
    private CitaErrorLogger errorLogger;

    @Test
    void validRequestStartsIntegrationFlow() throws Exception {
        CitaRequest request = validRequest();

        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.estado").value("ACEPTADA"))
                .andExpect(jsonPath("$.idCita").value("CITA-1001"))
                .andExpect(jsonPath("$.mensaje").value("Cita recibida e integrada correctamente"));

        verify(producerTemplate).sendBody(eq("direct:procesarCita"), eq(request));
    }

    @Test
    void invalidRequestReturnsControlledErrorAndLogsRejection() throws Exception {
        CitaRequest request = new CitaRequest();
        request.setIdCita("CITA-ERROR");
        request.setValor(new BigDecimal("-1"));

        mockMvc.perform(post("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value("RECHAZADA"))
                .andExpect(jsonPath("$.idCita").value("CITA-ERROR"));

        verify(producerTemplate, never()).sendBody(eq("direct:procesarCita"), eq(request));
        verify(errorLogger).logRejected(eq(request), org.mockito.ArgumentMatchers.anyString());
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
