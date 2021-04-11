package com.kantar.sessionsjob;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionsJobRecord {
    private final int homeNo;
    private final int channel;
    private final LocalDateTime startTime;
    //private LocalDateTime endTime;
    private final String activity;
    private LocalDateTime endTime;
    private long duration;


    public SessionsJobRecord(int homeNo, int channel, LocalDateTime startTime, String activity) {
        this.homeNo = homeNo;
        this.channel = channel;
        this.startTime = startTime;
        this.activity = activity;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getFormattedStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return formatter.format(startTime);
    }


    public int getHomeNo() {
        return homeNo;
    }

    public int getChannel() {
        return channel;
    }

    public String getActivity() {
        return activity;
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        setDuration();
    }

    private void setDuration() {
        long startTimeDuration = getSecondDuration(startTime);
        long endTimeDuration = getSecondDuration(endTime);
        duration = endTimeDuration - startTimeDuration + 1;
    }

    private long getSecondDuration(LocalDateTime t) {
        long h = t.getHour();
        long m = t.getMinute();
        long s = t.getSecond();
        return (h * 3600) + (m * 60) + s;
    }

    public long getDuration() {
        return duration;
    }

    public String getFormattedEndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return formatter.format(endTime);
    }

    @Override
    public String toString() {
        return "SessionsJobRecord{" +
                "homeNo=" + homeNo +
                ", channel=" + channel +
                ", startTime=" + getFormattedStartTime() +
                ", activity='" + activity + '\'' +
                ", endTime=" + getFormattedEndTime() +
                ", duration=" + duration +
                '}';
    }
}
