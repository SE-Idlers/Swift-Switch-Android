package com.example.win.easy

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.win.easy.exception.NonLoginException
import com.example.win.easy.exception.TimeoutException
import com.example.win.easy.repository.SongListDto
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.db.dao.SongListDao
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.web.service.LoginService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class _SongListRepositoryTest {

    @InjectMockKs lateinit var songListRepository: SongListRepository
    private val songListDao= mockkClass(SongListDao::class)
    @MockK lateinit var songListDto: SongListDto
    @RelaxedMockK lateinit var loginService: LoginService

    /**
     * 测试网络超时
     */
    @ExperimentalCoroutinesApi
    @Test(expected = TimeoutException::class)
    fun testRefreshOnlineTimeout()= runBlockingTest{
        every { loginService.hasLogin() } returns true
        songListRepository.refreshOnline()
    }

    /**
     * 测试未登录
     */
    @ExperimentalCoroutinesApi
    @Test(expected = NonLoginException::class)
    fun testRefreshOnlineNonLogin()= runBlockingTest {
        every { loginService.hasLogin() } returns false
        songListRepository.refreshOnline()
    }

    /**
     * 测试正常抓取
     */
    @ExperimentalCoroutinesApi
    @Test
    fun testRefreshOnline()= runBlockingTest{
        every { loginService.hasLogin() } returns true
        coEvery { songListDto.loadAll(any()) } coAnswers {
            advanceTimeBy(1000)
            onlineMockData
        }

        localMockLiveData.value=listOf(SongListDO(), SongListDO())
        assertEquals(2,songListRepository.allLiveData.value!!.size)

        launch { songListRepository.refreshOnline() }.join()
        assertEquals(4,songListRepository.allLiveData.value!!.size)
    }

    /**
     * 设置本地/网络数据的抓取mock
     */
    @ExperimentalCoroutinesApi
    @Before
    fun setUp(){
        every { songListDao.loadAll() } returns localMockLiveData
        MockKAnnotations.init(this)
        every { loginService.currentUid } returns mUid
        songListRepository.allLiveData.observeForever {  }
    }

    private var localMockLiveData= MutableLiveData<List<SongListDO>>()
    private var onlineMockData= listOf(SongListDO(), SongListDO())
    private val mUid=""
    @Rule
    @JvmField
    val instantTaskExecutorRule=InstantTaskExecutorRule()
}