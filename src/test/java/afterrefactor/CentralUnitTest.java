package afterrefactor;

import afterrefactor.sensor.Sensor;
import org.junit.Before;
import org.junit.Test;

import static afterrefactor.SensorTestStatus.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by »ÆÏè on 15-9-3.
 */
public class CentralUnitTest {
    private CentralUnit centralUnit;
    private MockHomeGuardView mockHomeGuardView = new MockHomeGuardView();
    private MockAudibleAlarm mockAudibleAlarm = new MockAudibleAlarm();
    private final Sensor sensor1 = mock(Sensor.class);
    private final Sensor sensor2 = mock(Sensor.class);
    private final Sensor sensor3 = mock(Sensor.class);

    @Before
    public void setUp() throws Exception {
        centralUnit = new CentralUnit(mockHomeGuardView, mockAudibleAlarm);

        centralUnit.registerSensor(sensor1);
        centralUnit.registerSensor(sensor2);
        centralUnit.registerSensor(sensor3);
    }

    @Test
    public void should_all_sensor_test_status_pending_after_run_sensor_test() throws Exception {
        // given
        // when
        when(sensor1.getId()).thenReturn("1");
        when(sensor2.getId()).thenReturn("2");
        when(sensor3.getId()).thenReturn("3");
        centralUnit.runSensorTest();
        // then
        assertThat(centralUnit.isSensorTestRunning(), is(true));
        assertThat(centralUnit.getSesnsorTestStatus().equals(PENDING), is(true));
        assertThat(centralUnit.getSensorTestStatusMap().get("1").equals(PENDING), is(true));
        assertThat(centralUnit.getSensorTestStatusMap().get("2").equals(PENDING), is(true));
        assertThat(centralUnit.getSensorTestStatusMap().get("3").equals(PENDING), is(true));
    }

    @Test
    public void should_running_sensor_test_flag_false_when_terminator_sensor_test() throws Exception {
        // given

        // when
        when(sensor1.getId()).thenReturn("1");
        when(sensor2.getId()).thenReturn("2");
        when(sensor3.getId()).thenReturn("3");
        centralUnit.runSensorTest();
        centralUnit.terminateSensorTest();
        // then
        assertThat(centralUnit.isSensorTestRunning(), is(false));
    }

    @Test
    public void should_sensor_test_status_fail_when_terminator_sensor_test_but_sensor_test_status_pending() throws Exception {
        // given
        // when
        when(sensor1.getId()).thenReturn("1");
        when(sensor2.getId()).thenReturn("2");
        when(sensor3.getId()).thenReturn("3");
        centralUnit.runSensorTest();
        centralUnit.terminateSensorTest();
        // then
        assertThat(centralUnit.getSesnsorTestStatus(), is(FAIL));
    }

    @Test
    public void should_sensor_test_status_pass_when_terminator_sensor_test_but_sensor_test_status_not_pending() throws Exception {
        // given
        // when
        when(sensor1.getId()).thenReturn("1");
        when(sensor2.getId()).thenReturn("2");
        when(sensor3.getId()).thenReturn("3");
        centralUnit.runSensorTest();
        centralUnit.getSensorTestStatusMap().put("1", PASS);
        centralUnit.getSensorTestStatusMap().put("2", PASS);
        centralUnit.getSensorTestStatusMap().put("3", PASS);
        centralUnit.terminateSensorTest();
        // then
        assertThat(centralUnit.getSesnsorTestStatus(), is(PASS));
    }

    @Test
    public void should_view_do_nothing_when_sensor_test_not_running() throws Exception {
        // given
        // when
        centralUnit.parseRadioBroadcast("1,TRIPPED");
        // then
        assertThat(mockHomeGuardView.getHasShowMessage(), is(false));
    }

    @Test
    public void should_view_show_message_when_sensor_test_running() throws Exception {
        // given
        // when
        when(sensor1.getId()).thenReturn("1");
        centralUnit.runSensorTest();
        centralUnit.parseRadioBroadcast("1,TRIPPED");
        // then
        assertThat(mockHomeGuardView.getHasShowMessage(), is(true));
    }

    @Test
    public void should_alarm_on_when_enable_alarm() throws Exception {
        // given
        // when
        when(sensor1.getId()).thenReturn("1");
        centralUnit.runSensorTest();
        centralUnit.getAlarmManager().setArmed(true);
        centralUnit.parseRadioBroadcast("1,TRIPPED");
        // then
        assertThat(mockAudibleAlarm.getIsOn(), is(true));
    }

    @Test
    public void should_alarm_off_when_disable_alarm() throws Exception {
        // given
        // when
        when(sensor1.getId()).thenReturn("1");
        centralUnit.runSensorTest();
        centralUnit.parseRadioBroadcast("1,TRIPPED");
        // then
        assertThat(mockAudibleAlarm.getIsOn(), is(false));
    }

    @Test
    public void should_turn_off_alarm_when_enter_valid_security_code() throws Exception {
        // given
        // when
        mockAudibleAlarm.sound();
        centralUnit.setSecurityCode("123");
        centralUnit.turnOffAlarm("123");
        // then
        assertThat(mockAudibleAlarm.getIsOn(), is(false));
    }

    @Test
    public void should_alarm_still_on_when_enter_invalid_security_code() throws Exception {
        // given
        // when
        mockAudibleAlarm.sound();
        centralUnit.setSecurityCode("123");
        centralUnit.turnOffAlarm("321");
        // then
        assertThat(mockAudibleAlarm.getIsOn(), is(true));
    }
}