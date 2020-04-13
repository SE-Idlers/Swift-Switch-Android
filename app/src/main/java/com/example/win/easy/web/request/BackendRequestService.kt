package com.example.win.easy.web.request

import com.example.win.easy.web.dto.SongListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendRequestService {
    @POST("resource/uid/phone")
    fun getUidByPhone(@Query("account") phone: String?, @Query("password") password: String?): Call<String?>?

    @POST("resource/uid/email")
    fun getUidByEmail(@Query("account") email: String?, @Query("password") password: String?): Call<String?>?

    @GET("resource/songlist/uid")
    suspend fun getAllSongListsByUid(@Query("uid") uid: String): List<SongListDTO>

    @POST("resource/songlist/phone")
    fun getAllSongListsByPhone(@Query("account") phone: String?, @Query("password") password: String?): Call<List<SongListDTO?>?>?

    @POST("resource/songlist/email")
    fun getAllSongListsByEmail(@Query("account") email: String?, @Query("password") password: String?): Call<List<SongListDTO?>?>?
}