package com.yourpackage.coachdevie.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class HabitEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public boolean active;
    public String createdAt;

    public HabitEntity(String name, boolean active, String createdAt) {
        this.name = name;
        this.active = active;
        this.createdAt = createdAt;
    }
}