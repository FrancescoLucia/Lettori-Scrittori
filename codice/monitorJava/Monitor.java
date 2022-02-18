import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private int numeroLettori = 0;
    private int numeroScrittori = 0;

    private int lettoriInAttesa = 0;
    private int scrittoriInAttesa = 0;

    private Lock lock = new ReentrantLock(); // Lock per l'aggiornamento delle variabili
    private Condition condScrittura = lock.newCondition();
    private Condition condLettura = lock.newCondition();

    // Metodo che uno scrittore chiama prima di iniziare a scrivere
    public void iniziaScrittura() throws InterruptedException {
        lock.lock();

        if (numeroScrittori == 1 || numeroLettori > 0) { // se c'è già uno scrittore o uno o più lettori
            ++numeroScrittori;
            condScrittura.await(); // Attendo il permesso per scrivere
            --numeroScrittori;
        }

        numeroScrittori = 1; // Ora uno scrittore sta scrivendo
        lock.unlock();
    }

    // Metodo che uno scrittore chiama quando ha terminato di scrivere
    public void terminaScrittura() {
        lock.lock();
        numeroScrittori = 0;

        if (lettoriInAttesa > 0) { // Se ci sono lettori in attesa gli consento la lettura, altrimenti consento la scrittura al prossimo lettore
            condLettura.signal();
        } else {
            condScrittura.signal();
        }
        lock.unlock();
    }

    // Metodo che il lettore chiama prima di iniziare a leggere
    public void iniziaLettura() throws InterruptedException {
        lock.lock();

        if (numeroScrittori == 1 || scrittoriInAttesa > 1) { // se un thread sta scrivendo o ci sono uno o più scrittori in attesa
            ++lettoriInAttesa;
            condLettura.await(); // attendo il permesso per leggere
            --lettoriInAttesa;
        }
        ++numeroLettori;
        condLettura.signal(); // anche altri thread possono leggere contemporaneamente
        lock.unlock();
    }

    // Metodo che il lettore chiama quando ha terminato di leggere
    public void terminaLettura() {
        lock.lock();

        --numeroLettori; // c'è un lettore in meno
        if (numeroLettori == 0) {
            condScrittura.signal(); // se il lettore era l'ultimo, mando una signal allo scrittore
        }
        lock.unlock();
    }
}