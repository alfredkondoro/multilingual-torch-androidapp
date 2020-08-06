package com.alfredkondoro.torchapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    Button buttontaa;
    private static final int CAMERA_REQUEST = 123;
    boolean CameraFlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);


        ActivityCompat.requestPermissions (MainActivity.this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

        CameraFlash = getPackageManager ().
                hasSystemFeature (PackageManager.FEATURE_CAMERA_FLASH);

        buttontaa = findViewById (R.id.buttonOff);

        buttontaa.setOnClickListener (new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (CameraFlash) {
                    if (buttontaa.getText ().toString ().contains ("ZIMA")) {
                        buttontaa.setText ("  WASHA  ");
                        flashLightOff ();
                    } else {
                        buttontaa.setText ("  ZIMA  ");
                        flashLightOn ();
                    }
                } else {
                    Toast.makeText (MainActivity.this, "Hakuna flash kwenye chombo chako",
                            Toast.LENGTH_SHORT).show ();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CameraFlash = getPackageManager().
                            hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                } else {
                    buttontaa.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Ruhusa ya camera imekataliwa", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
