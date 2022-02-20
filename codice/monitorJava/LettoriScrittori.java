public class LettoriScrittori {
    public static void main(String[] args) {

        final int NUMERO_LETTORI = 10;
        final int NUMERO_SCRITTORI = 5;

        // Creo le risorse
        Monitor monitor = new Monitor();
        Memoria memoriaCondivisa = new Memoria();

        Scrittore scrittore;
        Lettore lettore;

        // Creazione degli oggetti (threads) lettori e scrittori
        for (int i = 0; i < NUMERO_SCRITTORI; i++) {
            scrittore = new Scrittore(monitor, memoriaCondivisa);
            scrittore.start();
        }

        for (int i = 0; i < NUMERO_LETTORI; i++) {
            lettore = new Lettore(monitor, memoriaCondivisa);
            lettore.start();
        }

    }
}