import java.util.ArrayList;
import java.util.List;

public class Zapotrzebowanie {
	int id;
	int poczatek;
	int koniec;
	int wartosc;
	int iloscSciezek;
	List<Sciezka> listaSciezek = new ArrayList<Sciezka>();
	
	Zapotrzebowanie(int id, int poczatek_, int koniec_, int wartosc_){
		this.id = id;
		this.poczatek = poczatek_;
		this.koniec = koniec_;
		this.wartosc = wartosc_;
		this.iloscSciezek = 0;
	}

}
