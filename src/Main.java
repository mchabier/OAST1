import java.io.FileNotFoundException;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Program program = new Program();
        System.out.println("Start");
        try {
            program.start();
        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku! \n" + e);
        } catch (NumberFormatException e) {
            System.out.println("Błędny format danych! \n" + e);
        }
    }

}
