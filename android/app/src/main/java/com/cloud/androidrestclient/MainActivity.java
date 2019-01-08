package com.cloud.androidrestclient;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView responseText;
    ProtectedApi apiInterface;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Logging
    private static String RETROFITTAG = "RETROFITTAG";
    private static String testConnectionTemplateValidResponseTempate = "Received message: %s";
    private static String testConnectionTemplateInvalidResponseTempate = "Test connection failled: %s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(RETROFITTAG, "MainActivity - OnCreate" );

        Log.v(RETROFITTAG, "MainActivity - Testing connection" );
        final Button button = findViewById(R.id.testConnection_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                retrofit2EurekaTestConnection();
            }
        });
    }

    /**
     * Test eureka connection
     *
     */
    private void retrofit2EurekaTestConnection()
    {
        responseText = findViewById(R.id.responseText);
        responseText.setText("MainActivity - Testing...");

        apiInterface = ProtectedApiClient.getClient().create(ProtectedApi.class);
        if(apiInterface == null)
        {
            String logMessage = String.format(testConnectionTemplateInvalidResponseTempate, "Connection not available");
            Log.v(RETROFITTAG, logMessage);
            responseText.setText(logMessage);
        }

        Call<ProtectedModel> call  = apiInterface.requestData();
        call.enqueue(new Callback<ProtectedModel>()
        {
            @Override
            public void onResponse(Call<ProtectedModel> call, Response<ProtectedModel> response)
            {
                ProtectedModel result = response.body();
                Log.v(RETROFITTAG, String.format(testConnectionTemplateValidResponseTempate, result.getData()));
                responseText.setText(result.getData());
            }

            @Override
            public void onFailure(Call<ProtectedModel> call, Throwable t) {

                String logMessage = String.format(testConnectionTemplateInvalidResponseTempate, t.getCause());
                Log.e(RETROFITTAG, logMessage);
                responseText.setText(logMessage);
                call.cancel();
            }
        });
    }

    /**
     * Verify storage permisions
     * Currently not needed
     *
     * @param activity
     */
    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.v(RETROFITTAG, "Main Activity - Checking permissions");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
