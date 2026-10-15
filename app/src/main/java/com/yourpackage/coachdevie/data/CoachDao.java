package com.yourpackage.coachdevie.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoachDao {

    //Utilité : contient toutes les requêtes SQL Room.

    // Goals
    @Insert
    void insertGoal(GoalEntity goal);

    @Query("SELECT * FROM goals ORDER BY id DESC")
    List<GoalEntity> getAllGoals();

    @Query("DELETE FROM goals")
    void deleteAllGoals();

    // Habits
    @Insert
    void insertHabit(HabitEntity habit);

    @Query("SELECT * FROM habits WHERE active = 1 ORDER BY id DESC")
    List<HabitEntity> getActiveHabits();

    @Query("SELECT * FROM habits ORDER BY id DESC")
    List<HabitEntity> getAllHabits();

    @Query("DELETE FROM habits")
    void deleteAllHabits();

    // Daily Logs
    @Insert
    void insertDailyLog(DailyLogEntity dailyLog);

    @Query("SELECT * FROM daily_logs WHERE date = :date LIMIT 1")
    DailyLogEntity getDailyLogByDate(String date);

    @Query("SELECT * FROM daily_logs ORDER BY date DESC LIMIT 1")
    DailyLogEntity getLatestDailyLog();

    @Query("SELECT * FROM daily_logs ORDER BY date DESC LIMIT 7")
    List<DailyLogEntity> getLast7DailyLogs();

    @Query("SELECT * FROM daily_logs ORDER BY date DESC")
    List<DailyLogEntity> getAllDailyLogs();

    @Query("SELECT AVG(mood) FROM daily_logs")
    Float getAverageMood();

    @Query("SELECT COUNT(*) FROM daily_logs")
    int getTotalLogsCount();

    // Habit Logs
    @Insert
    void insertHabitLog(HabitLogEntity habitLog);

    @Query("SELECT * FROM habit_logs WHERE date = :date")
    List<HabitLogEntity> getHabitLogsByDate(String date);

    @Query("SELECT COUNT(*) FROM habit_logs WHERE date = :date AND completed = 1")
    int getCompletedHabitsCountByDate(String date);

    @Query("DELETE FROM habit_logs WHERE date = :date")
    void deleteHabitLogsByDate(String date);

    @Query("SELECT COUNT(*) FROM habit_logs WHERE completed = 1")
    int getTotalCompletedHabits();
}