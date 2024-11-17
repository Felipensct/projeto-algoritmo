import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * Interface gráfica para o sistema de montagem de cadeiras.
 * Esta classe fornece uma interface visual interativa para o processo
 * de montagem, verificação e desmontagem de cadeiras.
 * 
 * Características principais:
 * - Visualização em tempo real do processo de montagem
 * - Controles interativos para cada etapa do processo
 * - Log de atividades
 * - Suporte para montagem de múltiplas cadeiras
 * 
 * @author [Felipe] [Ariel]
 * @version 1.0
 * @see JFrame
 * @see Stack
 */
public class MontadorCadeiraGUI extends JFrame {
    /**
     * Pilha que armazena as peças da cadeira durante e após a montagem.
     * As peças são empilhadas na ordem em que são montadas.
     */
    private Stack<PecaCadeira> cadeiraMontada;

    /**
     * Pilha que armazena as peças disponíveis para montagem.
     * As peças são organizadas na ordem inversa para facilitar a montagem correta.
     */
    private Stack<PecaCadeira> pecasDisponiveis;

    /**
     * Área de texto para exibir o log de operações e mensagens do sistema.
     * Registra cada passo do processo de montagem, verificação e desmontagem.
     */
    private JTextArea logArea;

    /**
     * Botões de controle para as operações principais do sistema.
     * montarButton: Inicia o processo de montagem
     * verificarButton: Verifica se a montagem está correta
     * desmontarButton: Inicia o processo de desmontagem
     */
    private JButton montarButton;
    private JButton verificarButton;
    private JButton desmontarButton;

    /**
     * Componente para seleção da quantidade de cadeiras a serem montadas.
     * Permite valores positivos maiores que zero.
     */
    private JSpinner quantidadeSpinner;

    /**
     * Painel para visualização gráfica da cadeira sendo montada.
     * Atualiza em tempo real conforme o progresso da montagem.
     */
    private JPanel visualizacaoPanel;

    /**
     * Contadores para controle do processo de montagem.
     * cadeiraAtual: Indica qual cadeira está sendo montada no momento
     * totalCadeiras: Armazena o número total de cadeiras a serem montadas
     */
    private int cadeiraAtual = 0;
    private int totalCadeiras = 0;

    /**
     * Construtor da interface gráfica.
     * Inicializa a janela principal e configura todos os componentes necessários.
     * 
     * Sequência de inicialização:
     * 1. Configura o título da janela
     * 2. Inicializa os componentes visuais
     * 3. Inicializa as estruturas de dados (pilhas)
     * 
     * @throws HeadlessException se o ambiente não suportar interface gráfica
     */

    /**
     * Constantes para dimensões da janela
     */
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int LOG_ROWS = 10;
    private static final int LOG_COLUMNS = 40;
    private static final Dimension VISUALIZATION_SIZE = new Dimension(400, 400);

    /**
     * Construtor que inicializa a interface gráfica do sistema.
     * Configura o título da janela e inicializa todos os componentes necessários.
     * 
     * @throws HeadlessException se o ambiente não suportar interface gráfica
     */
    public MontadorCadeiraGUI() {
        super("Sistema de Montagem de Cadeiras");
        inicializarComponentes();
        inicializarPilhas();
    }

    /**
     * Inicializa e configura todos os componentes da interface gráfica.
     * Este método configura o layout, cria os painéis de controle,
     * área de visualização e log, além de configurar os listeners dos botões.
     * 
     * Estrutura da interface:
     * - Painel Superior: Controles de montagem e quantidade
     * - Painel Central: Visualização da cadeira
     * - Painel Inferior: Log de operações
     * 
     * Componentes principais:
     * - Spinner para seleção de quantidade
     * - Botões de controle (Montar, Verificar, Desmontar)
     * - Painel de visualização customizado
     * - Área de log com scroll
     */
    private void inicializarComponentes() {
        // Configuração básica da janela
        configurarJanelaPrincipal();

        // Inicialização dos painéis
        JPanel controlPanel = criarPainelControle();
        criarPainelVisualizacao();
        JScrollPane logPanel = criarPainelLog();

        // Montagem da interface
        montarInterface(controlPanel, logPanel);

        // Configuração dos listeners
        configurarEventos();
    }

