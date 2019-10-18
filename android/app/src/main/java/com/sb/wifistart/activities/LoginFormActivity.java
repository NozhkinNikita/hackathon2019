package com.sb.wifistart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sb.wifistart.R;

public class LoginFormActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private TextView info;
    private Button loginBtn;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        info = findViewById(R.id.incorrectAttemptsCounter);
        loginBtn = findViewById(R.id.loginButton);

        info.setText("No of attempts remaining: 5");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(login.getText().toString(), password.getText().toString());
            }
        });
    }

    private void validate(String login, String password) {
        if (login.equalsIgnoreCase("Serg") && password.equals("1234")) {
            Intent mainIntent = new Intent(LoginFormActivity.this, MainScreenActivity.class);
            startActivity(mainIntent);
        } else {
            counter--;

            info.setText("No of attempts remaining: " + counter);

            if (counter == 0) {
                loginBtn.setEnabled(false);
            }
        }
    }
}
