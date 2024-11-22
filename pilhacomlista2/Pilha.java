package pilhacomlista2;

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
        return null; // Retorna null se a pilha estiver vazia
    }

        // Remove e devolve o elemento do final da pilha
        public Object removerProximo() {
            if (!pEmpty()) {
                Object objeto = lista.pega(0);
                lista.remove(0);
                return objeto;
            }
            return null; // Retorna null se a pilha estiver vazia
        }
}
