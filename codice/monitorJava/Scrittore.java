public class Scrittore extends Thread {
    private Monitor monitor;
    private Memoria memoria;

    public Scrittore(Monitor monitor, Memoria memoria) {

        this.monitor = monitor;
        this.memoria = memoria;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 3001)); // attesa casuale per i test
            monitor.iniziaScrittura();
            System.out.println("Scrittore " + getName() + " moltiplica per un valore casuale tra 1 e 10 ");
            int valore = (int) (Math.random() * 10 + 1);
            memoria.scrivi(valore);
            monitor.terminaScrittura();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
