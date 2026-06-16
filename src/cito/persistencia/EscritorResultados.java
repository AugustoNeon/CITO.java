package cito.persistencia;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import cito.modelo.Pessoa;

public class EscritorResultados {

    private static final DateTimeFormatter HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String arquivoParcial;
    private int totalPesquisas;

    public EscritorResultados(String arquivoParcial) {
        this.arquivoParcial = arquivoParcial;
        this.totalPesquisas = 0;
    }

    public int getTotalPesquisas() {
        return totalPesquisas;
    }

    public void registrarParcial(String titulo, List<String> linhas) throws IOException {
        totalPesquisas++;
        PrintWriter pw = new PrintWriter(new FileWriter(arquivoParcial, true));
        pw.println("[" + LocalDateTime.now().format(HORA) + "] Pesquisa #" + totalPesquisas);
        pw.println(titulo);
        if (linhas.isEmpty()) {
            pw.println("  (nenhum resultado)");
        } else {
            for (String l : linhas) {
                pw.println("  " + l);
            }
        }
        pw.println();
        pw.close();
    }

    public void escreverFinal(String caminho) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(caminho));
        pw.println("CITO - Resumo final da sessao");
        pw.println("Gerado em: " + LocalDateTime.now().format(HORA));
        pw.println("Total de pesquisas realizadas: " + totalPesquisas);
        pw.close();
    }

    public static List<String> resumir(List<? extends Pessoa> pessoas, List<String> destino) {
        for (Pessoa p : pessoas) {
            destino.add(p.getResumo());
        }
        return destino;
    }
}
