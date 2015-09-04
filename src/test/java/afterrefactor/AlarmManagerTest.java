package afterrefactor;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by »ÆÏè on 15-9-4.
 */
public class AlarmManagerTest {
    private AlarmManager alarmManager;
    final MockAudibleAlarm audibleAlarm = new MockAudibleAlarm();

    @Before
    public void setUp() throws Exception {
        alarmManager = new AlarmManager(audibleAlarm);
    }

    @Test
    public void should_disable_alarm() throws Exception {
        // given
        // when
        alarmManager.disableAlarm();
        // then
        assertThat(audibleAlarm.getIsOn(), is(false));
    }

    @Test
    public void should_enable_alarm_when_armed() throws Exception {
        // given
        // when
        alarmManager.setArmed(true);
        alarmManager.enableAlarm();
        // then
        assertThat(audibleAlarm.getIsOn(), is(true));
    }

    @Test
    public void should_not_enable_alarm_when_not_armed() throws Exception {
        // given
        // when
        alarmManager.enableAlarm();
        // then
        assertThat(audibleAlarm.getIsOn(), is(false));
    }
}