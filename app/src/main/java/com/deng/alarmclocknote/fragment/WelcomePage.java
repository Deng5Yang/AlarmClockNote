package com.deng.alarmclocknote.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.deng.alarmclocknote.dao.MessageEvent;
import com.deng.alarmclocknote.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class WelcomePage extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private View view;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.welcome_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LottieAnimationView welcomeAni = view.findViewById(R.id.welcome_ani);
        welcomeAni.setAnimation(R.raw.welcome_ani);
        welcomeAni.addAnimatorListener(listener);
        welcomeAni.playAnimation();
        super.onViewCreated(view, savedInstanceState);
    }

    Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            Log.e(TAG,"animate start");
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            Log.e(TAG,"animate end");
            Navigation.findNavController(view).navigate(R.id.homePage);
            HashMap<String,Object> map = new HashMap<>();
            map.put("hideNavigation",false);
            EventBus.getDefault().post(new MessageEvent(map));
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

}
