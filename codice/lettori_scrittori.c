#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>

#define NUMERO_LETTORI 10
#define NUMERO_SCRITTORI 5

sem_t semaforo_scrittura;
pthread_mutex_t mutex;
int variabile_condivisa = 0;
int numero_lettori = 0;

void *scrittore(void* id_thread) {
    unsigned long tid = (unsigned long) id_thread;
    sem_wait(&semaforo_scrittura);
    variabile_condivisa += 10;
    printf("Scrittore %lu ha incrementato la variabile di 10\n", tid);
    sem_post(&semaforo_scrittura);
}

void *lettore(void* id_thread) {
    unsigned long tid = (unsigned long)id_thread;
    pthread_mutex_lock(&mutex);
    numero_lettori++;
    if (numero_lettori == 1) {
        sem_wait(&semaforo_scrittura);
    }
    pthread_mutex_unlock(&mutex);

    printf("Lettore %lu - valore letto -> %d\n", tid, variabile_condivisa);

    pthread_mutex_lock(&mutex);
    numero_lettori--;
    if (numero_lettori == 0) {
        sem_post(&semaforo_scrittura);
    }
    pthread_mutex_unlock(&mutex);
}

int main() {
    pthread_t lettori[NUMERO_LETTORI], scrittori[NUMERO_SCRITTORI];
    pthread_mutex_init(&mutex, NULL);
    sem_init(&semaforo_scrittura, 0, 1);

    for (unsigned long i = 0; i < NUMERO_SCRITTORI; i++) {
        pthread_create(&scrittori[i], NULL, scrittore, (void*)i);
    }

    for (unsigned long i = 0; i < NUMERO_LETTORI; i++) {
        pthread_create(&lettori[i], NULL, lettore, (void*)i);
    }

    for (unsigned long j = 0; j < NUMERO_SCRITTORI; j++) {
        pthread_join(scrittori[j], NULL);
    }

    for (unsigned long h = 0; h < NUMERO_LETTORI; h++) {
        pthread_join(lettori[h], NULL);
    }
}
