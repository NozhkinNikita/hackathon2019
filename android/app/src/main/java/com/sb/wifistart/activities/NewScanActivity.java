package com.sb.wifistart.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.sb.wifistart.R;
import com.sb.wifistart.common.CommonVarsHolder;
import com.sb.wifistart.httprequests.LocationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class NewScanActivity extends AppCompatActivity {

    private TextView locationName;
    private TextView locationStartDate;

    private Button startScanBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scan);

        locationName = findViewById(R.id.locationName);
        locationStartDate = findViewById(R.id.locationStartDate);

        locationName.setText(CommonVarsHolder.locationName);
        locationStartDate.setText(CommonVarsHolder.locationStartDate.replace("T"," "));

        addItemsOnSpinner();

    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        /*locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        List<String> list = locationResponses.stream().map(locationResponse -> locationResponse.getName()).collect(Collectors.toList());
        list.add("Do kuchi location");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(dataAdapter);*/
    }

}