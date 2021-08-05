package com.deng.alarmclocknote.dao;

import android.app.Activity;
import android.view.View;

public class CommonUtil {

    private final String TAG = this.getClass().getSimpleName();

    public void hideOriginNavigation(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
