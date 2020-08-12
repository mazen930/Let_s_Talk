package com.example.letstalk.Fragments;

import com.example.letstalk.Notifications.MyResponse;
import com.example.letstalk.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAzwGaCeo:APA91bH8NQMGlg3IpVbegqRcOJNfsX2aap2--n0rZeYccNQ8qUYTP-yyy907aXVDCKx4Pkfp2N43kmIsicAFyrqFJCWtddqTF6WWDxJlPwRqXulebpn77zIvQ0pupKJHyqe-ZJ6aIp3_"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifications(@Body Sender body);
}
