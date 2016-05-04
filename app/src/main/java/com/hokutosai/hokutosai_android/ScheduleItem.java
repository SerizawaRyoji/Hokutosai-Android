package com.hokutosai.hokutosai_android;

import java.util.List;

/**
 * Created by ryoji on 2016/05/04.
 */
public class ScheduleItem {

    int schedule_id;
    String date;
    String day;
    List<Event> timetable;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public List<Event> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<Event> timetable) {
        this.timetable = timetable;
    }
}
