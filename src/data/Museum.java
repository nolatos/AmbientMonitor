package data;

/**
 * Created by olive on 8/09/2018.
 */
public class Museum {

    private int id;
    private double temperature;
    private double humidity;
    private boolean doorOpened;
    private boolean isCloudy;
    private boolean isRaining;


    public Museum(int id, double temperature, double humidity, boolean doorOpened, boolean isCloudy, boolean isRaining) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.doorOpened = doorOpened;
        this.isCloudy = isCloudy;
        this.isRaining = isRaining;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

}
