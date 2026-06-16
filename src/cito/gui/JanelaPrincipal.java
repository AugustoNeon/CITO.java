package cito.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cito.excecoes.CpfInvalidoException;
import cito.modelo.Consulta;
import cito.modelo.Medico;
import cito.modelo.Paciente;
import cito.persistencia.EscritorResultados;
import cito.servico.Servico;

public class JanelaPrincipal extends JFrame {

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Servico servico;
    private EscritorResultados escritor;

    private JTextField campoCodMedico;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JTextField campoMeses;
    private JTextArea saidaMedico;

    private JTextField campoCpf;
    private JTextField campoCodMedicoPac;
    private JTextArea saidaPaciente;

    public JanelaPrincipal(Servico servico) {
        this.servico = servico;

        new File("resultados").mkdirs();
        this.escritor = new EscritorResultados("resultados/resultados_parciais.txt");

        setTitle("CITO - Medicos e Pacientes");
        setSize(640, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                gravarFinal();
            }
        });

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Interface do Medico", criarAbaMedico());
        abas.addTab("Interface do Paciente", criarAbaPaciente());
        add(abas);
    }

    private JPanel criarAbaMedico() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        campoCodMedico = new JTextField(8);
        campoDataInicio = new JTextField(10);
        campoDataFim = new JTextField(10);
        campoMeses = new JTextField(5);

        JPanel l1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        l1.add(new JLabel("Codigo do medico:"));
        l1.add(campoCodMedico);
        JButton btPacientes = new JButton("1) Pacientes do medico");
        l1.add(btPacientes);
        form.add(l1);

        JPanel l2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        l2.add(new JLabel("Periodo (dd/mm/aaaa):"));
        l2.add(campoDataInicio);
        l2.add(new JLabel("ate"));
        l2.add(campoDataFim);
        JButton btPeriodo = new JButton("2) Consultas no periodo");
        l2.add(btPeriodo);
        form.add(l2);

        JPanel l3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        l3.add(new JLabel("Sem consultar ha mais de (meses):"));
        l3.add(campoMeses);
        JButton btInativos = new JButton("3) Pacientes inativos");
        l3.add(btInativos);
        form.add(l3);

        saidaMedico = new JTextArea(15, 50);
        saidaMedico.setEditable(false);

        painel.add(form, BorderLayout.NORTH);
        painel.add(new JScrollPane(saidaMedico), BorderLayout.CENTER);

        btPacientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarPacientesDoMedico();
            }
        });
        btPeriodo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarConsultasNoPeriodo();
            }
        });
        btInativos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarPacientesInativos();
            }
        });

        return painel;
    }

    private void pesquisarPacientesDoMedico() {
        Integer codigo = lerInteiro(campoCodMedico.getText(), "codigo do medico");
        if (codigo == null) {
            return;
        }
        List<Paciente> pacientes = servico.pacientesDoMedico(codigo);
        List<String> linhas = EscritorResultados.resumir(pacientes, new ArrayList<String>());
        mostrar(saidaMedico, "Pacientes do medico " + codigo, linhas);
    }

    private void pesquisarConsultasNoPeriodo() {
        Integer codigo = lerInteiro(campoCodMedico.getText(), "codigo do medico");
        if (codigo == null) {
            return;
        }
        LocalDate inicio = lerData(campoDataInicio.getText());
        LocalDate fim = lerData(campoDataFim.getText());
        if (inicio == null || fim == null) {
            return;
        }
        List<Consulta> consultas = servico.consultasDoMedicoNoPeriodo(codigo, inicio, fim);
        mostrar(saidaMedico, "Consultas do medico " + codigo + " no periodo",
                linhasDeConsultas(consultas));
    }

    private void pesquisarPacientesInativos() {
        Integer codigo = lerInteiro(campoCodMedico.getText(), "codigo do medico");
        Integer meses = lerInteiro(campoMeses.getText(), "meses");
        if (codigo == null || meses == null) {
            return;
        }
        List<Paciente> pacientes = servico.pacientesSemConsultaHaMais(codigo, meses);
        List<String> linhas = EscritorResultados.resumir(pacientes, new ArrayList<String>());
        mostrar(saidaMedico, "Pacientes do medico " + codigo
                + " sem consulta ha mais de " + meses + " meses", linhas);
    }

    private JPanel criarAbaPaciente() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        campoCpf = new JTextField(14);
        campoCodMedicoPac = new JTextField(8);

        JPanel l1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        l1.add(new JLabel("CPF do paciente:"));
        l1.add(campoCpf);
        JButton btMedicos = new JButton("1) Medicos do paciente");
        l1.add(btMedicos);
        form.add(l1);

        JPanel l2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        l2.add(new JLabel("Codigo do medico:"));
        l2.add(campoCodMedicoPac);
        JButton btPassadas = new JButton("2) Consultas ja realizadas");
        l2.add(btPassadas);
        form.add(l2);

        JPanel l3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btAgendadas = new JButton("3) Consultas agendadas (futuras)");
        l3.add(btAgendadas);
        form.add(l3);

        saidaPaciente = new JTextArea(15, 50);
        saidaPaciente.setEditable(false);

        painel.add(form, BorderLayout.NORTH);
        painel.add(new JScrollPane(saidaPaciente), BorderLayout.CENTER);

        btMedicos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarMedicosDoPaciente();
            }
        });
        btPassadas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarConsultasPassadas();
            }
        });
        btAgendadas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarConsultasAgendadas();
            }
        });

        return painel;
    }

    private void pesquisarMedicosDoPaciente() {
        String cpf = lerCpf(campoCpf.getText());
        if (cpf == null) {
            return;
        }
        List<Medico> medicos = servico.medicosDoPaciente(cpf);
        List<String> linhas = EscritorResultados.resumir(medicos, new ArrayList<String>());
        mostrar(saidaPaciente, "Medicos do paciente " + cpf, linhas);
    }

    private void pesquisarConsultasPassadas() {
        String cpf = lerCpf(campoCpf.getText());
        Integer codigo = lerInteiro(campoCodMedicoPac.getText(), "codigo do medico");
        if (cpf == null || codigo == null) {
            return;
        }
        List<Consulta> consultas = servico.consultasPassadasComMedico(cpf, codigo);
        mostrar(saidaPaciente, "Consultas realizadas do paciente " + cpf
                + " com o medico " + codigo, linhasDeConsultas(consultas));
    }

    private void pesquisarConsultasAgendadas() {
        String cpf = lerCpf(campoCpf.getText());
        if (cpf == null) {
            return;
        }
        List<Consulta> consultas = servico.consultasAgendadas(cpf);
        mostrar(saidaPaciente, "Consultas agendadas do paciente " + cpf,
                linhasDeConsultas(consultas));
    }

    private List<String> linhasDeConsultas(List<Consulta> consultas) {
        List<String> linhas = new ArrayList<>();
        for (Consulta c : consultas) {
            linhas.add(c.toString());
        }
        return linhas;
    }

    private void mostrar(JTextArea area, String titulo, List<String> linhas) {
        StringBuilder sb = new StringBuilder();
        sb.append(titulo).append("\n");
        if (linhas.isEmpty()) {
            sb.append("(nenhum resultado encontrado)\n");
        } else {
            for (String l : linhas) {
                sb.append("  ").append(l).append("\n");
            }
        }
        area.setText(sb.toString());

        try {
            escritor.registrarParcial(titulo, linhas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gravar resultado: " + e.getMessage());
        }
    }

    private Integer lerInteiro(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um numero valido para: " + campo);
            return null;
        }
    }

    private LocalDate lerData(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FMT_DATA);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data invalida (use dd/mm/aaaa): " + texto);
            return null;
        }
    }

    private String lerCpf(String texto) {
        try {
            Paciente.validarCpf(texto.trim());
            return texto.trim();
        } catch (CpfInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        }
    }

    private void gravarFinal() {
        try {
            escritor.escreverFinal("resultados/resultados_finais.txt");
        } catch (IOException e) {
            System.out.println("Nao foi possivel gravar o resultado final: " + e.getMessage());
        }
    }
}
