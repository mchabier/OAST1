import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Boolean ocenRozwiazanie(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 0; i < listaZapotrzebowan_.size(); i++) {
            Zapotrzebowanie zapotrzebowanie = listaZapotrzebowan_.get(i);
            Integer[] rozlozenieZapotrzebowania = rozwiazanie[i];

            for (int j = 0; j < zapotrzebowanie.listaSciezek.size(); j++) {
                Sciezka sciezka = zapotrzebowanie.listaSciezek.get(j);

                for (Integer idLacza : sciezka.listaIdLaczy) {
                    int iloscLambd = rozlozenieZapotrzebowania[j];
                    int juzZsumowana = map.getOrDefault(idLacza, 0);

                    map.put(idLacza, juzZsumowana + iloscLambd);
                }
            }
        }

        for (Lacze lacze : listaLaczy_) {
            if (map.get(lacze.id) > lacze.ilPar * lacze.iloscLambd)
                return false;
        }

        return true;
    }
}
