package cito.servico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cito.modelo.BaseDados;
import cito.modelo.Consulta;
import cito.modelo.Medico;
import cito.modelo.Paciente;

public class Servico {

    private BaseDados base;

    public Servico(BaseDados base) {
        this.base = base;
    }

    public BaseDados getBase() {
        return base;
    }

    public List<Paciente> pacientesDoMedico(int codigo) {
        Medico m = base.buscarMedico(codigo);
        if (m == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(m.getPacientes());
    }

    public List<Consulta> consultasDoMedicoNoPeriodo(int codigo, LocalDate inicio, LocalDate fim) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : base.getConsultas()) {
            if (c.getCodigoMedico() == codigo
                    && !c.getData().isBefore(inicio)
                    && !c.getData().isAfter(fim)) {
                resultado.add(c);
            }
        }
        ordenarPorDataHora(resultado);
        return resultado;
    }

    public List<Paciente> pacientesSemConsultaHaMais(int codigo, int meses) {
        LocalDate limite = LocalDate.now().minusMonths(meses);
        List<Paciente> resultado = new ArrayList<>();
        Medico m = base.buscarMedico(codigo);
        if (m == null) {
            return resultado;
        }
        for (Paciente p : m.getPacientes()) {
            LocalDate ultima = ultimaConsultaPassada(p, codigo);
            if (ultima != null && ultima.isBefore(limite)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    private LocalDate ultimaConsultaPassada(Paciente p, int codigoMedico) {
        LocalDate ultima = null;
        for (Consulta c : p.getConsultas()) {
            if (c.getCodigoMedico() == codigoMedico && c.ehPassada()) {
                if (ultima == null || c.getData().isAfter(ultima)) {
                    ultima = c.getData();
                }
            }
        }
        return ultima;
    }

    public List<Medico> medicosDoPaciente(String cpf) {
        List<Medico> resultado = new ArrayList<>();
        Paciente p = base.buscarPaciente(cpf);
        if (p == null) {
            return resultado;
        }
        for (Consulta c : p.getConsultas()) {
            Medico m = base.buscarMedico(c.getCodigoMedico());
            if (m != null && !resultado.contains(m)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    public List<Consulta> consultasPassadasComMedico(String cpf, int codigoMedico) {
        List<Consulta> resultado = new ArrayList<>();
        Paciente p = base.buscarPaciente(cpf);
        if (p == null) {
            return resultado;
        }
        for (Consulta c : p.getConsultas()) {
            if (c.getCodigoMedico() == codigoMedico && c.ehPassada()) {
                resultado.add(c);
            }
        }
        ordenarPorDataHora(resultado);
        return resultado;
    }

    public List<Consulta> consultasAgendadas(String cpf) {
        List<Consulta> resultado = new ArrayList<>();
        Paciente p = base.buscarPaciente(cpf);
        if (p == null) {
            return resultado;
        }
        for (Consulta c : p.getConsultas()) {
            if (c.ehFutura()) {
                resultado.add(c);
            }
        }
        ordenarPorDataHora(resultado);
        return resultado;
    }

    private void ordenarPorDataHora(List<Consulta> lista) {
        lista.sort(Comparator.comparing(Consulta::getData).thenComparing(Consulta::getHorario));
    }
}
