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

    private long globalLicznik = 0;
    private long globalLicznikRozw = 0;

    private void algorytmBruteForce(int index, Rozwiazanie rozwiazanie) {
        if (index == listaZapotrzebowan.size()) {
            // mamy jedno rozwiązanie
            rozwiazania.add(rozwiazanie);
            //Komentarz
            System.out.println("Dodałem nowe rozwiązanie: " + ++globalLicznikRozw);
            System.out.print("Rozw: ");
            for (Integer[] integers : rozwiazanie.getRozwiazanie()) {
                for (Integer integer : integers) {
                    System.out.print(integer);
                }
            }
            System.out.println();
        } else {
            for (int j = 0; j < listaZapotrzebowan.get(index).getLiczbeRozlozen(); j++) {
                Rozwiazanie noweRozwiazanie = rozwiazanie.getCopy();
                //Komentarz
                System.out.print(++globalLicznik + " Zapotrzebowanie: " + listaZapotrzebowan.get(index).id + " ");


                List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(j);
                Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);

                //Komentarz
                String z = rozlozenieZapotrzebowania.stream().map(x -> x.toString()).collect(Collectors.toList()).toString();
                System.out.println("dodałem takie rozlozenie: " + z + " | nr rozlozenia: " + j);

                noweRozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
                algorytmBruteForce(index + 1, noweRozwiazanie);
            }
            List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(0);
            Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);
            rozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
        }
    }
}
