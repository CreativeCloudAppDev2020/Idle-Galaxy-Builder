import java.math.BigDecimal;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedData implements Serializable {
    /**
     * This class contains attributes of saved game data.
     * */

    public Player player;
    public ArrayList<IdleGalaxyBuilder.Planet> planets = new ArrayList<>();
    public BigDecimal clicker = new BigDecimal("1");
    public BigDecimal clickerPrice = new BigDecimal("20");
    public long saveTime = System.currentTimeMillis();

    public String toString(){
        String res = ""; // initial value
        res += player.toString() + "\n";
        for (IdleGalaxyBuilder.Planet planet : planets){
            res += planet.toString() + "\n";
        }

        res += "Clicker Level: " + clicker + "\n";
        res += "Clicker Price: " + clickerPrice + "\n";
        res += "Save Time (milliseconds): " + saveTime + "\n";
        return res;
    }
}
