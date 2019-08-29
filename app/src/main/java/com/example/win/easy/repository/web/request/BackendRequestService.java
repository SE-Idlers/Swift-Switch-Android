package com.example.win.easy.repository.web.request;

import com.example.win.easy.repository.web.dto.SongListDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BackendRequestService {

    @POST("resource/uid/phone")
    Call<String> getUidByPhone(@Query("account") String phone, @Query("password") String password);

    @POST("resource/uid/email")
    Call<String> getUidByEmail(@Query("account")String email,@Query("password")String password);

    @GET("resource/songlist/uid")
    Call<List<SongListDTO>> getAllSongListsByUid(@Query("uid") String uid);

    @POST("resource/songlist/phone")
    Call<List<SongListDTO>> getAllSongListsByPhone(@Query("account")String phone,@Query("password")String password);

    @POST("resource/songlist/email")
    Call<List<SongListDTO>> getAllSongListsByEmail(@Query("account")String email,@Query("password")String password);

}
