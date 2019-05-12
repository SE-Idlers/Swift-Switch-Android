package com.example.win.easy.repository.web;

import com.example.win.easy.repository.web.domain.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BackendResourceWebService {

    @POST("resource/uid/phone")
    Call<Response> getUidByPhone(@Query("account") String phone, @Query("password") String password);

    @POST("resource/uid/email")
    Call<Response> getUidByEmail(@Query("account")String email,@Query("password")String password);

    @GET("resource/songlist/uid")
    Call<Response> getAllSongListsByUid(@Query("uid") String uid);

    @POST("resource/songlist/phone")
    Call<Response> getAllSongListsByPhone(@Query("account")String phone,@Query("password")String password);

    @POST("resource/songlist/email")
    Call<Response> getAllSongListsByEmail(@Query("account")String email,@Query("password")String password);

}
