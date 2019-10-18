package com.sb.wifistart.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sb.wifistart.R;

import java.util.ArrayList;

public class StackedBarActivity extends AppCompatActivity {

    private BarChart stackedChart;

    private ArrayList<BarEntry> defValues() {
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        dataVals.add(new BarEntry(0, new float[]{2, 4.5f, 3}));
        dataVals.add(new BarEntry(1, new float[]{2, 7f, 3}));
        dataVals.add(new BarEntry(3, new float[]{2, 6.5f, 6}));
        return dataVals;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_bar);

        stackedChart = findViewById(R.id.stackedBarChart);

        int[] colorClass = new int[]{Color.BLUE, Color.CYAN, Color.RED};
        BarDataSet barDataSet = new BarDataSet(defValues(), "WiFi signals");
        barDataSet.setColors(colorClass);

        BarData barData = new BarData(barDataSet);
        stackedChart.setData(barData);
    }
}