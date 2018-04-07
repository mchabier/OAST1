import java.io.Serializable;

public class Ustawienia implements Serializable {

    public Ustawienia() {}

    private String plikWejsciowy;
    private String plikWyjsciowy;
    private int maxLiczbaIteracji;
    private int liczbaRozwiazanPoczątkowych;
    private int ileWybieramyDoReprodukcji;
    private int ziarnoWyboruRodzicaDoReprodukcji;
    private double prawdopodobienstwoKrzyzowania;
    private int ziarnoKrzyzowania;
    private double prawdopodobienstwoMutacji;
    private int ziarnoMutacji;
    private int ziarnoGenerowaniaRozwiazan;

    public String getPlikWejsciowy() {
        return plikWejsciowy;
    }

    public void setPlikWejsciowy(String plikWejsciowy) {
        this.plikWejsciowy = plikWejsciowy;
    }

    public String getPlikWyjsciowy() {
        return plikWyjsciowy;
    }

    public void setPlikWyjsciowy(String plikWyjsciowy) {
        this.plikWyjsciowy = plikWyjsciowy;
    }

    public int getMaxLiczbaIteracji() {
        return maxLiczbaIteracji;
    }

    public void setMaxLiczbaIteracji(int maxLiczbaIteracji) {
        this.maxLiczbaIteracji = maxLiczbaIteracji;
    }

    public int getLiczbaRozwiazanPoczątkowych() {
        return liczbaRozwiazanPoczątkowych;
    }

    public void setLiczbaRozwiazanPoczątkowych(int liczbaRozwiazanPoczątkowych) {
        this.liczbaRozwiazanPoczątkowych = liczbaRozwiazanPoczątkowych;
    }

    public int getIleWybieramyDoReprodukcji() {
        return ileWybieramyDoReprodukcji;
    }

    public void setIleWybieramyDoReprodukcji(int ileWybieramyDoReprodukcji) {
        this.ileWybieramyDoReprodukcji = ileWybieramyDoReprodukcji;
    }

    public int getZiarnoWyboruRodzicaDoReprodukcji() {
        return ziarnoWyboruRodzicaDoReprodukcji;
    }

    public void setZiarnoWyboruRodzicaDoReprodukcji(int ziarnoWyboruRodzicaDoReprodukcji) {
        this.ziarnoWyboruRodzicaDoReprodukcji = ziarnoWyboruRodzicaDoReprodukcji;
    }

    public double getPrawdopodobienstwoKrzyzowania() {
        return prawdopodobienstwoKrzyzowania;
    }

    public void setPrawdopodobienstwoKrzyzowania(double prawdopodobienstwoKrzyzowania) {
        this.prawdopodobienstwoKrzyzowania = prawdopodobienstwoKrzyzowania;
    }

    public int getZiarnoKrzyzowania() {
        return ziarnoKrzyzowania;
    }

    public void setZiarnoKrzyzowania(int ziarnoKrzyzowania) {
        this.ziarnoKrzyzowania = ziarnoKrzyzowania;
    }

    public double getPrawdopodobienstwoMutacji() {
        return prawdopodobienstwoMutacji;
    }

    public void setPrawdopodobienstwoMutacji(double prawdopodobienstwoMutacji) {
        this.prawdopodobienstwoMutacji = prawdopodobienstwoMutacji;
    }

    public int getZiarnoMutacji() {
        return ziarnoMutacji;
    }

    public void setZiarnoMutacji(int ziarnoMutacji) {
        this.ziarnoMutacji = ziarnoMutacji;
    }

    public int getZiarnoGenerowaniaRozwiazan() {
        return ziarnoGenerowaniaRozwiazan;
    }

    public void setZiarnoGenerowaniaRozwiazan(int ziarnoGenerowaniaRozwiazan) {
        this.ziarnoGenerowaniaRozwiazan = ziarnoGenerowaniaRozwiazan;
    }
}
