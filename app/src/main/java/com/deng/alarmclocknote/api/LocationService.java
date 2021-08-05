package com.deng.alarmclocknote.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * retrofit 透過Google Geocoding API 獲取當前所在位置資訊
 */
public interface LocationService {
    @GET("maps/api/geocode/json")
    Call<ResponseBody> getLocationInfo(@Query("latlng") String latlng,@Query("language") String language,@Query("key") String apiKey);
}
