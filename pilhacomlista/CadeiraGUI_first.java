package pilhacomlista;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class CadeiraGUI_first {
    private Pilha pilha = new Pilha();
    private JTextArea areaTexto;
    private JButton botaoMontar, botaoVerificar, botaoDesmontar, botaoLimpar;

    // Itens da cadeira na sequência correta
    private String[] itens = {
            "Perna da mesa - 1", "Acento", "Arruela - 1", "Parafuso - 1",
            "Perna da mesa - 2", "Acento", "Arruela - 2", "Parafuso - 2",
            "Perna da mesa - 3", "Acento", "Arruela - 3", "Parafuso - 3",
            "Perna da mesa - 4", "Acento", "Arruela - 4", "Parafuso - 4",
            "Suporte para encosto", "Encosto"
    };

    public CadeiraGUI_first() {
        JFrame frame = new JFrame("Montagem de Cadeira");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        botaoMontar = new JButton("Montar");
        botaoVerificar = new JButton("Verificar Montagem");
        botaoDesmontar = new JButton("Desmontar");
        botaoLimpar = new JButton("Limpar");

        painelBotoes.add(botaoMontar);
        painelBotoes.add(botaoVerificar);
        painelBotoes.add(botaoDesmontar);
        painelBotoes.add(botaoLimpar);

        frame.add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        botaoMontar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                montarCadeira();
            }
        });

        botaoVerificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarMontagem();
            }
        });

        botaoDesmontar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desmontarCadeira();
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

    // Montar a cadeira (push na pilha)
    private void montarCadeira() {
        pilha = new Pilha(); // Reinicia a pilha para montagem nova
        for (String item : itens) {
            pilha.push(item);
            areaTexto.append("Montado: " + item + "\n");
        }
        areaTexto.append("Montagem concluída!\n\n");
    }

    // Verificar se a montagem está correta
    private void verificarMontagem() {
        Pilha pilhaVerificacao = new Pilha();
        for (String item : itens) {
            pilhaVerificacao.push(item);
        }

        boolean montagemCorreta = true;
        for (int i = itens.length - 1; i >= 0; i--) {
            if (!itens[i].equals(pilha.removerFinal())) {
                montagemCorreta = false;
                break;
            }
        }

        if (montagemCorreta) {
            areaTexto.append("Montagem correta!\n");
        } else {
            areaTexto.append("Montagem incorreta!\n");
        }
    }

    // Desmontar a cadeira (pop da pilha)
    private void desmontarCadeira() {
        if (pilha.pEmpty()) {
            areaTexto.append("A pilha está vazia, nada para desmontar!\n");
        } else {
            areaTexto.append("Desmontando:\n");
            while (!pilha.pEmpty()) {
                areaTexto.append("Desmontado: " + pilha.removerFinal() + "\n");
            }
            areaTexto.append("Desmontagem concluída!\n\n");
        }
    }

    // Limpar a área de texto
    private void limpar() {
        areaTexto.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CadeiraGUI_first::new);
    }
}
