import org.apfloat.*;

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

        // Load saved game data if it exists
        try {
            FileInputStream fileInputStream = new FileInputStream("Saved Idle Galaxy Builder.txt");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            savedData = (SavedData) in.readObject();
            in.close();
            fileInputStream.close();

            // Getting the time now
            long now = System.currentTimeMillis();
            long difference = now - savedData.saveTime;
            long differenceInSeconds = (long) (difference / 1000.0);

            // Give offline rewards to the player
            Apfloat toAdd = new Apfloat("0");
            for (IdleGalaxyBuilder.Planet planet : savedData.planets){
                if (planet.getLevel().compareTo(new Apfloat("0")) > 0) {
                    toAdd.add(planet.getEnergyProductionRate().multiply(new Apfloat(differenceInSeconds)));
                }

                if (planet.unlocked){
                    planet.unlock();
                }
            }

            savedData.player.energy = savedData.player.energy.add(toAdd);
            JOptionPane.showMessageDialog(null, "You have gained " + toAdd + " energy for being " +
                    "offline for " + differenceInSeconds + " seconds!");
        }
        catch (IOException | ClassNotFoundException ioException) {
            // Creating new game data
            savedData = new SavedData();
            savedData.player = new Player();
            savedData.planets = planets;
        }

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
                    savedData.clicker = savedData.clicker.add(new Apfloat(1));
                    savedData.player.energy = savedData.player.energy.subtract(savedData.clickerPrice);
                    savedData.clickerPrice = savedData.clickerPrice.multiply(new Apfloat(2));
                    JOptionPane.showMessageDialog(null, "You have improved your clicker!");
                }
                else{
                    JOptionPane.showMessageDialog(null, "You have insufficient energy!");
                }
            }
        });

        // Actualize the progress
        javax.swing.Timer actualizeProgress = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                energyLabel.setText("Energy: " + savedData.player.energy);
                clickerLabel.setText("Clicker Level: " + savedData.clicker);
                increaseClickerButton.setText("Improve Clicker (Energy Cost: " + savedData.clickerPrice + ")");
            }
        });
        actualizeProgress.start();

        // Unlock more planets
        javax.swing.Timer getMorePlanets = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < savedData.planets.size(); i++){
                    Planet currPlanet = savedData.planets.get(i);
                    if (i == 0){
                        if (!currPlanet.unlocked && savedData.clicker.compareTo(new Apfloat("2")) > 0) {
                            currPlanet.unlock();
                        }
                    }
                    else{
                        if (!currPlanet.unlocked && savedData.planets.get(i - 1).level.compareTo(new Apfloat("2")) > 0){
                            currPlanet.unlock();
                        }
                    }

                }
            }
        });
        getMorePlanets.start();

        // Produce energy with planets
        javax.swing.Timer produceWithPlanets = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Planet planet : savedData.planets){
                    if (planet.level.compareTo(new Apfloat("0")) > 0) {
                        savedData.player.energy = savedData.player.energy.add(planet.energyProductionRate);
                    }
                }
            }
        });
        produceWithPlanets.start();

        // Save game data
        saveGameButton = new JButton("SAVE GAME");
        saveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    savedData.saveTime = System.currentTimeMillis();
                    FileOutputStream fileOutputStream = new FileOutputStream("Saved Idle Galaxy Builder.txt");
                    ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
                    out.writeObject(savedData);
                    out.close();
                    fileOutputStream.close();
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

    public class Planet implements Serializable {
        /**
         * This class contains attributes of an affordable planet
         * */

        // Non graphical variables
        private final String name;
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

        public String toString(){
            String res = ""; // initial value
            res += "Name: " + name + "\n";
            res += "Level: " + level + "\n";
            res += "Energy/s: " + energyProductionRate + "\n";
            res += "Energy cost: " + energyCost + "\n";
            return res;
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
            if (savedData.player.energy.compareTo(energyCost) >= 0){
                level = level.add(new Apfloat(1));
                savedData.player.energy = savedData.player.energy.subtract(energyCost);
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IdleGalaxyBuilder idleGalaxyBuilder = new IdleGalaxyBuilder();
        idleGalaxyBuilder.setTitle("IDLE GALAXY BUILDER");
        idleGalaxyBuilder.setSize(1200, 1000);
        idleGalaxyBuilder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        idleGalaxyBuilder.setVisible(true);
    }
}
