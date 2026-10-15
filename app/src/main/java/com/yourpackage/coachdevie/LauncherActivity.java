/*package com.yourpackage.coachdevie;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.CoachRepository;

public class LauncherActivity extends AppCompatActivity {

    CoachRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new CoachRepository(this);

        repository.getAllGoals(goals -> runOnUiThread(() -> {
            if (goals == null || goals.isEmpty()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, HomeActivity.class));
            }
            finish();
        }));
    }
}*/


package com.yourpackage.coachdevie;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.AppDatabase;
import com.yourpackage.coachdevie.data.CoachDao;
import com.yourpackage.coachdevie.data.GoalEntity;
import com.yourpackage.coachdevie.data.HabitEntity;
import com.yourpackage.coachdevie.data.DailyLogEntity;
import com.yourpackage.coachdevie.data.HabitLogEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class LauncherActivity extends AppCompatActivity {
//Utilité : premier écran lancé par l’application.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            CoachDao dao = db.coachDao();

            int totalLogs = dao.getTotalLogsCount();

            if (totalLogs == 0) {
                preloadDemoData(dao);
            }

            List<GoalEntity> goals = dao.getAllGoals();

            runOnUiThread(() -> {
                if (goals == null || goals.isEmpty()) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                finish();
            });
        });
    }

    private void preloadDemoData(CoachDao dao) {
        dao.deleteAllGoals();
        dao.deleteAllHabits();

        dao.insertGoal(new GoalEntity("Se sentir plus calme", "Santé mentale", false));
        dao.insertGoal(new GoalEntity("Dormir plus tôt", "Bien-être", false));
        dao.insertGoal(new GoalEntity("Être plus constant", "Discipline", false));

        dao.insertHabit(new HabitEntity("Boire de l’eau", true, "2026-04-01"));
        dao.insertHabit(new HabitEntity("Dormir avant 23 h", true, "2026-04-01"));
        dao.insertHabit(new HabitEntity("Faire du sport", true, "2026-04-01"));
        dao.insertHabit(new HabitEntity("Étudier 1 heure", true, "2026-04-01"));

        List<HabitEntity> habits = dao.getActiveHabits();

        insertDay(dao, habits, "2026-04-06", 2, 2, 4,
                "Je me suis réveillé fatigué et j’ai eu du mal à me concentrer.",
                "J’ai malgré tout avancé sur une tâche importante",
                "Manque de sommeil et pensées envahissantes",
                "Ma famille",
                "Éteindre le téléphone plus tôt",
                new boolean[]{true, false, false, true});

        insertDay(dao, habits, "2026-04-07", 3, 3, 3,
                "Journée un peu plus stable, même si je restais dispersé.",
                "Je suis sorti marcher 20 minutes",
                "Difficulté à rester motivé",
                "Le calme du matin",
                "Commencer la journée par une seule priorité",
                new boolean[]{true, true, false, true});

        insertDay(dao, habits, "2026-04-08", 4, 4, 2,
                "Je me suis senti plus aligné et plus productif.",
                "J’ai terminé une tâche qui traînait depuis longtemps",
                "Légère procrastination en fin de journée",
                "Mes progrès",
                "Garder ce rythme demain",
                new boolean[]{true, true, true, true});

        insertDay(dao, habits, "2026-04-09", 3, 2, 4,
                "Le stress est remonté à cause de trop de choses à gérer.",
                "Je n’ai pas abandonné",
                "Surcharge mentale",
                "Un ami à l’écoute",
                "Faire une chose à la fois",
                new boolean[]{true, false, true, false});

        insertDay(dao, habits, "2026-04-10", 5, 4, 2,
                "Journée très positive, je me suis senti confiant.",
                "J’ai fait du sport et bien travaillé",
                "Un peu de fatigue le soir",
                "Mon corps",
                "Répéter cette routine",
                new boolean[]{true, true, true, true});

        insertDay(dao, habits, "2026-04-11", 4, 3, 2,
                "Journée apaisée et globalement équilibrée.",
                "J’ai passé une soirée tranquille",
                "Le téléphone m’a un peu dispersé",
                "Le repos",
                "Réduire le temps d’écran",
                new boolean[]{true, true, false, true});

        insertDay(dao, habits, "2026-04-12", 3, 3, 3,
                "Journée correcte mais avec pas mal de réflexion intérieure.",
                "Je suis resté présent malgré mes pensées",
                "Tendance à trop réfléchir",
                "Une petite avancée",
                "Commencer demain avec clarté",
                new boolean[]{true, false, true, true});

        insertDay(dao, habits, "2026-04-13", 2, 2, 4,
                "Je me suis senti vidé et lent presque toute la journée.",
                "J’ai quand même tenu une habitude",
                "Fatigue + stress",
                "Le soutien autour de moi",
                "Prioriser le sommeil",
                new boolean[]{true, false, false, true});

        insertDay(dao, habits, "2026-04-14", 3, 3, 3,
                "Journée un peu meilleure, sans être exceptionnelle.",
                "Je suis sorti prendre l’air",
                "Faible motivation en début d’après-midi",
                "Un moment calme",
                "Faire une tâche importante tôt",
                new boolean[]{true, true, false, true});

        insertDay(dao, habits, "2026-04-15", 4, 4, 2,
                "Bonne énergie et meilleur focus que les jours précédents.",
                "J’ai avancé sérieusement sur mon travail",
                "Envie de repousser certaines tâches secondaires",
                "Mes progrès récents",
                "Continuer avec constance",
                new boolean[]{true, true, true, true});

        insertDay(dao, habits, "2026-04-16", 3, 2, 4,
                "Le stress a pris plus de place aujourd’hui.",
                "Je suis resté fonctionnel malgré tout",
                "Trop de choses en tête",
                "Un échange rassurant",
                "Me simplifier la journée de demain",
                new boolean[]{true, false, true, false});

        insertDay(dao, habits, "2026-04-17", 5, 4, 2,
                "Très bonne journée, je me suis senti solide et motivé.",
                "Sport + discipline + bonne concentration",
                "Petite fatigue physique",
                "Ma santé et mon énergie",
                "Reproduire ce cadre demain",
                new boolean[]{true, true, true, true});

        insertDay(dao, habits, "2026-04-18", 4, 3, 2,
                "Journée équilibrée avec un bon niveau de sérénité.",
                "J’ai gardé une bonne présence mentale",
                "Un peu trop de téléphone",
                "Le calme du soir",
                "Mieux protéger mon attention",
                new boolean[]{true, true, false, true});

        insertDay(dao, habits, "2026-04-19", 3, 3, 3,
                "Journée moyenne, avec des émotions mélangées.",
                "Je suis resté stable",
                "Je me suis un peu perdu dans mes pensées",
                "Une petite victoire simple",
                "Démarrer demain avec intention",
                new boolean[]{true, false, true, true});
    }

    private void insertDay(CoachDao dao,
                           List<HabitEntity> habits,
                           String date,
                           int mood,
                           int energy,
                           int stress,
                           String notes,
                           String win,
                           String challenge,
                           String gratitude,
                           String tomorrowFocus,
                           boolean[] completed) {

        dao.insertDailyLog(new DailyLogEntity(
                date,
                mood,
                energy,
                stress,
                notes,
                win,
                challenge,
                gratitude,
                tomorrowFocus
        ));

        for (int i = 0; i < habits.size() && i < completed.length; i++) {
            dao.insertHabitLog(new HabitLogEntity(
                    habits.get(i).id,
                    date,
                    completed[i]
            ));
        }
    }
}