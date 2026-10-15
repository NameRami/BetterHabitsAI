package com.yourpackage.coachdevie.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class GoalEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String text;
    public String category;
    public boolean completed;

    public GoalEntity(String text, String category, boolean completed) {
        this.text = text;
        this.category = category;
        this.completed = completed;
    }
}