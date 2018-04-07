import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {

    public List<Zapotrzebowanie> listaZapotrzebowan = new ArrayList<Zapotrzebowanie>();
    public List<Lacze> listaLaczy = new ArrayList<Lacze>();
    Ustawienia ustawienia;


    public void start() throws NumberFormatException, FileNotFoundException {
        wczytajPlikKonfiguracyjny();
        wczytajDane();

        System.out.println("Wczytano dane...");
        System.out.println("Liczba zapotrzebowań: " + listaZapotrzebowan.size());
        System.out.println("Liczba łączy: " + listaLaczy.size());

        boolean czyKonczyc = false;
        Integer opcja = -1;
        while (!czyKonczyc) {
            opcja = pokazMenu();
            switch (opcja) {
                case 0:
                    AlgorytmBruteForce bruteForce = new AlgorytmBruteForce(listaZapotrzebowan, listaLaczy);
                    bruteForce.algorytmBruteForce(-1);
                    bruteForce.wypiszRozwiazania();

                    break;
                case 1:
                    AlgorytmEwolucyjny algorytmEwolucyjny = new AlgorytmEwolucyjny(listaZapotrzebowan, listaLaczy);
                    algorytmEwolucyjny.rozpocznijDzialanieAlgorytmu();

                    break;
                case 2:
                    czyKonczyc = true;
                    break;
                default:
                    break;
            }
        }
    }

    private void wczytajPlikKonfiguracyjny() throws FileNotFoundException{
        FileInputStream file = new FileInputStream(new File("ustawienia.xml"));
        BufferedInputStream bis = new BufferedInputStream(file);
        /*XMLEncoder xmlEncoder = new XMLEncoder(file);
        u.setPlikWejsciowy("siec.txt");
        u.setPlikWyjsciowy("wynik.txt");
        u.setMaxLiczbaIteracji(500);
        u.setLiczbaRozwiazanPoczątkowych(1000);
        u.setIleWybieramyDoReprodukcji(500);
        u.setZiarnoWyboruRodzicaDoReprodukcji(2);
        u.setPrawdopodobienstwoKrzyzowania(0.5);
        u.setZiarnoKrzyzowania(3);
        u.setPrawdopodobienstwoMutacji(0.5);
        u.setZiarnoMutacji(4);
        u.setZiarnoGenerowaniaRozwiazan(56790);
        xmlEncoder.writeObject(u);
        xmlEncoder.close();*/

        XMLDecoder xmlDecoder = new XMLDecoder(file);
        ustawienia = (Ustawienia) xmlDecoder.readObject();
    }

    private void wczytajDane() throws FileNotFoundException, NumberFormatException {
        File file = new File(ustawienia.getPlikWejsciowy());
        Scanner scanner = new Scanner(file);

        wczytajSiec(scanner);
        wczytajSeparator(scanner);
        wczytajZapotrzebowania(scanner);
    }

    private void wczytajSiec(Scanner scanner) {
        String linia;
        String[] liniaLista;
        Lacze lacze;
        int id = 0;

        int il_laczy = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < il_laczy; i++) {
            linia = scanner.nextLine();
            liniaLista = linia.split(" ");
            id = i + 1;
            lacze = new Lacze(id, Integer.parseInt(liniaLista[0]), Integer.parseInt(liniaLista[1]), Integer.parseInt(liniaLista[2]), Float.parseFloat(liniaLista[3]), Integer.parseInt(liniaLista[4]));
            listaLaczy.add(lacze);
        }
    }

    private void wczytajZapotrzebowania(Scanner scanner) {
        scanner.nextLine();
        int il_zapotrzebowan = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < il_zapotrzebowan; i++) {
            wczytajPojedynczeZapotrzebowanie(i + 1, scanner);
        }
    }

    private void wczytajPojedynczeZapotrzebowanie(int id, Scanner scanner) {
        String linia;
        String[] liniaLista;
        Zapotrzebowanie zapotrzebowanie;

        scanner.nextLine();
        linia = scanner.nextLine();
        liniaLista = linia.split(" ");

        zapotrzebowanie = new Zapotrzebowanie(id, Integer.parseInt(liniaLista[0]), Integer.parseInt(liniaLista[1]), Integer.parseInt(liniaLista[2]));
        zapotrzebowanie.iloscSciezek = Integer.parseInt(scanner.nextLine());

        for (int j = 0; j < zapotrzebowanie.iloscSciezek; j++) {
            wczytajSciezke(scanner, linia, liniaLista, zapotrzebowanie);
        }
        listaZapotrzebowan.add(zapotrzebowanie);

    }

    private void wczytajSciezke(Scanner scanner, String linia, String[] liniaLista, Zapotrzebowanie zapotrzebowanie) {
        linia = scanner.nextLine();
        liniaLista = linia.split(" ");
        List<Integer> listaId = new ArrayList<Integer>();
        for (int a = 1; a < liniaLista.length; a++) {
            listaId.add(Integer.parseInt(liniaLista[a]));
        }
        zapotrzebowanie.listaSciezek.add(new Sciezka(Integer.parseInt(liniaLista[0]), listaId));

    }

    private void wczytajSeparator(Scanner scanner) {
        int separator = Integer.parseInt(scanner.nextLine());
        if (separator != -1) {
            System.out.println("Bledny format danych! \n");
            return;
        }
    }

    private Integer pokazMenu() {
        System.out.println("Jeśli chcesz rozpocząć brute force wciśnij [0]");
        System.out.println("Jeśli chcesz rozpocząć algorytm ewolucyjny wciśnij [1]");
        System.out.println("Jeśli chcesz zakończyć wciśnij [2]");
        System.out.println("Wprowadź odpowiedź: ");

        Scanner in = new Scanner(System.in);
        int num = in.nextInt();

        return num;
    }
}
