import java.util.ArrayList;
import java.util.List;

public class BrutalForce {

    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();

    public BrutalForce(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        listaZapotrzebowan = listaZapotrzebowan_;
        listaLaczy = listaLaczy_;
    }

    public void algorytmBruteForce() {
        // generowanie wszystkich

        int najwiekszaLiczbaSciezek = listaZapotrzebowan.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
        Integer[][] pojedynczeRozwiazanie = new Integer[listaZapotrzebowan.size()][najwiekszaLiczbaSciezek];
        List<Integer[][]> listaWszystkichRozwiazan = new ArrayList<>();
        List<List<List<Integer>>> listaWszystkichMozliwosciWszystkichZapotrzebowan = new ArrayList<>();

        for (int i = 0; i < listaZapotrzebowan.size(); i++) {
            List<List<Integer>> wszystkieMozliwosciDlaJednegoZapotrzebowania = new ArrayList<List<Integer>>();

            generujWszystkieMozliweRozlozeniaZapotrzebowania(
                    new ArrayList<Integer>(),
                    listaZapotrzebowan.get(i).iloscSciezek,
                    listaZapotrzebowan.get(i).wartosc,
                    wszystkieMozliwosciDlaJednegoZapotrzebowania);

            listaWszystkichMozliwosciWszystkichZapotrzebowan.add(wszystkieMozliwosciDlaJednegoZapotrzebowania);
        }

        for (List<List<Integer>> wszystkieZPojedynczegoZapotrzebowania : listaWszystkichMozliwosciWszystkichZapotrzebowan) {

        }
    }

    private void generujWszystkieMozliweRozlozeniaZapotrzebowania(List<Integer> jednoRozlozenieZapotrzebowania, int liczbaSciezek, int wartoscZapotrzebowania, List<List<Integer>> wynik) {
        if (liczbaSciezek == 0) {
            if (wartoscZapotrzebowania == 0) {
                /* Odkomentuj jesli chcesz zobaczyć co generuje ta metoda.
                String z = prefix.stream().map(x -> x.toString()).collect(Collectors.toList()).toString();
                System.out.println(z);*/
                wynik.add(jednoRozlozenieZapotrzebowania);
            }
        } else {
            for (int i = 0; i <= wartoscZapotrzebowania/* && i < 10*/; i++) {
                List<Integer> copyList = new ArrayList<Integer>(jednoRozlozenieZapotrzebowania);
                copyList.add(i);
                generujWszystkieMozliweRozlozeniaZapotrzebowania(copyList, liczbaSciezek - 1, wartoscZapotrzebowania - i, wynik);
            }
        }
    }
}
