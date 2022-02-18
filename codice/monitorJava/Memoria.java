// CLasse con il solo scopo di consentire l'accesso alla memoria condivisa globalmente attraverso l'unica istanza dell'oggetto Memoria nel main
public class Memoria {

    private int valoreCondiviso = 1;

    public void scrivi(int valore) {
        this.valoreCondiviso *= valore;
    }

    public int leggi() {
        return this.valoreCondiviso;
    }
}