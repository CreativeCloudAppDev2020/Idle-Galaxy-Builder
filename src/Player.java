import java.math.BigDecimal;

import java.io.Serializable;

public class Player implements Serializable {
    /**
     * This class contains attributes of the player in this game.
     * */

    public BigDecimal energy;

    public Player(){
        energy = new BigDecimal("0");
    }

    @Override
    public String toString() {
        return "Energy: " + energy + "\n";
    }
}
