package afterrefactor;

import afterrefactor.sensor.Sensor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

import static afterrefactor.SensorTestStatus.*;

public class CentralUnit
{

	private boolean armed = false;
	private String securityCode;
	private List<Sensor> sensors = Lists.newLinkedList();
	private HomeGuardView view = new TextView();
	private AudibleAlarm audibleAlarm = new TextAudibleAlarm();

	// members to help with sensor tests
	private Map<String, SensorTestStatus> sensorTestStatusMap = Maps.newHashMap();
	private boolean runningSensorTest;
	private SensorTestStatus sensorTestStatus;

	public boolean isArmed()
	{
		return armed;
	}

	public void arm()
	{
		armed = true;
	}

	public void setSecurityCode(String code)
	{
		securityCode = code;
	}

	public boolean isValidCode(String code)
	{
		return securityCode.equals(code);
	}

	public void enterCode(String code)
	{
		if(isValidCode(code))
		{
			armed = false;
			audibleAlarm.silence();
		}
	}

	public boolean audibleAlarmIsOn()
	{
		return false;
	}

	public List getSensors()
	{
		return sensors;
	}

	public void registerSensor(Sensor sensor)
	{
		sensors.add(sensor);
	}

	public void setView(HomeGuardView view)
	{
		this.view = view;
	}

	public void setAudibleAlarm(AudibleAlarm alarm)
	{
		audibleAlarm = alarm;
	}

	public void parseRadioBroadcast(String packet)
	{
		//parse the packet
		String[] tokens = packet.split(",");
		String id = tokens[0];
		String status = tokens[1];

		// find sensor with id
		Sensor sensor = null;
		for(Iterator iterator = sensors.iterator(); iterator.hasNext();)
		{
			Sensor s = (Sensor) iterator.next();
			if(s.getId().equals(id))
			{
				sensor = s;
				break;
			}
		}

		//trip or reset sensor
		if(sensor != null)
		{
			if("TRIPPED".equals(status))
				sensor.trip();
			else
				sensor.reset();
		}

		//get the message from the sensor and display it
		String message = sensor.getSensorMessage();
		view.showMessage(message);

		// sound the alarm if armed
		if(isArmed())
			audibleAlarm.sound();

		// check if a sensor test is running and adjust status
		if(runningSensorTest)
		{
			if("TRIPPED".equals(status))
			{
				sensorTestStatusMap.put(id, PASS);
			}

			// check to see if test is complete
			boolean done = true;
			for (Object o : sensorTestStatusMap.values()) {
				String testStatus = (String) o;
				if (PENDING.equals(testStatus)) {
					done = false;
					break;
				}
			}

			//terminate test if complete
			if(done)
				terminateSensorTest();
		}
	}

	public void runSensorTest()
	{
		setRunningSensorTestFlag(true);
		sensorTestStatus = PENDING;

		// initialize the status map
		for (Sensor sensor : sensors) {
			sensorTestStatusMap.put(sensor.getId(), sensorTestStatus);
		}
	}

	private void setRunningSensorTestFlag(boolean flag) {
		runningSensorTest = flag;
	}

	// used during sensor test
	public void terminateSensorTest()
	{
		setRunningSensorTestFlag(false);

		// look at individual sensor status to determine the overall test status
		sensorTestStatus = PASS;
		for (Object o : sensorTestStatusMap.values()) {
			String status = (String) o;
			if (status.equals(PENDING)) {
				sensorTestStatus = FAIL;
				break;
			}
		}
	}

	// used during sensor test
	public SensorTestStatus getSesnsorTestStatus()
	{
		return sensorTestStatus;
	}

	// used during sensor test
	public Map getSensorTestStatusMap()
	{
		return sensorTestStatusMap;
	}
}
