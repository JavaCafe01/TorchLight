package com.gsnathan.torchlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Gokul Swaminathan on 3/26/2018.
 */

public class Utils {
    public static String FIRST_INSTALL = "first-install";

    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public static Intent emailIntent(String emailAddress, String subject, String text, String title) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(email, title);
    }

    public static Intent webIntent(String url) {
        Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        return link;
    }

    public static Intent navIntent(Context context, Class activity) {
        Intent navigate = new Intent(context, activity);
        return navigate;
    }

    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    //basically show a toast message on android
    public static void showToast(String msg, int time, Context context) {
        CharSequence text = msg;
        int duration = time;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void updateSpeed(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        double speed = (double)sp.getInt("speed_slider", 100);
        FlashLight.speed = 1/(speed/100);
    }
}
