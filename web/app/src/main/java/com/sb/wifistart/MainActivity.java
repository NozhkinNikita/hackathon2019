package com.sb.wifistart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private TextView info;
    private Button loginBtn;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Intent mainIntent = new Intent(MainActivity.this, MainScreenActivity.class);
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
