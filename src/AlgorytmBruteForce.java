import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorytmBruteForce {

    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();
    private List<Rozwiazanie> rozwiazania = new ArrayList<>();

    public List<Rozwiazanie> getRozwiazania() {
        return rozwiazania;
    }

    public AlgorytmBruteForce(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        listaZapotrzebowan = listaZapotrzebowan_;
        listaLaczy = listaLaczy_;
    }

    public void algorytmBruteForce(int liczbaRozwiazanDoWygenerowania) {
        for (Zapotrzebowanie zapotrzebowanie : listaZapotrzebowan) {
            zapotrzebowanie.generujWszystkieMozliweRozlozeniaZapotrzebowania();
        }

        // wymiar y tablicy rozwiązania
        int najwiekszaLiczbaSciezek = listaZapotrzebowan.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
        algorytmBruteForce(0, new Rozwiazanie(listaZapotrzebowan.size(), najwiekszaLiczbaSciezek), liczbaRozwiazanDoWygenerowania);
    }

    private long globalLicznik = 0;
    private long globalLicznikRozw = 0;

    private void algorytmBruteForce(int index, Rozwiazanie rozwiazanie, int liczbaRozwiazanDoWygenerowania) {
        if (index == listaZapotrzebowan.size()) {
            // mamy jedno rozwiązanie

            if (rozwiazania.size() == liczbaRozwiazanDoWygenerowania) {
                return;
            }
            //znalezione rozwiazie | koniec
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
                algorytmBruteForce(index + 1, noweRozwiazanie, liczbaRozwiazanDoWygenerowania);
            }
            List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(0);
            Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);
            rozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
        }
    }

    public void wypiszRozwiazania() {
        //TODO wypisanie wszytkich rozwiazan dodanych w rekurencji spełniajacych warunek
        try(FileWriter fw = new FileWriter("wynikiSiecMala.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (Rozwiazanie rozwiazanie : rozwiazania) {
                if (rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy).CzyAkceptowalne) {
                    rozwiazanie.zapiszDoPliku(out, listaZapotrzebowan, listaLaczy);
                    break;
                }
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }


    }
}