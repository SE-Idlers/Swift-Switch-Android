package com.example.win.easy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO

abstract class SongListViewModel : ViewModel() {
    /**
     * 获取所有歌单（本地和网络）
     * @return 可观测的所有歌单数据
     */
    abstract fun loadAll(): LiveData<List<SongListDO>>

    /**
     * 获取某个歌单内的所有歌曲
     * @param songListDO 要获取的歌单
     * @return 这个歌单里的歌曲列表
     */
    abstract fun loadSongsIn(songListDO: SongListDO): LiveData<List<SongDO>?>

    abstract fun insert(songListDO: SongListDO)

    abstract fun launchLoadSongListsBySong(songDO: SongDO, block: (songs: List<SongListDO>)->Unit)
}