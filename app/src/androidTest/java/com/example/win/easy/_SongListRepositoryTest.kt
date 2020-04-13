package com.example.win.easy

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.win.easy.repository.SongListDto
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.db.dao.SongListDao
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.web.service.LoginService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class _SongListRepositoryTest {

    @InjectMockKs lateinit var songListRepository: SongListRepository
    @MockK lateinit var songListDao: SongListDao
    @MockK lateinit var songListDto: SongListDto
    @MockK lateinit var loginService: LoginService

    /**
     * 测试未登录下加载所有
     */
    @ExperimentalCoroutinesApi
    @Test
    fun testNonLoginLoadAll()=runBlockingTest{
        every { loginService.hasLogin() } returns false
        setNetworkNonTimeout()
        val allData=songListRepository.loadAll().apply { observeForever {} }
        localMockLiveData.postValue(listOf(SongListDO(), SongListDO()))
        assertEquals(allData.value!!.size,2)
    }

    /**
     * 测试登录下加载所有，网络超时
     */
    @ExperimentalCoroutinesApi
    @Test(expected = Throwable::class)
    fun testLoginLoadAllTimeout()= runBlockingTest{
        every { loginService.hasLogin() } returns true
        setNetworkTimeout()
        launch{
            songListRepository.loadAll()
        }
    }

    /**
     * 测试登录下加载所有，网络正常
     */
    @ExperimentalCoroutinesApi
    @Test
    fun testLoginLoadAll()= runBlockingTest{
        every { loginService.hasLogin() } returns true
        setNetworkNonTimeout()
        val allData=songListRepository.loadAll().apply { observeForever{} }
        localMockLiveData.value=listOf(SongListDO(), SongListDO())
        assertEquals(allData.value!!.size,4)
        coVerify { songListDto.loadAll(mUid) }
    }

    /**
     * 测试仅加载本地
     */
    @ExperimentalCoroutinesApi
    @Test
    fun testLoadLocalOnly()=runBlockingTest{
        val allData=songListRepository.loadLocalOnly().apply { observeForever {} }
        localMockLiveData.postValue(listOf(SongListDO(), SongListDO()))
        assertEquals(allData.value!!.size,2)
    }

    /**
     * 设置本地/网络数据的抓取mock
     */
    @ExperimentalCoroutinesApi
    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        every { songListDao.loadAll() } returns localMockLiveData
        every { loginService.currentUid } returns mUid
    }

    private fun setNetworkNonTimeout(){
        coEvery { songListDto.loadAll(any()) } coAnswers {
            delay(4000)
            onlineMockData
        }
    }

    private fun setNetworkTimeout(){
        coEvery { songListDto.loadAll(any()) } coAnswers {
            delay(5000)
            onlineMockData
        }
    }

    private var localMockLiveData= MutableLiveData<List<SongListDO>>()
    private var onlineMockData= listOf(SongListDO(), SongListDO())
    private val mUid=""
    @Rule
    @JvmField
    val instantTaskExecutorRule=InstantTaskExecutorRule()
}