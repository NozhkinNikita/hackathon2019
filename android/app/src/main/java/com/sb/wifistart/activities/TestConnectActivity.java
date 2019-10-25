package com.sb.wifistart.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.sb.wifistart.R;
import com.sb.wifistart.dto.ipref.IprefResult;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestConnectActivity extends AppCompatActivity {

    //    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private String ipref = "iperf";
    private String argument = "-J";

    private int i = 0;
    private Handler hdlr = new Handler();

    private interface DataListener{
        void onDataReady(IprefResult iprefResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connect);

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("speedtest.hostkey.ru");
        spinnerArray.add("iperf.worldstream.nl");
        spinnerArray.add("peedtest.wtnet.de");
        spinnerArray.add("iperf.volia.net");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        String server = sItems.getSelectedItem().toString();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Button test = findViewById(R.id.btnTest);
        test.setOnClickListener(view -> {
            try {
                progressBar.setVisibility(View.VISIBLE);
                clearResult();

                i = progressBar.getProgress();
                new Thread(new Runnable() {
                    public void run() {
                        while (i < 100) {
                            i += 1;
                            // Update the progress bar and display the current value in text view
                            hdlr.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(i);
                                }
                            });

                            try {
                                // Sleep for 100 milliseconds to show the progress slowly.
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }).start();

                setDataToText(server, new DataListener() {
                    @Override
                    public void onDataReady(IprefResult iprefResult) {
                        renderedResult(iprefResult);
                    }
                });
            } catch (Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println("boom: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void setDataToText(final String server, final DataListener dataListener) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String appFileDirectory = getFilesDir().getPath();

                    System.out.println("Attempting to copy this file: " + appFileDirectory);

                    saveIperf(appFileDirectory);

                    System.out.println("Copy ipref");

                    String filePath = appFileDirectory + File.separator + ipref;

                    setPermissions(filePath);

                    String runCommand = filePath + " -c " + server + " " + argument + " > " + appFileDirectory + File.separator + "res.json";

                    final StringBuilder result = getJson(runCommand);

                    IprefResult iprefResult = parseJson(result.toString());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            dataListener.onDataReady(iprefResult);
                        }
                    });
                } catch (Exception e) {
                    System.out.println("boom: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void clearResult() {
        TextView tvSendedValue = findViewById(R.id.tvSendedValue);
        tvSendedValue.setText("");

        TextView tvSendedPerSecondValue = findViewById(R.id.tvSendedPerSecondValue);
        tvSendedPerSecondValue.setText("");

        TextView tvReceivedValue = findViewById(R.id.tvReceivedValue);
        tvReceivedValue.setText("");

        TextView tvReceivedPerSecondValue = findViewById(R.id.tvReceivedPerSecondValue);
        tvReceivedPerSecondValue.setText("");
    }

    private void renderedResult(IprefResult iprefResult) {
        if (iprefResult.getError() == null) {
            TextView tvSendedValue = findViewById(R.id.tvSendedValue);
            tvSendedValue.setText(iprefResult.getEnd().getSum_sent().getBytes().toString());

            TextView tvSendedPerSecondValue = findViewById(R.id.tvSendedPerSecondValue);
            tvSendedPerSecondValue.setText(iprefResult.getEnd().getSum_sent().getBits_per_second().toString());

            TextView tvReceivedValue = findViewById(R.id.tvReceivedValue);
            tvReceivedValue.setText(iprefResult.getEnd().getSum_received().getBytes().toString());

            TextView tvReceivedPerSecondValue = findViewById(R.id.tvReceivedPerSecondValue);
            tvReceivedPerSecondValue.setText(iprefResult.getEnd().getSum_received().getBits_per_second().toString());

            TextView tvStatus = findViewById(R.id.tvStatus);
            tvStatus.setText("");

            List<String> seconds = new ArrayList<>();
            List<BarEntry> values = new ArrayList<>();
            for(int i = 0; i < iprefResult.getIntervals().size(); i++) {
                seconds.add(String.valueOf(i+1));
                values.add(new BarEntry(i, Float.valueOf(iprefResult.getIntervals().get(i).getSumInterval().getBytes().toString())));
            }

            BarChart barChart = findViewById(R.id.barChart);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setLabelRotationAngle(45);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(seconds));

            BarDataSet barDataSet = new BarDataSet(values, "Test chanel");
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.invalidate();
        } else {
            TextView tvStatus = findViewById(R.id.tvStatus);
            tvStatus.setText(iprefResult.getError());

            clearResult();
        }
    }

    private void saveIperf(String appFileDirectory) throws IOException {
        InputStream in = this.getResources().openRawResource(R.raw.iperf);
        System.out.println("outDir: " + appFileDirectory);
        File outFile = new File(appFileDirectory, ipref);
        OutputStream out = new FileOutputStream(outFile);
        IOUtils.copy(in, out);
        in.close();
        out.flush();
        out.close();
    }

    private StringBuilder getJson(String runCommand) throws IOException {
        Process process = Runtime.getRuntime().exec(runCommand);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("line: " + line);
            result.append(line + "\n");

        }
        reader.close();
        process.destroy();
        return result;
    }

    private IprefResult parseJson(String jsonString) {
        System.out.println("jsonString: " + jsonString);

        Gson gson = new Gson();
        IprefResult iprefResult = gson.fromJson(jsonString, IprefResult.class);

        System.out.println("parseJson done: " + iprefResult);

        return iprefResult;
    }

    private void setPermissions(String path) {
        File file = new File(path);
        file.setExecutable(true);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    createFile();
//                } else {
//                    System.out.println("user is stuped!");
//                }
//                return;
//            }
//        }
//    }
//
//    public void checkWritePermissionAndCreateFile() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//
//                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//            // Carry out functionality that you needed permission for
//            createFile();
//        }
//    }
//
//    private void createFile() {
//        try {
//            String folder_main = "Hton";
//
//            File f = new File("/data/local/tmp", File.separator + folder_main);
//            if (!f.exists()) {
//                if (!f.mkdir()) {
//                    System.out.println("not created");
//                } else {
//                    testConnect(f.getPath());
//                }
//            } else {
//                testConnect(f.getPath());
//            }
//        } catch (Exception e) {
//            System.out.println("boom: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void testConnect(String folderDir) {
//        try {
//            String filePath = folderDir + File.separator + ipref;
//
//            File f = new File(filePath);
//            if (!f.exists()) {
//                setupBinaries(folderDir);
//            }
//
//            setPermissions(filePath);
//
//            Process process = Runtime.getRuntime().exec(filePath + " -c " + server + " " + argument);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            final StringBuilder result = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result.append(line + "\n");
//            }
//            reader.close();
//            process.destroy();
//
//            System.out.println("testConnect: " + result.toString());
//        } catch (Exception e) {
//            System.out.println("boom: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private void setPermissions(String path) {
//        File file = new File(path);
//        file.setExecutable(true);
//    }
//
//    private void setupBinaries(String folderDir) {
//        try {
//            InputStream in = this.getResources().openRawResource(R.raw.iperf);
//            OutputStream out = new FileOutputStream(folderDir + File.separator + ipref);
//            byte[] buf = new byte[1024];
//            int len;
//
//            while ((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            in.close();
//            out.flush();
//            out.close();
//            Runtime.getRuntime().exec("chmod 751 " + folderDir + File.separator + ipref);
//        } catch (Exception e) {
//            System.out.println("boom: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
