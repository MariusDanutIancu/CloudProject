package com.cloud.androidrestclient;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView responseText;
    ProtectedApi apiInterface;
    private static String RETROFITTAG="RETROFITTAG";

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(RETROFITTAG, "RUNNING" );

        //verifyStoragePermissions(this);

        responseText = (TextView) findViewById(R.id.responseText);
        apiInterface = ProtectedApiClient.getClient().create(ProtectedApi.class);
        Call<ProtectedModel> call  = apiInterface.requestData();
        call.enqueue(new Callback<ProtectedModel>()
        {
            @Override
            public void onResponse(Call<ProtectedModel> call, Response<ProtectedModel> response) {

                ProtectedModel result = response.body();
                Log.v(RETROFITTAG, result.getData());
                responseText.setText(result.getData());

            }

            @Override
            public void onFailure(Call<ProtectedModel> call, Throwable t) {
                Log.v(RETROFITTAG, "CANCEL");
                Log.v(RETROFITTAG, t.getMessage());
                call.cancel();
            }
        });

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.v(RETROFITTAG, "Checking permissions");

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
