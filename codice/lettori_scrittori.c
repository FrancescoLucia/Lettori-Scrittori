#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#define NUMERO_LETTORI 12
#define NUMERO_SCRITTORI 7

sem_t semaforo_scrittura;
pthread_mutex_t mutex; // per la sezione critica relativa all'aggiornamento della variabile numero_lettori
int variabile_condivisa = 1;
int numero_lettori = 0; // lettori contemporaneamente attivi

float randomFloat( float min, float max )
{
    float scale = rand() / (float) RAND_MAX; /* [0, 1.0] */
    return min + scale * ( max - min );      /* [min, max] */
}

void *scrittore(void* id_thread) {
    sleep(randomFloat(0, 3));
    //sleep(0.9); // sleep per ritardare l'esecuzione degli scrittori (a scopo di testing)
    unsigned long tid = (unsigned long) id_thread;
    sem_wait(&semaforo_scrittura); // attende la possibilità di scrivere
    variabile_condivisa *= rand()%10+1;
    printf("Scrittore %lu ha moltiplicato la variabile per un numero casuale\n", tid);
    sem_post(&semaforo_scrittura); // rilascia il semaforo 
    
}

void *lettore(void* id_thread) {
    sleep(randomFloat(0, 3));
    unsigned long tid = (unsigned long)id_thread;
    pthread_mutex_lock(&mutex); // entra nella sezione critica
    numero_lettori++;
    if (numero_lettori == 1) {
	    // se si tratta del primo lettore attende che la scrittura termini
        sem_wait(&semaforo_scrittura);
    }
    pthread_mutex_unlock(&mutex);

    printf("Lettore %lu - valore letto -> %d\n", tid, variabile_condivisa);

    pthread_mutex_lock(&mutex);
    numero_lettori--;
    if (numero_lettori == 0) {
	    // se si tratta dell'ultimo lettore rilascia il semaforo per la scrittura
        sem_post(&semaforo_scrittura);
    }
    pthread_mutex_unlock(&mutex);
}

int main() {
    srand(time(NULL));

    pthread_t lettori[NUMERO_LETTORI], scrittori[NUMERO_SCRITTORI]; // crea i threads
    // inizializza il semaforo e il mutex
    pthread_mutex_init(&mutex, NULL);
    sem_init(&semaforo_scrittura, 0, 1);

    // Creazione Threads
    for (unsigned long i = 0; i < NUMERO_SCRITTORI; i++) {
        pthread_create(&scrittori[i], NULL, scrittore, (void*)i);
    }

    for (unsigned long i = 0; i < NUMERO_LETTORI; i++) {
        pthread_create(&lettori[i], NULL, lettore, (void*)i);
    }
	
    // Join sui threads in esecuzione
    for (unsigned long j = 0; j < NUMERO_SCRITTORI; j++) {
        pthread_join(scrittori[j], NULL);
    }

    for (unsigned long h = 0; h < NUMERO_LETTORI; h++) {
        pthread_join(lettori[h], NULL);
    }

    // Libera le risorse
    sem_destroy(&semaforo_scrittura);
    pthread_mutex_destroy(&mutex);
    return 0;
}
