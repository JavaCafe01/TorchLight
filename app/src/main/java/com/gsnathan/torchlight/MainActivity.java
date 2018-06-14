package com.gsnathan.torchlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.kobakei.ratethisapp.RateThisApp;

import org.jetbrains.annotations.NotNull;

import io.ghyeok.stickyswitch.widget.StickySwitch;

/**
 * Created by Gokul Swaminathan on 3/26/2018.
 */

public class MainActivity extends AppCompatActivity {

    private boolean useDarkTheme;
    private StickySwitch stickySwitch;
    private int stickyColor;
    private int stickyBackColor;
    private CameraManager cameraManager;
    private FlashLight torch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getSharedPreferences("prefs", MODE_PRIVATE);
        useDarkTheme = pref.getBoolean("dark_theme", false);

        if (useDarkTheme) {
            setTheme(R.style.DarkTheme);
            stickyColor = 0xFFFFFFFF;   //white
            stickyBackColor = 0xFF000000;   //black
        } else {
            setTheme(R.style.AppTheme);
            stickyColor = 0xFFCD4844;   //redtheme
            stickyBackColor = 0xFFC0C0C0;   //silver
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onStartUp();
        initWidgets();

        // Custom condition: 5 days and 5 launches
        RateThisApp.Config config = new RateThisApp.Config(5, 5);
        RateThisApp.init(config);
        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }

    private void onStartUp() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = prefs.getBoolean(Utils.FIRST_INSTALL, true);
        if (isFirstRun) {
            startActivity(new Intent(this, MainIntroActivity.class));
            playTargets();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Utils.FIRST_INSTALL, false);
            editor.commit();
        }

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        torch = new FlashLight(cameraManager, getApplicationContext());
    }

    private void playTargets() {
        final TapTargetSequence homeSequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.sticky_switch), "Main Toggle", "Turn the flashlight on and off with this.").targetRadius(100).outerCircleColor(R.color.redPrimaryDark)
                )
                .listener(new TapTargetSequence.Listener() {

                    @Override
                    public void onSequenceFinish() {
                        startActivity(Utils.navIntent(getApplicationContext(), MorseActivity.class));
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        startActivity(Utils.navIntent(getApplicationContext(), MorseActivity.class));
                    }
                });
        homeSequence.start();
    }

    private void initWidgets() {
        stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);
        stickySwitch.setTextColor(stickyColor);
        stickySwitch.setSliderBackgroundColor(stickyBackColor);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                if (direction == StickySwitch.Direction.RIGHT) {
                    torch.flashLightOn();
                } else if (direction != StickySwitch.Direction.RIGHT) {
                    torch.flashLightOff();
                } else {
                    Utils.showToast("Torch failed.", Toast.LENGTH_LONG, getApplicationContext());
                }
            }
        });
    }

    private void flashSOS() {
        final String sosMorse = "... --- ...";
        morseToFlash(sosMorse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void morseToFlash(String morse) {
        Handler handler = new Handler();
        int delay = 0;

        for (int x = 0; x < morse.length(); x++) {

            if (morse.charAt(x) == '.') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOn();
                    }
                }, (delay += 200));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOff();
                    }
                }, (delay += 200));

            } else if (morse.charAt(x) == '-') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOn();
                    }
                }, (delay += 500));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOff();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_morse:
                startActivity(Utils.navIntent(this, MorseActivity.class));
                return true;
            case R.id.action_about:
                startActivity(Utils.navIntent(this, AboutActivity.class));
                return true;
            case R.id.action_sos:
                flashSOS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
