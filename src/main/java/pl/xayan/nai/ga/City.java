package pl.xayan.nai.ga;

public class City {
    private int x;
    private int y;

    private final int MAP_WIDTH = 500;
    private final int MAP_HEIGHT = 500;

    public City() {
        this.x = (int) (Math.random() * 500);
        this.y = (int) (Math.random() * 500);
    }

    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double distanceTo(City city) {
        int xDistance = Math.abs(getX() - city.getX());
        int yDistance = Math.abs(getY() - city.getY());
        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );

        return distance;
    }

    @Override
    public String toString() {
        return getX()+", "+getY();
    }
}