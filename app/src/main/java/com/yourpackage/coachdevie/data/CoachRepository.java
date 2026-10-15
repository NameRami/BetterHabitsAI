package com.yourpackage.coachdevie.data;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoachRepository {
    //Utilité : couche intermédiaire entre les écrans et la base.

    private final CoachDao coachDao;
    private final ExecutorService executorService;

    public interface DataCallback<T> {
        void onResult(T result);
    }

    public CoachRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        coachDao = db.coachDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void saveGoalsAndHabits(List<GoalEntity> goals, List<HabitEntity> habits) {
        executorService.execute(() -> {
            coachDao.deleteAllGoals();
            coachDao.deleteAllHabits();

            for (GoalEntity goal : goals) {
                coachDao.insertGoal(goal);
            }

            for (HabitEntity habit : habits) {
                coachDao.insertHabit(habit);
            }
        });
    }

    public void getActiveHabits(DataCallback<List<HabitEntity>> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getActiveHabits()));
    }

    public void saveDailyLogWithHabits(DailyLogEntity dailyLog, List<HabitLogEntity> habitLogs) {
        executorService.execute(() -> {
            coachDao.insertDailyLog(dailyLog);
            coachDao.deleteHabitLogsByDate(dailyLog.date);

            for (HabitLogEntity habitLog : habitLogs) {
                coachDao.insertHabitLog(habitLog);
            }
        });
    }

    public void getDailyLogByDate(String date, DataCallback<DailyLogEntity> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getDailyLogByDate(date)));
    }

    public void getLatestDailyLog(DataCallback<DailyLogEntity> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getLatestDailyLog()));
    }

    public void getLast7DailyLogs(DataCallback<List<DailyLogEntity>> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getLast7DailyLogs()));
    }

    public void getAllGoals(DataCallback<List<GoalEntity>> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getAllGoals()));
    }

    public void getAverageMood(DataCallback<Float> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getAverageMood()));
    }

    public void getCompletedHabitsCountByDate(String date, DataCallback<Integer> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getCompletedHabitsCountByDate(date)));
    }

    public void getTotalLogsCount(DataCallback<Integer> callback) {
        executorService.execute(() -> callback.onResult(coachDao.getTotalLogsCount()));
    }
}