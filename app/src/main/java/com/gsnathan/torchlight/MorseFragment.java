package com.gsnathan.torchlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class MorseFragment extends Fragment {

    private boolean useDarkTheme;
    private Button morseButton;
    private EditText editMorse;
    private TextView viewMorse;
    private CameraManager cameraManager;
    private FlashLight torch;
    private View view;
    private int theme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        changeTheme();

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), theme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        view = localInflater.inflate(R.layout.fragment_morse, container, false);

        onStartUp();
        initWidgets();

        return view;
    }

    private void changeTheme()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        useDarkTheme = pref.getBoolean("theme_pref", false);

        if (useDarkTheme) {
            theme = R.style.DarkTheme;
        } else {
            theme = R.style.AppTheme;
        }
    }

    private void onStartUp() {
        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        torch = new FlashLight(cameraManager, getContext());
    }

    private void initWidgets() {
        editMorse = (EditText)  view.findViewById(R.id.editTextMorse);
        morseButton = (Button)  view.findViewById(R.id.button_convert);
        morseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashText();
            }
        });
        viewMorse = (TextView)  view.findViewById(R.id.textViewMorse);
    }

    public void flashText() {
        Utils.updateSpeed(getContext());
        String text = editMorse.getText().toString();
        torch.morseToFlash(MorseCode.decodeEnglish(text), morseButton, getActivity());
        viewMorse.setText(MorseCode.decodeEnglish(text));
    }
}
