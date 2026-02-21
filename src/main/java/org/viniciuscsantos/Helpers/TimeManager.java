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

    public String getElapsedTime(String timerName) {
        long[] times = timers.get(timerName);
        if(times == null) {
            return "00:00:00.000";
        }

        long end = (times[1] == 0L) ? System.nanoTime() : times[1];
        return this.formatNanos(end - times[0]);
    }

    public long getElapsedNanos(String timerName) {
        long[] times = timers.get(timerName);
        if(times == null) {
            return 0;
        }

        long end = (times[1] == 0L) ? System.nanoTime() : times[1];
        return end - times[0];
    }

    public void printElapsedTime(String timerName) {
        IO.println(timerName + ": " + getElapsedTime(timerName));
    }

    public void printElapsedNanos(String timerName) {
        IO.println(timerName + ": " + getElapsedNanos(timerName));
    }

    private String formatNanos(long nanos) {
        long hours = TimeUnit.NANOSECONDS.toHours(nanos);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos) % 60;
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos) % 60;
        long milis = TimeUnit.NANOSECONDS.toMillis(nanos) % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milis);
    }
}

