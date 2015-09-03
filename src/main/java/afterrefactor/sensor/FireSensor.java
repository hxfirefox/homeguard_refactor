package afterrefactor.sensor;

/**
 * Created by »ÆÏè on 15-8-22.
 */
public class FireSensor extends Sensor {
    public FireSensor(String id, String location, SensorType type) {
        super(id, location, type);
    }

    @Override
    public String getSensorMessage() {
        return String.format("%s%s", getLocation(), isTripped() ? " is on FIRE!" : " temperature is normal");
    }
}
