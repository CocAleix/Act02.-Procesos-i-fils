package Act02;

import java.util.Random;
import java.util.Scanner;

public class MaximoVector {

    static class HiloBuscador extends Thread {
        private int[] vector;
        private int inicio;
        private int fin;
        private int maximoLocal;
        private int idHilo;

        public HiloBuscador(int id, int[] vector, int inicio, int fin) {
            this.idHilo = id;
            this.vector = vector;
            this.inicio = inicio;
            this.fin = fin;
            this.maximoLocal = Integer.MIN_VALUE;
        }

        public int getMaximoLocal() {
            return maximoLocal;
        }

        public int getIdHilo() {
            return idHilo;
        }

        @Override
        public void run() {
            for (int i = inicio; i < fin; i++) {
                if (vector[i] > maximoLocal) {
                    maximoLocal = vector[i];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        // Entrada de datos
        System.out.print("Introduce el tamaño del vector: ");
        int tamVector = sc.nextInt();
        System.out.print("Introduce el número de hilos: ");
        int numHilos = sc.nextInt();

        // Inicialización del vector con aleatorios
        int[] vector = new int[tamVector];
        for (int i = 0; i < tamVector; i++) {
            vector[i] = rnd.nextInt(10000); // Aleatorios 0-9999
        }

        // Reparto de carga
        int rango = tamVector / numHilos; // Tamaño para cada hilo
        HiloBuscador[] hilos = new HiloBuscador[numHilos];

        // Crear y lanzar hilos
        for (int i = 0; i < numHilos; i++) {
            int inicio = i * rango;
            int fin = inicio + rango;
            // Ojo: Si es el último hilo, NO le damos el resto. 
            // El enunciado dice que las posiciones sobrantes las procesa el MAIN.
            
            hilos[i] = new HiloBuscador(i, vector, inicio, fin);
            hilos[i].start();
        }

        // Esperar hilos y buscar el ganador entre ellos
        int maxGlobal = Integer.MIN_VALUE;
        String quienLoEncontro = "Nadie";

        try {
            for (HiloBuscador h : hilos) {
                h.join(); // Sincronización
                if (h.getMaximoLocal() > maxGlobal) {
                    maxGlobal = h.getMaximoLocal();
                    quienLoEncontro = "Hilo " + h.getIdHilo();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Procesar posiciones sobrantes (Resto de la división) por el MAIN
        int inicioSobrante = numHilos * rango;
        if (inicioSobrante < tamVector) {
            System.out.println("El proceso principal analiza desde la posición " + inicioSobrante + " hasta el final.");
            for (int i = inicioSobrante; i < tamVector; i++) {
                if (vector[i] > maxGlobal) {
                    maxGlobal = vector[i];
                    quienLoEncontro = "Programa Principal (Main)";
                }
            }
        }

        // Resultado final
        System.out.println("--------------------------------");
        System.out.println("Valor Máximo encontrado: " + maxGlobal);
        System.out.println("Encontrado por: " + quienLoEncontro);
        
        sc.close();
    }
}