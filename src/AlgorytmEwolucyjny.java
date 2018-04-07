import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlgorytmEwolucyjny {
    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();
    private List<Rozwiazanie> rozwiazania = new ArrayList<>();

    private int maxLiczbaIteracji = 500;
    private int liczbaRozwiazanPoczątkowych = 1000;
    private int ileWybieramyDoReprodukcji = 500;
    private int ziarnoWyboruRodzicaDoReprodukcji = 2;
    private double prawdopodobienstwoKrzyzowania = 0.5;
    private int ziarnoKrzyzowania = 3;
    private double prawdopodobienstwoMutacji = 0.5;
    private int ziarnoMutacji = 4;
    private int ziarnoGenerowaniaRozwiazan = 56790;

    public AlgorytmEwolucyjny(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        listaZapotrzebowan = listaZapotrzebowan_;
        listaLaczy = listaLaczy_;
    }

    public void rozpocznijDzialanieAlgorytmu() {
         //TODO: jakąś heurystykę trzeba wymyślić - może w zależności liczba zpotrzebowań
         // Nie więcej niż liczbaRozwiazanPoczątkowych

        //Inicjalizacja rozwiazan początkowych
        /*AlgorytmBruteForce algorytmBruteForce = new AlgorytmBruteForce(listaZapotrzebowan, listaLaczy);
        algorytmBruteForce.algorytmBruteForce(liczbaRozwiazanPoczątkowych);
        List<Rozwiazanie> zbiorRozwiazan = algorytmBruteForce.getRozwiazania();
        zbiorRozwiazan.forEach(x -> x.ocenRozwiazanie(listaZapotrzebowan, listaLaczy));*/

        /*List<Rozwiazanie>*/
        Map<OcenaRozwiazania, Rozwiazanie> zbiorRozwiazan = generujRozwiazaniaPoczatkowe(liczbaRozwiazanPoczątkowych);

        /*LinkedHashMap<Double, Rozwiazanie> zbiorRozwiazan = new LinkedHashMap<Double, Rozwiazanie>();
        for (Rozwiazanie rozwiazanie : rozwiazania) {
            zbiorRozwiazan.put(
                    rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy).Koszt,
                    rozwiazanie);
        }*/

        boolean warunekStopu = true;

        int licznik = 0;
        List<Rozwiazanie> doReprodukcji = new ArrayList<>();
        while (warunekStopu) {
            licznik++;
            System.out.println("Iteracja: " + licznik);
            if (maxLiczbaIteracji == licznik) {
                warunekStopu = false;
            }
            //Bierzemu określoną liczbę rodziców zdolną do reprodukcji
            //Na liście
            //doReprodukcji = wezNLosowychElementowZListy(/*new ArrayList<Rozwiazanie>(zbiorRozwiazan.values())*/zbiorRozwiazan, ileWybieramyDoReprodukcji, new Random(ziarnoWyboruRodzicaDoReprodukcji));
            //Na słowniku
            doReprodukcji.clear();
            Set<Map.Entry<OcenaRozwiazania, Rozwiazanie>> set = zbiorRozwiazan.entrySet();
            for (int i = 0; i < set.size() && i < ileWybieramyDoReprodukcji; i++) {
                doReprodukcji.add(set.iterator().next().getValue().getCopy());
            }

            //Krzyżowanie
            for (int i = 0; i < ileWybieramyDoReprodukcji / 2; i++) {
                wykonajKrzyzowanie(
                        doReprodukcji.get(i),
                        doReprodukcji.get(ileWybieramyDoReprodukcji / 2 - i - 1));
            }

            //Mutacja + ocena
            OcenaRozwiazania ocenaRozwiazania = null;
            for (Rozwiazanie rozwiazanie : doReprodukcji) {
                wykonajMutacje(rozwiazanie);
                ocenaRozwiazania = rozwiazanie.ocenRozwiazanie(listaZapotrzebowan, listaLaczy);
                if (ocenaRozwiazania.CzyAkceptowalne) {
                    zbiorRozwiazan.put(
                            rozwiazanie.getOcenaRozwiazania(),
                            rozwiazanie);
//                    zbiorRozwiazan.add(rozwiazanie);
                }
            }


            //wybór najlepszych

            /*doReprodukcji
                    .parallelStream()
                    .filter(x -> x.getOcenaRozwiazania().CzyAkceptowalne)
                    .collect(Collectors.toList());*/
//            List<Rozwiazanie> result = new ArrayList<Rozwiazanie>(zbiorRozwiazan.values());
//            Collections.sort(zbiorRozwiazan);
            zbiorRozwiazan = zbiorRozwiazan
                    .entrySet()
                    .parallelStream()
                    .limit(liczbaRozwiazanPoczątkowych)
                    .collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
        }

        Set<Map.Entry<OcenaRozwiazania, Rozwiazanie>> set = zbiorRozwiazan.entrySet();
        for (int i = 0; i < 100; i++) {
            Map.Entry<OcenaRozwiazania, Rozwiazanie> entry = set.iterator().next();
            System.out.println("Rozwiazanie " + (i + 1) + " " + entry.getKey().CzyAkceptowalne + " : " + entry.getValue().getOcenaRozwiazania().Koszt);
        }
    }

    private void wykonajKrzyzowanie(Rozwiazanie rodzic1, Rozwiazanie rodzic2) {
        Random random = new Random(ziarnoKrzyzowania);
        double wartoscLosowa = 0;
        Integer[] tmp = null;

        Integer[][] rodzic1Rozwiazanie = rodzic1.getRozwiazanie();
        Integer[][] rodzic2Rozwiazanie = rodzic2.getRozwiazanie();
        for (int i = 0; i < rodzic1Rozwiazanie.length; i++) {
            wartoscLosowa = random.nextDouble();

            if (wartoscLosowa < prawdopodobienstwoKrzyzowania) {
                // zamiana genów
                tmp = rodzic1Rozwiazanie[i];
                rodzic1Rozwiazanie[i] = rodzic2Rozwiazanie[i];
                rodzic2Rozwiazanie[i] = tmp;
            }
        }
    }

    private void wykonajMutacje(Rozwiazanie rozwiazanie) {
        Random random = new Random(ziarnoMutacji);
        double wartoscLosowa = 0;

        Integer[][] rozwiazanieArray = rozwiazanie.getRozwiazanie();
        Integer[] gen = null;
        Zapotrzebowanie zapotrzebowanie = null;
        for (int i = 0; i < listaZapotrzebowan.size(); i++) {
            gen = rozwiazanieArray[i];
            wartoscLosowa = random.nextDouble();

            if (wartoscLosowa < prawdopodobienstwoMutacji) {
                zapotrzebowanie = listaZapotrzebowan.get(i);

                //Mutacja jest permutacja
                //TODO akoniecznie zmienić zasadę mutacji - nie permutować tylko np. jedną wartość zapotrzebowania gdzieś przenosić na inną ścieżkę, bo teraz tak naprawdę nie ma mutacji
                for (int j = zapotrzebowanie.iloscSciezek - 1; j >= 0; --j) {
                    Collections.swap(Arrays.asList(gen), j, random.nextInt(j + 1));
                }
            }
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

    private Map<OcenaRozwiazania, Rozwiazanie> generujRozwiazaniaPoczatkowe(int liczbaRozwiazanDoWygenerowania) {
        int najwiekszaLiczbaSciezek = listaZapotrzebowan.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
//        List<Rozwiazanie> listaRozwiazan = new ArrayList<>();
        Map<OcenaRozwiazania, Rozwiazanie> listaRozwiazan = new TreeMap<OcenaRozwiazania, Rozwiazanie>();

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

            System.out.print("Wygenerowałem " + (++licznikTMP) + " rozwiązanie: ");
            if (tmp.ocenRozwiazanie(listaZapotrzebowan, listaLaczy).CzyAkceptowalne) {
                listaRozwiazan.put(tmp.getOcenaRozwiazania(), tmp);
                System.out.println("dobre");
            } else {
//                i--;
                listaRozwiazan.put(tmp.getOcenaRozwiazania(), tmp);
                System.out.println("zle");
            }
        }

        return listaRozwiazan;
    }
}
