import java.util.ArrayList;
import java.util.List;

public class Zapotrzebowanie {
    int id;
    int poczatek;
    int koniec;
    int wartosc;
    int iloscSciezek;
    List<Sciezka> listaSciezek = new ArrayList<Sciezka>();
    private List<List<Integer>> rozlozeniaZapotrzebowania = new ArrayList<>();

    Zapotrzebowanie(int id, int poczatek_, int koniec_, int wartosc_) {
        this.id = id;
        this.poczatek = poczatek_;
        this.koniec = koniec_;
        this.wartosc = wartosc_;
        this.iloscSciezek = 0;
    }

    public void dodajRozlozenieZapotrzebowania(List<Integer> rozlozenieZapotrzebowania) {
        rozlozeniaZapotrzebowania.add(rozlozenieZapotrzebowania);
    }

    public Integer getLiczbeRozlozen() {
        return rozlozeniaZapotrzebowania.size();
    }

    public List<Integer> getRozlozenieZapotrzebowania(int index) {
        return rozlozeniaZapotrzebowania.get(index);
    }

    public void generujWszystkieMozliweRozlozeniaZapotrzebowania() {
        generujWszystkieMozliweRozlozeniaZapotrzebowania(
                new ArrayList<Integer>(),
                iloscSciezek,
                wartosc,
                rozlozeniaZapotrzebowania);
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
