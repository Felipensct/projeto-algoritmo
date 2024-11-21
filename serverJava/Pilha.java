public class Pilha {
    private Lista lista = new Lista();

    // Insere um elemento no topo da pilha
    public void push(Object objeto) {
        lista.add(objeto);
    }

    // Verifica se a pilha está vazia
    public boolean pEmpty() {
        return lista.vazia();
    }

    // Remove e devolve o elemento do topo da pilha
    public Object removerFinal() {
        if (!pEmpty()) {
            Object objeto = lista.pega(lista.tamanho() - 1);
            lista.remove(lista.tamanho() - 1);
            return objeto;
        }
        return null;
    }

    // Obtém o tamanho da pilha
    public int tamanho() {
        return lista.tamanho();
    }

    // Obtém um elemento da pilha por índice
    public Object pegar(int indice) {
        return lista.pega(indice);
    }
}
