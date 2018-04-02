import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rozwiazanie implements Comparable {
    private Integer[][] rozwiazanie;
    private OcenaRozwiazania ocenaRozwiazania;

    public Rozwiazanie(Integer liczbaZapotrzebowan, Integer najwiekszaLiczbaSciezek) {
        rozwiazanie = new Integer[liczbaZapotrzebowan][najwiekszaLiczbaSciezek];
    }

    public Rozwiazanie(Integer[][] rozwiazanie_) {
        rozwiazanie = rozwiazanie_;
    }

    public Integer[][] getRozwiazanie() {
        return rozwiazanie;
    }

    public Rozwiazanie getCopy() {
        return new Rozwiazanie(rozwiazanie.clone());
    }

    public OcenaRozwiazania getOcenaRozwiazania() {
        return ocenaRozwiazania;
    }

    public OcenaRozwiazania ocenRozwiazanie(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        ocenaRozwiazania = new OcenaRozwiazania();
        ocenaRozwiazania.CzyAkceptowalne = true;
        ocenaRozwiazania.Koszt = Double.valueOf(0);

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

        Integer liczbaLambd = 0;
        try {
            for (Lacze lacze : listaLaczy_) {
                if (lacze == null) {
                    String asd = "asdas";
                }

                liczbaLambd = map.get(lacze.id);

                if (liczbaLambd == null) {
                    liczbaLambd = 0;
                }

                if (liczbaLambd > lacze.ilPar * lacze.iloscLambd) {
                    ocenaRozwiazania.CzyAkceptowalne = false;
                }
                ocenaRozwiazania.Koszt += Math.ceil(liczbaLambd / lacze.iloscLambd) * lacze.kosztPary;
            }
        } catch (Exception ex) {
            String asd = "asdas";
        }


        return ocenaRozwiazania;
    }

    public void zapiszDoPliku() {
        //TODO: zrobic zapis do pliku
    }

    @Override
    public int compareTo(Object o) {
        return this.ocenaRozwiazania.Koszt.compareTo(((Rozwiazanie)o).ocenaRozwiazania.Koszt);
    }
}
