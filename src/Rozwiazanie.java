import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rozwiazanie implements Comparable {
    private Integer[][] rozwiazanie;
    private OcenaRozwiazania ocenaRozwiazania;
    Map<Integer, Integer> odlozoneSygnalyPerLaczeId;

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
        return new Rozwiazanie(Arrays.stream(rozwiazanie)
                .map(gen -> gen == null ? null : Arrays.stream(gen)
                        .map(x -> x == null ? null : Integer.valueOf(x))
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new));
    }

    public OcenaRozwiazania getOcenaRozwiazania() {
        return ocenaRozwiazania;
    }

    public OcenaRozwiazania ocenRozwiazanie(List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        odlozoneSygnalyPerLaczeId = new HashMap<Integer, Integer>();
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
                    int juzZsumowana = odlozoneSygnalyPerLaczeId.getOrDefault(idLacza, 0);

                    odlozoneSygnalyPerLaczeId.put(idLacza, juzZsumowana + iloscLambd);
                }
            }
        }

        Integer liczbaLambd = 0;
        try {
            for (Lacze lacze : listaLaczy_) {
                liczbaLambd = odlozoneSygnalyPerLaczeId.get(lacze.id);

                if (liczbaLambd == null) {
                    liczbaLambd = 0;
                }

                if (liczbaLambd > lacze.iloscPar * lacze.liczbaLambdWeWloknie) {
                    ocenaRozwiazania.CzyAkceptowalne = false;
                }
                int ileNadmiarowychLambd = liczbaLambd - lacze.iloscPar * lacze.liczbaLambdWeWloknie;
                ileNadmiarowychLambd = ileNadmiarowychLambd >= 0 ? ileNadmiarowychLambd : 0;
                //ocenaRozwiazania.Koszt += Math.ceil((double) ileNadmiarowychLambd / lacze.liczbaLambdWeWloknie) * lacze.kosztPary;
                ocenaRozwiazania.Koszt += ileNadmiarowychLambd;
            }
        } catch (Exception ex) {
            String asd = "asdas";
        }


        return ocenaRozwiazania;
    }

    public void zapiszDoStrumieniaWyjsciowego(PrintWriter out, List<Zapotrzebowanie> listaZapotrzebowan_, List<Lacze> listaLaczy_) {
        if (ocenaRozwiazania == null) {
            return;
        }

        out.println("### Rozwiazanie ### ");
        out.println("Koszt: " + ocenaRozwiazania.Koszt);
        out.println("### Links load: ");

        int liczbaLambd = 0;
        for (Lacze lacze : listaLaczy_) {
            out.print(lacze.id + " ");

            liczbaLambd = odlozoneSygnalyPerLaczeId.get(lacze.id);

            out.print(liczbaLambd + "/" + lacze.iloscPar * lacze.liczbaLambdWeWloknie + " ");//Liczba odlozonych sygnalow na mozliwe sygnaly

            out.println((int) Math.ceil((double) liczbaLambd / lacze.liczbaLambdWeWloknie) + "/" + lacze.iloscPar);//Liczba wykorzystanych włókien na wszystkie włókna
        }

        out.println("### Tablica zapotrzebowań: ");

        int najwiekszaLiczbaSciezek = listaZapotrzebowan_.stream().mapToInt(x -> x.iloscSciezek).max().getAsInt();
        for (int j = 0; j < najwiekszaLiczbaSciezek; j++) {
            for (int i = 0; i < rozwiazanie.length; i++) {
                if (rozwiazanie[i].length > j) {
                    out.print(rozwiazanie[i][j] + " ");
                } else {
                    out.print(0 + " ");
                }
            }
            out.println();
        }
    }

    @Override
    public int compareTo(Object o) {
        return this.ocenaRozwiazania.compareTo(((Rozwiazanie) o).ocenaRozwiazania);
    }
}
