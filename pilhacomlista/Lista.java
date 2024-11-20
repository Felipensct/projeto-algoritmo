package pilhacomlista;

public class Lista {
    private Object[] elementos = new Object[100];
    private int total = 0;

    public void add(Object dados) {
        this.elementos[this.total] = dados;
        this.total++;
    }

    public boolean vazia() {
        return total == 0;
    }

    public Object pega(int posicao) {
        if (posicao < 0 || posicao >= total) {
            throw new IllegalArgumentException("**POSICAO INVALIDA**");
        }
        return elementos[posicao];
    }

    public int tamanho() {
        return total;
    }

    public void remove(int posicao) {
        if (posicao < 0 || posicao >= total) {
            throw new IllegalArgumentException("**POSICAO INVALIDA**");
        }
        for (int i = posicao; i < total - 1; i++) {
            elementos[i] = elementos[i + 1];
        }
        total--;
    }
}
