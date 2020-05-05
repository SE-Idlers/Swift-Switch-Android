package com.example.win.easy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.exception.TimeoutException
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.repository.repo.Repo
import com.example.win.easy.value_object.VOUtil
import com.example.win.easy.viewmodel.SongListViewModelImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class _SongListViewModelTest {

    @InjectMockKs lateinit var songListViewModel: SongListViewModelImpl
    private val songListRepository: SongListRepository= mockkClass(SongListRepository::class)
    @MockK lateinit var repo: Repo // dummy mock
    @MockK lateinit var voUtil: VOUtil // dummy mock

    val testDispatcher=TestCoroutineDispatcher()

    /**
     * 测试正常加载本地+网络
     */
    @Test
    fun testLoadAll()= runBlockingTest{
        setUpAllData()

        songListViewModel.loadAll().let {
            it.observeForever{}

            // 验证数据
            assertEquals(5,it.value!!.size)
        }
    }

    /**
     * 测试网络加载失败
     */
    @Test
    fun testLoadAllTimeout()= runBlockingTest{
        setUpLocalOnlyAllData()
        coEvery { songListRepository.refreshOnline() } throws Throwable("Test")

        songListViewModel.loadAll().let {
            it.observeForever{}

            // 验证数据
            assertEquals(2,it.value!!.size)

            // 验证spinner与snackbar
            assertEquals(false,songListViewModel.spinner.value)
            assertEquals("Test",songListViewModel.snackbar.value)
        }
    }

    private val mSongListDO=SongListDO()
    private val localSongsLiveData=MutableLiveData(listOf(SongDO(), SongDO()))
    private val onlineSongs=listOf(SongDO(), SongDO(), SongDO())

    @Test
    fun testLoadLocalSongsIn()= runBlockingTest {
        mSongListDO.source=DataSource.Local
        coEvery { songListRepository.loadSongsIn(mSongListDO) } returns localSongsLiveData
        val songsLiveData=songListViewModel.loadSongsIn(mSongListDO).apply { observeForever{} }
        assertEquals(2,localSongsLiveData.value!!.size)
    }

    @Test
    fun testLoadOnlineSongsIn()=testDispatcher.runBlockingTest {
        mSongListDO.source=DataSource.WangYiYun
        coEvery { songListRepository.loadOnlineSongsIn(mSongListDO) } coAnswers {
            delay(4000)
            onlineSongs
        }
        val songsLiveData=songListViewModel.loadSongsIn(mSongListDO).apply { observeForever{} }
        advanceTimeBy(4000)
        assertEquals(3,songsLiveData.value!!.size)
    }

    @Test
    fun testLoadOnlineSongsInTimeout()=testDispatcher.runBlockingTest {
        mSongListDO.source=DataSource.WangYiYun
        coEvery { songListRepository.loadOnlineSongsIn(mSongListDO) } coAnswers {
            delay(4000)
            throw TimeoutException("Timeout")
        }
        val songsLiveData=songListViewModel.loadSongsIn(mSongListDO).apply { observeForever{} }
        advanceTimeBy(4000)
        assertEquals(null,songsLiveData.value)
        assertEquals(false,songListViewModel.spinner.value)
        assertEquals("Timeout",songListViewModel.snackbar.value)
    }

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        every { songListRepository.allLiveData } returns mAllData
        MockKAnnotations.init(this)
        songListViewModel.snackbar.observeForever{}
        songListViewModel.spinner.observeForever{}
        localSongsLiveData.observeForever{}
    }

    private fun resetAllData(){
        mAllData.removeSource(mLocalData)
        mAllData.removeSource(mOnlineData)
    }

    private fun setUpLocalOnlyAllData(){
        resetAllData()
        mAllData.run {
            addSource(mLocalData){
                postValue(mLocalData.value)
            }
        }
    }

    private fun setUpAllData(){
        resetAllData()
        mAllData.run {
            addSource(mLocalData){
                val localSize=it!!.size
                val onlineSize=mOnlineData.value!!.size
                postValue(List(localSize+onlineSize){index->
                    if (index<localSize)it[index]
                    else mOnlineData.value!![index-localSize]
                })
            }
            addSource(mOnlineData){
                val localSize=mLocalData.value!!.size
                val onlineSize=it!!.size
                postValue(List(localSize+onlineSize){index->
                    if (index<localSize)mLocalData.value!![index]
                    else it[index-localSize]
                })
            }
        }
    }

    private val mLocalData=MutableLiveData(listOf(SongListDO(),SongListDO()))
    private val mOnlineData=MutableLiveData(listOf(SongListDO(),SongListDO(), SongListDO()))
    private val mAllData=MediatorLiveData<List<SongListDO>>()


    @Rule
    @JvmField
    val instantTaskExecutionRule=InstantTaskExecutorRule()
}