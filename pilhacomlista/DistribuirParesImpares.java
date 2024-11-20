package pilhacomlista;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class DistribuirParesImpares {

    public void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Stack<Integer> pilha = new Stack<>();
        Queue<Integer> filaPares = new LinkedList<>();
        Queue<Integer> filaImpares = new LinkedList<>();

        // Entrada de dados na pilha
        System.out.println("Insira números inteiros (digite um número negativo para encerrar):");
        while (true) {
            int numero = scanner.nextInt();
            if (numero < 0) {
                break;  // Encerrar a entrada ao receber um número negativo
            }
            pilha.push(numero);
        }

        // Transferir os números da pilha para as filas de pares e ímpares
        Stack<Integer> pilhaOriginal = (Stack<Integer>) pilha.clone();  // Clonamos a pilha para exibição posterior

        while (!pilha.isEmpty()) {
            int numero = pilha.pop();
            if (numero % 2 == 0) {
                filaPares.add(numero);
            } else {
                filaImpares.add(numero);
            }
        }

        // Saída da pilha original
        System.out.println("\nPilha digitada:");
        while (!pilhaOriginal.isEmpty()) {
            System.out.println(pilhaOriginal.pop());
        }

        // Saída da fila de números pares
        if (filaPares.isEmpty()) {
            System.out.println("\nFila de números pares está vazia.");
        } else {
            System.out.println("\nFila de números pares:");
            for (int num : filaPares) {
                System.out.println(num);
            }
        }

        // Saída da fila de números ímpares
        if (filaImpares.isEmpty()) {
            System.out.println("\nFila de números ímpares está vazia.");
        } else {
            System.out.println("\nFila de números ímpares:");
            for (int num : filaImpares) {
                System.out.println(num);
            }
        }

        scanner.close();
    }
}
