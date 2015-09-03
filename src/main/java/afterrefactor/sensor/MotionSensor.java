package afterrefactor.sensor;

/**
 * Created by »ÆÏè on 15-8-22.
 */
public class MotionSensor extends Sensor {

    public static final String MOTION_FOUND = "Motion detected in %s";
    public static final String MOTION_LESS = "%s is motionless";

    public MotionSensor(String id, String location, SensorType type) {
        super(id, location, type);
    }

    @Override
    public String getSensorMessage() {
        return isTripped() ? String.format(MOTION_FOUND, getLocation()) : String.format(MOTION_LESS, getLocation());
    }
}
