package afterrefactor.sensor;

public abstract class Sensor {
    private String id;
    private String location;
    private SensorType type;
    private boolean tripped;

    public Sensor(String id, String location, SensorType type) {
        this.id = id;
        this.location = location;
        this.type = type;
    }

    public abstract String getSensorMessage();

    public String getId() {
        return id;
    }

    public SensorType getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public void trip() {
        tripped = true;
    }

    public void reset() {
        tripped = false;
    }

    public boolean isTripped() {
        return tripped;
    }
}
