package com.gsnathan.torchlight;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.widget.Toast;

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = sp.getBoolean("theme_pref", false);

        if (useDarkTheme) {
            setTheme(R.style.DarkTheme);
            getListView().setBackgroundColor(getResources().getColor(R.color.black));
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SwitchPreference toggle = (SwitchPreference) findPreference("theme_pref");
            toggle.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Utils.showToast("Go back to set theme", Toast.LENGTH_SHORT, getContext());
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(Utils.navIntent(getApplicationContext(), MainActivity.class));
    }
}
