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

    @Before
    public void setUp() throws Exception {
        centralUnit = new CentralUnit();
    }

    @Test
    public void should_all_sensor_test_status_pending_after_run_sensor_test() throws Exception {
        // given
        final Sensor sensor1 = mock(Sensor.class);
        final Sensor sensor2 = mock(Sensor.class);
        final Sensor sensor3 = mock(Sensor.class);
        centralUnit.registerSensor(sensor1);
        centralUnit.registerSensor(sensor2);
        centralUnit.registerSensor(sensor3);
        // when
        when(sensor1.getId()).thenReturn("1");
        when(sensor2.getId()).thenReturn("2");
        when(sensor3.getId()).thenReturn("3");
        centralUnit.runSensorTest();
        // then
        assertThat(centralUnit.getSesnsorTestStatus().equals(PENDING), is(true));
        assertThat(centralUnit.getSensorTestStatusMap().get("1").equals(PENDING), is(true));
        assertThat(centralUnit.getSensorTestStatusMap().get("2").equals(PENDING), is(true));
        assertThat(centralUnit.getSensorTestStatusMap().get("3").equals(PENDING), is(true));
    }
}