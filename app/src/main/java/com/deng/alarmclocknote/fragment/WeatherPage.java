package com.deng.alarmclocknote.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deng.alarmclocknote.databinding.WeatherLayoutBinding;
import com.deng.alarmclocknote.viewModel.PositionViewModel;
import com.deng.alarmclocknote.viewModel.WeatherViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class WeatherPage extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private WeatherLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WeatherLayoutBinding.inflate(inflater,container,false);
        PositionViewModel positionViewModel = new ViewModelProvider(requireActivity()).get(PositionViewModel.class);
        WeatherViewModel weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        binding.setPosition(positionViewModel);
        binding.setWeather(weatherViewModel);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}
