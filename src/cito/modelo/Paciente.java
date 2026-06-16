package cito.modelo;

import java.util.ArrayList;
import java.util.List;

import cito.excecoes.CpfInvalidoException;

public class Paciente extends Pessoa {

    private String cpf;
    private List<Consulta> consultas;

    public Paciente(String cpf, String nome) throws CpfInvalidoException {
        super(nome);
        validarCpf(cpf);
        this.cpf = cpf;
        this.consultas = new ArrayList<>();
    }

    public String getCpf() {
        return cpf;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void adicionarConsulta(Consulta c) {
        consultas.add(c);
    }

    @Override
    public String papel() {
        return "Paciente";
    }

    @Override
    public String identificador() {
        return cpf;
    }

    public static void validarCpf(String cpf) throws CpfInvalidoException {
        if (cpf == null) {
            throw new CpfInvalidoException("CPF nulo");
        }
        String so = cpf.replaceAll("[^0-9]", "");
        if (so.length() != 11) {
            throw new CpfInvalidoException("CPF deve ter 11 digitos: " + cpf);
        }
        boolean todosIguais = true;
        for (int i = 1; i < so.length(); i++) {
            if (so.charAt(i) != so.charAt(0)) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) {
            throw new CpfInvalidoException("CPF invalido: " + cpf);
        }
        if (!digitosConferem(so)) {
            throw new CpfInvalidoException("CPF invalido (digito verificador): " + cpf);
        }
    }

    private static boolean digitosConferem(String so) {
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (so.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int d1 = (resto < 2) ? 0 : 11 - resto;
        if (d1 != (so.charAt(9) - '0')) {
            return false;
        }
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (so.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int d2 = (resto < 2) ? 0 : 11 - resto;
        return d2 == (so.charAt(10) - '0');
    }
}
