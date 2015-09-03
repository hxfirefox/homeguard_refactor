package afterrefactor.sensor;

/**
 * Created by »ÆÏè on 15-8-22.
 */
public class MotionSensor extends Sensor {
    public MotionSensor(String id, String location, SensorType type) {
        super(id, location, type);
    }

    @Override
    public String getSensorMessage() {
        return isTripped() ? String.format("Motion detected in %s", getLocation()) : String.format("%s is motionless", getLocation());
    }
}
