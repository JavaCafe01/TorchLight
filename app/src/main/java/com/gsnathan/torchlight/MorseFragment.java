package com.gsnathan.torchlight;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MorseFragment extends Fragment {

    private Button morseButton;
    private EditText editMorse;
    private TextView viewMorse;
    private CameraManager cameraManager;
    private FlashLight torch;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        view = localInflater.inflate(R.layout.fragment_morse, container, false);

        onStartUp();
        initWidgets();

        return view;
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
                        button.setTextColor( getActivity().getColor(R.color.redAccent));
                    }
                }, (delay += 200));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOff();
                        button.setTextColor( getActivity().getColor(R.color.white));
                    }
                }, (delay += 200));

            } else if (morse.charAt(x) == '-') {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOn();
                        button.setTextColor( getActivity().getColor(R.color.redPrimaryDark));
                    }
                }, (delay += 500));

                handler.postDelayed(new Runnable() {
                    public void run() {
                        torch.flashLightOff();
                        button.setTextColor( getActivity().getColor(R.color.white));
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
