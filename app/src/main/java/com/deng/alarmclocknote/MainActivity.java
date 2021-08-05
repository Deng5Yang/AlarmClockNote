package com.deng.alarmclocknote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.deng.alarmclocknote.dao.CommonUtil;
import com.deng.alarmclocknote.dao.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private RelativeLayout navigation;
    private CommonUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.bottom_navigation);
    }

    /**
     * 自製BottomNavigation click事件(切換頁)
     * @param view
     */
    public void navigationClick(View view){
        switch(view.getId()){
            case R.id.clock:
                Navigation.findNavController(this,R.id.nav_host).navigate(R.id.clockPage);
                break;
            case R.id.weather:
                Navigation.findNavController(this,R.id.nav_host).navigate(R.id.weatherPage);
                break;
            case R.id.main:
                Navigation.findNavController(this,R.id.nav_host).navigate(R.id.homePage);
                break;
            case R.id.calendar:
                break;
            case R.id.todo_list:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //隱藏原廠Navigation
        hideOriginNavigation();

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent msg){
        HashMap<String, Object> map = msg.getMap();
        if (navigation.getVisibility()==View.GONE && map.containsKey("hideNavigation") && !TextUtils.isEmpty(map.get("hideNavigation").toString())) {
            Log.e(TAG, "Event map: " + map);
            boolean hideNavigation = Boolean.parseBoolean(map.get("hideNavigation").toString());
            if (!hideNavigation) {
                navigation.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private void hideOriginNavigation(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

}