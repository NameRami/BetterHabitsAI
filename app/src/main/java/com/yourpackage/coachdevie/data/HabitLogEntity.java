package com.yourpackage.coachdevie.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habit_logs")
public class HabitLogEntity {
//Utilité : table qui enregistre si une habitude a été faite un jour donné.
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int habitId;
    public String date;
    public boolean completed;

    public HabitLogEntity(int habitId, String date, boolean completed) {
        this.habitId = habitId;
        this.date = date;
        this.completed = completed;
    }
}