package pilhacomlista;

public class TestePilha {
    public static void main(String[] args) {
        Pilha pilha = new Pilha();

        System.out.println("A pilha está vazia? " + pilha.pEmpty()); // Esperado: true

        pilha.push("Primeiro");
        pilha.push("Segundo");
        pilha.push("Terceiro");

        System.out.println("A pilha está vazia? " + pilha.pEmpty()); // Esperado: false

        System.out.println("Elemento removido: " + pilha.removerFinal()); // Esperado: "Terceiro"
        System.out.println("Elemento removido: " + pilha.removerFinal()); // Esperado: "Segundo"
        System.out.println("Elemento removido: " + pilha.removerFinal()); // Esperado: "Primeiro"

        System.out.println("A pilha está vazia? " + pilha.pEmpty()); // Esperado: true

        pilha.push("Primeiro");
        pilha.push("Segundo");
        pilha.push("Terceiro");

        System.out.println("A pilha está vazia? " + pilha.pEmpty()); // Esperado: false

        System.out.println("Elemento removido: " + pilha.removerProximo()); // Esperado: "Primeiro"
        System.out.println("Elemento removido: " + pilha.removerProximo()); // Esperado: "Segundo"
        System.out.println("Elemento removido: " + pilha.removerProximo()); // Esperado: "Terceiro"

        System.out.println("A pilha está vazia? " + pilha.pEmpty()); // Esperado: true

        System.out.println("Tentativa de remover de uma pilha vazia: " + pilha.removerProximo()); // Esperado: null
    }
}
