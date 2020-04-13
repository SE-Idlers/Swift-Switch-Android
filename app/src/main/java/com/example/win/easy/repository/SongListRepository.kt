package com.example.win.easy.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.win.easy.repository.db.dao.SongListDao
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.web.service.LoginService
import kotlinx.coroutines.withTimeout

class SongListRepository(private val songListDto: SongListDto,
                         private val songListDao: SongListDao,
                         private val loginService: LoginService) {

    private val allLiveData=MediatorLiveData<List<SongListDO>>()
    private var localLiveData: LiveData<List<SongListDO>>?=null
    private val onlineLiveData=MutableLiveData<List<SongListDO>>()

    /**
     * 加载本地与网络歌单
     * @exception Throwable 超时抛出异常
     */
    @Throws(Throwable::class)
    suspend fun loadAll(): LiveData<List<SongListDO>> {
        resetAllData()
        refreshLocal()
        refreshOnline()
        registerLocal()
        registerOnline()
        return allLiveData
    }

    /**
     * 仅加载本地
     */
    fun loadLocalOnly(): LiveData<List<SongListDO>>{
        resetAllData()
        refreshLocal()
        registerLocal()
        return allLiveData
    }

    /**
     * 手动加载网络
     */
    suspend fun loadOnlineManually(){
        refreshOnline()
        allLiveData.removeSource(onlineLiveData)
        registerOnline()
    }

    @Throws(Throwable::class)
    suspend fun refreshOnline(){
        onlineLiveData.postValue(loadOnline())
    }

    private fun registerLocal(){
        allLiveData.run {
            addSource(localLiveData!!) {
                val localSize = it.size
                val onlineSize = onlineLiveData.value?.size ?: 0
                postValue(MutableList(localSize + onlineSize) { index ->
                    if (index < localSize) it[index]
                    else onlineLiveData.value!![index - localSize]
                })
            }
        }
    }

    private fun registerOnline(){
        allLiveData.run {
            addSource(onlineLiveData) {
                val localSize=localLiveData!!.value?.size?:0
                val onlineSize= it?.size?:0
                postValue(MutableList(localSize+onlineSize){index ->
                    if (index<localSize) localLiveData!!.value!![index]
                    else it[index-localSize]
                })
            }
        }
    }

    private fun resetAllData(){
        allLiveData.removeSource(onlineLiveData)
        localLiveData?.let { allLiveData.removeSource(it) }
    }

    private fun refreshLocal(){
        localLiveData?:let {
            localLiveData=songListDao.loadAll()
        }
    }

    @Throws(Throwable::class)
    private suspend fun loadOnline(): List<SongListDO>?{
        var uid:String?=null
        synchronized(loginService){
            if (loginService.hasLogin())
                uid=loginService.currentUid
        }
        var onlineData: List<SongListDO>?=null
        uid?.let {
            try {
                withTimeout(5000){
                    onlineData=songListDto.loadAll(it)
                }
            }catch (cause: Throwable){
                throw Throwable("拉取网络信息失败")
            }
        }
        return onlineData
    }

}

