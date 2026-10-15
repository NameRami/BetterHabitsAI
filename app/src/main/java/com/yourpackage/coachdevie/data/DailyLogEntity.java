package com.yourpackage.coachdevie.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_logs")
public class DailyLogEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public int mood;
    public int energy;
    public int stress;
    public String notes;
    public String win;
    public String challenge;
    public String gratitude;
    public String tomorrowFocus;

    public DailyLogEntity(String date,
                          int mood,
                          int energy,
                          int stress,
                          String notes,
                          String win,
                          String challenge,
                          String gratitude,
                          String tomorrowFocus) {
        this.date = date;
        this.mood = mood;
        this.energy = energy;
        this.stress = stress;
        this.notes = notes;
        this.win = win;
        this.challenge = challenge;
        this.gratitude = gratitude;
        this.tomorrowFocus = tomorrowFocus;
    }
}