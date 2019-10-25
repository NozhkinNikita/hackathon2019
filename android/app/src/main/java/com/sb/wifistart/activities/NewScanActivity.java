package com.sb.wifistart.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.sb.wifistart.R;
import com.sb.wifistart.common.CommonVarsHolder;
import com.sb.wifistart.httprequests.LocationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewScanActivity extends AppCompatActivity {

    private TextView locationName;
    private TextView locationStartDate;


    private Spinner pointSpinner;

    private Button addPointBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scan);

        addItemsOnSpinner();

        locationName = findViewById(R.id.locationName);
        locationStartDate = findViewById(R.id.locationStartDate);

        locationName.setText(CommonVarsHolder.locationName);
        locationStartDate.setText(CommonVarsHolder.locationStartDate.replace("T", " "));

        Button addPointBtn = findViewById(R.id.addPointBtn);
        addPointBtn.setOnClickListener(view -> {
                    Intent addPointIntent = new Intent(NewScanActivity.this, AddPointActivity.class);
                    startActivity(addPointIntent);
                }
        );

    }

/*    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
       // popupWindow.setBackgroundDrawable((new ColorDrawable(android.graphics.Color.GRAY)));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
       // popupWindow.showAsDropDown(mBtnPopUpWindow, 0, 0,Gravity.LEFT);
        Drawable d = new ColorDrawable(Color.WHITE);

        R.layout.popup_window.

        d.setAlpha(130);
        getWindow().setBackgroundDrawable(d);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }*/


        // add items into spinner dynamically
    public void addItemsOnSpinner() {

        pointSpinner = (Spinner) findViewById(R.id.pointSpinner);
        List<String> list = CommonVarsHolder.currentPoints.stream().map(point -> point.getName()).collect(Collectors.toList());
        list.add("Do kuchi tochka");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointSpinner.setAdapter(dataAdapter);
    }

    }