package com.yourpackage.coachdevie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.yourpackage.coachdevie.data.CoachRepository;
import com.yourpackage.coachdevie.data.DailyLogEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
//Utilité : écran d’accueil avec statistiques et graphique.
    TextView tvTotalLogs, tvAvgMood;
    LinearLayout btnDailyCheckIn, btnWeeklyReport, btnChatAi, btnSetup;
    LineChart lineChartMood;
    CoachRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        repository = new CoachRepository(this);

        tvTotalLogs = findViewById(R.id.tvTotalLogs);
        tvAvgMood = findViewById(R.id.tvAvgMood);

        btnDailyCheckIn = findViewById(R.id.btnDailyCheckIn);
        btnWeeklyReport = findViewById(R.id.btnWeeklyReport);
        btnChatAi = findViewById(R.id.btnChatAi);
        btnSetup = findViewById(R.id.btnSetup);

        lineChartMood = findViewById(R.id.lineChartMood);

        btnDailyCheckIn.setOnClickListener(v ->
                startActivity(new Intent(this, CheckInActivity.class)));

        btnWeeklyReport.setOnClickListener(v ->
                startActivity(new Intent(this, WeeklyReportActivity.class)));

        btnChatAi.setOnClickListener(v ->
                startActivity(new Intent(this, ChatActivity.class)));

        btnSetup.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        loadStats();
        loadMoodChart();
    }

    private void loadStats() {
        repository.getTotalLogsCount(totalLogs -> runOnUiThread(() ->
                tvTotalLogs.setText(String.valueOf(totalLogs))
        ));

        repository.getAverageMood(avgMood -> runOnUiThread(() -> {
            if (avgMood == null) {
                tvAvgMood.setText("—");
            } else {
                tvAvgMood.setText(String.format("%.1f", avgMood));
            }
        }));
    }

    private void loadMoodChart() {
        repository.getLast7DailyLogs(logs -> runOnUiThread(() -> {
            if (logs == null || logs.isEmpty()) {
                lineChartMood.clear();
                lineChartMood.setNoDataText("Aucune donnée d’humeur pour le moment");
                lineChartMood.setNoDataTextColor(Color.parseColor("#4A4860"));
                return;
            }

            List<Entry> entries = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            for (int i = 0; i < logs.size(); i++) {
                DailyLogEntity log = logs.get(i);
                entries.add(new Entry(i, log.mood));
                if (log.date != null && log.date.length() >= 10) {
                    labels.add(log.date.substring(5)); // MM-dd
                } else {
                    labels.add("Jour " + (i + 1));
                }
            }

            LineDataSet dataSet = new LineDataSet(entries, "Humeur");
            dataSet.setLineWidth(2.5f);
            dataSet.setColor(Color.parseColor("#F5C842"));
            dataSet.setCircleColor(Color.parseColor("#F5C842"));
            dataSet.setCircleHoleColor(Color.parseColor("#141720"));
            dataSet.setCircleRadius(5f);
            dataSet.setCircleHoleRadius(2.5f);
            dataSet.setValueTextSize(10f);
            dataSet.setValueTextColor(Color.parseColor("#9A95A0"));
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setCubicIntensity(0.2f);

            dataSet.setDrawFilled(true);
            dataSet.setFillColor(Color.parseColor("#F5C842"));
            dataSet.setFillAlpha(15);

            LineData lineData = new LineData(dataSet);
            lineChartMood.setData(lineData);
            lineChartMood.setBackgroundColor(Color.TRANSPARENT);
            lineChartMood.setDrawGridBackground(false);
            lineChartMood.setBorderColor(Color.parseColor("#1E2238"));

            XAxis xAxis = lineChartMood.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setTextColor(Color.parseColor("#4A4860"));
            xAxis.setTextSize(10f);
            xAxis.setGridColor(Color.parseColor("#1E2238"));
            xAxis.setGridLineWidth(0.5f);
            xAxis.setAxisLineColor(Color.parseColor("#1E2238"));
            xAxis.setDrawAxisLine(false);

            lineChartMood.getAxisLeft().setAxisMinimum(0.5f);
            lineChartMood.getAxisLeft().setAxisMaximum(5.5f);
            lineChartMood.getAxisLeft().setLabelCount(5, true);
            lineChartMood.getAxisLeft().setTextColor(Color.parseColor("#4A4860"));
            lineChartMood.getAxisLeft().setTextSize(10f);
            lineChartMood.getAxisLeft().setGridColor(Color.parseColor("#1E2238"));
            lineChartMood.getAxisLeft().setGridLineWidth(0.5f);
            lineChartMood.getAxisLeft().setAxisLineColor(Color.TRANSPARENT);

            lineChartMood.getAxisRight().setEnabled(false);
            lineChartMood.getDescription().setEnabled(false);
            lineChartMood.getLegend().setEnabled(false);

            lineChartMood.setTouchEnabled(false);
            lineChartMood.animateX(600, com.github.mikephil.charting.animation.Easing.EaseInOutQuart);

            lineChartMood.invalidate();
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
        loadMoodChart();
    }
}