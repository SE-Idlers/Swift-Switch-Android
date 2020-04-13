package com.example.win.easy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
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

    private var allSongList: LiveData<List<SongListDO>>?=null

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
        allSongList?:viewModelScope.launch {
            allSongList=try {
                _spinner.postValue(true)
                songListRepository.loadAll()
            }catch (t: Throwable){
                _snackbar.postValue(t.message)
                songListRepository.loadLocalOnly()
            }finally {
                _spinner.postValue(false)
            }
        }
        return allSongList!!
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