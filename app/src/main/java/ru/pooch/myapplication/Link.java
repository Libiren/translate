package ru.pooch.myapplication;

import java.util.Map;

import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by User on 30.05.2016.
 */
public interface Link {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<Object> translate(@FieldMap Map<String, String> map) ;
}
