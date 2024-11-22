package pilhacomlista2;

public class Peca {
    private String nome;
    private int numero;
    private int numeroCadeira;

    public Peca(String nome, int numero, int numeroCadeira) {
        this.nome = nome;
        this.numero = numero;
        this.numeroCadeira = numeroCadeira;
    }

    public String getNome() {
        return nome;
    }

    public int getNumero() {
        return numero;
    }

    public Integer getNumeroCadeira() {
        return numeroCadeira;
    }

    @Override
    public String toString() {
        return "Peca: " +
                "nome='" + nome + '\'' +
                ", numero=" + numero +
                ", numeroCadeira=" + numeroCadeira;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Peca peca = (Peca) obj;

        if (numero != peca.numero) return false;
        if (numeroCadeira != peca.numeroCadeira) return false;
        return nome != null ? nome.equals(peca.nome) : peca.nome == null;
    }

    @Override
    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + numero;
        result = 31 * result + numeroCadeira;
        return result;
    }
}
