package com.alfredkondoro.torchapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.VolumeShaper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    ImageButton flashbutton;
    Button langbutton;
    private static final int CAMERA_REQUEST = 123;
    boolean CameraFlash = false;
    int FlashStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        loadLocale ();
        setContentView (R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar ();
        actionBar.setTitle (getResources ().getString (R.string.app_name));

        ActivityCompat.requestPermissions (MainActivity.this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

        CameraFlash = getPackageManager ().
                hasSystemFeature (PackageManager.FEATURE_CAMERA_FLASH);

        flashbutton = findViewById (R.id.imageButtonOnOff);
        langbutton = findViewById (R.id.langbutton);

        flashbutton.setOnClickListener (new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (CameraFlash) {
                    if(FlashStatus == 1){
                        FlashStatus = 0;
                        flashLightOff ();
                    }
                    else{
                        FlashStatus = 1;
                        flashLightOn ();
                    }
                } else {
                    Toast.makeText (MainActivity.this, "@string/app_name_capital",
                            Toast.LENGTH_SHORT).show ();
                }
            }
        });

        langbutton.setOnClickListener (new View.OnClickListener () {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

    }

    private void showChangeLanguageDialog() {
        final String[] listItems={"English", "French", "German", "Portuguese", "Spanish", "Swahili", "Turkish"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder (MainActivity.this);
        mBuilder.setTitle ("Choose Language...");
        mBuilder.setSingleChoiceItems (listItems, -1, new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    setLocale("en");
                    recreate ();
                }
                else if(i == 1){
                    setLocale("fr");
                    recreate ();
                }
                else if(i == 2){
                    setLocale("de");
                    recreate ();
                }
                else if(i == 3){
                    setLocale("pt");
                    recreate ();
                }
                else if(i == 4){
                    setLocale("es");
                    recreate ();
                }
                else if(i == 5){
                    setLocale("sw");
                    recreate ();
                }
                else if(i == 6){
                    setLocale("tr");
                    recreate ();
                }

                dialogInterface.dismiss ();
            }
        });

        AlertDialog mDialog = mBuilder.create ();
        mDialog.show ();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale (lang);
        Locale.setDefault (locale);
        Configuration config = new Configuration ();
        config.locale = locale;
        getBaseContext ().getResources ().updateConfiguration (config, getBaseContext ().getResources ().getDisplayMetrics ());

        SharedPreferences.Editor editor = getSharedPreferences ("Settings", MODE_PRIVATE).edit ();
        editor.putString ("My_Lang", lang);
        editor.apply ();
    }

    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences ("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString ("My_Lang", "");
        setLocale (language);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            Toast.makeText(MainActivity.this, "@string/torch_no_on", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            Toast.makeText(MainActivity.this, "@string/torch_no_off", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CameraFlash = getPackageManager ().
                        hasSystemFeature (PackageManager.FEATURE_CAMERA_FLASH);
            } else {
                flashbutton.setEnabled (false);
                Toast.makeText (MainActivity.this, "@string/app_permission", Toast.LENGTH_SHORT).show ();
            }
        }
    }
}
