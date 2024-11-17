import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Stack;
/*
 * Classe que gerencia as peças e montagem de uma cadeira.
 * As peças devem ser montadas em uma ordem específica para garantir
 * a integridade estrutural da cadeira.
 */
public class MontadorCadeira {
    private static class PecaCadeira {
        String nome;
        int ordem;

        public PecaCadeira(String nome, int ordem) {
            this.nome = nome;
            this.ordem = ordem;
        }
    }

    private Stack<PecaCadeira> cadeiraMontada;
    private Stack<PecaCadeira> pecasDisponiveis;

    public MontadorCadeira() {
        cadeiraMontada = new Stack<>();
        pecasDisponiveis = new Stack<>();
        inicializarPecas();
    }

    /**
     * Inicializa o conjunto de peças disponíveis para montagem da cadeira.
     * As peças são adicionadas em ordem inversa na pilha para garantir
     * que sejam montadas na sequência correta.
     * 
     * Sequência de montagem (ordem crescente de 1 a 18):
     * 1-4: Primeira perna (perna, acento, arruela, parafuso)
     * 5-8: Segunda perna (perna, acento, arruela, parafuso)
     * 9-12: Terceira perna (perna, acento, arruela, parafuso)
     * 13-16: Quarta perna (perna, acento, arruela, parafuso)
     * 17-18: Finalização (suporte para encosto, encosto)
     * 
     * @throws IllegalStateException se a pilha de peças disponíveis não estiver vazia
     */
    private void inicializarPecas() {
        // Limpa a pilha antes de inicializar (boa prática para evitar duplicações)
        pecasDisponiveis.clear();
        
        // Grupo 6: Encosto e suporte (peças 17-18)
        pecasDisponiveis.push(new PecaCadeira("Encosto", 18));
        pecasDisponiveis.push(new PecaCadeira("Suporte para encosto", 17));
        
        // Grupo 5: Quarta perna (peças 13-16)
        pecasDisponiveis.push(new PecaCadeira("Parafuso - 4", 16));
        pecasDisponiveis.push(new PecaCadeira("Arruela - 4", 15));
        pecasDisponiveis.push(new PecaCadeira("Acentos", 14));
        pecasDisponiveis.push(new PecaCadeira("Perna da mesa - 4", 13));
        
        // Grupo 4: Terceira perna (peças 9-12)
        pecasDisponiveis.push(new PecaCadeira("Parafuso - 3", 12));
        pecasDisponiveis.push(new PecaCadeira("Arruela - 3", 11));
        pecasDisponiveis.push(new PecaCadeira("Acentos", 10));
        pecasDisponiveis.push(new PecaCadeira("Perna da mesa - 3", 9));
        
        // Grupo 3: Segunda perna (peças 5-8)
        pecasDisponiveis.push(new PecaCadeira("Parafuso - 2", 8));
        pecasDisponiveis.push(new PecaCadeira("Arruela - 2", 7));
        pecasDisponiveis.push(new PecaCadeira("Acentos", 6));
        pecasDisponiveis.push(new PecaCadeira("Perna da mesa - 2", 5));
        
        // Grupo 2: Primeira perna (peças 1-4)
        pecasDisponiveis.push(new PecaCadeira("Parafuso - 1", 4));
        pecasDisponiveis.push(new PecaCadeira("Arruela - 1", 3));
        pecasDisponiveis.push(new PecaCadeira("Acentos", 2));
        pecasDisponiveis.push(new PecaCadeira("Perna da mesa - 1", 1));
    }

