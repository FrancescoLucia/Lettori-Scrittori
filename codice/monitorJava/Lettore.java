public class Lettore extends Thread {

    private Monitor monitor;
    private Memoria memoria;

    public Lettore(Monitor monitor, Memoria memoria) {

        this.memoria = memoria;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 3001)); // attesa casuale per i test
            monitor.iniziaLettura();
            System.out.println("Lettore " + getName() + " - valore -> " + memoria.leggi());
            monitor.terminaLettura();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
