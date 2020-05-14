package com.example.win.easy.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.win.easy.exception.NonLoginException
import com.example.win.easy.exception.TimeoutException
import com.example.win.easy.dao.SongListDao
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongXSongListDO
import com.example.win.easy.dto.SongDto
import com.example.win.easy.dto.SongListDto
import com.example.win.easy.network.LoginService
import kotlinx.coroutines.withTimeout

class SongListRepository(private val songListDto: SongListDto,
                         private val songDto: SongDto,
                         private val songListDao: SongListDao,
                         private val loginService: LoginService) {

    val onlineLiveData=MutableLiveData<List<SongListDO>>()
    private val localLiveData=songListDao.loadAll()

    private val _allLiveData=MediatorLiveData<List<SongListDO>>()
    val allLiveData
        get() = _allLiveData

    init {
        _allLiveData.apply {
            addSource(localLiveData){
                val localSize = it.size
                val onlineSize = onlineLiveData.value?.size ?: 0
                postValue(MutableList(localSize + onlineSize) { index ->
                    if (index < localSize) it[index]
                    else onlineLiveData.value!![index - localSize]
                })
            }
            addSource(onlineLiveData) {
                val localSize= localLiveData.value?.size?:0
                val onlineSize= it?.size?:0
                postValue(MutableList(localSize+onlineSize){index ->
                    if (index<localSize) localLiveData.value!![index]
                    else it[index-localSize]
                })
            }
        }
    }

    /**
     * 根据歌单加载歌曲
     *  对本地歌单：加载全部歌曲
     *  对网络歌单：加载本地缓存的全部歌曲，应当尽快刷新、同步
     */
    fun loadSongsIn(songListDO: SongListDO)=songListDao.findSongsInSongList(songListDO.id!!)

    suspend fun insert(songListDO: SongListDO)=songListDao.insert(songListDO)

    suspend fun loadSongListBySong(songDO: SongDO)=songListDao.loadBySong(songDO.id!!)

    suspend fun loadOnlineSongsIn(songListDO: SongListDO) = songDto.loadBySongList(songListDO)

    /**
     * 刷新网络歌单
     */
    @Throws(TimeoutException::class,NonLoginException::class)
    suspend fun refreshOnline(){
        val newSongLists=loadOnline()
        onlineLiveData.postValue(newSongLists)
    }

    @Throws(Throwable::class)
    suspend fun loadOnline(): List<SongListDO>?{
        var uid:String?=null
        synchronized(loginService){
            if (!loginService.hasLogin())
                throw NonLoginException("未登录，无法拉取网络信息")
            uid=loginService.currentUid
        }
        var onlineData: List<SongListDO>?=null
        uid?.let {
            try {
                withTimeout(5000){
                    onlineData=songListDto.loadAll(it)
                }
            }catch (cause: Throwable){
                throw TimeoutException("拉取网络信息超时")
            }
        }
        return onlineData
    }

    suspend fun syncSongLists(oldSongLists: List<SongListDO>,newSongLists: List<SongListDO>){
        songListDao.deleteAll(oldSongLists.filterNot { it in newSongLists })
        songListDao.insertAll(newSongLists.filterNot { it in oldSongLists })
    }
}

