package com.sb.wifistart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sb.wifistart.R;
import com.sb.wifistart.dto.Point;
import com.sb.wifistart.dto.Scan;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.service.UserApiHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanPointsActivity extends Activity {

    TableLayout pointsListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        Point point = null;

        pointsListView = (TableLayout) findViewById(R.id.pointsList);

        TableRow row = new TableRow(getApplicationContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView pointName = new TextView(getApplicationContext());
        TextView pointBeginDate = new TextView(getApplicationContext());
        TextView pointEndDate = new TextView(getApplicationContext());
        TextView pointWasRescaned = new TextView(getApplicationContext());

        pointName.setText(point.getName());
        pointName.setTextAppearance(android.R.style.TextAppearance_Large);
        pointName.setGravity(Gravity.CENTER_HORIZONTAL);

        pointBeginDate.setText(point.getBegin().split("T")[0]);
        pointBeginDate.setTextAppearance(android.R.style.TextAppearance_Large);
        pointBeginDate.setGravity(Gravity.CENTER_HORIZONTAL);

        pointEndDate.setText(point.getEnd().split("T")[0]);
        pointEndDate.setTextAppearance(android.R.style.TextAppearance_Large);
        pointEndDate.setGravity(Gravity.CENTER_HORIZONTAL);

        pointWasRescaned.setText(point.getIsRepeat()? "Да": "Нет");
        pointWasRescaned.setTextAppearance(android.R.style.TextAppearance_Large);
        pointWasRescaned.setGravity(Gravity.CENTER_HORIZONTAL);

        row.addView(pointName);
        row.addView(pointBeginDate);
        row.addView(pointEndDate);
        row.addView(pointWasRescaned);

        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setPadding(5, 5, 5, 5);
        pointsListView.addView(row);
    }
}