    /**
     * Configura as propriedades básicas da janela principal.
     */
    private void configurarJanelaPrincipal() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
    }

    /**
     * Cria e configura o painel de controles superior.
     * 
     * @return JPanel configurado com os controles
     */
    private JPanel criarPainelControle() {
        JPanel controlPanel = new JPanel();
        
        // Configuração do spinner de quantidade
        JLabel quantidadeLabel = new JLabel("Quantidade de Cadeiras:");
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        quantidadeSpinner = new JSpinner(spinnerModel);

        // Criação e configuração dos botões
        montarButton = new JButton("Montar");
        verificarButton = new JButton("Verificar");
        desmontarButton = new JButton("Desmontar");

        // Estado inicial dos botões
        verificarButton.setEnabled(false);
        desmontarButton.setEnabled(false);

        // Adição dos componentes ao painel
        controlPanel.add(quantidadeLabel);
        controlPanel.add(quantidadeSpinner);
        controlPanel.add(montarButton);
        controlPanel.add(verificarButton);
        controlPanel.add(desmontarButton);

        return controlPanel;
    }

    /**
     * Cria e configura o painel de visualização da cadeira.
     */
    private void criarPainelVisualizacao() {
        visualizacaoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharCadeira(g);
            }
        };
        visualizacaoPanel.setPreferredSize(VISUALIZATION_SIZE);
        visualizacaoPanel.setBackground(Color.WHITE);
    }

    /**
     * Cria e configura o painel de log com barra de rolagem.
     * 
     * @return JScrollPane contendo a área de log
     */
    private JScrollPane criarPainelLog() {
        logArea = new JTextArea(LOG_ROWS, LOG_COLUMNS);
        logArea.setEditable(false);
        return new JScrollPane(logArea);
    }

    /**
     * Monta a interface principal adicionando os painéis nas posições corretas.
     * 
     * @param controlPanel painel de controles
     * @param logPanel painel de log
     */
    private void montarInterface(JPanel controlPanel, JScrollPane logPanel) {
        add(controlPanel, BorderLayout.NORTH);
        add(visualizacaoPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);
    }

    /**
     * Configura os eventos (listeners) para os botões da interface.
     * Cada botão é associado ao seu respectivo método de ação.
     */
    private void configurarEventos() {
        montarButton.addActionListener(_ -> iniciarMontagem());
        verificarButton.addActionListener(_ -> verificarMontagem());
        desmontarButton.addActionListener(_ -> desmontarCadeira());
    }

    /**
     * Desenha a representação visual da cadeira no painel de visualização.
     * Este método é chamado automaticamente pelo sistema de repaint do Java
     * e também pode ser invocado manualmente para atualizar a visualização.
     * 
     * O desenho é progressivo e depende do número de peças montadas:
     * - 1-4: Primeira perna
     * - 5-8: Segunda perna
     * - 9-12: Terceira perna
     * - 13: Quarta perna
     * - 14-16: Assento (azul)
     * - 17-18: Encosto (verde)
     * 
     * @param g O contexto gráfico fornecido pelo sistema
     */
    private void desenharCadeira(Graphics g) {
        // Verifica se existem peças para desenhar
        if (cadeiraMontada == null || cadeiraMontada.isEmpty()) {
            return;
        }

        // Configura o contexto gráfico para melhor qualidade de renderização
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calcula o centro do painel para posicionamento da cadeira
        int centerX = visualizacaoPanel.getWidth() / 2;
        int centerY = visualizacaoPanel.getHeight() / 2;

        // Obtém o número atual de peças montadas
        int numPecas = cadeiraMontada.size();
        
        // Desenha as pernas da cadeira
        // Primeira perna (frente, esquerda)
        if (numPecas >= 1) g2d.fillRect(centerX - 100, centerY + 50, 20, 100);
        // Segunda perna (frente, direita)
        if (numPecas >= 5) g2d.fillRect(centerX + 80, centerY + 50, 20, 100);
        // Terceira perna (trás, esquerda)
        if (numPecas >= 9) g2d.fillRect(centerX - 100, centerY - 50, 20, 100);
        // Quarta perna (trás, direita)
        if (numPecas >= 13) g2d.fillRect(centerX + 80, centerY - 50, 20, 100);

        // Desenha o assento da cadeira quando houver peças suficientes
        if (numPecas >= 14) {
            g2d.setColor(Color.BLUE);
            g2d.fillRect(centerX - 100, centerY - 20, 200, 40);
        }

        // Desenha o encosto da cadeira quando todas as peças estiverem próximas da montagem completa
        if (numPecas >= 17) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(centerX - 80, centerY - 120, 160, 100);
        }
    }

    /**
     * Adiciona uma mensagem ao log e atualiza a posição do cursor.
     * A mensagem é anexada ao final do log com uma nova linha,
     * e o cursor é posicionado automaticamente para mostrar o texto mais recente.
     *
     * @param mensagem A mensagem a ser adicionada ao log
     */
    private void log(String mensagem) {
        logArea.append(mensagem + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    /**
     * Inicializa as estruturas de dados (pilhas) necessárias para
     * o processo de montagem da cadeira.
     * Cria duas pilhas vazias:
     * - cadeiraMontada: para armazenar as peças já montadas
     * - pecasDisponiveis: para armazenar as peças disponíveis para montagem
     */
    private void inicializarPilhas() {
        cadeiraMontada = new Stack<>();
        pecasDisponiveis = new Stack<>();
    }

    /**
     * Inicializa o conjunto de peças disponíveis para montagem da cadeira.
     * As peças são adicionadas na pilha em ordem inversa para garantir
     * a sequência correta de montagem quando forem retiradas.
     * 
     * Ordem das peças (de baixo para cima na pilha):
     * 1. Perna da mesa 1 (com seus componentes)
     * 2. Perna da mesa 2 (com seus componentes)
     * 3. Perna da mesa 3 (com seus componentes)
     * 4. Perna da mesa 4 (com seus componentes)
     * 5. Suporte para encosto
     * 6. Encosto
     */
    private void inicializarPecas() {
        // Limpa a pilha antes de adicionar novas peças
        pecasDisponiveis.clear();
        
        // Define a ordem das peças para montagem
        String[] pecas = {
            "Encosto", "Suporte para encosto",
            "Parafuso - 4", "Arruela - 4", "Acentos", "Perna da mesa - 4",
            "Parafuso - 3", "Arruela - 3", "Acentos", "Perna da mesa - 3",
            "Parafuso - 2", "Arruela - 2", "Acentos", "Perna da mesa - 2",
            "Parafuso - 1", "Arruela - 1", "Acentos", "Perna da mesa - 1"
        };
        
        // Adiciona as peças na pilha em ordem inversa
        // para que sejam retiradas na ordem correta
        for (int i = pecas.length - 1; i >= 0; i--) {
            pecasDisponiveis.push(new PecaCadeira(pecas[i], pecas.length - i));
        }
    }

    /**
     * Inicia o processo de montagem de uma nova cadeira.
     * Este método controla o fluxo de montagem das cadeiras, mantendo o controle
     * do número total de cadeiras a serem montadas e da cadeira atual.
     * 
     * Funcionamento:
     * 1. Na primeira execução (cadeiraAtual = 0):
     *    - Obtém o número total de cadeiras do spinner
     * 2. Para cada cadeira:
     *    - Incrementa o contador de cadeiras
     *    - Inicializa as peças
     *    - Inicia o processo de montagem
     *    - Atualiza o estado dos botões
     * 
     * Controle de Interface:
     * - Habilita o botão de verificação
     * - Desabilita o botão de montagem durante o processo
     * 
     * @throws IllegalStateException se houver erro na obtenção do valor do spinner
     */
    private void iniciarMontagem() {
        // Inicialização do total de cadeiras (apenas na primeira execução)
        if (cadeiraAtual == 0) {
            totalCadeiras = (Integer) quantidadeSpinner.getValue();
        }
        
        // Incrementa o contador e verifica se ainda há cadeiras para montar
        cadeiraAtual++;
        if (cadeiraAtual <= totalCadeiras) {
            // Registra início da montagem no log
            log("\nIniciando montagem da cadeira " + cadeiraAtual + " de " + totalCadeiras);
            
            // Inicializa e inicia o processo de montagem
            inicializarPecas();
            montarCadeira();
            
            // Atualiza estado dos botões
            verificarButton.setEnabled(true);
            montarButton.setEnabled(false);
        }
    }

    /**
     * Realiza o processo de montagem da cadeira de forma animada.
     * Este método utiliza um Timer para criar uma animação do processo de montagem,
     * adicionando uma peça a cada 500 milissegundos para uma visualização gradual.
     * 
     * Processo de montagem:
     * 1. Limpa qualquer montagem anterior
     * 2. Inicia um timer que:
     *    - Remove uma peça da pilha de peças disponíveis
     *    - Adiciona a peça à cadeira sendo montada
     *    - Atualiza o log com a peça atual
     *    - Atualiza a visualização
     *    - Para quando todas as peças forem montadas
     * 
     * Taxa de Atualização: 500ms (2 peças por segundo)
     * 
     * @see Timer
     * @see PecaCadeira
     */
    private void montarCadeira() {
        // Limpa a cadeira atual para nova montagem
        cadeiraMontada.clear();

        // Configura o timer para animação (500ms de intervalo)
        Timer timer = new Timer(500, null);

        // Configura o listener do timer para processar cada etapa da montagem
        timer.addActionListener(_ -> {
            if (!pecasDisponiveis.isEmpty()) {
                // Remove uma peça das disponíveis e adiciona à montagem
                PecaCadeira peca = pecasDisponiveis.pop();
                cadeiraMontada.push(peca);
                
                // Registra a ação no log
                log("Montando: " + peca.nome);
                
                // Atualiza a visualização
                visualizacaoPanel.repaint();
            } else {
                // Para o timer quando todas as peças foram montadas
                timer.stop();
            }
        });

        // Inicia o processo de montagem
        timer.start();
    }

    /**
     * Verifica se a cadeira foi montada na ordem correta de peças.
     * O método utiliza uma pilha temporária para preservar a estrutura original
     * durante a verificação, permitindo restaurar o estado após o processo.
     * 
     * Processo de verificação:
     * 1. Verifica cada peça na ordem de montagem
     * 2. Compara com a ordem esperada
     * 3. Registra erros encontrados
     * 4. Restaura a estrutura original
     * 5. Atualiza o estado dos botões
     * 
     * Estados dos botões após verificação:
     * - Habilita o botão de desmontagem
     * - Desabilita o botão de verificação
     * 
     * @return boolean true se a montagem estiver correta, false caso contrário
     * @see PecaCadeira
     */
    private boolean verificarMontagem() {
        // Inicia o processo de verificação
        log("\nVerificando montagem...");
        
        // Pilha temporária para preservar a estrutura durante verificação
        Stack<PecaCadeira> temp = new Stack<>();
        boolean montagemCorreta = true;
        int ordemEsperada = 1;

        // Primeira fase: Verificação da ordem das peças
        while (!cadeiraMontada.isEmpty()) {
            PecaCadeira peca = cadeiraMontada.pop();
            if (peca.ordem != ordemEsperada) {
                montagemCorreta = false;
                log("Erro na montagem! Peça " + peca.nome + " está na posição errada.");
            }
            temp.push(peca);
            ordemEsperada++;
        }

        // Segunda fase: Restauração da estrutura original
        while (!temp.isEmpty()) {
            cadeiraMontada.push(temp.pop());
        }

        // Registra o resultado da verificação
        if (montagemCorreta) {
            log("Montagem realizada corretamente!");
        }

        // Atualiza estado dos botões
        desmontarButton.setEnabled(true);
        verificarButton.setEnabled(false);
        
        return montagemCorreta;
    }

    /**
     * Realiza o processo de desmontagem da cadeira de forma animada.
     * Similar ao processo de montagem, utiliza um Timer para criar uma
     * animação de desmontagem, removendo uma peça a cada 500 milissegundos.
     * 
     * Processo de desmontagem:
     * 1. Remove peças uma a uma da cadeira montada
     * 2. Atualiza o log e a visualização a cada peça
     * 3. Ao finalizar:
     *    - Habilita botão de montagem se houver mais cadeiras
     *    - Desabilita botão de desmontagem
     * 
     * Taxa de Atualização: 500ms (2 peças por segundo)
     * 
     * @see Timer
     * @see PecaCadeira
     */
    private void desmontarCadeira() {
        // Configura o timer para animação (500ms de intervalo)
        Timer timer = new Timer(500, null);

        // Configura o listener do timer para processar cada etapa da desmontagem
        timer.addActionListener(_ -> {
            if (!cadeiraMontada.isEmpty()) {
                // Remove uma peça da cadeira montada
                PecaCadeira peca = cadeiraMontada.pop();
                log("Desmontando: " + peca.nome);
                visualizacaoPanel.repaint();
            } else {
                // Finaliza o processo de desmontagem
                timer.stop();
                
                // Atualiza estado dos botões baseado no número de cadeiras restantes
                if (cadeiraAtual < totalCadeiras) {
                    montarButton.setEnabled(true);
                }
                desmontarButton.setEnabled(false);
            }
        });

        // Inicia o processo de desmontagem
        timer.start();
    }

    /**
     * Classe interna que representa uma peça da cadeira.
     * Cada peça possui um nome para identificação e uma ordem
     * que determina sua posição correta na sequência de montagem.
     */
    private static class PecaCadeira {
        /** Nome identificador da peça */
        String nome;
        
        /** 
         * Ordem da peça na sequência de montagem
         * Valores de 1 a 18, onde 1 é a primeira peça a ser montada
         */
        int ordem;

        /**
         * Construtor para criar uma nova peça da cadeira.
         * 
         * @param nome Nome identificador da peça
         * @param ordem Posição da peça na sequência de montagem (1-18)
         */
        public PecaCadeira(String nome, int ordem) {
            this.nome = nome;
            this.ordem = ordem;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MontadorCadeiraGUI gui = new MontadorCadeiraGUI();
            gui.setVisible(true);
        });
    }
}