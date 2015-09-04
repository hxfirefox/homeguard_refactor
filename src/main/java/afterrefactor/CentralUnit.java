package afterrefactor;

import afterrefactor.alarm.AudibleAlarm;
import afterrefactor.sensor.Sensor;
import afterrefactor.view.HomeGuardView;
import afterrefactor.alarm.TextAudibleAlarm;
import afterrefactor.view.TextView;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

import static afterrefactor.SensorTestStatus.*;

public class CentralUnit {
    private static final String SENSOR_STATUS_TRIPPED = "TRIPPED";
    private String securityCode;
    private List<Sensor> sensors = Lists.newLinkedList();
    private final HomeGuardView view;

    // members to help with sensor tests
    private Map<String, SensorTestStatus> sensorTestStatusMap = Maps.newHashMap();
    private boolean runningSensorTest;
    private SensorTestStatus finalSensorTestStatus;

    private final AlarmManager alarmManager;

    public CentralUnit(HomeGuardView view, AudibleAlarm audibleAlarm) {
        this.view = view;
        this.alarmManager = new AlarmManager(audibleAlarm);
    }

    public CentralUnit() {
        this(new TextView(), new TextAudibleAlarm());
    }

    public void registerSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void parseRadioBroadcast(String packet) {
        // check if a sensor test is running and adjust status
        if (!isSensorTestRunning()) {
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

        // check to see if test is complete
        //terminate test if complete
        if (isSensorTestDone()) {
            terminateSensorTest();
        }

        // sound the alarm if armed
        alarmManager.enableAlarm();
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

    public void setSecurityCode(String code) {
        securityCode = code;
    }

    public void turnOffAlarm(String code) {
        if (isValidCode(code)) {
            alarmManager.disableAlarm();
        }
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    @VisibleForTesting
    protected SensorTestStatus getSesnsorTestStatus() {
        return finalSensorTestStatus;
    }

    @VisibleForTesting
    protected Map getSensorTestStatusMap() {
        return sensorTestStatusMap;
    }

    @VisibleForTesting
    protected boolean isSensorTestRunning() {
        return runningSensorTest;
    }

    private boolean isValidCode(String code) {
        return securityCode.equals(code);
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
        if (sensor == null) {
            return;
        }
        if (SENSOR_STATUS_TRIPPED.equals(status)) {
            sensor.trip();
        }
        else {
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

    private SensorTestStatus checkSensorTestStatus() {
        for (SensorTestStatus status : sensorTestStatusMap.values()) {
            if (status.equals(PENDING)) {
                return FAIL;
            }
        }
        return PASS;
    }
}
