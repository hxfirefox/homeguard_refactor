package afterrefactor.sensor;

import org.junit.Test;

import static afterrefactor.sensor.SensorType.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by »ÆÏè on 15-8-22.
 */
public class SensorTest {

    private Sensor sensor;

    @Test
    public void should_sensor_msg_end_with_is_closed_when_sensor_no_tripped_and_type_door() throws Exception {
        // given
        sensor = new DoorSensor("1", "here", DOOR);
        // when
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" is closed"), is(true));
    }

    @Test
    public void should_sensor_msg_end_with_is_sealed_when_sensor_no_tripped_and_type_window() throws Exception {
        // given
        sensor = new WindowSensor("1", "here", WINDOW);
        // when
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" is sealed"), is(true));
    }

    @Test
    public void should_sensor_msg_end_with_temperature_is_normal_when_sensor_no_tripped_and_type_fire() throws Exception {
        // given
        sensor = new FireSensor("1", "here", FIRE);
        // when
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" temperature is normal"), is(true));
    }

    @Test
    public void should_sensor_msg_end_with_is_motionless_when_sensor_no_tripped_and_type_motion() throws Exception {
        // given
        sensor = new MotionSensor("1", "here", MOTION);
        // when
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" is motionless"), is(true));
    }

    @Test
    public void should_sensor_msg_end_with_is_open_when_sensor_tripped_and_type_door() throws Exception {
        // given
        sensor = new DoorSensor("1", "here", DOOR);
        // when
        sensor.trip();
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" is open"), is(true));
    }

    @Test
    public void should_sensor_msg_end_with_is_ajar_when_sensor_tripped_and_type_window() throws Exception {
        // given
        sensor = new WindowSensor("1","here",WINDOW);
        // when
        sensor.trip();
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" is ajar"), is(true));
    }

    @Test
    public void should_sensor_msg_start_with_motion_detected_in_when_sensor_tripped_and_type_motion() throws Exception {
        // given
        sensor = new MotionSensor("1", "here", MOTION);
        // when
        sensor.trip();
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.startsWith("Motion detected in "), is(true));
    }

    @Test
    public void should_sensor_msg_end_with_is_on_fire_when_sensor_tripped_and_type_fire() throws Exception {
        // given
        sensor = new FireSensor("1", "here", FIRE);
        // when
        sensor.trip();
        final String sensorMessage = sensor.getSensorMessage();
        // then
        assertThat(sensorMessage.endsWith(" is on FIRE!"), is(true));
    }
}