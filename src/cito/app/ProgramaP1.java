package cito.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cito.excecoes.CpfInvalidoException;
import cito.excecoes.DadosInvalidosException;
import cito.modelo.BaseDados;
import cito.modelo.Paciente;
import cito.persistencia.LeitorCsv;
import cito.persistencia.RepositorioBinario;

public class ProgramaP1 {

    private static final String PASTA_DADOS = "dados";
    private static final String PASTA_RESULTADOS = "resultados";

    public static void main(String[] args) {
        LeitorCsv leitor = new LeitorCsv();
        BaseDados base = new BaseDados();

        try {
            base.getMedicos().addAll(leitor.lerMedicos(PASTA_DADOS + "/medicos.csv"));
            base.getConsultas().addAll(leitor.lerConsultas(PASTA_DADOS + "/consultas.csv"));

            carregarPacientes(leitor, base,
                    PASTA_DADOS + "/pacientes.csv",
                    PASTA_RESULTADOS + "/cadastros_rejeitados.txt");

            base.reindexar();

            new RepositorioBinario().salvar(base, PASTA_DADOS + "/base.dat");

            System.out.println("P1 finalizado com sucesso.");
            System.out.println("Medicos carregados:   " + base.getMedicos().size());
            System.out.println("Pacientes carregados: " + base.getPacientes().size());
            System.out.println("Consultas carregadas: " + base.getConsultas().size());
            System.out.println("Base salva em " + PASTA_DADOS + "/base.dat");
        } catch (IOException e) {
            System.out.println("Erro ao acessar arquivo: " + e.getMessage());
        } catch (DadosInvalidosException e) {
            System.out.println("Arquivo de dados invalido: " + e.getMessage());
        }
    }

    private static void carregarPacientes(LeitorCsv leitor, BaseDados base,
            String caminho, String caminhoRejeitados) throws IOException {

        File pastaRej = new File(caminhoRejeitados).getParentFile();
        if (pastaRej != null) {
            pastaRej.mkdirs();
        }

        BufferedReader br = new BufferedReader(new FileReader(caminho));
        PrintWriter rejeitados = new PrintWriter(new FileWriter(caminhoRejeitados));
        rejeitados.println("Pacientes rejeitados na carga:");

        String linha;
        int n = 0;
        while ((linha = br.readLine()) != null) {
            n++;
            if (n == 1 || linha.isBlank()) {
                continue;
            }
            try {
                Paciente p = leitor.montarPaciente(linha);
                base.getPacientes().add(p);
            } catch (CpfInvalidoException | DadosInvalidosException e) {
                rejeitados.println("linha " + n + ": " + e.getMessage());
                System.out.println("Paciente rejeitado (linha " + n + "): " + e.getMessage());
            }
        }
        br.close();
        rejeitados.close();
    }
}
