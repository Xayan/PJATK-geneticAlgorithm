package pl.xayan.nai.ga;

public class Population {
    private Travel[] travels;

    public Population(TravelManager travelManager, int populationSize, boolean initialise) {
        this.travels = new Travel[populationSize];
        // If we need to initialise a population of tours do so
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < getPopulationSize(); i++) {
                Travel newTour = new Travel(travelManager);
                newTour.generateIndividual();
                saveTour(i, newTour);
            }
        }
    }

    // Saves a tour
    public void saveTour(int index, Travel tour) {
        this.travels[index] = tour;
    }

    // Gets a tour from population
    public Travel getTour(int index) {
        return this.travels[index];
    }

    // Gets the best tour in the population
    public Travel getFittest() {
        Travel fittest = this.travels[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < getPopulationSize(); i++) {
            if (fittest.getFitness() <= getTour(i).getFitness()) {
                fittest = getTour(i);
            }
        }
        return fittest;
    }

    // Gets population size
    public int getPopulationSize() {
        return this.travels.length;
    }
}