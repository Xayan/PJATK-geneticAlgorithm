package pl.xayan.nai;

import pl.xayan.nai.Simulation;
import pl.xayan.nai.ga.Travel;

import java.util.ArrayList;
import java.util.List;

public class PlayThread implements Runnable {
    private Simulation simulation;
    private List<Travel> fittests = new ArrayList<>();
    private int start;

    public PlayThread(Simulation simulation, List<Travel> fittests, int start) {
        super();

        this.simulation = simulation;
        this.fittests = fittests;
        this.start = start;
    }

    @Override
    public void run() {
        this.simulation.setCurrentPopulation(0);

        int lastDistance = 0;

        for(int i = start; i < this.fittests.size(); i++) {
            Travel fittest = this.fittests.get(i);

            if(fittest.getDistance() != lastDistance) {
                lastDistance = fittest.getDistance();

                simulation.setCurrentPopulation(i);
                simulation.drawMap();

                try {
                    Thread.sleep(200);
                } catch(InterruptedException e) {
                    break;
                }
            }
        }
    }
}
