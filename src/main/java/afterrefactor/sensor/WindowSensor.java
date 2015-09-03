package afterrefactor.sensor;

/**
 * Created by »ÆÏè on 15-8-22.
 */
public class WindowSensor extends Sensor {

    public static final String WINDOW_AJAR = " is ajar";
    public static final String WINDOW_CLOSE = " is sealed";

    public WindowSensor(String id, String location, SensorType type) {
        super(id, location, type);
    }

    @Override
    public String getSensorMessage() {
        return String.format("%s%s", getLocation(), isTripped() ? WINDOW_AJAR : WINDOW_CLOSE);
    }
}
