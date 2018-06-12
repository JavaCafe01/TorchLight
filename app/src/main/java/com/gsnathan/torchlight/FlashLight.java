package com.gsnathan.torchlight;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.widget.Toast;

/**
 * Created by Gokul Swaminathan on 3/30/2018.
 */

public class FlashLight {

    private boolean flashLightStatus;
    private CameraManager cameraManager;
    private Context context;

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
            Utils.showToast("Cannot turn flashlight on", Toast.LENGTH_LONG, context);
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
}
