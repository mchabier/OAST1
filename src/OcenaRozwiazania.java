import java.util.UUID;

public class OcenaRozwiazania implements Comparable {
    public Boolean CzyAkceptowalne;
    public Double Koszt;
    private UUID id;

    public OcenaRozwiazania() {
        id = UUID.randomUUID();
    }

    public int compareTo(Object o) {
        OcenaRozwiazania ocenaRozwiazaniaInna = (OcenaRozwiazania) o;
        if (this.CzyAkceptowalne && !ocenaRozwiazaniaInna.CzyAkceptowalne) {
            return -1;
        } else if (!this.CzyAkceptowalne && ocenaRozwiazaniaInna.CzyAkceptowalne) {
            return 1;
        }

        int value = this.Koszt.compareTo(ocenaRozwiazaniaInna.Koszt);

        if (value == 0) {
            return this.id.compareTo(ocenaRozwiazaniaInna.id);
        }

        return value;
    }
}
