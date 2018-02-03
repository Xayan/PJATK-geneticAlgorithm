package pl.xayan.nai;

import pl.xayan.nai.ga.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Simulation extends JFrame {
    private Map mapPanel;
    private TravelManager travelManager;
    private Thread playThread;
    private List<Travel> fittests = new ArrayList<>();

    private JLabel generationLabel;
    private JSlider generationSlider;
    private JLabel distanceLabel;

    private int currentPopulation = 0;
    private int populationCount;
    private int populationMembers;
    private int cityCount;

    public Simulation() throws HeadlessException {
        this.setTitle("Simulation");
        this.setSize(1000, 540);
        this.setVisible(true);
        this.setLayout(new GridLayout(0, 2));

        Dimension mapPanelDimension = new Dimension(500, 500);
        this.mapPanel = new Map();
        this.mapPanel.setSize(mapPanelDimension);
        this.mapPanel.setMinimumSize(mapPanelDimension);
        this.mapPanel.setMaximumSize(mapPanelDimension);
        this.mapPanel.setPreferredSize(mapPanelDimension);

        JPanel statsPanel = new JPanel();

        this.generationLabel = new JLabel();
        this.generationLabel.setFont(new Font("Arial", Font.BOLD, 24));

        this.distanceLabel = new JLabel();
        this.distanceLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton firstButton = new JButton("<< First");
        firstButton.addActionListener(e -> {
            currentPopulation = 0;
            drawMap();
        });

        JButton previousButton = new JButton("< Previous");
        previousButton.addActionListener(e -> {
            if(currentPopulation <= 0) return;

            int currentDist = fittests.get(currentPopulation).getDistance();
            int higherDist = 0;
            for(int i = currentPopulation - 1; i >= 0; i--) {
                int distance = fittests.get(i).getDistance();
                if(distance > currentDist) {
                    higherDist = distance;
                    break;
                }
            }

            for(int i = 0; i < currentPopulation; i++) {
                if(fittests.get(i).getDistance() == higherDist) {
                    currentPopulation = i;
                    break;
                }
            }

            drawMap();
        });

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            updatePlayThread();
        });

        JButton nextButton = new JButton("Next >");
        nextButton.addActionListener(e -> {
            if(currentPopulation >= fittests.size() - 1) return;

            int currentDist = fittests.get(currentPopulation).getDistance();
            for(int i = currentPopulation + 1; i < fittests.size(); i++) {
                if(fittests.get(i).getDistance() < currentDist) {
                    currentPopulation = i;
                    break;
                }
            }

            drawMap();
        });

        JButton lastButton = new JButton("Last >>");
        lastButton.addActionListener(e -> {
            currentPopulation = fittests.size() - 1;
            drawMap();
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 5, 10, 10));
        buttonsPanel.add(firstButton);
        buttonsPanel.add(previousButton);
        buttonsPanel.add(playButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(lastButton);

        this.generationSlider = new JSlider(0, 0);
        this.generationSlider.setPaintLabels(true);
        this.generationSlider.setPaintTicks(true);
        generationSlider.addChangeListener(e -> {
            currentPopulation = generationSlider.getValue();
            if(currentPopulation >= fittests.size()) {
                currentPopulation = fittests.size() - 1;
            }

            drawMap();
        });

        JCheckBox cityNumberCheckbox = new JCheckBox("Show cities indexes", false);
        cityNumberCheckbox.addActionListener(e -> {
            this.mapPanel.setShowCityNumbers(cityNumberCheckbox.isSelected());

            drawMap();
        });

        statsPanel.setLayout(new GridLayout(0, 1, 20, 20));
        statsPanel.add(generationLabel);
        statsPanel.add(distanceLabel);
        statsPanel.add(buttonsPanel);
        statsPanel.add(generationSlider);
        statsPanel.add(cityNumberCheckbox);
        statsPanel.add(new JLabel());
        statsPanel.add(new JLabel());
        statsPanel.add(new JLabel());
        statsPanel.add(new JLabel());
        statsPanel.add(new JLabel());

        this.add(this.mapPanel);
        this.add(statsPanel);
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

    public void setPopulationMembers(int populationMembers) {
        this.populationMembers = populationMembers;
    }

    public void setCityCount(int cityCount) {
        this.cityCount = cityCount;
    }

    public void prepare() {
        this.travelManager = new TravelManager();

        System.out.println();
        System.out.println("================");
        System.out.println("=NEW SIMULATION=");
        System.out.println("================");

        for(int i = 0; i < this.cityCount; i++) {
            City city = new City();
            travelManager.addCity(city);
        }

        // Initialize population
        Population currentPopulation = new Population(travelManager, this.populationMembers, true);

        GA ga = new GA(travelManager);
        ga.setMutationRate(1.0 / cityCount);

        int maxStreak = (int) (1.0 / populationMembers * 100000.0);
        for (int i = 0; (i < this.populationCount || this.populationCount == 0); i++) {
            int streak = getScoreStreak();
            System.out.println("Generation #" + i + ": " + currentPopulation.getFittest().getDistance() + " (streak: " + streak + ")");
            currentPopulation = ga.evolvePopulation(currentPopulation);
            fittests.add(currentPopulation.getFittest());

            if(this.populationCount == 0 && streak >= maxStreak) {
                break;
            }

        }

        this.drawMap();
    }

    public void drawMap() {
        Travel fittest = this.fittests.get(this.currentPopulation);

        this.generationSlider.setMinorTickSpacing((int) (this.fittests.size() / 100));
        this.generationSlider.setMajorTickSpacing((int) (this.fittests.size() / 10));
        this.generationSlider.setMinimum(0);
        this.generationSlider.setMaximum(this.fittests.size());
        this.generationSlider.setValue(this.currentPopulation);

        this.generationLabel.setText("Generation: " + (this.currentPopulation + 1) + "/" + this.fittests.size());

        this.distanceLabel.setText("Distance: " + fittest.getDistance());

        this.mapPanel.setTravelManager(this.travelManager);
        this.mapPanel.setTravel(fittest);
        this.mapPanel.repaint();
    }

    public void setCurrentPopulation(int currentPopulation) {
        this.currentPopulation = currentPopulation;
    }

    private int getScoreStreak() {
        if(fittests.isEmpty()) {
            return 0;
        }

        int minScore = fittests.get(fittests.size() - 1).getDistance();
        int streak = 1;

        for(int i = 0; i < fittests.size() - 1; i++) {
            if(fittests.get(i).getDistance() == minScore) {
                streak++;
            }
        }

        return streak;
    }

    private void updatePlayThread() {
        if(playThread == null) {
            playThread = new Thread(new PlayThread(this, this.fittests, currentPopulation));
            playThread.start();
        } else {
            playThread.interrupt();
            playThread = null;
        }
    }
}
