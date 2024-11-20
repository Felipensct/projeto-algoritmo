package pilhacomlista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class CadeiraGUI {
    private Pilha pilha = new Pilha();
    private JTextArea areaTexto;
    private JButton botaoAdicionarPasso, botaoVerificarPasso, botaoFinalizarMontagem, botaoLimpar;
    private JComboBox<String> comboPasso;
    private JSpinner spinnerElementos, spinnerCadeiras;
    private String[] itens = {
            "Perna da mesa", "Acento", "Arruela", "Parafuso", "Suporte para encosto", "Encosto"
    };
    private String[] sequenciaMontagem = {
            "Perna da mesa - 1", "Acento", "Arruela - 1", "Parafuso - 1",
            "Perna da mesa - 2", "Acento", "Arruela - 2", "Parafuso - 2",
            "Perna da mesa - 3", "Acento", "Arruela - 3", "Parafuso - 3",
            "Perna da mesa - 4", "Acento", "Arruela - 4", "Parafuso - 4",
            "Suporte para encosto", "Encosto"
    };
    private int etapaAtual = 0;

    public CadeiraGUI() {
        JFrame frame = new JFrame("Montagem de Cadeira");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel painelControle = new JPanel(new GridLayout(4, 2));

        spinnerCadeiras = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        painelControle.add(new JLabel("Número de cadeiras:"));
        painelControle.add(spinnerCadeiras);

        comboPasso = new JComboBox<>(itens);
        painelControle.add(new JLabel("Escolher passo:"));
        painelControle.add(comboPasso);

        spinnerElementos = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        painelControle.add(new JLabel("Quantidade de itens:"));
        painelControle.add(spinnerElementos);

        botaoAdicionarPasso = new JButton("Adicionar Passo");
        botaoVerificarPasso = new JButton("Verificar Passo");
        botaoFinalizarMontagem = new JButton("Finalizar Montagem");
        botaoLimpar = new JButton("Limpar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoAdicionarPasso);
        painelBotoes.add(botaoVerificarPasso);
        painelBotoes.add(botaoFinalizarMontagem);
        painelBotoes.add(botaoLimpar);

        frame.add(painelControle, BorderLayout.NORTH);
        frame.add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        botaoAdicionarPasso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarPasso();
            }
        });

        botaoVerificarPasso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarPasso();
            }
        });

        botaoFinalizarMontagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarMontagemCompleta();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });

        frame.setVisible(true);
    }

    private void adicionarPasso() {
        int numeroCadeiras = (int) spinnerCadeiras.getValue();
        String passoSelecionado = (String) comboPasso.getSelectedItem();
        int quantidadeItens = (int) spinnerElementos.getValue();

        for (int i = 1; i <= quantidadeItens; i++) {
            pilha.push(passoSelecionado + " - " + i);
            areaTexto.append("Adicionado: " + passoSelecionado + " - " + i + "\n");
        }

        areaTexto.append("Etapa atual de " + numeroCadeiras + " cadeiras\n");
    }

    private void verificarPasso() {
        if (etapaAtual < sequenciaMontagem.length) {
            String esperado = sequenciaMontagem[etapaAtual];
            Object atual = pilha.removerFinal();

            if (esperado.equals(atual)) {
                areaTexto.append("Passo correto: " + esperado + "\n");
            } else {
                areaTexto.append("Erro no passo! Esperado: " + esperado + ", Obtido: " + atual + "\n");
            }

            etapaAtual++;
        } else {
            areaTexto.append("Todos os passos já foram realizados.\n");
        }
    }

    private void verificarMontagemCompleta() {
        if (pilha.pEmpty()) {
            areaTexto.append("Montagem completa e correta!\n");
        } else {
            areaTexto.append("Ainda restam itens na pilha, montagem incompleta!\n");
        }
    }

    private void limpar() {
        areaTexto.setText("");
        pilha = new Pilha();
        etapaAtual = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CadeiraGUI::new);
    }
}
