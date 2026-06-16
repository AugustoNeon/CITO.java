package cito.app;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import cito.modelo.BaseDados;
import cito.persistencia.RepositorioBinario;
import cito.servico.Servico;
import cito.gui.JanelaPrincipal;

public class ProgramaP2 {

    public static void main(String[] args) {
        try {
            BaseDados base = new RepositorioBinario().restaurar("dados/base.dat");
            base.reindexar();
            Servico servico = new Servico(base);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JanelaPrincipal janela = new JanelaPrincipal(servico);
                    janela.setVisible(true);
                }
            });
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Nao foi possivel ler a base. Rode o P1 antes.\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Arquivo binario incompativel: " + e.getMessage());
        }
    }
}
