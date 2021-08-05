package com.deng.alarmclocknote.viewModel;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;

import com.deng.alarmclocknote.api.LocationService;
import com.deng.alarmclocknote.R;
import com.deng.alarmclocknote.model.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PositionViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getSimpleName();
    private final Context context;
    private final String GOOGLE_GEOCODING_KEY;
    public MutableLiveData<Position> position;

    public PositionViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        GOOGLE_GEOCODING_KEY = context.getString(R.string.GOOGLE_GEOCODING_KEY);
        if(position==null || TextUtils.isEmpty(position.getValue().getCity()))getLocationInfo();
    }

    /**
     * 取得當前位置資訊
     */
    public void getLocationInfo() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Position position = new Position();
                    position.setCity(city);
                    position.setDistrict(district);
                    setPosition(position);

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

    public MutableLiveData<Position> getPosition() {
        if(position == null){
            position = new MutableLiveData<>();
            position.setValue(new Position());
        }
        return position;
    }

    public void setPosition(Position position) {
        if(this.position == null){
            this.position = new MutableLiveData<>();
        }
        this.position.setValue(position);
    }
}
