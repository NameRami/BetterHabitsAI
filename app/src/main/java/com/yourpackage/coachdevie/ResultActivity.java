package com.yourpackage.coachdevie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.CoachRepository;
import com.yourpackage.coachdevie.data.DailyLogEntity;
import com.yourpackage.coachdevie.data.GoalEntity;
import com.yourpackage.coachdevie.network.ApiClient;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    TextView tvResult;
    Button btnBackHome;
    CoachRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvResult = findViewById(R.id.tvResult);
        btnBackHome = findViewById(R.id.btnBackHome);

        repository = new CoachRepository(this);

        btnBackHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        generateCoaching();
    }

    private void generateCoaching() {
        repository.getLatestDailyLog(today -> {
            if (today == null) {
                runOnUiThread(() -> tvResult.setText("Aucun journal trouvé."));
                return;
            }

            repository.getAverageMood(avgMood -> {
                repository.getAllGoals(goals -> {
                    new Thread(() -> {
                        try {
                            String prompt = buildPrompt(today, avgMood, goals);
                            String result = ApiClient.generateCoaching(prompt);
                            runOnUiThread(() -> tvResult.setText(result));
                        } catch (Exception e) {
                            runOnUiThread(() -> tvResult.setText("Erreur : " + e.getMessage()));
                        }
                    }).start();
                });
            });
        });
    }

    private String buildPrompt(DailyLogEntity today, Float avgMood, List<GoalEntity> goals) {
        StringBuilder goalsText = new StringBuilder();
        for (GoalEntity goal : goals) {
            goalsText.append("- ").append(goal.text).append("\n");
        }

        String averageMoodText = avgMood == null ? "Pas encore de moyenne hebdomadaire" : String.format("%.1f / 5", avgMood);

        return "Tu es un coach de vie bienveillant mais pratique.\n\n" +
                "Journal du jour :\n" +
                "Humeur : " + today.mood + "/5\n" +
                "Énergie : " + today.energy + "/5\n" +
                "Stress : " + today.stress + "/5\n" +
                "Victoire : " + today.win + "\n" +
                "Défi : " + today.challenge + "\n" +
                "Gratitude : " + today.gratitude + "\n" +
                "Priorité de demain : " + today.tomorrowFocus + "\n" +
                "Notes : " + today.notes + "\n\n" +
                "Objectifs principaux :\n" + goalsText + "\n" +
                "Humeur moyenne globale : " + averageMoodText + "\n\n" +
                "Donne :\n" +
                "1. une observation sur la journée\n" +
                "2. une action pratique pour aujourd’hui ou demain\n" +
                "3. trois courtes phrases motivantes";
    }
}