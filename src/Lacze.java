
public class Lacze {
	
	int id;
	int poczatek;
	int koniec;
	int iloscPar;
	float kosztPary;
	int liczbaLambdWeWloknie;
	
	public Lacze() {
		this.id = 0;
		this.poczatek = 0;
		this.koniec = 0;
		this.iloscPar = 0;
		this.kosztPary = 0;
	}
	
	public Lacze(int id_, int poczatek_, int koniec_, int ilPar_, float kosztPary_, int iloscLambd_) {
		this.id = id_;
		this.poczatek = poczatek_;
		this.koniec = koniec_;
		this.iloscPar = ilPar_;
		this.kosztPary = kosztPary_;
		this.liczbaLambdWeWloknie = iloscLambd_;
	}
}
