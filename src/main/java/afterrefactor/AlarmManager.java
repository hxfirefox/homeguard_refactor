package afterrefactor;

import afterrefactor.alarm.AudibleAlarm;

/**
 * Created by »ÆÏè on 15-9-4.
 */
public class AlarmManager {
    private boolean armed;
    private AudibleAlarm audibleAlarm;

    public AlarmManager(AudibleAlarm audibleAlarm) {
        this.audibleAlarm = audibleAlarm;
    }

    private boolean isArmed() {
        return armed;
    }

    public void enableAlarm() {
        if (isArmed()) {
            audibleAlarm.sound();
        }
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
    }

    public void disableAlarm() {
        setArmed(false);
        audibleAlarm.silence();
    }
}
