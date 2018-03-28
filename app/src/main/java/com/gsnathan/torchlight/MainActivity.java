package com.gsnathan.torchlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

/**
 * Created by Gokul Swaminathan on 3/26/2018.
 */

public class MainActivity extends AppCompatActivity {

    private boolean flashLightStatus = false;
    private boolean useDarkTheme;
    private Button sosButton;
    private SwitchButton switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getSharedPreferences("prefs", MODE_PRIVATE);
        useDarkTheme = pref.getBoolean("dark_theme", false);

        if (useDarkTheme) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onStartUp();
        initWidgets();
    }

    private void onStartUp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = prefs.getBoolean("FIRSTRUN", true);
        if (isFirstRun) {
            startActivity(new Intent(this, MainIntroActivity.class));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }
    }

    private void initWidgets() {
        sosButton = (Button) findViewById(R.id.button_sos);
        switchButton = (SwitchButton) findViewById(R.id.switch_light);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    flashLightOn();
                } else if (!isChecked) {
                    flashLightOff();
                } else {
                    Utils.showToast("Torch failed.", Toast.LENGTH_LONG, getApplicationContext());
                }
            }
        });

    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            //mark button
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            //mark button
        } catch (CameraAccessException e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(Utils.navIntent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void flashSOS(View v) {
        final String sosMorse = "111000111";
        morseToFlash(sosMorse);
    }

    private void morseToFlash(String morse) {
        Handler handler = new Handler();
        int delay = 0;

        for (int x = 0; x < morse.length(); x++) {
            if (morse.charAt(x) == '1') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOn();
                        sosButton.setTextColor(getColor(R.color.redAccent));
                    }
                }, (delay += 350));


                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOff();
                        sosButton.setTextColor(getColor(R.color.white));
                    }
                }, (delay += 350));
            } else if (morse.charAt(x) == '0') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOn();
                        sosButton.setTextColor(getColor(R.color.redPrimaryDark));
                    }
                }, (delay += 600));


                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOff();
                        sosButton.setTextColor(getColor(R.color.white));
                    }
                }, (delay += 600));
            }
        }
    }
}
