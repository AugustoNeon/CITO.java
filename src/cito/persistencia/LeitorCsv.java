package cito.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cito.excecoes.CpfInvalidoException;
import cito.excecoes.DadosInvalidosException;
import cito.modelo.Consulta;
import cito.modelo.Medico;
import cito.modelo.Paciente;

public class LeitorCsv {

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public List<Medico> lerMedicos(String caminho) throws IOException, DadosInvalidosException {
        List<Medico> lista = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String linha;
        int n = 0;
        while ((linha = br.readLine()) != null) {
            n++;
            if (n == 1 || linha.isBlank()) {
                continue;
            }
            String[] campos = linha.split(";");
            if (campos.length < 2) {
                br.close();
                throw new DadosInvalidosException("medicos.csv linha " + n + ": faltam campos");
            }
            int codigo = parseInteiro(campos[0].trim(), n);
            lista.add(new Medico(codigo, campos[1].trim()));
        }
        br.close();
        return lista;
    }

    public Paciente montarPaciente(String linha) throws DadosInvalidosException, CpfInvalidoException {
        String[] campos = linha.split(";");
        if (campos.length < 2) {
            throw new DadosInvalidosException("paciente sem todos os campos: " + linha);
        }
        return new Paciente(campos[0].trim(), campos[1].trim());
    }

    public List<Consulta> lerConsultas(String caminho) throws IOException, DadosInvalidosException {
        List<Consulta> lista = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String linha;
        int n = 0;
        while ((linha = br.readLine()) != null) {
            n++;
            if (n == 1 || linha.isBlank()) {
                continue;
            }
            String[] campos = linha.split(";");
            if (campos.length < 4) {
                br.close();
                throw new DadosInvalidosException("consultas.csv linha " + n + ": faltam campos");
            }
            LocalDate data = parseData(campos[0].trim(), n);
            LocalTime hora = parseHora(campos[1].trim(), n);
            int cod = parseInteiro(campos[2].trim(), n);
            lista.add(new Consulta(data, hora, cod, campos[3].trim()));
        }
        br.close();
        return lista;
    }

    private int parseInteiro(String valor, int linha) throws DadosInvalidosException {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException("linha " + linha + ": numero invalido '" + valor + "'");
        }
    }

    private LocalDate parseData(String valor, int linha) throws DadosInvalidosException {
        try {
            return LocalDate.parse(valor, FMT_DATA);
        } catch (Exception e) {
            throw new DadosInvalidosException("linha " + linha + ": data invalida '" + valor + "'");
        }
    }

    private LocalTime parseHora(String valor, int linha) throws DadosInvalidosException {
        try {
            return LocalTime.parse(valor, FMT_HORA);
        } catch (Exception e) {
            throw new DadosInvalidosException("linha " + linha + ": horario invalido '" + valor + "'");
        }
    }
}
