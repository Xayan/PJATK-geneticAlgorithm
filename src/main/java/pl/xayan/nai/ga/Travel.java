package pl.xayan.nai.ga;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Travel {
    private List<City> travels = new ArrayList<>();
    private TravelManager travelManager;

    private double fitness = 0;
    private int distance = 0;


    public Travel(TravelManager travelManager) {
        this.travelManager = travelManager;

        for (int i = 0; i < this.travelManager.getCityCount(); i++) {
            this.travels.add(null);
        }
    }

    public Travel(ArrayList<City> travels){
        this.travels = travels;
    }

    public void generateIndividual() {
        for (int cityIndex = 0; cityIndex < this.travelManager.getCityCount(); cityIndex++) {
            setCity(cityIndex, this.travelManager.getCity(cityIndex));
        }

        Collections.shuffle(this.travels);
    }

    public City getCity(int tourPosition) {
        return this.travels.get(tourPosition);
    }

    public void setCity(int tourPosition, City city) {
        this.travels.set(tourPosition, city);

        fitness = 0;
        distance = 0;
    }

    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }

    // Gets the total distance of the tour
    public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex = 0; cityIndex < getSize(); cityIndex++) {
                City fromCity = getCity(cityIndex);
                City destinationCity;

                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
                if(cityIndex+1 < getSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }

                // Get the distance between the two cities
                tourDistance += fromCity.distanceTo(destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }

    public int getSize() {
        return this.travels.size();
    }

    public boolean containsCity(City city){
        return this.travels.contains(city);
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < getSize(); i++) {
            geneString += getCity(i)+"|";
        }
        return geneString;
    }
}