public class CadeiraGUI {
    private Pilha pilha = new Pilha();
    private int etapaAtual = 0;

    private String[] sequenciaMontagem = {
        "Perna da mesa - 1", "Acento", "Arruela - 1", "Parafuso - 1",
        "Perna da mesa - 2", "Acento", "Arruela - 2", "Parafuso - 2",
        "Perna da mesa - 3", "Acento", "Arruela - 3", "Parafuso - 3",
        "Perna da mesa - 4", "Acento", "Arruela - 4", "Parafuso - 4",
        "Suporte para encosto", "Encosto"
    };

    public void adicionarPasso(String passo, int quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            pilha.push(passo + " - " + i);
        }
    }

    public String verificarPasso() {
        if (etapaAtual < sequenciaMontagem.length) {
            String esperado = sequenciaMontagem[etapaAtual];
            Object atual = pilha.removerFinal();

            etapaAtual++;
            return (esperado.equals(atual))
                ? "Passo correto: " + esperado
                : "Erro no passo! Esperado: " + esperado + ", Obtido: " + atual;
        }
        return "Todos os passos já foram realizados.";
    }

    public String verificarMontagemCompleta() {
        return pilha.pEmpty()
            ? "Montagem completa e correta!"
            : "Ainda restam itens na pilha, montagem incompleta!";
    }

    public void limpar() {
        pilha = new Pilha();
        etapaAtual = 0;
    }
}
