package pilhacomlista2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

class Peca {
    private String nome;
    private int numero;
    private int numeroCadeira;

    public Peca(String nome, int numero, int numeroCadeira) {
        this.nome = nome;
        this.numero = numero;
        this.numeroCadeira = numeroCadeira;
    }

    public String getNome() {
        return nome;
    }

    public int getNumero() {
        return numero;
    }

    public int getNumeroCadeira() {
        return numeroCadeira;
    }

    @Override
    public String toString() {
        return "Cadeira " + numeroCadeira + ": " + nome + " - " + numero;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Peca peca = (Peca) obj;
        return numero == peca.numero && 
               nome.equals(peca.nome) && 
               numeroCadeira == peca.numeroCadeira;
    }
}

public class MontagemCadeira extends JFrame {
    private Pilha pilhaMontagem;
    private Pilha pilhaDesmontagem;
    private DefaultListModel<String> listModel;
    private JList<String> listaComponentes;
    private JTextArea logArea;
    private JComboBox<String> pecaCombo;
    private JComboBox<Integer> numeroCombo;
    private JSpinner spinnerCadeiras;
    private int cadeiraAtual = 1;
    private int totalCadeiras = 1;
    private static final Peca[] SEQUENCIA_BASE = {
            new Peca("Perna da mesa", 1, 1),
            new Peca("Acentos", 1, 1),
            new Peca("Arruela", 1, 1),
            new Peca("Parafuso", 1, 1),
            new Peca("Perna da mesa", 2, 1),
            new Peca("Acentos", 1, 1),
            new Peca("Arruela", 2, 1),
            new Peca("Parafuso", 2, 1),
            new Peca("Perna da mesa", 3, 1),
            new Peca("Acentos", 1, 1),
            new Peca("Arruela", 3, 1),
            new Peca("Parafuso", 3, 1),
            new Peca("Perna da mesa", 4, 1),
            new Peca("Acentos", 1, 1),
            new Peca("Arruela", 4, 1),
            new Peca("Parafuso", 4, 1),
            new Peca("Suporte para encosto", 1, 1),
            new Peca("Encosto", 1, 1)
    };

    public MontagemCadeira() {
        pilhaMontagem = new Pilha();
        pilhaDesmontagem = new Pilha();
        configurarJanela();
        inicializarComponentes();
        configurarLayout();
    }

