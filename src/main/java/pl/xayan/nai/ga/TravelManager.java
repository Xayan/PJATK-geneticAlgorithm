package pl.xayan.nai.ga;

import java.util.ArrayList;

public class TravelManager {
    private ArrayList<City> destinationCities = new ArrayList<>();

    public void addCity(City city) {
        destinationCities.add(city);
    }

    public City getCity(int index){
        return destinationCities.get(index);
    }

    public int getCityCount(){
        return destinationCities.size();
    }
}