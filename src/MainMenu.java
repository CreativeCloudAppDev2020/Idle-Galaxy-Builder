import org.apfloat.Apfloat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MainMenu extends JFrame {
    /**
     * This main menu can be used for the user to load saved game data.
     * */

    // Graphical variables
    Container container;
    JButton loadButton = new JButton("LOAD");
    JButton newGameButton = new JButton("NEW GAME");

    // Non graphical variable
    IdleGalaxyBuilder idleGalaxyBuilder = new IdleGalaxyBuilder();

    public MainMenu(){
        container = getContentPane();
        container.setLayout(new GridLayout(5, 1));

        // Loading saved game data
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the saved game data
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream("Saved Idle Galaxy Builder.txt"));
                    SavedData savedData = (SavedData) in.readObject();
                    SavedData.saveTime = savedData.saveTime;
                    SavedData.player = savedData.player;
                    SavedData.planets = savedData.planets;

                    // Getting the time now
                    long now = System.currentTimeMillis();
                    long difference = now - SavedData.saveTime;
                    long differenceInSeconds = (long) (difference / 1000.0);

                    // Give offline rewards to the player
                    Apfloat toAdd = new Apfloat("0");
                    for (IdleGalaxyBuilder.Planet planet : SavedData.planets){
                        if (planet.getLevel().compareTo(new Apfloat("0")) > 0) {
                            toAdd.add(planet.getEnergyProductionRate().multiply(new Apfloat(differenceInSeconds)));
                        }
                    }

                    SavedData.player.energy = SavedData.player.energy.add(toAdd);
                } catch (IOException | ClassNotFoundException ioException) {
                    // Creating new game data
                    SavedData.player = new Player();
                }

                // Move to the game frame
                idleGalaxyBuilder.setVisible(true);
                idleGalaxyBuilder.initialize();
                setVisible(false);
            }
        });

        // Creating new game data
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavedData.player = new Player();

                // Move to the game frame
                idleGalaxyBuilder.setVisible(true);
                idleGalaxyBuilder.initialize();
                setVisible(false);
            }
        });

        container.add(new JLabel("Press 'LOAD' to load saved game data."));
        container.add(new JLabel("Press 'NEW GAME' to create new game data."));
        container.add(loadButton);
        container.add(new JLabel("")); // blank label
        container.add(newGameButton);
    }

    public static void main(String[] args){
        MainMenu mainMenu = new MainMenu();
        mainMenu.setTitle("MAIN MENU");
        mainMenu.setSize(600, 500);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setVisible(true);
    }
}
