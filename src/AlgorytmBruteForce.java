import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlgorytmBruteForce {

    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();
    private List<Rozwiazanie> rozwiazania = new ArrayList<>();
    private int maksymalnyCzasMilisekundy = 1000000;
    private String plikWyjsciowy;
    long start = 0;
    private long elapsedTime = 0;

    public AlgorytmBruteForce(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_, Ustawienia ustawienia) {
        listaZapotrzebowan = listaZapotrzebowan_;
        listaLaczy = listaLaczy_;

        maksymalnyCzasMilisekundy = ustawienia.getMaksymalnyCzasMilisekundy();
        plikWyjsciowy = ustawienia.getPlikWyjsciowy();
    }

    public void algorytmBruteForce(int liczbaRozwiazanDoWygenerowania, boolean czyWarunekStopu) {
        rozwiazania = new ArrayList<>();
        start = System.currentTimeMillis();
        for (Zapotrzebowanie zapotrzebowanie : listaZapotrzebowan) {
            zapotrzebowanie.generujWszystkieMozliweRozlozeniaZapotrzebowania();
            if (System.currentTimeMillis() - start > maksymalnyCzasMilisekundy && czyWarunekStopu) {
                System.out.println("Przekroczono czas wykonywania algorytmu.");
                return;
            }
        }
        System.out.println("Wygenerowano wszystkie możliwe rozłożenia zapotrzebowań");
        // wymiar y tablicy rozwiązania
        int najwiekszaLiczbaSciezek = listaZapotrzebowan.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
        boolean result = algorytmBruteForce(0, new Rozwiazanie(listaZapotrzebowan.size(), najwiekszaLiczbaSciezek), liczbaRozwiazanDoWygenerowania, czyWarunekStopu);
        if (result) {
            System.out.println("Wygenerowano wszystkie możliwe rozwiązania");
        } else {
            System.out.println("Przekroczono czas wykonywania algorytmu. Znaleziono " + rozwiazania.size() + " rozwiązań.");
        }

        elapsedTime = System.currentTimeMillis() - start;
        float elapsedTimeSec = elapsedTime/1000F;

        System.out.println("Najlepsze rozwiązanie znaleziono w czasie: " + elapsedTimeSec + " [s]");
        rozwiazania.forEach(x -> x.ocenRozwiazanie(listaZapotrzebowan, listaLaczy));
        Collections.sort(rozwiazania);
        Rozwiazanie najlepszeRozwiazanie = rozwiazania.get(0);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        PrintWriter out = new PrintWriter(bw);
        najlepszeRozwiazanie.zapiszDoStrumieniaWyjsciowego(out, listaZapotrzebowan, listaLaczy);
        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long globalLicznik = 0;
    private long globalLicznikRozw = 0;

    private boolean algorytmBruteForce(int index, Rozwiazanie rozwiazanie, int liczbaRozwiazanDoWygenerowania, boolean czyWarunekStopu) {
        if (System.currentTimeMillis() - start > maksymalnyCzasMilisekundy && czyWarunekStopu) {
            return false;
        }
        if (index == listaZapotrzebowan.size()) {
            // mamy jedno rozwiązanie

            if (rozwiazania.size() == liczbaRozwiazanDoWygenerowania) {
                return true;
            }

            //znalezione rozwiazie | koniec
            rozwiazania.add(rozwiazanie);

            /*//Komentarz
            System.out.println("Dodałem nowe rozwiązanie: " + ++globalLicznikRozw);
            System.out.print("Rozw: ");
            for (Integer[] integers : rozwiazanie.getRozwiazanie()) {
                for (Integer integer : integers) {
                    System.out.print(integer);
                }
            }

            System.out.println();*/
        } else {
            for (int j = 0; j < listaZapotrzebowan.get(index).getLiczbeRozlozen(); j++) {
                Rozwiazanie noweRozwiazanie = rozwiazanie.getCopy();

                /*//Komentarz
                System.out.print(++globalLicznik + " Zapotrzebowanie: " + listaZapotrzebowan.get(index).id + " ");*/

                List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(j);
                Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);

                /*//Komentarz
                String z = rozlozenieZapotrzebowania.stream().map(x -> x.toString()).collect(Collectors.toList()).toString();
                System.out.println("dodałem takie rozlozenie: " + z + " | nr rozlozenia: " + j);*/

                noweRozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
                algorytmBruteForce(index + 1, noweRozwiazanie, liczbaRozwiazanDoWygenerowania, czyWarunekStopu);
            }
            List<Integer> rozlozenieZapotrzebowania = listaZapotrzebowan.get(index).getRozlozenieZapotrzebowania(0);
            Integer[] rozlozenieZapotrzebowaniaArray = rozlozenieZapotrzebowania.toArray(new Integer[rozlozenieZapotrzebowania.size()]);
            rozwiazanie.getRozwiazanie()[index] = rozlozenieZapotrzebowaniaArray;
        }

        return true;
    }

    public void zapiszRozwiazaniaDoPliku() {
        //TODO wypisanie wszytkich rozwiazan dodanych w rekurencji spełniajacych warunek
        try (FileWriter fw = new FileWriter("BruteForce" + plikWyjsciowy, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)
        ) {
            for (Rozwiazanie rozwiazanie : rozwiazania) {
                if (rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy).CzyAkceptowalne) {
                    rozwiazanie.zapiszDoStrumieniaWyjsciowego(out, listaZapotrzebowan, listaLaczy);
                    break;
                }
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}