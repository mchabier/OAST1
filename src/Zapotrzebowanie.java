import java.util.ArrayList;
import java.util.List;

public class Zapotrzebowanie {
	int poczatek;
	int koniec;
	int wartosc;
	int iloscSciezek;
	List<Sciezka> listaSciezek = new ArrayList<Sciezka>();
	
	Zapotrzebowanie(int poczatek_, int koniec_, int wartosc_){
		poczatek = poczatek_;
		koniec = koniec_;
		wartosc = wartosc_;
		iloscSciezek = 0;
	}

}
