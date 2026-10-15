package com.yourpackage.coachdevie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yourpackage.coachdevie.data.CoachRepository;
import com.yourpackage.coachdevie.data.GoalEntity;
import com.yourpackage.coachdevie.data.HabitEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
//Utilité : écran de configuration initiale.
    CheckBox cbGoalFit, cbGoalStudy, cbGoalSleep, cbGoalDiscipline;
    CheckBox cbHabitGym, cbHabitStudy, cbHabitWater, cbHabitSleep;
    EditText etCustomGoal, etCustomHabit;
    Button btnSaveAndNext;

    CoachRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new CoachRepository(this);

        cbGoalFit = findViewById(R.id.cbGoalFit);
        cbGoalStudy = findViewById(R.id.cbGoalStudy);
        cbGoalSleep = findViewById(R.id.cbGoalSleep);
        cbGoalDiscipline = findViewById(R.id.cbGoalDiscipline);

        cbHabitGym = findViewById(R.id.cbHabitGym);
        cbHabitStudy = findViewById(R.id.cbHabitStudy);
        cbHabitWater = findViewById(R.id.cbHabitWater);
        cbHabitSleep = findViewById(R.id.cbHabitSleep);

        etCustomGoal = findViewById(R.id.etCustomGoal);
        etCustomHabit = findViewById(R.id.etCustomHabit);
        btnSaveAndNext = findViewById(R.id.btnSaveAndNext);

        btnSaveAndNext.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        List<GoalEntity> goals = new ArrayList<>();
        List<HabitEntity> habits = new ArrayList<>();

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (cbGoalFit.isChecked()) goals.add(new GoalEntity("Se remettre en forme", "Santé", false));
        if (cbGoalStudy.isChecked()) goals.add(new GoalEntity("Mieux étudier", "Apprentissage", false));
        if (cbGoalSleep.isChecked()) goals.add(new GoalEntity("Dormir plus tôt", "Bien-être", false));
        if (cbGoalDiscipline.isChecked()) goals.add(new GoalEntity("Être plus discipliné", "Mentalité", false));

        String customGoal = etCustomGoal.getText().toString().trim();
        if (!customGoal.isEmpty()) {
            goals.add(new GoalEntity(customGoal, "Personnalisé", false));
        }

        if (cbHabitGym.isChecked()) habits.add(new HabitEntity("Salle de sport", true, today));
        if (cbHabitStudy.isChecked()) habits.add(new HabitEntity("Étudier 2 heures", true, today));
        if (cbHabitWater.isChecked()) habits.add(new HabitEntity("Boire de l’eau", true, today));
        if (cbHabitSleep.isChecked()) habits.add(new HabitEntity("Dormir avant 23 h", true, today));

        String customHabit = etCustomHabit.getText().toString().trim();
        if (!customHabit.isEmpty()) {
            habits.add(new HabitEntity(customHabit, true, today));
        }

        repository.saveGoalsAndHabits(goals, habits);

        Toast.makeText(this, "Enregistré avec succès ✅", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}