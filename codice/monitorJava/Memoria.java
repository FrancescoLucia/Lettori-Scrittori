public class Memoria {

    private int valoreCondiviso = 1;

    public void scrivi(int valore) {
        this.valoreCondiviso *= valore;
    }

    public int leggi() {
        return this.valoreCondiviso;
    }
}