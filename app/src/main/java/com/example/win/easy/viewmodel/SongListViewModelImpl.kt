package com.example.win.easy.viewmodel

import androidx.lifecycle.*
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.exception.SongListToCreateAlreadyExistLocallyException
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.repository.SongRepository
import kotlinx.coroutines.launch

class SongListViewModelImpl(
        private val songRepository: SongRepository,
        private val songListRepository: SongListRepository) : SongListViewModel() {

    private val allSongList= songListRepository.allLiveData
    private lateinit var songsInList: MutableLiveData<List<SongDO>?>

    private val _spinner=MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _snackbar=MutableLiveData<String?>()
    val snackbar: LiveData<String?>
        get()= _snackbar


    /**
     * 尝试获取所有歌单：
     * 1. 未超时：获取本地+网络
     * 2. 超时：仅获取本地
     */
    override fun loadAll(): LiveData<List<SongListDO>>{
        launchLoading {
            val newSongLists=songListRepository.loadOnline()!!
            val oldSongLists=songListRepository.onlineLiveData.value?.toList()?: emptyList()
            viewModelScope.launch {
                songListRepository.syncSongLists(oldSongLists,newSongLists)
            }
            songListRepository.onlineLiveData.value=newSongLists
        }
        return allSongList
    }

    /**
     * 加载歌单内的歌曲
     */
    override fun loadSongsIn(songListDO: SongListDO):LiveData<List<SongDO>?>{
        return if(songListDO.source==DataSource.Local)
            songListRepository.loadSongsIn(songListDO)
        else{
            songsInList=MutableLiveData<List<SongDO>?>()
            refreshOnlineSongList(songListDO)
            songsInList
        }
    }

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    override fun insert(songListDO: SongListDO) {
        allSongList.value?.let {
            for (existedSongList in it)
                if (existedSongList.name==songListDO.name&&existedSongList.source==DataSource.Local)
                    throw SongListToCreateAlreadyExistLocallyException()
        }
        viewModelScope.launch {
            songListRepository.insert(songListDO)
        }
    }

    /**
     * 刷新歌单内的歌曲（仅网络歌单应当使用）
     */
    private fun refreshOnlineSongList(songListDO: SongListDO){
        launchLoading {
            songsInList.value=songListRepository.loadOnlineSongsIn(songListDO)
            viewModelScope.launch {
                songRepository.syncSongsInSongList(songListDO,songsInList.value!!)
            }
        }
    }

    /**
     * 统一的loading状态控制
     * @param block 执行时在viewModelScope中，它将执行限制在UI线程中，因此对viewmodel的赋值可以直接set
     */
    private fun launchLoading(block: suspend ()->Unit)=viewModelScope.launch{
        try {
            _spinner.postValue(true)
            block()
        }catch (t: Throwable){
            _snackbar.postValue(t.message)
        }finally {
            _spinner.postValue(false)
        }
    }

    override fun launchLoadSongListsBySong(songDO: SongDO, block: (songs: List<SongListDO>) -> Unit){
        viewModelScope.launch {
            block(songListRepository.loadSongListBySong(songDO))
        }
    }
}