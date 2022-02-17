public class Scrittore extends Thread{
    private Monitor monitor;
    private Memoria memoria;

    public Scrittore(Monitor monitor, Memoria memoria) {
        
        this.monitor = monitor;
        this.memoria = memoria;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random()*3001));
            monitor.iniziaScrittura();
            System.out.println("Scrittore " + getName() + " moltiplica per 2 ");
            memoria.scrivi(2);
            monitor.terminaScrittura();

        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
