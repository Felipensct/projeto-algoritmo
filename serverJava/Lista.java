public class Lista {
    private Object[] elementos = new Object[100]; // Armazena os elementos
    private int total = 0; // Quantidade atual de elementos na lista

    // Adiciona um elemento à lista
    public void add(Object dados) {
        if (total >= elementos.length) {
            throw new IllegalStateException("Lista cheia! Não é possível adicionar mais elementos.");
        }
        this.elementos[this.total] = dados;
        this.total++;
    }

    // Verifica se a lista está vazia
    public boolean vazia() {
        return total == 0;
    }

    // Retorna o elemento na posição especificada
    public Object pega(int posicao) {
        if (posicao < 0 || posicao >= total) {
            throw new IllegalArgumentException("**POSIÇÃO INVÁLIDA**");
        }
        return elementos[posicao];
    }

    // Retorna o número de elementos na lista
    public int tamanho() {
        return total;
    }

    // Remove o elemento na posição especificada
    public void remove(int posicao) {
        if (posicao < 0 || posicao >= total) {
            throw new IllegalArgumentException("**POSIÇÃO INVÁLIDA**");
        }
        for (int i = posicao; i < total - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        elementos[total - 1] = null; // Limpa o último elemento
        total--;
    }

    // Retorna uma representação em String de todos os elementos da lista
    @Override
    public String toString() {
        if (vazia()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < total; i++) {
            sb.append(elementos[i]);
            if (i < total - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
