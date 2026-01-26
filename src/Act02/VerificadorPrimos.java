package Act02;

import java.util.ArrayList;
import java.util.Scanner;

public class VerificadorPrimos {

    static class HiloPrimo extends Thread {
        private int numero;
        private boolean esPrimo;

        public HiloPrimo(int numero) {
            this.numero = numero;
        }

        public boolean isEsPrimo() {
            return esPrimo;
        }

        public int getNumero() {
            return numero;
        }

        @Override
        public void run() {
            this.esPrimo = checkPrimo(this.numero);
        }

        private boolean checkPrimo(int n) {
            if (n <= 1) return false;
            if (n == 2) return true;
            if (n % 2 == 0) return false;
            // Comprobamos impares hasta la raíz cuadrada
            for (int i = 3; i <= Math.sqrt(n); i += 2) {
                if (n % i == 0) return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<HiloPrimo> listaHilos = new ArrayList<>();

        System.out.println("Introduce números para verificar si son primos.");
        System.out.println("Introduce -1 para finalizar la entrada y comenzar el proceso.");

        while (true) {
            System.out.print("Número: ");
            int n = sc.nextInt();
            
            if (n == -1) break;

            // Creamos el hilo para este número pero NO lo arrancamos aún (o podríamos arrancarlo ya).
            // El enunciado dice "crear un fil per cada número", lo añadimos a la lista.
            HiloPrimo h = new HiloPrimo(n);
            listaHilos.add(h);
        }

        System.out.println("--- Iniciando verificación concurrente ---");

        // Arrancamos todos los hilos
        for (HiloPrimo h : listaHilos) {
            h.start();
        }

        // Esperamos y mostramos resultados en el orden introducido
        try {
            for (HiloPrimo h : listaHilos) {
                h.join();
                String estado = h.isEsPrimo() ? "ES PRIMO" : "NO ES PRIMO";
                System.out.println("El número " + h.getNumero() + " " + estado);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("--- Fin del proceso ---");
        sc.close();
    }
}