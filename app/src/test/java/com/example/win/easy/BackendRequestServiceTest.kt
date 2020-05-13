package com.example.win.easy

import com.example.win.easy.web.request.BackendRequestService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

class BackendRequestServiceTest {

    private val backendRequestService=
            Retrofit.Builder()
                    .baseUrl("http://127.0.0.1:9000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(BackendRequestService::class.java)

    @Test
    fun testRequest(): Unit = runBlocking{
        val songLists=backendRequestService.getAllSongListsByUid("1846879130")
        val songs=backendRequestService.getSongsBySongListId("3148199869")
        val uid=backendRequestService.getUidByPhone("15564278737","zxc486251379")
        assertNotEquals(0,songLists.size)
        assertNotEquals(0,songs.size)
        assertNotEquals("",uid)
    }
}