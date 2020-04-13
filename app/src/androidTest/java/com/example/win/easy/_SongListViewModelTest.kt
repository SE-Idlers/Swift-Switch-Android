package com.example.win.easy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.repository.repo.Repo
import com.example.win.easy.value_object.VOUtil
import com.example.win.easy.viewmodel.SongListViewModelImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class _SongListViewModelTest {

    @InjectMockKs lateinit var songListViewModel: SongListViewModelImpl
    @MockK lateinit var songListRepository: SongListRepository
    @MockK lateinit var repo: Repo // dummy mock
    @MockK lateinit var voUtil: VOUtil // dummy mock

    /**
     * 测试正常加载本地+网络
     */
    @ExperimentalCoroutinesApi
    @Test
    fun testLoadAll()= runBlockingTest{
        setUpCompleteAllData()
        songListViewModel.loadAll().let {
            it.observeForever{}
            assertEquals(5,it.value!!.size)
        }
    }

    /**
     * 测试网络加载失败
     */
    @ExperimentalCoroutinesApi
    @Test
    fun testLoadAllTimeout()= runBlockingTest{
        setUpLocalOnlyAllData()
        coEvery { songListRepository.loadAll() } throws Throwable("")
        songListViewModel.loadAll().let {
            it.observeForever{}
            assertEquals(2,it.value!!.size)
        }
    }

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        coEvery { songListRepository.loadAll() } returns mAllData
        coEvery { songListRepository.loadLocalOnly() } returns mAllData
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

    private fun setUpCompleteAllData(){
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