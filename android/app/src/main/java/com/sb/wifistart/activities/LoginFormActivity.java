package com.sb.wifistart.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sb.wifistart.R;
import com.sb.wifistart.httprequests.LoginRequest;
import com.sb.wifistart.httprequests.UserApi;
import com.sb.wifistart.httprequests.UserResponse;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                postData();
                /*validate(login.getText().toString(), password.getText().toString());*/
            }
        });
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void postData() {

        OkHttpClient okHttpClient = getUnsafeOkHttpClient();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://172.30.14.91:8443")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        UserApi yourUsersApi = restAdapter.create(UserApi.class);

        Call call = yourUsersApi.login(new LoginRequest(login.getText().toString(), password.getText().toString()));

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                System.out.println("on response login");
                if (response.body() != null) {
                    UserResponse userResponse = (UserResponse) response.body();
                    String token = userResponse.getToken();
                    Intent mainIntent = new Intent(LoginFormActivity.this, MainScreenActivity.class);
                    startActivity(mainIntent);
                } else {
                    System.out.println("Invalid user/password");
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {

                System.out.println("on failure login");
                /*
                Error callback
                */
            }
        });



        /*try {
            URL url = new URL("https://localhost:8443/login");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
        // Create a new HttpClient and Post Header
        /*HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://localhost:8443/login");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", "admin"));
            nameValuePairs.add(new BasicNameValuePair("password", "password"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            System.out.println("Suka");
        } catch (IOException e) {
            System.out.println("qwe");
        }*/
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
