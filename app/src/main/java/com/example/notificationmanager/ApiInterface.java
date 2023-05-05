package com.example.notificationmanager;

import static com.example.notificationmanager.Constants.content_type;
import static com.example.notificationmanager.Constants.server_key;

import com.example.notificationmanager.model.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization: key="+server_key,"Content-Type:"+content_type})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);
}
