package afterrefactor.sensor;

/**
 * Created by »ÆÏè on 15-8-22.
 */
public class FireSensor extends Sensor {

    public static final String FIRE_ON = " is on FIRE!";
    public static final String FIRE_OFF = " temperature is normal";

    public FireSensor(String id, String location, SensorType type) {
        super(id, location, type);
    }

    @Override
    public String getSensorMessage() {
        return String.format("%s%s", getLocation(), isTripped() ? FIRE_ON : FIRE_OFF);
    }
}
