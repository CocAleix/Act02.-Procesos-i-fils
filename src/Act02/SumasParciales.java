package Act02;

import java.util.Scanner;

public class SumasParciales {

    // Clase que define el comportamiento del hilo
    static class HiloSuma extends Thread {
        private int id;
        private long resultado;
        // Objeto estático para sincronizar la entrada por consola
        private static final Object lock = new Object(); 

        public HiloSuma(int id) {
            this.id = id;
        }

        public long getResultado() {
            return resultado;
        }

        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            int n = 0;

            // Sincronizamos para que los mensajes no se mezclen
            synchronized (lock) {
                System.out.print("Hilo " + id + " > Introduce un número N: ");
                try {
                    // Leemos el número que introducirá el usuario para este hilo
                    if (sc.hasNextInt()) {
                        n = sc.nextInt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Realizamos la suma de 1 a N
            long suma = 0;
            for (int i = 1; i <= n; i++) {
                suma += i;
            }
            this.resultado = suma;
        }
    }

    public static void main(String[] args) {
        Scanner scMain = new Scanner(System.in);

        // --- CAMBIO: Ahora pedimos el número por teclado en lugar de args[0] ---
        System.out.print("Introduce el número de hilos a crear: ");
        int numHilos = scMain.nextInt();

        HiloSuma[] hilos = new HiloSuma[numHilos];

        System.out.println("--- Iniciando " + numHilos + " hilos ---");

        // 1. Crear y arrancar los hilos
        for (int i = 0; i < numHilos; i++) {
            hilos[i] = new HiloSuma(i);
            hilos[i].start();
        }

        // 2. Esperar a que todos terminen (join) y mostrar resultados
        try {
            for (int i = 0; i < numHilos; i++) {
                hilos[i].join(); // El main espera aquí
                System.out.println("El hilo " + i + " ha calculado la suma: " + hilos[i].getResultado());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("--- Fin del programa ---");
        // No cerramos scMain aquí para evitar cerrar System.in si otros hilos lo usan, 
        // aunque en este punto el programa ya termina.
    }
}
