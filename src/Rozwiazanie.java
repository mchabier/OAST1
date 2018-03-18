import java.util.List;

public class Rozwiazanie {
    private Integer[][] rozwiazanie;

    public Rozwiazanie(Integer liczbaZapotrzebowan, Integer najwiekszaLiczbaSciezek) {
        rozwiazanie = new Integer[liczbaZapotrzebowan][najwiekszaLiczbaSciezek];
    }

    public Rozwiazanie(Integer[][] rozwiazanie_) {
        rozwiazanie = rozwiazanie_;
    }

    public Integer[][] getRozwiazanie() {
        return rozwiazanie;
    }

    public void setRozwiazanie(Integer[][] rozwiazanie) {
        this.rozwiazanie = rozwiazanie;
    }

    public Rozwiazanie getCopy() {
        return new Rozwiazanie(rozwiazanie.clone());
    }
}
