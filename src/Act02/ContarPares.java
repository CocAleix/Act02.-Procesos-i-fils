package Act02;

import java.util.Random;
import java.util.Scanner;

public class ContarPares {

    static class HiloContador extends Thread {
        private int[] vector;
        private int inicio;
        private int fin;
        private int cuentaParcial;

        public HiloContador(int[] vector, int inicio, int fin) {
            this.vector = vector;
            this.inicio = inicio;
            this.fin = fin;
            this.cuentaParcial = 0;
        }

        public int getCuentaParcial() {
            return cuentaParcial;
        }

        @Override
        public void run() {
            for (int i = inicio; i < fin; i++) {
                if (vector[i] % 2 == 0) {
                    cuentaParcial++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        System.out.print("Tamaño del vector: ");
        int tam = sc.nextInt();
        System.out.print("Número de hilos: ");
        int numHilos = sc.nextInt();

        int[] vector = new int[tam];
        for (int i = 0; i < tam; i++) vector[i] = rnd.nextInt(100);

        HiloContador[] hilos = new HiloContador[numHilos];
        int rango = tam / numHilos; 
        int resto = tam % numHilos;

        int indiceActual = 0;

        // Estrategia de reparto: Asignamos rangos.
        // El enunciado dice "El proceso principal será el que realizará la SUMA de todos los resultados".
        // Así que dejamos que los hilos cuenten y el main solo agrega totales.
        
        for (int i = 0; i < numHilos; i++) {
            int fin = indiceActual + rango;
            
            // Para asegurar que se procesa todo el vector, añadimos el resto al último hilo
            // (Opcional: Si quisiéramos seguir la lógica estricta del ej anterior, el resto lo contaría el main).
            // Aquí asignaremos el resto al último hilo para que los hilos hagan todo el trabajo de conteo.
            if (i == numHilos - 1) {
                fin += resto;
            }

            hilos[i] = new HiloContador(vector, indiceActual, fin);
            hilos[i].start();
            indiceActual = fin;
        }

        int totalPares = 0;
        try {
            for (int i = 0; i < numHilos; i++) {
                hilos[i].join(); // Esperamos a que termine
                int parcial = hilos[i].getCuentaParcial();
                System.out.println("Hilo " + i + " encontró " + parcial + " pares.");
                totalPares += parcial; // El main realiza la suma total
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total de números pares en el vector: " + totalPares);
        sc.close();
    }
}
