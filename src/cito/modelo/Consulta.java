package cito.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Consulta implements Serializable {

    private LocalDate data;
    private LocalTime horario;
    private int codigoMedico;
    private String cpfPaciente;

    public Consulta(LocalDate data, LocalTime horario, int codigoMedico, String cpfPaciente) {
        this.data = data;
        this.horario = horario;
        this.codigoMedico = codigoMedico;
        this.cpfPaciente = cpfPaciente;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public int getCodigoMedico() {
        return codigoMedico;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public boolean ehFutura() {
        return !data.isBefore(LocalDate.now());
    }

    public boolean ehPassada() {
        return data.isBefore(LocalDate.now());
    }

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String toString() {
        return data.format(FMT_DATA) + " " + horario.format(FMT_HORA)
                + " | medico " + codigoMedico + " | paciente " + cpfPaciente;
    }
}
