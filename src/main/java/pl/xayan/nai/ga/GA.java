package pl.xayan.nai.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
    private TravelManager travelManager;

    private double mutationRate = 0.005;
    private final int tournamentSize = 5;

    public GA(TravelManager travelManager) {
        this.travelManager = travelManager;
    }

    // Evolves a population over one generation
    public Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(this.travelManager, pop.getPopulationSize(), false);

        newPopulation.saveTour(0, pop.getFittest());
        int elitismOffset = 1;

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = elitismOffset; i < newPopulation.getPopulationSize(); i++) {
            // Select parents
            Travel parent1 = tournamentSelection(pop);
            Travel parent2 = tournamentSelection(pop);
            // Crossover parents
            Travel child = crossover(parent1, parent2);
            // Add child to new population
            newPopulation.saveTour(i, child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int i = elitismOffset; i < newPopulation.getPopulationSize(); i++) {
            mutate(newPopulation.getTour(i));
        }

        return newPopulation;
    }

    // Applies crossover to a set of parents and creates offspring
    public Travel crossover(Travel parent1, Travel parent2) {
        // Create new child tour
        Travel child = new Travel(this.travelManager);

        // Get start and end sub tour positions for parent1's tour
        int startPos = (int) (Math.random() * parent1.getSize());
        int endPos = (int) (Math.random() * parent1.getSize());

        // Loop and add the sub tour from parent1 to our child
        for (int i = 0; i < child.getSize(); i++) {
            // If our start position is less than the end position
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setCity(i, parent1.getCity(i));
            } // If our start position is larger
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setCity(i, parent1.getCity(i));
                }
            }
        }

        // Loop through parent2's city tour
        for (int i = 0; i < parent2.getSize(); i++) {
            // If child doesn't have the city add it
            if (!child.containsCity(parent2.getCity(i))) {
                // Loop to find a spare position in the child's tour
                for (int ii = 0; ii < child.getSize(); ii++) {
                    // Spare position found, add city
                    if (child.getCity(ii) == null) {
                        child.setCity(ii, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        return child;
    }

    // Mutate a travel using swap mutation
    private void mutate(Travel travel) {
        Random random = new Random();

        if(random.nextDouble() <= 0.3) {
            int pos1 = 0, pos2 = 0;
            do {
                pos1 = random.nextInt(travel.getSize() - 1);
                pos2 = random.nextInt(travel.getSize() - 1);
            } while(Math.abs(pos1-pos2) < 1);

            reverseSequence(travel, Math.min(pos1, pos2), Math.max(pos1, pos2));

            if(random.nextDouble() <= 0.5) {
                return;
            }
        }

        // Loop through cities
        for(int tourPos1 = 0; tourPos1 < travel.getSize(); tourPos1++){
            // Apply mutation rate
            if(random.nextDouble() < mutationRate){
                int mutationType = random.nextInt(100);

                if(mutationType <= 30 && tourPos1 > 1) {
                    int newPos = tourPos1 - (random.nextInt(tourPos1 - 1) + 1);

                    shiftCityLeft(travel, tourPos1, newPos);
                } else if(mutationType <= 60 && tourPos1 < travel.getSize() - 1) {
                    int newPos = tourPos1 + (random.nextInt(travel.getSize() - tourPos1 - 1) + 1);

                    shiftCityRight(travel, tourPos1, newPos);
                } else {
                    // Get a second random position in the travel
                    int newPos = (int) (travel.getSize() * Math.random());

                    this.swapCity(travel, tourPos1, newPos);
                }
            }
        }
    }

    // Selects candidate tour for crossover
    private Travel tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(this.travelManager, tournamentSize, false);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getPopulationSize());
            tournament.saveTour(i, pop.getTour(randomId));
        }

        return tournament.getFittest();
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    private void swapCity(Travel travel, int pos1, int pos2) {
        City city1 = travel.getCity(pos1);
        City city2 = travel.getCity(pos2);

        travel.setCity(pos2, city1);
        travel.setCity(pos1, city2);
    }

    private void shiftCityRight(Travel travel, int pos1, int pos2) {
        if(pos1 >= pos2) {
            return;
        }

        City city = travel.getCity(pos1);
        for(int i = pos1; i < pos2; i++) {
            travel.setCity(i, travel.getCity(i + 1));
        }
        travel.setCity(pos2, city);
    }

    private void shiftCityLeft(Travel travel, int pos1, int pos2) {
        if(pos1 <= pos2) {
            return;
        }

        City city = travel.getCity(pos1);
        for(int i = pos1; i > pos2; i--) {
            travel.setCity(i, travel.getCity(i - 1));
        }
        travel.setCity(pos2, city);
    }

    private void reverseSequence(Travel travel, int startIndex, int endIndex) {
        List<City> cities = new ArrayList<>();
        for(int i = startIndex; i <= endIndex; i++) {
            cities.add(travel.getCity(i));
        }

        for(int i = 0; i < cities.size(); i++) {
            travel.setCity(startIndex + i, cities.get(cities.size() - 1 - i));
        }
    }
}