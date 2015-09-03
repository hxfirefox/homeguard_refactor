package afterrefactor;

public class MockAudibleAlarm implements AudibleAlarm
{
	public boolean isOn = false;

	public void sound()
	{
		isOn = true;
	}

	public void silence()
	{
		isOn = false;
	}
}
