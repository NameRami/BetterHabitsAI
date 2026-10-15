package com.yourpackage.coachdevie.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                GoalEntity.class,
                HabitEntity.class,
                DailyLogEntity.class,
                HabitLogEntity.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
//Utilité : définit la base de données Room.
    public abstract CoachDao coachDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "coach_de_vie_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}