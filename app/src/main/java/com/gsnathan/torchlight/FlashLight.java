package com.gsnathan.torchlight;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Gokul Swaminathan on 3/30/2018.
 */

public class FlashLight {

    private boolean flashLightStatus;
    private CameraManager cameraManager;
    private Context context;
    public static double speed;

    public FlashLight(CameraManager cameraManager, Context context) {
        this.cameraManager = cameraManager;
        this.context = context;
        flashLightStatus = false;
    }

    public void flashLightOn() {
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
            //mark button
        } catch (Exception e) {
            Utils.showToast("Cannot turn flashlight on", Toast.LENGTH_SHORT, context);
        }
    }

    public void flashLightOff() {
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
            //mark button
        } catch (Exception e) {
            Utils.showToast("Cannot turn flashlight on", Toast.LENGTH_LONG, context);
        }
    }

    public void morseToFlash(String morse, final Button button, final Activity act) {
        Handler handler = new Handler();
        int delay = 0;

        for (int x = 0; x < morse.length(); x++) {

            if (morse.charAt(x) == '.') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOn();
                        button.setTextColor(act.getColor(R.color.redAccent));
                    }
                }, (delay += (200 * speed)));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOff();
                        button.setTextColor(act.getColor(R.color.white));
                    }
                }, (delay += (200 * speed)));

            } else if (morse.charAt(x) == '-') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOn();
                        button.setTextColor(act.getColor(R.color.redPrimaryDark));
                    }
                }, (delay += (500 * speed)));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOff();
                        button.setTextColor(act.getColor(R.color.white));
                    }
                }, (delay += (500 * speed)));

            } else if (morse.charAt(x) == ' ') {
                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, (delay += (300 * speed)));


                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, (delay += (300 * speed)));
            }
        }
    }

    public void morseToFlash(String morse) {
        Handler handler = new Handler();
        int delay = 0;

        for (int x = 0; x < morse.length(); x++) {

            if (morse.charAt(x) == '.') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOn();
                    }
                }, (delay += (200 * speed)));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOff();
                    }
                }, (delay += (200 * speed)));

            } else if (morse.charAt(x) == '-') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOn();
                    }
                }, (delay += (500 * speed)));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        flashLightOff();
                    }
                }, (delay += (500 * speed)));

            } else if (morse.charAt(x) == ' ') {
                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, (delay += (300 * speed)));


                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, (delay += (300 * speed)));
            }
        }
    }
}
