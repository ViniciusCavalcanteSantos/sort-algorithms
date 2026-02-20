package org.viniciuscsantos.Helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeManager {
    Map<String, long[]> timers = new HashMap<>();

    public void startTimer(String timerName) {
        long[] array = {System.nanoTime(), 0L};
        timers.put(timerName, array);
    }

    public void finishTimer(String timerName) {
        long[] times = timers.get(timerName);
        if(times != null) {
            times[1] = System.nanoTime();
        }
    }

    public String getTimePassed(String timerName) {
        long[] times = timers.get(timerName);
        if(times == null) {
            return "00:00:00.000";
        }

        return this.formatNanos(times[1] - times[0]);
    }

    private String formatNanos(long nanos) {
        long hours = TimeUnit.NANOSECONDS.toHours(nanos);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos) % 60;
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos) % 60;
        long milis = TimeUnit.NANOSECONDS.toMillis(nanos) % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milis);
    }
}

