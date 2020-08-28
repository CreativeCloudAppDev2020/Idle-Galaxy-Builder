import org.apfloat.Apfloat;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedData implements Serializable {
    /**
     * This class contains attributes of saved game data.
     * */

    public static Player player;
    public static ArrayList<IdleGalaxyBuilder.Planet> planets = new ArrayList<>();
    public static Apfloat clicker = new Apfloat("1");
    public static Apfloat clickerPrice = new Apfloat("20");
    public static long saveTime = System.currentTimeMillis();
}