    private void configurarJanela() {
        setTitle("Sistema de Montagem de Cadeiras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        String[] pecas = {"Perna da mesa", "Acentos", "Arruela", "Parafuso", 
                         "Suporte para encosto", "Encosto"};
        Integer[] numeros = {1, 2, 3, 4};
        
        pecaCombo = new JComboBox<>(pecas);
        numeroCombo = new JComboBox<>(numeros);
        
        // Spinner para selecionar número de cadeiras
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        spinnerCadeiras = new JSpinner(spinnerModel);
        
        listModel = new DefaultListModel<>();
        listaComponentes = new JList<>(listModel);
        logArea = new JTextArea();
        logArea.setEditable(false);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Painel superior com configuração de cadeiras
        JPanel painelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelConfig.add(new JLabel("Número de Cadeiras:"));
        painelConfig.add(spinnerCadeiras);
        JButton btnIniciar = new JButton("Iniciar Montagem");
        painelConfig.add(btnIniciar);

        // Painel de montagem
        JPanel painelMontagem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelMontagem.add(new JLabel("Peça:"));
        painelMontagem.add(pecaCombo);
        painelMontagem.add(new JLabel("Número:"));
        painelMontagem.add(numeroCombo);
        JButton btnMontar = new JButton("Montar Peça");
        JButton btnDesmontar = new JButton("Desmontar Tudo");
        JButton btnDesmontarPassoAPasso = new JButton("Desmontar Passo a Passo");
        painelMontagem.add(btnMontar);
        painelMontagem.add(btnDesmontar);
        painelMontagem.add(btnDesmontarPassoAPasso);

        // Painel superior combinado
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1));
        painelSuperior.add(painelConfig);
        painelSuperior.add(painelMontagem);

        // Configurar eventos
        btnIniciar.addActionListener(e -> iniciarNovaMontagem());
        btnMontar.addActionListener(e -> montarPeca());
        btnDesmontar.addActionListener(e -> realizarDesmontagem());
        btnDesmontarPassoAPasso.addActionListener(e -> realizarDesmontagemPassoAPasso());

        // Painel central
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaComponentes),
                new JScrollPane(logArea));
        splitPane.setResizeWeight(0.5);

        add(painelSuperior, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Status da montagem
        JLabel statusLabel = new JLabel("Cadeira atual: 1");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void iniciarNovaMontagem() {
        totalCadeiras = (int) spinnerCadeiras.getValue();
        cadeiraAtual = 1;
        pilhaMontagem = new Pilha();
        listModel.clear();
        logArea.setText("");
        logArea.append("Iniciando montagem de " + totalCadeiras + " cadeira(s)\n");
        logArea.append("Montando cadeira 1\n");
    }

    private Peca[] getSequenciaCorreta(int numeroCadeira) {
        Peca[] sequencia = new Peca[SEQUENCIA_BASE.length];
        for (int i = 0; i < SEQUENCIA_BASE.length; i++) {
            Peca pecaBase = SEQUENCIA_BASE[i];
            sequencia[i] = new Peca(pecaBase.getNome(), pecaBase.getNumero(), numeroCadeira);
        }
        return sequencia;
    }

    private boolean validarProximaPeca(Peca peca) {
        int indiceAtual = listModel.getSize() % SEQUENCIA_BASE.length;
        Peca[] sequenciaAtual = getSequenciaCorreta(cadeiraAtual);
        return peca.equals(sequenciaAtual[indiceAtual]);
    }

    private void montarPeca() {
        if (cadeiraAtual > totalCadeiras) {
            JOptionPane.showMessageDialog(this,
                "Todas as cadeiras já foram montadas!",
                "Montagem Concluída",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String nome = (String) pecaCombo.getSelectedItem();
        int numero = (int) numeroCombo.getSelectedItem();
        Peca peca = new Peca(nome, numero, cadeiraAtual);
        
        if (validarProximaPeca(peca)) {
            pilhaMontagem.push(peca);  // Utilizando o método push da pilha
            listModel.addElement(peca.toString());
            logArea.append("Peça montada corretamente: " + peca + "\n");

            // Verificar se completou uma cadeira
            if (listModel.getSize() % SEQUENCIA_BASE.length == 0) {
                cadeiraAtual++;
                if (cadeiraAtual <= totalCadeiras) {
                    logArea.append("\nCadeira " + (cadeiraAtual-1) + " completa!\n");
                    logArea.append("Iniciando montagem da cadeira " + cadeiraAtual + "\n");
                } else {
                    logArea.append("\nTodas as cadeiras foram montadas com sucesso!\n");
                    JOptionPane.showMessageDialog(this,
                        "Todas as " + totalCadeiras + " cadeiras foram montadas com sucesso!",
                        "Montagem Concluída",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            logArea.setForeground(Color.RED);
            logArea.append("Erro: A peça " + peca + " está fora da ordem correta.\n");
            logArea.setForeground(Color.BLACK);
        }
    }

    private void realizarDesmontagem() {
        while (!pilhaMontagem.pEmpty()) {  // Usando pEmpty()
            Peca peca = (Peca) pilhaMontagem.removerFinal();  // Usando removerFinal()
            logArea.append("Desmontando: " + peca + "\n");
        }
        logArea.append("Desmontagem concluída.\n");
    }

    private void realizarDesmontagemPassoAPasso() {
        if (!pilhaMontagem.pEmpty()) {
            Peca peca = (Peca) pilhaMontagem.removerFinal();  // Usando removerFinal()
            logArea.append("Desmontando peça: " + peca + "\n");
        } else {
            logArea.append("A pilha de montagem já está vazia.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MontagemCadeira().setVisible(true));
    }
}
