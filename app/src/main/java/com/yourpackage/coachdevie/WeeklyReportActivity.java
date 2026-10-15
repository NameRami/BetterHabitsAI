package com.yourpackage.coachdevie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.CoachRepository;
import com.yourpackage.coachdevie.data.DailyLogEntity;
import com.yourpackage.coachdevie.network.ApiClient;

import java.util.List;

public class WeeklyReportActivity extends AppCompatActivity {

    TextView tvReport;
    Button btnBackHome;
    CoachRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Utilité : génère un bilan hebdomadaire IA.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_report);

        tvReport = findViewById(R.id.tvReport);
        btnBackHome = findViewById(R.id.btnBackHome);

        repository = new CoachRepository(this);

        btnBackHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        generateWeeklyReport();
    }

    private void generateWeeklyReport() {
        repository.getLast7DailyLogs(weekLogs -> {
            new Thread(() -> {
                try {
                    if (weekLogs == null || weekLogs.isEmpty()) {
                        runOnUiThread(() -> tvReport.setText("Pas encore de données hebdomadaires."));
                        return;
                    }

                    StringBuilder data = new StringBuilder();

                    for (DailyLogEntity c : weekLogs) {
                        data.append("Date : ").append(c.date)
                                .append("\nMood: ").append(c.mood).append("/5")
                                .append("\nEnergy: ").append(c.energy).append("/5")
                                .append("\nStress: ").append(c.stress).append("/5")
                                .append("\nWin: ").append(c.win)
                                .append("\nChallenge: ").append(c.challenge)
                                .append("\nGratitude: ").append(c.gratitude)
                                .append("\nTomorrow focus: ").append(c.tomorrowFocus)
                                .append("\nNotes: ").append(c.notes)
                                .append("\n----------------------\n");
                    }

                    String prompt =
                            "Tu es un coach de vie hebdomadaire émotionnellement intelligent.\n\n" +
                                    "Analyse la semaine de l’utilisateur à partir de ces données du journal :\n\n" +
                                    data + "\n" +
                                    "Écris la réponse dans un style BEAU, CHALEUREUX et CLAIR.\n" +
                                    "Utilise des emojis naturellement.\n" +
                                    "N’utilise PAS de titres markdown comme ### ni de titres numérotés bruts comme '### 1.'\n" +
                                    "Ne donne PAS un aspect technique.\n" +
                                    "Fais en sorte que ce soit chaleureux, moderne et facile à lire.\n\n" +
                                    "Utilise exactement ce format :\n\n" +
                                    "🌈 Ambiance de la semaine :\n" +
                                    "Écris 2 à 3 phrases claires sur la tendance émotionnelle de la semaine.\n\n" +
                                    "📌 Ce qui a marqué :\n" +
                                    "Écris 2 à 3 puces courtes sur les schémas répétés.\n\n" +
                                    "⚠️ Défi principal :\n" +
                                    "Écris 2 à 3 phrases sur la plus grande difficulté observée.\n\n" +
                                    "💡 Conseil bienveillant pour la semaine prochaine :\n" +
                                    "Donne 3 suggestions pratiques et courtes.\n\n" +
                                    "✨ Encouragement :\n" +
                                    "Termine par un paragraphe motivant et chaleureux.\n\n" +
                                    "Garde une réponse élégante, claire et inspirante.";

                    String result = ApiClient.generateCoaching(prompt);

                    runOnUiThread(() -> tvReport.setText(result));

                } catch (Exception e) {
                    runOnUiThread(() -> tvReport.setText("Erreur : " + e.getMessage()));
                }
            }).start();
        });
    }
}