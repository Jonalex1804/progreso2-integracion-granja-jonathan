package edu.udla.integracion.progreso2.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CitaRequest {

    private String idCita;
    private String paciente;
    private String correo;
    private String especialidad;
    private String fechaCita;
    private String sede;
    private BigDecimal valor;

    public String getIdCita() {
        return idCita;
    }

    public void setIdCita(String idCita) {
        this.idCita = idCita;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaRequest that)) {
            return false;
        }
        return Objects.equals(idCita, that.idCita)
                && Objects.equals(paciente, that.paciente)
                && Objects.equals(correo, that.correo)
                && Objects.equals(especialidad, that.especialidad)
                && Objects.equals(fechaCita, that.fechaCita)
                && Objects.equals(sede, that.sede)
                && Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCita, paciente, correo, especialidad, fechaCita, sede, valor);
    }
}
