import org.apfloat.Apfloat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class IdleGalaxyBuilder extends JFrame {
    /**
     * This class contains attributes of the idle game "Idle Galaxy Builder".
     * */

    // Non graphical variables
    private SavedData savedData = new SavedData();

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

    public IdleGalaxyBuilder(){
        if (SavedData.player == null) {
            savedData.player = new Player();
        }
        else{
            savedData.player = SavedData.player;
        }
        container = getContentPane();
        container.setLayout(new GridLayout(7, 1));

        Planet Lelvaetov = new Planet("Lelvaetov", new Apfloat("0"), new Apfloat("1"), new Apfloat("1e2"));
        planets.add(Lelvaetov);

        Planet Rebos = new Planet("Rebos", new Apfloat("0"), new Apfloat("1e2"), new Apfloat("1e4"));
        planets.add(Rebos);

        Planet Hunides = new Planet("Hunides", new Apfloat("0"), new Apfloat("1e4"), new Apfloat("1e7"));
        planets.add(Hunides);

        Planet Lloinerth = new Planet("Lloinerth", new Apfloat("0"), new Apfloat("1e7"), new Apfloat("1e11"));
        planets.add(Lloinerth);

        Planet Bameliv = new Planet("Bameliv", new Apfloat("0"), new Apfloat("1e11"), new Apfloat("1e16"));
        planets.add(Bameliv);

        Planet Nalmeuhiri = new Planet("Nalmeuhiri", new Apfloat("0"), new Apfloat("1e16"), new Apfloat("1e22"));
        planets.add(Nalmeuhiri);

        Planet Elvayama = new Planet("Elvayama", new Apfloat("0"), new Apfloat("1e22"), new Apfloat("1e29"));
        planets.add(Elvayama);

        Planet Colnora = new Planet("Colnora", new Apfloat("0"), new Apfloat("1e29"), new Apfloat("1e37"));
        planets.add(Colnora);

        Planet Acryria = new Planet("Acryria", new Apfloat("0"), new Apfloat("1e37"), new Apfloat("1e46"));
        planets.add(Acryria);

        Planet Maogantu = new Planet("Maogantu", new Apfloat("0"), new Apfloat("1e46"), new Apfloat("1e56"));
        planets.add(Maogantu);

        Planet Semia = new Planet("Semia", new Apfloat("0"), new Apfloat("1e56"), new Apfloat("1e67"));
        planets.add(Semia);

        Planet Lloemia = new Planet("Lloemia", new Apfloat("0"), new Apfloat("1e67"), new Apfloat("1e79"));
        planets.add(Lloemia);

        Planet Zagatov = new Planet("Zagatov", new Apfloat("0"), new Apfloat("1e79"), new Apfloat("1e92"));
        planets.add(Zagatov);

        Planet Ninriavis = new Planet("Ninriavis", new Apfloat("0"), new Apfloat("1e92"), new Apfloat("1e106"));
        planets.add(Ninriavis);

        Planet Invuiturn = new Planet("Invuiturn", new Apfloat("0"), new Apfloat("1e106"), new Apfloat("1e121"));
        planets.add(Invuiturn);

        Planet Tociuq = new Planet("Tociuq", new Apfloat("0"), new Apfloat("1e121"), new Apfloat("1e137"));
        planets.add(Tociuq);

        Planet Odides = new Planet("Odides", new Apfloat("0"), new Apfloat("1e137"), new Apfloat("1e154"));
        planets.add(Odides);

        Planet Oacury = new Planet("Oacury", new Apfloat("0"), new Apfloat("1e154"), new Apfloat("1e172"));
        planets.add(Oacury);

        Planet Chualiv = new Planet("Chualiv", new Apfloat("0"), new Apfloat("1e172"), new Apfloat("1e191"));
        planets.add(Chualiv);

        Planet Strunenope = new Planet("Strunenope", new Apfloat("0"), new Apfloat("1e191"), new Apfloat("1e211"));
        planets.add(Strunenope);

        Planet Xotriahiri = new Planet("Xotriahiri", new Apfloat("0"), new Apfloat("1e211"), new Apfloat("1e232"));
        planets.add(Xotriahiri);

        SavedData.planets = planets;

        // Produce energy by hand
        energyLabel = new JLabel("Energy: " + SavedData.player.energy);
        gainEnergyButton = new JButton("Gain Energy");
        gainEnergyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavedData.player.energy = SavedData.player.energy.add(SavedData.clicker);
            }
        });

        // Improve clicking production rate
        clickerLabel = new JLabel("Clicker Level: " + SavedData.clicker);
        increaseClickerButton = new JButton("Improve Clicker (Energy Cost: " + SavedData.clickerPrice + ")");
        increaseClickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseClicker();
            }

            private void increaseClicker(){
                // if SavedData.player.energy >= clickerPrice
                if (SavedData.player.energy.compareTo(SavedData.clickerPrice) >= 0){
                    SavedData.clicker = SavedData.clicker.add(new Apfloat(1));
                    SavedData.player.energy = SavedData.player.energy.subtract(SavedData.clickerPrice);
                    SavedData.clickerPrice = SavedData.clickerPrice.multiply(new Apfloat(2));
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
                energyLabel.setText("Energy: " + SavedData.player.energy);
                clickerLabel.setText("Clicker Level: " + SavedData.clicker);
                increaseClickerButton.setText("Improve Clicker (Energy Cost: " + SavedData.clickerPrice + ")");
            }
        }, 0, 25);

        // Unlock more planets
        java.util.Timer getMorePlanets = new java.util.Timer();
        getMorePlanets.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (int i = 0; i < SavedData.planets.size(); i++){
                    Planet currPlanet = SavedData.planets.get(i);
                    if (i == 0){
                        if (!currPlanet.unlocked && SavedData.clicker.compareTo(new Apfloat("2")) > 0) {
                            currPlanet.unlock();
                        }
                    }
                    else{
                        if (!currPlanet.unlocked && SavedData.planets.get(i - 1).level.compareTo(new Apfloat("2")) > 0){
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
                for (Planet planet : SavedData.planets){
                    if (planet.level.compareTo(new Apfloat("0")) > 0) {
                        SavedData.player.energy = SavedData.player.energy.add(planet.energyProductionRate);
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
        private String name;
        private Apfloat level;
        private Apfloat energyProductionRate; // amount of energy produced per second
        private Apfloat energyCost;
        private boolean unlocked = false;

        // Graphical variables
        JLabel label;
        JButton button;

        // Constructor
        public Planet(String name, Apfloat level, Apfloat energyProductionRate, Apfloat energyCost){
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

        public Apfloat getEnergyProductionRate() {
            return energyProductionRate;
        }

        public Apfloat getLevel() {
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
            if (SavedData.player.energy.compareTo(energyCost) >= 0){
                level = level.add(new Apfloat(1));
                SavedData.player.energy = SavedData.player.energy.subtract(energyCost);
                if (level.compareTo(new Apfloat("1")) > 0) {
                    energyProductionRate = energyProductionRate.multiply(new Apfloat("2"));
                }
                energyCost = energyCost.multiply(new Apfloat("2"));
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

    public static void initialize(){
        IdleGalaxyBuilder idleGalaxyBuilder = new IdleGalaxyBuilder();
        idleGalaxyBuilder.setTitle("IDLE GALAXY BUILDER");
        idleGalaxyBuilder.setSize(1200, 1000);
        idleGalaxyBuilder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        idleGalaxyBuilder.setVisible(true);
    }

    public static void main(String[] args){
        initialize();
    }
}
