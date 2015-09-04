package afterrefactor;

import afterrefactor.alarm.AudibleAlarm;

public class MockAudibleAlarm implements AudibleAlarm
{
	private boolean isOn;

	public boolean getIsOn() {
		return isOn;
	}

	public void sound()
	{
		isOn = true;
	}

	public void silence()
	{
		isOn = false;
	}
}
