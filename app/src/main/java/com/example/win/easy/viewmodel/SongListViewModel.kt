package com.example.win.easy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.view.main.SongListToCreateAlreadyExistLocallyException

abstract class SongListViewModel : ViewModel() {
    /**
     *
     * 获取所有歌单（本地和网络）
     * @return 可观测的所有歌单数据
     */
    abstract fun loadAll(): LiveData<List<SongListDO>>

    /**
     *
     * 获取某个歌单内的所有歌曲
     * @param songListVO 要获取的歌单
     * @return 这个歌单里的歌曲列表
     */
    abstract fun songsOf(songListVO: SongListVO?): List<SongVO?>?
    abstract fun songsNotIn(songListVO: SongListVO?): List<SongVO?>?
    abstract fun addSongsTo(songVOs: List<SongVO>, songListVO: SongListVO)

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    abstract fun create(songListVO: SongListVO?)
}