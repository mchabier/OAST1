import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrutalForce {

    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();
    private List<Rozwiazanie> rozwiazania = new ArrayList<>();

    public BrutalForce(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        listaZapotrzebowan = listaZapotrzebowan_;
        listaLaczy = listaLaczy_;
    }

    public void algorytmBruteForce() {
        int najwiekszaLiczbaSciezek = listaZapotrzebowan.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
        algorytmBruteForce(0, new Rozwiazanie(listaZapotrzebowan.size(), najwiekszaLiczbaSciezek));
    }

    private void algorytmBruteForce(int index, Rozwiazanie rozwiazanie) {
        if (index == listaZapotrzebowan.size()) {
            // mamy jedno rozwiązanie
            rozwiazania.add(rozwiazanie);
            Boolean wartosc = rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy);
        } else {
            for (int j = 0; j < listaZapotrzebowan.get(index).getLiczbeRozlozen(); j++) {
                Rozwiazanie noweRozwiazanie = rozwiazanie.getCopy();

                List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(j);
                Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);

                noweRozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
                algorytmBruteForce(index + 1, noweRozwiazanie);
            }
            List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(0);
            Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);
            rozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
        }
    }
}
