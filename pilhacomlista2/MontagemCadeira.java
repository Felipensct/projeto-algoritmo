package pilhacomlista2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Deque;
import java.util.LinkedList;

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

public class MontagemCadeira extends JFrame {
    private Pilha pilhaMontagem;
    private Pilha pilhaDesmontagem;
    private Deque<Peca> filaSequencia;
    // private Queue<Peca> filaSequencia;  // Fila para controle de sequência
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
        filaSequencia = new LinkedList<>();
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
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
    
        JPanel painelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelConfig.add(new JLabel("Número de Cadeiras:"));
        painelConfig.add(spinnerCadeiras);
        JButton btnIniciar = new JButton("Iniciar Montagem");
        painelConfig.add(btnIniciar);
    
        JPanel painelMontagem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelMontagem.add(new JLabel("Peça:"));
        painelMontagem.add(pecaCombo);
        painelMontagem.add(new JLabel("Número:"));
        painelMontagem.add(numeroCombo);
        JButton btnMontar = new JButton("Montar Peça");
        painelMontagem.add(btnMontar);
    
        // Botões de desmontagem
        JButton btnDesmontarPeca = new JButton("Desmontar Peça");
        JButton btnDesmontarTudo = new JButton("Desmontar Tudo");
        painelMontagem.add(btnDesmontarPeca);
        painelMontagem.add(btnDesmontarTudo);
    
        JPanel painelSuperior = new JPanel(new GridLayout(2, 1));
        painelSuperior.add(painelConfig);
        painelSuperior.add(painelMontagem);
    
        btnIniciar.addActionListener(e -> iniciarNovaMontagem());
        btnMontar.addActionListener(e -> montarPeca());
        btnDesmontarPeca.addActionListener(e -> desmontarPeca());
        btnDesmontarTudo.addActionListener(e -> desmontarTudo());
    
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaComponentes),
                new JScrollPane(logArea));
        splitPane.setResizeWeight(0.5);
    
        add(painelSuperior, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    
        JLabel statusLabel = new JLabel("Cadeira atual: 1");
        add(statusLabel, BorderLayout.SOUTH);

        JButton btnExibirStatus = new JButton("Exibir Status");
        painelConfig.add(btnExibirStatus);

// Ação do botão
btnExibirStatus.addActionListener(e -> exibirStatusCadeiras());
    }
    private void iniciarNovaMontagem() {
        totalCadeiras = (int) spinnerCadeiras.getValue();
        cadeiraAtual = 1;
        pilhaMontagem = new Pilha();
        listModel.clear();
        logArea.setText("");
        logArea.append("Iniciando montagem de " + totalCadeiras + " cadeira(s)\n");
        logArea.append("Montando cadeira 1\n");
        preencherFilaSequencia();
    }

    private void preencherFilaSequencia() {
        filaSequencia.clear();
        for (int i = 0; i < totalCadeiras; i++) {
            for (Peca peca : SEQUENCIA_BASE) {
                filaSequencia.add(new Peca(peca.getNome(), peca.getNumero(), i + 1));
            }
        }
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
    
        if (peca.equals(filaSequencia.peek())) {
            pilhaMontagem.push(peca);
            listModel.addElement(peca.toString());
            logArea.append("Peça montada corretamente: " + peca + "\n");
            filaSequencia.poll();  // Remove a peça da fila de montagem
    
            if (filaSequencia.isEmpty() || !filaSequencia.peek().getNumeroCadeira().equals(cadeiraAtual)) {
                cadeiraAtual++;
                if (cadeiraAtual <= totalCadeiras) {
                    logArea.append("Montagem da cadeira " + cadeiraAtual + " iniciada.\n");
                } else {
                    logArea.append("Montagem de todas as cadeiras concluída.\n");
                }
            }
        } else {
            Peca pecaEsperada = filaSequencia.peek();  // Obtém a peça que deveria ser montada
            logArea.append("Erro: Peça fora da ordem esperada. Esperada: " + pecaEsperada + "\n");
        }
    }
    

    private void desmontarPeca() {
        if (pilhaMontagem.pEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nenhuma peça para desmontar.",
                "Erro",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Remove a peça da pilha de montagem
        Peca pecaDesmontada = (Peca) pilhaMontagem.removerFinal();
        pilhaDesmontagem.push(pecaDesmontada);  // Adiciona à pilha de desmontagem
        listModel.remove(listModel.getSize() - 1);  // Remove da lista de componentes montados
        logArea.append("Peça desmontada: " + pecaDesmontada + "\n");
        
        // Adiciona a peça desmontada no começo da fila para garantir que seja a próxima peça
        filaSequencia.addFirst(pecaDesmontada);  // Agora a peça desmontada será a próxima a ser montada
        logArea.append("Peça desmontada adicionada novamente à fila de montagem: " + pecaDesmontada + "\n");
    
        // Atualiza a montagem, se necessário
        // if (!filaSequencia.isEmpty() && filaSequencia.peek().getNumeroCadeira().equals(cadeiraAtual)) {
        //     montarPeca();  // Chama a função de montar peça, se necessário
        // }
    }    
    
    
    private void desmontarTudo() {
        if (pilhaMontagem.pEmpty()) { // Corrigido
            JOptionPane.showMessageDialog(this,
                "Nenhuma peça para desmontar.",
                "Erro",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        logArea.append("Desmontando todas as peças...\n");
        while (!pilhaMontagem.pEmpty()) { // Corrigido
            Peca pecaDesmontada = (Peca) pilhaMontagem.removerFinal(); // Corrigido
            pilhaDesmontagem.push(pecaDesmontada);
            listModel.remove(listModel.getSize() - 1);
            logArea.append("Peça desmontada: " + pecaDesmontada + "\n");
            filaSequencia.addFirst(pecaDesmontada);  // Agora a peça desmontada será a próxima a ser montada
            logArea.append("Peça desmontada adicionada novamente à fila de montagem: " + pecaDesmontada + "\n");
        }
        logArea.append("Todas as peças foram desmontadas.\n");
    }

    private void exibirStatusCadeiras() {
        logArea.setText(""); // Limpa os logs
    
        logArea.append("Status das Cadeiras:\n");
        for (int i = 1; i <= totalCadeiras; i++) {
            if (i < cadeiraAtual) {
                logArea.append("Cadeira " + i + ": Completa\n");
            } else if (i == cadeiraAtual) {
                logArea.append("Cadeira " + i + ": Em andamento\n");
            } else {
                logArea.append("Cadeira " + i + ": Na fila\n");
            }
        }
    
        logArea.append("\nComponentes Montados:\n");
        if (pilhaMontagem.pEmpty()) {
            logArea.append("Nenhum componente montado ainda.\n");
            return;
        }
    
        // Exibir componentes da pilha de montagem
        Pilha tempPilha = new Pilha();
        while (!pilhaMontagem.pEmpty()) {
            Peca peca = (Peca) pilhaMontagem.removerFinal();
            tempPilha.push(peca);
            logArea.append(peca.toString() + "\n");
        }
    
        // Recolocar os itens na pilha original
        while (!tempPilha.pEmpty()) {
            pilhaMontagem.push(tempPilha.removerFinal());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MontagemCadeira().setVisible(true));
    }
}