    /**
     * Realiza o processo de montagem da cadeira, transferindo as peças
     * da pilha de peças disponíveis para a pilha de cadeira montada.
     * O processo utiliza uma pilha temporária para inverter a ordem
     * das peças, garantindo a montagem na sequência correta.
     * 
     * Processo de montagem:
     * 1. Inverte a ordem das peças usando uma pilha temporária
     * 2. Monta cada peça na ordem correta
     * 3. Registra cada etapa da montagem no log do sistema
     * 
     * @throws IllegalStateException se não houver peças disponíveis para montagem
     * @see PecaCadeira
     */
    public void montarCadeira() {
        System.out.println("\nIniciando processo de montagem da cadeira...");
        
        // Pilha temporária para inverter a ordem das peças
        Stack<PecaCadeira> pecasTemp = new Stack<>();
        
        // Primeira inversão: transfere todas as peças para pilha temporária
        while (!pecasDisponiveis.isEmpty()) {
            pecasTemp.push(pecasDisponiveis.pop());
        }
        
        // Segunda inversão: monta a cadeira na ordem correta
        while (!pecasTemp.isEmpty()) {
            PecaCadeira peca = pecasTemp.pop();
            cadeiraMontada.push(peca);
            // Registro do processo de montagem
            System.out.println("Montando: " + peca.nome);
        }
    }

/**
 * Verifica se a cadeira foi montada na ordem correta.
 * O processo de verificação compara a ordem de cada peça montada com a sequência esperada,
 * preservando a estrutura original da cadeira após a verificação.
 * 
 * Processo de verificação:
 * 1. Transfere temporariamente as peças para uma pilha auxiliar
 * 2. Verifica a ordem de cada peça
 * 3. Restaura a cadeira ao estado original
 * 
 * @return boolean Retorna true se a montagem está correta, false caso contrário
 * @throws IllegalStateException se a pilha da cadeira montada estiver vazia
 */
public boolean verificarMontagem() {
    System.out.println("\nVerificando montagem...");
    
    // Pilha temporária para preservar a estrutura durante a verificação
    Stack<PecaCadeira> temp = new Stack<>();
    boolean montagemCorreta = true;
    int ordemEsperada = 1;

    // Primeira fase: Verificação da ordem das peças
    while (!cadeiraMontada.isEmpty()) {
        PecaCadeira peca = cadeiraMontada.pop();
        // Verifica se a peça atual está na ordem esperada
        if (peca.ordem != ordemEsperada) {
            montagemCorreta = false;
            System.out.println("Erro na montagem! Peça " + peca.nome + 
                             " está na posição errada.");
        }
        // Preserva a peça na pilha temporária
        temp.push(peca);
        ordemEsperada++;
    }

    // Segunda fase: Restauração da cadeira ao estado original
    while (!temp.isEmpty()) {
        cadeiraMontada.push(temp.pop());
    }

    return montagemCorreta;
}

/**
 * Realiza o processo de desmontagem da cadeira.
 * Retira cada peça da cadeira montada na ordem inversa da montagem
 * e as coloca de volta na pilha de peças disponíveis.
 * 
 * Processo de desmontagem:
 * 1. Remove peças uma a uma do topo da pilha de cadeira montada
 * 2. Registra cada peça removida no log
 * 3. Adiciona as peças de volta à pilha de peças disponíveis
 * 
 * Observações:
 * - A ordem de desmontagem é naturalmente inversa à ordem de montagem
 * - As peças ficam disponíveis para uma nova montagem após o processo
 * 
 * @throws IllegalStateException se tentar desmontar uma cadeira que não está montada
 * @see PecaCadeira
 * @see #montarCadeira()
 */
public void desmontarCadeira() {
    System.out.println("\nIniciando processo de desmontagem...");
    
    // Remove cada peça da cadeira montada e a coloca nas peças disponíveis
    while (!cadeiraMontada.isEmpty()) {
        PecaCadeira peca = cadeiraMontada.pop();
        System.out.println("Desmontando: " + peca.nome);
        pecasDisponiveis.push(peca);
    }
}

    /**
     * Método principal que controla o fluxo de execução do programa.
     * Permite ao usuário especificar a quantidade de cadeiras a serem montadas
     * e gerencia o ciclo completo de montagem, verificação e desmontagem para cada cadeira.
     * 
     * Fluxo do programa:
     * 1. Solicita ao usuário o número de cadeiras
     * 2. Valida a entrada (deve ser maior que zero)
     * 3. Para cada cadeira:
     *    - Realiza a montagem
     *    - Verifica se está correta
     *    - Realiza a desmontagem
     * 
     * @param args Argumentos da linha de comando (não utilizados)
     * @throws InputMismatchException se a entrada do usuário não for um número inteiro
     * @throws IllegalArgumentException se o número de cadeiras for menor ou igual a zero
     */
    public static void main(String[] args) {
        // Inicialização do scanner para leitura de entrada do usuário
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Solicitação e leitura do número de cadeiras
            System.out.print("Quantas cadeiras deseja montar? ");
            int numeroCadeiras = scanner.nextInt();

            // Validação da entrada
            if (numeroCadeiras <= 0) {
                throw new IllegalArgumentException("Número inválido de cadeiras!");
            }

            // Processo iterativo de montagem para cada cadeira
            for (int indiceCadeira = 1; indiceCadeira <= numeroCadeiras; indiceCadeira++) {
                System.out.println("\n=== Cadeira " + indiceCadeira + " ===");
                
                // Criação de nova instância para cada cadeira
                MontadorCadeira montador = new MontadorCadeira();
                
                // Fase 1: Montagem da cadeira
                montador.montarCadeira();
                
                // Fase 2: Verificação da montagem
                boolean montagemCorreta = montador.verificarMontagem();
                System.out.println(montagemCorreta ? 
                    "Montagem realizada corretamente!" : 
                    "Montagem com erros!");
                
                // Fase 3: Desmontagem da cadeira
                montador.desmontarCadeira();
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: Por favor, insira um número inteiro válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            // Fechamento do scanner para liberar recursos
            scanner.close();
        }
    }
}