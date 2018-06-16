package com.gsnathan.torchlight;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import org.jetbrains.annotations.NotNull;

public class MainFragment extends Fragment {

    private boolean useDarkTheme;
    private StickySwitch stickySwitch;
    private int stickyColor;
    private int stickyBackColor;
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

        view = localInflater.inflate(R.layout.fragment_main, container, false);

        onStartUp();
        initWidgets();

        return view;
    }

    private void onStartUp() {
        cameraManager = (CameraManager) this.getActivity().getSystemService(Context.CAMERA_SERVICE);
        torch = new FlashLight(cameraManager, getContext());
    }

    private void initWidgets() {
        stickySwitch = (StickySwitch) view.findViewById(R.id.sticky_switch);
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
                    Utils.showToast("Torch failed.", Toast.LENGTH_LONG, getContext());
                }
            }
        });
    }
}
