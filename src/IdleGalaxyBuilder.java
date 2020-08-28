import java.math.BigDecimal;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class IdleGalaxyBuilder extends JFrame implements Serializable {
    /**
     * This class contains attributes of the idle game "Idle Galaxy Builder".
     * */

    // Non graphical variables
    public SavedData savedData;

    // Graphical variables
    int numberOfColumns = 7;
    Container container;

    JLabel energyLabel;
    JButton gainEnergyButton;

    JLabel clickerLabel;
    JButton increaseClickerButton;

    JButton saveGameButton;

    // Initialise planets

    ArrayList<Planet> planets = new ArrayList<>();

    public IdleGalaxyBuilder() {
        // Load saved game data if it exists
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("Saved Idle Galaxy Builder.txt"));
            savedData = (SavedData) in.readObject();
            in.close();

            // Getting the time now
            long now = System.currentTimeMillis();
            long difference = now - savedData.saveTime;
            long differenceInSeconds = (long) (difference / 1000.0);

            // Give offline rewards to the player
            BigDecimal toAdd = new BigDecimal("0");
            for (IdleGalaxyBuilder.Planet planet : savedData.planets){
                if (planet.getLevel().compareTo(new BigDecimal("0")) > 0) {
                    toAdd.add(planet.getEnergyProductionRate().multiply(new BigDecimal(differenceInSeconds)));
                }
            }

            savedData.player.energy = savedData.player.energy.add(toAdd);
        }
        catch (IOException | ClassNotFoundException ioException) {
            // Creating new game data
            savedData = new SavedData();
            savedData.player = new Player();
        }

        container = getContentPane();
        container.setLayout(new GridLayout(7, 1));

        Planet Lelvaetov = new Planet("Lelvaetov", new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("1e2"));
        planets.add(Lelvaetov);

        Planet Rebos = new Planet("Rebos", new BigDecimal("0"), new BigDecimal("1e2"), new BigDecimal("1e4"));
        planets.add(Rebos);

        Planet Hunides = new Planet("Hunides", new BigDecimal("0"), new BigDecimal("1e4"), new BigDecimal("1e7"));
        planets.add(Hunides);

        Planet Lloinerth = new Planet("Lloinerth", new BigDecimal("0"), new BigDecimal("1e7"), new BigDecimal("1e11"));
        planets.add(Lloinerth);

        Planet Bameliv = new Planet("Bameliv", new BigDecimal("0"), new BigDecimal("1e11"), new BigDecimal("1e16"));
        planets.add(Bameliv);

        Planet Nalmeuhiri = new Planet("Nalmeuhiri", new BigDecimal("0"), new BigDecimal("1e16"), new BigDecimal("1e22"));
        planets.add(Nalmeuhiri);

        Planet Elvayama = new Planet("Elvayama", new BigDecimal("0"), new BigDecimal("1e22"), new BigDecimal("1e29"));
        planets.add(Elvayama);

        Planet Colnora = new Planet("Colnora", new BigDecimal("0"), new BigDecimal("1e29"), new BigDecimal("1e37"));
        planets.add(Colnora);

        Planet Acryria = new Planet("Acryria", new BigDecimal("0"), new BigDecimal("1e37"), new BigDecimal("1e46"));
        planets.add(Acryria);

        Planet Maogantu = new Planet("Maogantu", new BigDecimal("0"), new BigDecimal("1e46"), new BigDecimal("1e56"));
        planets.add(Maogantu);

        Planet Semia = new Planet("Semia", new BigDecimal("0"), new BigDecimal("1e56"), new BigDecimal("1e67"));
        planets.add(Semia);

        Planet Lloemia = new Planet("Lloemia", new BigDecimal("0"), new BigDecimal("1e67"), new BigDecimal("1e79"));
        planets.add(Lloemia);

        Planet Zagatov = new Planet("Zagatov", new BigDecimal("0"), new BigDecimal("1e79"), new BigDecimal("1e92"));
        planets.add(Zagatov);

        Planet Ninriavis = new Planet("Ninriavis", new BigDecimal("0"), new BigDecimal("1e92"), new BigDecimal("1e106"));
        planets.add(Ninriavis);

        Planet Invuiturn = new Planet("Invuiturn", new BigDecimal("0"), new BigDecimal("1e106"), new BigDecimal("1e121"));
        planets.add(Invuiturn);

        Planet Tociuq = new Planet("Tociuq", new BigDecimal("0"), new BigDecimal("1e121"), new BigDecimal("1e137"));
        planets.add(Tociuq);

        Planet Odides = new Planet("Odides", new BigDecimal("0"), new BigDecimal("1e137"), new BigDecimal("1e154"));
        planets.add(Odides);

        Planet Oacury = new Planet("Oacury", new BigDecimal("0"), new BigDecimal("1e154"), new BigDecimal("1e172"));
        planets.add(Oacury);

        Planet Chualiv = new Planet("Chualiv", new BigDecimal("0"), new BigDecimal("1e172"), new BigDecimal("1e191"));
        planets.add(Chualiv);

        Planet Strunenope = new Planet("Strunenope", new BigDecimal("0"), new BigDecimal("1e191"), new BigDecimal("1e211"));
        planets.add(Strunenope);

        Planet Xotriahiri = new Planet("Xotriahiri", new BigDecimal("0"), new BigDecimal("1e211"), new BigDecimal("1e232"));
        planets.add(Xotriahiri);

        savedData.planets = planets;

        // Produce energy by hand
        energyLabel = new JLabel("Energy: " + savedData.player.energy);
        gainEnergyButton = new JButton("Gain Energy");
        gainEnergyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savedData.player.energy = savedData.player.energy.add(savedData.clicker);
            }
        });

        // Improve clicking production rate
        clickerLabel = new JLabel("Clicker Level: " + savedData.clicker);
        increaseClickerButton = new JButton("Improve Clicker (Energy Cost: " + savedData.clickerPrice + ")");
        increaseClickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseClicker();
            }

            private void increaseClicker(){
                // if SavedData.player.energy >= clickerPrice
                if (savedData.player.energy.compareTo(savedData.clickerPrice) >= 0){
                    savedData.clicker = savedData.clicker.add(new BigDecimal(1));
                    savedData.player.energy = savedData.player.energy.subtract(savedData.clickerPrice);
                    savedData.clickerPrice = savedData.clickerPrice.multiply(new BigDecimal(2));
                    JOptionPane.showMessageDialog(null, "You have improved your clicker!");
                }
                else{
                    JOptionPane.showMessageDialog(null, "You have insufficient energy!");
                }
            }
        });

        // Actualize the progress
        java.util.Timer actualizeProgress = new java.util.Timer();
        actualizeProgress.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                energyLabel.setText("Energy: " + savedData.player.energy);
                clickerLabel.setText("Clicker Level: " + savedData.clicker);
                increaseClickerButton.setText("Improve Clicker (Energy Cost: " + savedData.clickerPrice + ")");
            }
        }, 0, 25);

        // Unlock more planets
        java.util.Timer getMorePlanets = new java.util.Timer();
        getMorePlanets.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < savedData.planets.size(); i++){
                    Planet currPlanet = savedData.planets.get(i);
                    if (i == 0){
                        if (!currPlanet.unlocked && savedData.clicker.compareTo(new BigDecimal("2")) > 0) {
                            currPlanet.unlock();
                        }
                    }
                    else{
                        if (!currPlanet.unlocked && savedData.planets.get(i - 1).level.compareTo(new BigDecimal("2")) > 0){
                            currPlanet.unlock();
                        }
                    }

                }
            }
        }, 0, 2000);

        // Produce energy with planets
        java.util.Timer produceWithPlanets = new java.util.Timer();
        produceWithPlanets.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (Planet planet : savedData.planets){
                    if (planet.level.compareTo(new BigDecimal("0")) > 0) {
                        savedData.player.energy = savedData.player.energy.add(planet.energyProductionRate);
                    }
                }
            }
        }, 0, 1000);

        // Save game data
        saveGameButton = new JButton("SAVE GAME");
        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    savedData.saveTime = System.currentTimeMillis();
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Saved Idle Galaxy Builder.txt"));
                    out.writeObject(savedData);
                    out.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        container.add(energyLabel);
        container.add(gainEnergyButton);
        container.add(new JLabel("")); // empty label
        container.add(clickerLabel);
        container.add(increaseClickerButton);
        container.add(new JLabel("")); // empty label
        container.add(saveGameButton);
    }

    public class Planet {
        /**
         * This class contains attributes of an affordable planet
         * */

        // Non graphical variables
        private final String name;
        private BigDecimal level;
        private BigDecimal energyProductionRate; // amount of energy produced per second
        private BigDecimal energyCost;
        private boolean unlocked = false;

        // Graphical variables
        JLabel label;
        JButton button;

        // Constructor
        public Planet(String name, BigDecimal level, BigDecimal energyProductionRate, BigDecimal energyCost){
            // Non graphical variables
            this.name = name;
            this.level = level;
            this.energyProductionRate = energyProductionRate;
            this.energyCost = energyCost;

            // Graphical variables
            label = new JLabel();
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    improve();
                }
            });
        }

        public String toString(){
            String res = ""; // initial value
            res += "Name: " + name + "\n";
            res += "Level: " + level + "\n";
            res += "Energy/s: " + energyProductionRate + "\n";
            res += "Energy cost: " + energyCost + "\n";
            return res;
        }

        public BigDecimal getEnergyProductionRate() {
            return energyProductionRate;
        }

        public BigDecimal getLevel() {
            return level;
        }

        public void unlock(){
            numberOfColumns += 3;
            unlocked = true;
            container.setLayout(new GridLayout(numberOfColumns, 1));
            container.add(new JLabel(""));
            container.add(label);
            container.add(button);
            setSize(1200, getHeight() + 120);
            actualize();
        }

        public void improve(){
            // if SavedData.player.energy >= energyCost
            if (savedData.player.energy.compareTo(energyCost) >= 0){
                level = level.add(new BigDecimal(1));
                savedData.player.energy = savedData.player.energy.subtract(energyCost);
                if (level.compareTo(new BigDecimal("1")) > 0) {
                    energyProductionRate = energyProductionRate.multiply(new BigDecimal("2"));
                }
                energyCost = energyCost.multiply(new BigDecimal("2"));
                JOptionPane.showMessageDialog(null, "You have improved " + name + "!");
            }
            else{
                JOptionPane.showMessageDialog(null, "You have insufficient energy!");
            }
            actualize();
        }

        public void actualize(){
            label.setText(name + " (Level " + level + ") produces " + energyProductionRate + " Energy/s");
            button.setText("Improve (Energy cost: " + energyCost + ")");
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IdleGalaxyBuilder idleGalaxyBuilder = new IdleGalaxyBuilder();
        idleGalaxyBuilder.setTitle("IDLE GALAXY BUILDER");
        idleGalaxyBuilder.setSize(1200, 1000);
        idleGalaxyBuilder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        idleGalaxyBuilder.setVisible(true);
    }
}
