package cito.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseDados implements Serializable {

    private List<Medico> medicos;
    private List<Paciente> pacientes;
    private List<Consulta> consultas;

    public BaseDados() {
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        consultas = new ArrayList<>();
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public Medico buscarMedico(int codigo) {
        for (Medico m : medicos) {
            if (m.getCodigo() == codigo) {
                return m;
            }
        }
        return null;
    }

    public Paciente buscarPaciente(String cpf) {
        String alvo = cpf.replaceAll("[^0-9]", "");
        for (Paciente p : pacientes) {
            if (p.getCpf().replaceAll("[^0-9]", "").equals(alvo)) {
                return p;
            }
        }
        return null;
    }

    public void reindexar() {
        for (Medico m : medicos) {
            m.getPacientes().clear();
        }
        for (Paciente p : pacientes) {
            p.getConsultas().clear();
        }
        for (Consulta c : consultas) {
            Medico m = buscarMedico(c.getCodigoMedico());
            Paciente p = buscarPaciente(c.getCpfPaciente());
            if (m != null && p != null) {
                p.adicionarConsulta(c);
                m.adicionarPaciente(p);
            }
        }
    }
}
