package afterrefactor;

import afterrefactor.sensor.Sensor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

import static afterrefactor.SensorTestStatus.*;

public class CentralUnit {
    private boolean armed = false;
    private String securityCode;
    private List<Sensor> sensors = Lists.newLinkedList();
    private final HomeGuardView view;
    private final AudibleAlarm audibleAlarm;

    // members to help with sensor tests
    private Map<String, SensorTestStatus> sensorTestStatusMap = Maps.newHashMap();
    private boolean runningSensorTest;
    private SensorTestStatus finalSensorTestStatus;

    public CentralUnit(HomeGuardView view, AudibleAlarm audibleAlarm) {
        this.view = view;
        this.audibleAlarm = audibleAlarm;
    }

    public CentralUnit() {
        this(new TextView(), new TextAudibleAlarm());
    }

    public boolean isArmed() {
        return armed;
    }

    public void arm() {
        armed = true;
    }

    public void setSecurityCode(String code) {
        securityCode = code;
    }

    public boolean isValidCode(String code) {
        return securityCode.equals(code);
    }

    public void enterCode(String code) {
        if (isValidCode(code)) {
            armed = false;
            audibleAlarm.silence();
        }
    }

    public boolean audibleAlarmIsOn() {
        return false;
    }

    public List getSensors() {
        return sensors;
    }

    public void registerSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void parseRadioBroadcast(String packet) {
        // check if a sensor test is running and adjust status
        if (!runningSensorTest) {
            return;
        }

        //parse the packet
        String[] tokens = packet.split(",");
        String id = tokens[0];
        String status = tokens[1];

        // find sensor with id
        Sensor sensor = chooseSensor(id);

        //trip or reset sensor
        tripOrResetSensor(status, sensor);
        passSensorTest(id, status);

        //get the message from the sensor and display it
        view.showMessage(sensor.getSensorMessage());

        // sound the alarm if armed
        if (isArmed()) {
            audibleAlarm.sound();
        }

        // check to see if test is complete
        //terminate test if complete
        if (isSensorTestDone())
            terminateSensorTest();
    }

    private void passSensorTest(String id, String status) {
        if ("TRIPPED".equals(status)) {
            sensorTestStatusMap.put(id, PASS);
        }
    }

    private boolean isSensorTestDone() {
        for (SensorTestStatus value : sensorTestStatusMap.values()) {
            if (PENDING.equals(value)) {
                return false;
            }
        }
        return true;
    }

    private void tripOrResetSensor(String status, Sensor sensor) {
        if (sensor != null) {
            if ("TRIPPED".equals(status))
                sensor.trip();
            else
                sensor.reset();
        }
    }

    private Sensor chooseSensor(String id) {
        for (Sensor s : sensors) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void runSensorTest() {
        changeRunningFlagAndSensorTestStatus(true, PENDING);

        // initialize the status map
        initAllSensorsTestStatus();
    }

    // used during sensor test
    public void terminateSensorTest() {
        changeRunningFlagAndSensorTestStatus(false, checkSensorTestStatus());
    }

    private void changeRunningFlagAndSensorTestStatus(boolean runningFlag, SensorTestStatus testStatus) {
        setRunningSensorTestFlag(runningFlag);
        finalSensorTestStatus = testStatus;
    }

    private void initAllSensorsTestStatus() {
        for (Sensor sensor : sensors) {
            sensorTestStatusMap.put(sensor.getId(), PENDING);
        }
    }

    private void setRunningSensorTestFlag(boolean flag) {
        runningSensorTest = flag;
    }

    @VisibleForTesting
    protected boolean getRunningSensorTestFlag() {
        return runningSensorTest;
    }

    private SensorTestStatus checkSensorTestStatus() {
        for (SensorTestStatus status : sensorTestStatusMap.values()) {
            if (status.equals(PENDING)) {
                return FAIL;
            }
        }
        return PASS;
    }

    // used during sensor test
    public SensorTestStatus getSesnsorTestStatus() {
        return finalSensorTestStatus;
    }

    // used during sensor test
    public Map getSensorTestStatusMap() {
        return sensorTestStatusMap;
    }
}
