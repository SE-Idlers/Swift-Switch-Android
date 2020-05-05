package com.example.win.easy.viewmodel

import androidx.lifecycle.*
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.repository.repo.Repo
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.value_object.VOUtil
import com.example.win.easy.view.main.SongListToCreateAlreadyExistLocallyException
import kotlinx.coroutines.launch
import java.util.*

class SongListViewModelImpl(private val songListRepository: SongListRepository,
                            private val repo: Repo,
                            private val voUtil: VOUtil) : SongListViewModel() {

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
            songListRepository.refreshOnline()
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

    /**
     * 刷新歌单内的歌曲（仅网络歌单应当使用）
     */
    private fun refreshOnlineSongList(songListDO: SongListDO){
        launchLoading {
            songsInList.value=songListRepository.loadOnlineSongsIn(songListDO)
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

    /**
     * {@inheritDoc}
     */
    override fun songsOf(songListVO: SongListVO?): List<SongVO?>? {
        //获取，避免空值，转化类型
        var songsInRepo = repo.songsOf(voUtil.toDO(songListVO))
        songsInRepo = avoidNullSongs(songsInRepo)
        return toSongsVO(songsInRepo)
    }

    override fun songsNotIn(songListVO: SongListVO?): List<SongVO?>? {
        return null
    }

    override fun addSongsTo(songVOs: List<SongVO>, songListVO: SongListVO) {}

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    override fun create(songListVO: SongListVO?) {
    }

    private fun initAllSongListLiveData(): LiveData<List<SongListVO>> {
        val allSongListDOLiveData = repo.allSongList

        //空值避免，类型转换
        return Transformations.map(allSongListDOLiveData) { allSongListFromRepo: List<SongListDO>? ->
            val allSongListDO = avoidNullSongLists(allSongListFromRepo)
            toAllSongListVO(allSongListDO)
        }
    }

    private fun avoidNullSongLists(songListDOs: List<SongListDO>?): List<SongListDO> {
        return songListDOs ?: ArrayList()
    }

    private fun toAllSongListVO(songListDOs: List<SongListDO>): List<SongListVO> {
        val allSongListVO: MutableList<SongListVO> = ArrayList()
        for (songListDO in songListDOs) allSongListVO.add(voUtil.toVO(songListDO))
        return allSongListVO
    }

    private fun avoidNullSongs(songDOs: List<SongDO>?): List<SongDO> {
        return songDOs ?: ArrayList()
    }

    private fun toSongsVO(songDOs: List<SongDO>): List<SongVO?> {
        val songsVO: MutableList<SongVO?> = ArrayList()
        for (songDO in songDOs) songsVO.add(voUtil.toVO(songDO))
        return songsVO
    }

}