package com.gsnathan.torchlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

/**
 * Created by Gokul Swaminathan on 3/26/2018.
 */

public class MorseActivity extends AppCompatActivity {

    private boolean useDarkTheme;
    private Button morseButton;
    private EditText editMorse;
    private TextView viewMorse;
    private CameraManager cameraManager;
    private FlashLight torch;

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
        setContentView(R.layout.activity_morse);

        onStartUp();
        initWidgets();
    }

    private void onStartUp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = prefs.getBoolean("FIRSTINSTALLMORSE1", true);
        if (isFirstRun) {
            playTargets();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FIRSTINSTALLMORSE1", false);
            editor.commit();
        }
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        torch = new FlashLight(cameraManager, getApplicationContext());
    }

    private void playTargets() {
        final TapTargetSequence morseSequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.editTextMorse), "Convert to Morse", "Put some phrases here and click convert to play the morse code!").targetRadius(110).transparentTarget(true).outerCircleColor(R.color.redPrimaryDark)
                )
                .listener(new TapTargetSequence.Listener() {

                    @Override
                    public void onSequenceFinish() {
                        startActivity(Utils.navIntent(getApplicationContext(), AboutActivity.class));
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        startActivity(Utils.navIntent(getApplicationContext(), AboutActivity.class));
                    }
                });
        morseSequence.start();
    }

    private void initWidgets() {
        editMorse = (EditText) findViewById(R.id.editTextMorse);
        morseButton = (Button) findViewById(R.id.button_convert);
        viewMorse = (TextView) findViewById(R.id.textViewMorse);
    }

    public void flashText(View v) {
        String text = editMorse.getText().toString();
        morseToFlash(MorseCode.decodeEnglish(text), morseButton);
        viewMorse.setText(MorseCode.decodeEnglish(text));
    }

    private void morseToFlash(String morse, final Button button) {
        Handler handler = new Handler();
        int delay = 0;

        for (int x = 0; x < morse.length(); x++) {

            if (morse.charAt(x) == '.') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOn();
                        button.setTextColor(getColor(R.color.redAccent));
                    }
                }, (delay += 200));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOff();
                        button.setTextColor(getColor(R.color.white));
                    }
                }, (delay += 200));

            } else if (morse.charAt(x) == '-') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOn();
                        button.setTextColor(getColor(R.color.redPrimaryDark));
                    }
                }, (delay += 500));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOff();
                        button.setTextColor(getColor(R.color.white));
                    }
                }, (delay += 500));

            } else if (morse.charAt(x) == ' ') {
                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, (delay += 300));


                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, (delay += 300));
            }
        }
    }
}
