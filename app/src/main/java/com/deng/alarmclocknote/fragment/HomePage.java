package com.deng.alarmclocknote.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deng.alarmclocknote.api.LocationService;
import com.deng.alarmclocknote.databinding.HomePageLayoutBinding;
import com.deng.alarmclocknote.viewModel.PositionViewModel;
import com.deng.alarmclocknote.viewModel.WeatherViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePage extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private HomePageLayoutBinding binding;
    private final int REQUEST_CODE = 1;
    private final String GOOGLE_GEOCODING_KEY = "AIzaSyBCxuD-VRQgN3g5-7alOL3YR0eXZQSL4-w";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomePageLayoutBinding.inflate(inflater,container,false);
        PositionViewModel positionViewModel = new ViewModelProvider(requireActivity()).get(PositionViewModel.class);
        WeatherViewModel weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        binding.setPosition(positionViewModel);
        binding.setWeather(weatherViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askPermission();
//        if (getActivity() != null)
//            getLocationInfo();
//        else
//            Log.e(TAG, "getLocation error due to getActivity==null");
    }

    /**
     * 請求權限
     */
    private void askPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ArrayList<String> tempAry = new ArrayList<>();
        for (String permission : permissions) {
            if (getContext() != null && ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED)
                tempAry.add(permission);
        }
        if (tempAry.size() > 0 && getActivity() != null) {
            String[] requestPermissions = tempAry.toArray(new String[0]);
            ActivityCompat.requestPermissions(getActivity(), requestPermissions, REQUEST_CODE);
        }
    }

    /**
     * 取得當前位置資訊
     */
    private void getLocationInfo() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        String provider = locationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        Log.e(TAG,"location.lat: "+location.getLatitude());
        Log.e(TAG,"location.lon: "+location.getLongitude());
        String latlng = location.getLatitude()+","+location.getLongitude();

        String url = "https://maps.googleapis.com/";
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(url)
//                                .addConverterFactory()
                                .build();
        LocationService locationService = retrofit.create(LocationService.class);
        Call<ResponseBody> call = locationService.getLocationInfo(latlng,"zh-TW",GOOGLE_GEOCODING_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG,"getLocation success");
//                Log.e(TAG,"getLocation response: "+response.body());
                try {
                    String result = response.body().string();
                    Log.e(TAG,"getLocation result: "+result);
                    JSONObject json = new JSONObject(result);
                    JSONArray resultsAry = json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                    String district = resultsAry.getJSONObject(3).getString("long_name");
                    String city = resultsAry.getJSONObject(4).getString("long_name");
                    Log.e(TAG,"response district: "+district);
                    Log.e(TAG,"response city: "+city);
                } catch (IOException ie) {
                    Log.e(TAG,"error: "+ie);
                } catch (JSONException je){
                    Log.e(TAG,"error: "+je);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"getLocation fail");
            }
        });

    }

}
