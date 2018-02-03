package pl.xayan.nai;

import pl.xayan.nai.ga.City;
import pl.xayan.nai.ga.Travel;
import pl.xayan.nai.ga.TravelManager;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {
    private TravelManager travelManager;
    private Travel travel;
    private boolean showCityNumbers = false;

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.clearRect(0, 0, 500, 500);

        if(travel != null) {
            g.setColor(Color.red);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for(int i = 0; i < travel.getSize(); i++) {
                City city1 = travel.getCity(i);

                int nextCity = i + 1;
                if(nextCity >= travel.getSize()) {
                    nextCity = 0;
                }
                City city2 = travel.getCity(nextCity);

                g.drawLine(city1.getX(), city1.getY(), city2.getX(), city2.getY());
            }

            g2d.setStroke(new BasicStroke(0));
        }

        if(travelManager != null) {
            g.setColor(Color.BLACK);

            for(int i = 0; i < travelManager.getCityCount(); i++) {
                City city = travelManager.getCity(i);

                g.fillOval(city.getX() - 4, city.getY() - 4, 8, 8);
            }

            if(showCityNumbers && travel != null) {
                for(int i = 0; i < travel.getSize(); i++) {
                    City city = travel.getCity(i);
                    String name = "" + i;
                    g.drawString(name, city.getX() + 5, city.getY() + 5);
                }
            }
        }
    }

    public void setTravelManager(TravelManager travelManager) {
        this.travelManager = travelManager;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public void setShowCityNumbers(boolean showCityNumbers) {
        this.showCityNumbers = showCityNumbers;
    }
}
