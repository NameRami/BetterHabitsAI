package com.yourpackage.coachdevie;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.CoachRepository;
import com.yourpackage.coachdevie.data.GoalEntity;
import com.yourpackage.coachdevie.data.HabitEntity;
import com.yourpackage.coachdevie.network.ApiClient;

public class ChatActivity extends AppCompatActivity {
    //Utilité : écran de chat avec le coach IA.
    EditText etUserMessage;
    LinearLayout btnSend;
    TextView tvChatResponse;

    TextView tvPromptAnxious;
    TextView tvPromptHabits;
    TextView tvPromptMotivate;

    CoachRepository repository;
    private String coachContext = "Aucun contexte personnel disponible pour le moment.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etUserMessage = findViewById(R.id.etUserMessage);
        btnSend = findViewById(R.id.btnSend);
        tvChatResponse = findViewById(R.id.tvChatResponse);

        tvPromptAnxious = findViewById(R.id.tvPromptAnxious);
        tvPromptHabits = findViewById(R.id.tvPromptHabits);
        tvPromptMotivate = findViewById(R.id.tvPromptMotivate);

        repository = new CoachRepository(this);

        tvChatResponse.setText("Salut 👋 J’ai chargé tes objectifs récents, tes habitudes et ton historique d’humeur. Demande-moi ce que tu veux.");

        loadCoachContext();

        btnSend.setOnClickListener(v -> sendMessage());

        attachPrompt(tvPromptAnxious);
        attachPrompt(tvPromptHabits);
        attachPrompt(tvPromptMotivate);
    }

    private void attachPrompt(TextView promptView) {
        promptView.setOnClickListener(v -> {
            String text = promptView.getText().toString();
            etUserMessage.setText(text);
            etUserMessage.setSelection(text.length());
            etUserMessage.requestFocus();
        });
    }

    private void loadCoachContext() {
        repository.getAllGoals(goals -> {
            repository.getActiveHabits(habits -> {
                repository.getLatestDailyLog(latestLog -> {
                    repository.getAverageMood(avgMood -> {
                        repository.getTotalLogsCount(totalLogs -> {

                            StringBuilder goalsText = new StringBuilder();
                            if (goals != null) {
                                for (GoalEntity goal : goals) {
                                    goalsText.append("- ").append(goal.text).append("\n");
                                }
                            }

                            StringBuilder habitsText = new StringBuilder();
                            if (habits != null) {
                                for (HabitEntity habit : habits) {
                                    habitsText.append("- ").append(habit.name).append("\n");
                                }
                            }

                            String latestLogText = "Aucune entrée récente.";
                            if (latestLog != null) {
                                latestLogText =
                                        "Humeur : " + latestLog.mood + "/5\n" +
                                                "Énergie : " + latestLog.energy + "/5\n" +
                                                "Stress : " + latestLog.stress + "/5\n" +
                                                "Victoire : " + latestLog.win + "\n" +
                                                "Défi : " + latestLog.challenge + "\n" +
                                                "Gratitude : " + latestLog.gratitude + "\n" +
                                                "Priorité de demain : " + latestLog.tomorrowFocus + "\n" +
                                                "Notes : " + latestLog.notes;
                            }

                            String averageMoodText = avgMood == null
                                    ? "Pas encore de données"
                                    : String.format("%.1f / 5", avgMood);

                            coachContext =
                                    "Profil utilisateur pour le coaching :\n\n" +
                                            "Objectifs :\n" + goalsText + "\n" +
                                            "Habitudes actives :\n" + habitsText + "\n" +
                                            "Dernière entrée du journal :\n" + latestLogText + "\n\n" +
                                            "Humeur moyenne : " + averageMoodText + "\n" +
                                            "Nombre total d’entrées du journal : " + totalLogs + "\n";
                        });
                    });
                });
            });
        });
    }

    private void sendMessage() {
        String userMessage = etUserMessage.getText().toString().trim();

        if (userMessage.isEmpty()) {
            tvChatResponse.setText("Écris quelque chose d’abord.");
            return;
        }

        tvChatResponse.setText("Réflexion en cours...");

        new Thread(() -> {
            try {
                String prompt =
                        "Tu es un coach de vie personnel chaleureux, émotionnellement intelligent et pratique.\n" +
                                "Utilise le profil utilisateur ci-dessous pour personnaliser ta réponse.\n" +
                                "Sois bienveillant, clair, élégant et concis.\n" +
                                "Utilise quelques emojis naturellement.\n" +
                                "Ne sois pas générique.\n\n" +
                                coachContext + "\n" +
                                "Message de l’utilisateur : " + userMessage + "\n\n" +
                                "Réponds comme un vrai coach personnel qui se souvient des objectifs, des habitudes et de l’état émotionnel récent de l’utilisateur.";

                String result = ApiClient.generateCoaching(prompt);

                runOnUiThread(() -> tvChatResponse.setText(result));

            } catch (Exception e) {
                runOnUiThread(() -> tvChatResponse.setText("Erreur : " + e.getMessage()));
            }
        }).start();
    }
}