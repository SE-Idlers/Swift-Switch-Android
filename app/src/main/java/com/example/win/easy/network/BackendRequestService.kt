package com.example.win.easy.network

import com.example.win.easy.db.SongDTO
import com.example.win.easy.db.SongListDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendRequestService {
    @POST("uid/phone")
    suspend fun getUidByPhone(@Query("account") phone: String, @Query("password") password: String): String

    @POST("uid/email")
    suspend fun getUidByEmail(@Query("account") email: String, @Query("password") password: String): String

    @GET("songlist/uid")
    suspend fun getAllSongListsByUid(@Query("uid") uid: String): List<SongListDTO>

    @GET("song/list")
    suspend fun getSongsBySongListId(@Query("songListId") songListId: String): List<SongDTO>
}