package com.example.win.easy

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.repository.SongRepository
import com.example.win.easy.repository.db.dao.SongDao
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.database.OurDatabase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class SongRepositoryTest {

    @get:Rule val instantTaskExecutorRule=InstantTaskExecutorRule()
    @InjectMockKs lateinit var songRepository: SongRepository
    @SpyK lateinit var songDao: SongDao
    private lateinit var db: OurDatabase

    @Before
    fun setUp() {
        val context=ApplicationProvider.getApplicationContext<Context>()
        db= Room.inMemoryDatabaseBuilder(context,OurDatabase::class.java).build()
        songDao=db.songDao()
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown(){
        db.close()
    }

    private val mSong=SongDO(songPath="path")

    /**
     * 测试本地歌曲插入，只有数据库中当前没有（没有相同文件路径）的歌曲才会触发插入
     */
    @Test
    fun testInsertOnUniquePath()= runBlocking{
        mSong.name="before"
        songRepository.insertOnUniquePath(mSong)
        var allSong=songDao.loadAll().apply { observeForever{} }
        assertEquals(1,allSong.value!!.size)
        assertEquals("before",allSong.value!![0].name)
        assertEquals(mSong.songPath,allSong.value!![0].songPath)

        mSong.name="after"
        songRepository.insertOnUniquePath(mSong)
        allSong=songDao.loadAll().apply { observeForever{} }
        assertEquals(1,allSong.value!!.size)
        assertEquals("before",allSong.value!![0].name)
        assertEquals(mSong.songPath,allSong.value!![0].songPath)
    }

}