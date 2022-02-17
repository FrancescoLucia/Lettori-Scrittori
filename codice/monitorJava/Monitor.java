import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private int numeroLettori = 0;
    private int numeroScrittori = 0;

    private int lettoriInAttesa = 0;
    private int scrittoriInAttesa = 0;

    private Lock lock = new ReentrantLock();
    private Condition condScrittura = lock.newCondition();
    private Condition condLettura = lock.newCondition();

    public void iniziaScrittura() throws InterruptedException {
        lock.lock();

        if (numeroScrittori == 1 || numeroLettori > 0) {
            ++numeroScrittori;
            condScrittura.await();
            --numeroScrittori;
        }

        numeroScrittori = 1;
        lock.unlock();
    }

    public void terminaScrittura() {
        lock.lock();
        numeroScrittori = 0;

        if (lettoriInAttesa > 0) {
            condLettura.signal();
        } else {
            condScrittura.signal();
        }
        lock.unlock();
    }

    public void iniziaLettura() throws InterruptedException {
        lock.lock();

        if (numeroScrittori == 1 || scrittoriInAttesa > 1) {
            ++lettoriInAttesa;
            condLettura.await();
            --lettoriInAttesa;
        }
        ++numeroLettori;
        condLettura.signal();
        lock.unlock();
    }

    public void terminaLettura() {
        lock.lock();

        --numeroLettori;
        if (numeroLettori == 0) {
            condScrittura.signal();
        }
        lock.unlock();
    }
}