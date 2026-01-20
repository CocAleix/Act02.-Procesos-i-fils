package Act02;

import java.util.Scanner;

public class FibonacciConcurrent {

    static class HiloFibonacci extends Thread {
        private int index;
        private long resultado;

        public HiloFibonacci(int index) {
            this.index = index;
        }

        public long getResultado() {
            return resultado;
        }

        @Override
        public void run() {
            // Calculamos Fibonacci iterativo (más eficiente que recursivo para hilos)
            this.resultado = calcularFibonacci(this.index);
        }

        private long calcularFibonacci(int n) {
            if (n <= 0) return 0;
            if (n == 1) return 1;

            long a = 0, b = 1;
            for (int i = 2; i <= n; i++) {
                long temp = a + b;
                a = b;
                b = temp;
            }
            return b;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce el número de hilos (elementos Fibonacci) a crear: ");
        int numHilos = sc.nextInt();

        HiloFibonacci[] hilos = new HiloFibonacci[numHilos];

        // 1. Crear y arrancar hilos. 
        // El hilo 0 calcula Fib(0), el hilo 1 Fib(1), etc.
        for (int i = 0; i < numHilos; i++) {
            hilos[i] = new HiloFibonacci(i);
            hilos[i].start();
        }

        System.out.println("Calculando...");

        // 2. Sincronización y muestra de resultados
        try {
            for (int i = 0; i < numHilos; i++) {
                hilos[i].join(); // Esperar
                System.out.println("Hilo " + i + " -> Fibonacci(" + i + ") = " + hilos[i].getResultado());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        sc.close();
    }
}
