package com.example.win.easy.repository.web;

import com.example.win.easy.repository.web.domain.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BackendResourceWebService {

    @POST("/uid/phone")
    Call<Response> getUidByPhone(@Query("account") String phone, @Query("password") String password);

    @POST("/uid/email")
    Call<Response> getUidByEmail(@Query("account")String email,@Query("password")String password);

    @GET("/songlist/uid")
    Call<Response> getAllSongListsByUid(@Query("uid") String uid);

    @POST("/songlist/phone")
    Call<Response> getAllSongListsByPhone(@Query("account")String phone,@Query("password")String password);

    @POST("/songlist/email")
    Call<Response> getAllSongListsByEmail(@Query("account")String email,@Query("password")String password);

}
