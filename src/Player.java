import org.apfloat.Apfloat;

import java.io.Serializable;

public class Player implements Serializable {
    /**
     * This class contains attributes of the player in this game.
     * */

    public Apfloat energy;

    public Player(){
        energy = new Apfloat("0");
    }

    @Override
    public String toString() {
        return "Energy: " + energy + "\n";
    }
}
