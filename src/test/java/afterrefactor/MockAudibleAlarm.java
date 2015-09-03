package afterrefactor;

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
