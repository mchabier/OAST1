import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {
		Program program = new Program();
		try {
			program.start();
		} catch (FileNotFoundException e) {
			System.out.println("Nie znaleziono pliku! \n" + e);
		} catch (NumberFormatException e) {
			System.out.println("B³êdny format danych! \n" + e);
		}
	}

}
