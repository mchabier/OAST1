import java.io.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlgorytmEwolucyjny {
    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();
    private List<Rozwiazanie> najlepszeRozwiazaniaWGeneracjach = new ArrayList<>();
    private List<Rozwiazanie> najlepszeRozwiazaniePerGeneracja = new ArrayList<>();

    private int maxLiczbaIteracji = 500;
    private int maksymalnyCzasMilisekundy = 1000000;
    private int maksymalnaLiczbaMutacji = 10000;
    private int liczbaGeneracjiObserwowanych = 100;
    private int liczbaRozwiazanPoczątkowych = 1000;
    private int ileWybieramyDoReprodukcji = 500;
    private int ziarnoWyboruRodzicaDoReprodukcji = 2;
    private double prawdopodobienstwoKrzyzowania = 0.5;
    private int ziarnoKrzyzowania = 3;
    private double prawdopodobienstwoMutacji = 0.5;
    private int ziarnoMutacji = 4;
    private int ziarnoGenerowaniaRozwiazan = 56790;

    private String plikWyjsciowy;

    int licznikIteracji = 0;
    int licznikMutacji = 0;

    long start = 0;
    long elapsedTime = 0;

    Random randomKrzyzowanie;
    Random randomMutacja;

    public AlgorytmEwolucyjny(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_, Ustawienia ustawienia) {
        listaZapotrzebowan = listaZapotrzebowan_;
        listaLaczy = listaLaczy_;

        maxLiczbaIteracji = ustawienia.getMaxLiczbaIteracji();
        maksymalnyCzasMilisekundy = ustawienia.getMaksymalnyCzasMilisekundy();
        maksymalnaLiczbaMutacji = ustawienia.getMaksymalnaLiczbaMutacji();
        liczbaGeneracjiObserwowanych = ustawienia.getLiczbaGeneracjiObserwowanych();
        liczbaRozwiazanPoczątkowych = ustawienia.getLiczbaRozwiazanPoczątkowych();
        ileWybieramyDoReprodukcji = ustawienia.getIleWybieramyDoReprodukcji();
        ziarnoWyboruRodzicaDoReprodukcji = ustawienia.getZiarnoWyboruRodzicaDoReprodukcji();
        prawdopodobienstwoKrzyzowania = ustawienia.getPrawdopodobienstwoKrzyzowania();
        ziarnoKrzyzowania = ustawienia.getZiarnoKrzyzowania();
        prawdopodobienstwoMutacji = ustawienia.getPrawdopodobienstwoMutacji();
        ziarnoMutacji = ustawienia.getZiarnoMutacji();
        ziarnoGenerowaniaRozwiazan = ustawienia.getZiarnoGenerowaniaRozwiazan();

        plikWyjsciowy = ustawienia.getPlikWyjsciowy();
    }

    public void rozpocznijDzialanieAlgorytmu(int wybranyWarunekStopu) {
        System.out.println("Działanie algorytmu zostało rozpoczęte.");
        najlepszeRozwiazaniaWGeneracjach = new ArrayList<>();
        najlepszeRozwiazaniePerGeneracja = new ArrayList<>();
        //TODO: jakąś heurystykę trzeba wymyślić - może w zależności liczba zpotrzebowań
        //Inicjalizacja rozwiazan początkowych

        TreeMap<OcenaRozwiazania, Rozwiazanie> zbiorRozwiazan = generujRozwiazaniaPoczatkowe(liczbaRozwiazanPoczątkowych);
        System.out.println("Wygenerowano " + zbiorRozwiazan.size() + " rozwiązań początkowych.");

        boolean warunekStopu = true;

        licznikIteracji = 0;
        licznikMutacji = 0;
        List<Rozwiazanie> doReprodukcji = new ArrayList<>();
        start = System.currentTimeMillis();
        Rozwiazanie najlepszeRozwiazanie = null;
        randomKrzyzowanie = new Random(ziarnoKrzyzowania);
        randomMutacja = new Random(ziarnoMutacji);
        while (warunekStopu) {
            licznikIteracji++;
            //System.out.println("Iteracja: " + licznikIteracji);

            //Bierzemu określoną liczbę rodziców zdolną do reprodukcji
            doReprodukcji.clear();
            Set<Map.Entry<OcenaRozwiazania, Rozwiazanie>> set = zbiorRozwiazan.entrySet();
            for (int i = 0; i < set.size() && i < ileWybieramyDoReprodukcji; i++) {
                doReprodukcji.add(set.iterator().next().getValue().getCopy());
            }

            int ileDoReprodTmp = ileWybieramyDoReprodukcji > set.size() ? set.size() : ileWybieramyDoReprodukcji;
            //Krzyżowanie
            for (int i = 0; i < ileDoReprodTmp / 2; i++) {
                wykonajKrzyzowanie(
                        doReprodukcji.get(i),
                        doReprodukcji.get(ileDoReprodTmp / 2 - i - 1));
            }

            //Mutacja + ocena
            OcenaRozwiazania ocenaRozwiazania = null;
            for (Rozwiazanie rozwiazanie : doReprodukcji) {
                wykonajMutacje(rozwiazanie);
                ocenaRozwiazania = rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy);
                //if (ocenaRozwiazania.CzyAkceptowalne) {
                    zbiorRozwiazan.put(
                            ocenaRozwiazania,
                            rozwiazanie);
                //}
            }

            // Bierzemy najlepsze rozwiązania
            zbiorRozwiazan = zbiorRozwiazan
                    .entrySet()
                    .parallelStream()
                    .limit(liczbaRozwiazanPoczątkowych)
                    .collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);

            najlepszeRozwiazanie = zbiorRozwiazan.firstEntry().getValue();
            najlepszeRozwiazaniaWGeneracjach.add(najlepszeRozwiazanie);

            // Sprawdzamy warunki stopu
            if ((maxLiczbaIteracji == licznikIteracji && wybranyWarunekStopu == 1) ||
                    (maksymalnyCzasMilisekundy > System.currentTimeMillis() - start && wybranyWarunekStopu == 2) ||
                    (maksymalnaLiczbaMutacji == licznikMutacji && wybranyWarunekStopu == 3)) {
                warunekStopu = false;
            }

            najlepszeRozwiazaniePerGeneracja.add(najlepszeRozwiazanie);
            // Sprawdzenie poprawy w N kolejnych generacjach
            if (licznikIteracji % liczbaGeneracjiObserwowanych == 0 && wybranyWarunekStopu == 4) {
                OcenaRozwiazania najlepszeRozwiazanieNajstarsze = najlepszeRozwiazaniePerGeneracja.get(0).getOcenaRozwiazania();
                OcenaRozwiazania najlepszeRozwiazanieNajnowsze = najlepszeRozwiazaniePerGeneracja.get(najlepszeRozwiazaniePerGeneracja.size() - 1).getOcenaRozwiazania();

                if (!najlepszeRozwiazanieNajstarsze.CzyAkceptowalne &&
                        najlepszeRozwiazanieNajnowsze.CzyAkceptowalne) {
                    warunekStopu = false;
                } else if (najlepszeRozwiazanieNajnowsze.Koszt < najlepszeRozwiazanieNajstarsze.Koszt) {
                    warunekStopu = false;
                }

                najlepszeRozwiazaniePerGeneracja.clear();
            }
        }

        elapsedTime = System.currentTimeMillis() - start;
        float elapsedTimeSec = elapsedTime / 1000F;

        System.out.println("Najlepsze rozwiązanie znaleziono w czasie: " + elapsedTimeSec + " [s]");
        najlepszeRozwiazanie = zbiorRozwiazan.firstEntry().getValue();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        PrintWriter out = new PrintWriter(bw);
        najlepszeRozwiazanie.zapiszDoStrumieniaWyjsciowego(out, listaZapotrzebowan, listaLaczy);
        try {
            bw.flush();
//            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void wykonajKrzyzowanie(Rozwiazanie rodzic1, Rozwiazanie rodzic2) {
        double wartoscLosowa = 0;
        Integer[] tmp = null;

        Integer[][] rodzic1Rozwiazanie = rodzic1.getRozwiazanie();
        Integer[][] rodzic2Rozwiazanie = rodzic2.getRozwiazanie();
        for (int i = 0; i < rodzic1Rozwiazanie.length; i++) {
            wartoscLosowa = randomKrzyzowanie.nextDouble();

            if (wartoscLosowa < prawdopodobienstwoKrzyzowania) {
                // zamiana genów
                tmp = rodzic1Rozwiazanie[i];
                rodzic1Rozwiazanie[i] = rodzic2Rozwiazanie[i];
                rodzic2Rozwiazanie[i] = tmp;
            }
        }
    }

    private void wykonajMutacje(Rozwiazanie rozwiazanie) {
        double wartoscLosowa = 0;

        Integer[][] rozwiazanieArray = rozwiazanie.getRozwiazanie();
        Integer[] gen = null;
        Zapotrzebowanie zapotrzebowanie = null;
        for (int i = 0; i < listaZapotrzebowan.size(); i++) {
            zapotrzebowanie = listaZapotrzebowan.get(i);
            if (zapotrzebowanie.wartosc <= 0) {
                continue;
            }

            gen = rozwiazanieArray[i];
            if (gen.length <= 1) {
                continue;
            }

            wartoscLosowa = randomMutacja.nextDouble();

            if (wartoscLosowa < prawdopodobienstwoMutacji) {

                mutujGen(gen, randomMutacja, null, null);
                licznikMutacji++;
                /*//Mutacja jest permutacja
                for (int j = zapotrzebowanie.iloscSciezek - 1; j >= 0; --j) {
                    Collections.swap(Arrays.asList(gen), j, random.nextInt(j + 1));
                }*/
            }
        }
    }

    private void mutujGen(Integer[] gen, Random random, Integer _firstIndex, Integer _secondIndex) {
        Integer firstIndex = losujFirstIndex(gen, random);
        Integer secondIndex = losujSecondIndex(gen, random, firstIndex);

        gen[firstIndex]--;
        gen[secondIndex]++;
    }

    private int losujFirstIndex(Integer[] gen, Random random) {
        Integer firstIndex = random.nextInt(gen.length);
        if (gen[firstIndex] > 0) {
            return firstIndex;
        } else {
            return losujFirstIndex(gen, random);
        }
    }

    private int losujSecondIndex(Integer[] gen, Random random, Integer firstIndex) {
        Integer secondIndex = random.nextInt(gen.length);
        if (secondIndex != firstIndex) {
            return secondIndex;
        } else {
            return losujSecondIndex(gen, random, firstIndex);
        }
    }

    private static List<Rozwiazanie> wezNLosowychElementowZListy(List<Rozwiazanie> list, int n, Random r) {
        int length = list.size();

        if (length < n) return list;

        //We don't need to shuffle the whole list
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i, r.nextInt(i + 1));
        }
        return list.subList(length - n, length).parallelStream().map(x -> x.getCopy()).collect(Collectors.toList());
    }

    private TreeMap<OcenaRozwiazania, Rozwiazanie> generujRozwiazaniaPoczatkowe(int liczbaRozwiazanDoWygenerowania) {
        int najwiekszaLiczbaSciezek = listaZapotrzebowan.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
//        List<Rozwiazanie> listaRozwiazan = new ArrayList<>();
        TreeMap<OcenaRozwiazania, Rozwiazanie> listaRozwiazan = new TreeMap<OcenaRozwiazania, Rozwiazanie>();

        Rozwiazanie tmp = null;
        Integer[][] rozwiazanie = null;
        Random r = new Random(ziarnoGenerowaniaRozwiazan);
        int max = 0;
        int licznikTMP = 0;
        for (int i = 0; i < liczbaRozwiazanDoWygenerowania; i++) {
            tmp = new Rozwiazanie(listaZapotrzebowan.size(), najwiekszaLiczbaSciezek);
            rozwiazanie = tmp.getRozwiazanie();

            for (int j = 0; j < listaZapotrzebowan.size(); j++) {
                max = listaZapotrzebowan.get(j).wartosc;
                for (int k = 0; k < rozwiazanie[j].length; k++) {
                    Integer x = r.nextInt(max);
                    max = max - x;
                    rozwiazanie[j][k] = x;
                    if (max == 0)
                        break;
                }
                rozwiazanie[j][rozwiazanie[j].length - 1] = max;
            }

            //System.out.print("Wygenerowałem " + (++licznikTMP) + " rozwiązanie: ");
            if (tmp.ocenRozwiazanie(listaZapotrzebowan, listaLaczy).CzyAkceptowalne) {
                listaRozwiazan.put(tmp.getOcenaRozwiazania(), tmp);
                //System.out.println("dobre");
            } else {
//                i--;
                listaRozwiazan.put(tmp.getOcenaRozwiazania(), tmp);
                //System.out.println("zle");
            }
        }

        return listaRozwiazan;
    }

    public void zapiszRozwiazaniaDoPliku() {
        //TODO wypisanie wszytkich rozwiazan dodanych w rekurencji spełniajacych warunek
        try (FileWriter fw = new FileWriter("Ewolucyjny" + plikWyjsciowy, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)
        ) {
            out.println();
            out.println("Zapis trajektorii algorytmu ewolucyjnego: ");
            out.println();
            int i = 1;
            for (Rozwiazanie rozwiazanie : najlepszeRozwiazaniaWGeneracjach) {
                out.println("Iteracja: " + i++);
                rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy);
                rozwiazanie.zapiszDoStrumieniaWyjsciowego(out, listaZapotrzebowan, listaLaczy);
            }

            bw.flush();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
