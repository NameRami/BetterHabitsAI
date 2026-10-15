package com.yourpackage.coachdevie.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checkins")
public class CheckInEntity {
//Utilité : ancien modèle possible pour un check-in simple.
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public int mood;
    public String completedHabits;
    public String win;
    public String challenge;

    public CheckInEntity(String date, int mood, String completedHabits, String win, String challenge) {
        this.date = date;
        this.mood = mood;
        this.completedHabits = completedHabits;
        this.win = win;
        this.challenge = challenge;
    }
}
