package com.yourpackage.coachdevie;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.CoachRepository;
import com.yourpackage.coachdevie.data.DailyLogEntity;
import com.yourpackage.coachdevie.data.HabitEntity;
import com.yourpackage.coachdevie.data.HabitLogEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckInActivity extends AppCompatActivity {
    //Utilité : écran du journal quotidien.
    private RadioGroup rgMood, rgEnergy, rgStress;
    private EditText etNotes, etWin, etChallenge, etGratitude, etTomorrowFocus;
    private Button btnGenerate;
    private LinearLayout habitsContainer;

    private CoachRepository repository;
    private String today;

    private List<HabitEntity> activeHabits = new ArrayList<>();
    private final List<CheckBox> habitCheckBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        repository = new CoachRepository(this);
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        rgMood = findViewById(R.id.rgMood);
        rgEnergy = findViewById(R.id.rgEnergy);
        rgStress = findViewById(R.id.rgStress);

        etNotes = findViewById(R.id.etNotes);
        etWin = findViewById(R.id.etWin);
        etChallenge = findViewById(R.id.etChallenge);
        etGratitude = findViewById(R.id.etGratitude);
        etTomorrowFocus = findViewById(R.id.etTomorrowFocus);

        btnGenerate = findViewById(R.id.btnGenerate);
        habitsContainer = findViewById(R.id.habitsContainer);

        loadHabits();

        btnGenerate.setOnClickListener(v -> checkAndSave());
    }

    private void loadHabits() {
        repository.getActiveHabits(habits -> runOnUiThread(() -> {
            activeHabits = habits != null ? habits : new ArrayList<>();
            habitsContainer.removeAllViews();
            habitCheckBoxes.clear();

            for (HabitEntity habit : activeHabits) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(habit.name);
                checkBox.setTextColor(Color.parseColor("#F0EDE8"));
                checkBox.setTextSize(15f);

                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                };
                int[] colors = new int[]{
                        Color.parseColor("#F5C842"),
                        Color.parseColor("#2C3050")
                };
                checkBox.setButtonTintList(new ColorStateList(states, colors));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 24);
                checkBox.setLayoutParams(params);

                habitsContainer.addView(checkBox);
                habitCheckBoxes.add(checkBox);
            }
        }));
    }

    private void checkAndSave() {
        repository.getDailyLogByDate(today, existing -> runOnUiThread(() -> {
            if (existing != null) {
                Toast.makeText(this, "Journal déjà enregistré aujourd’hui 👍", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            saveDailyLog();
        }));
    }

    private void saveDailyLog() {
        int mood = getSelectedLevel(rgMood);
        int energy = getSelectedLevel(rgEnergy);
        int stress = getSelectedLevel(rgStress);

        String notes = getTextOrDefault(etNotes, "");
        String win = getTextOrDefault(etWin, "Aucune victoire précisée");
        String challenge = getTextOrDefault(etChallenge, "Aucun défi précisé");
        String gratitude = getTextOrDefault(etGratitude, "Rien de précisé");
        String tomorrowFocus = getTextOrDefault(etTomorrowFocus, "Aucune priorité précisée");

        DailyLogEntity dailyLog = new DailyLogEntity(
                today,
                mood,
                energy,
                stress,
                notes,
                win,
                challenge,
                gratitude,
                tomorrowFocus
        );

        List<HabitLogEntity> habitLogs = new ArrayList<>();
        for (int i = 0; i < activeHabits.size(); i++) {
            HabitEntity habit = activeHabits.get(i);
            boolean completed = i < habitCheckBoxes.size() && habitCheckBoxes.get(i).isChecked();
            habitLogs.add(new HabitLogEntity(habit.id, today, completed));
        }

        repository.saveDailyLogWithHabits(dailyLog, habitLogs);

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("date", today);
        startActivity(intent);
        finish();
    }

    private int getSelectedLevel(RadioGroup group) {
        int checkedId = group.getCheckedRadioButtonId();
        if (checkedId == -1) {
            return 3;
        }

        int index = group.indexOfChild(findViewById(checkedId));
        return index >= 0 ? index + 1 : 3;
    }

    private String getTextOrDefault(EditText editText, String defaultValue) {
        String value = editText.getText().toString().trim();
        return value.isEmpty() ? defaultValue : value;
    }
}