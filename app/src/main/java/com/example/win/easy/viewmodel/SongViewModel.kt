package com.example.win.easy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.win.easy.repository.SongRepository
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.deprecated.repo.__SongRepository
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.value_object.VOUtil
import kotlinx.coroutines.launch
import java.util.*

class SongViewModel(
        private val songRepository: SongRepository,
        private val __songRepository: __SongRepository,
        private val voUtil: VOUtil) : ViewModel() {
    val allLocalSongs=songRepository.allLocalData
    private var allSongsLiveData: LiveData<List<SongVO>>? = null//对每个DO依次转化//从repo获取DO形式的歌曲，并提供一个映射函数//懒惰加载

    fun insertOnUniquePath(songDO: SongDO) =viewModelScope.launch {
        songRepository.insertOnUniquePath(songDO)
    }

    fun update(songDO: SongDO)=viewModelScope.launch {
        songRepository.update(songDO)
    }

    /**
     * 获取VO形式的所有歌曲
     * @return 所有歌曲的LiveData
     */
    @Deprecated(message = "暂时为了SearchFragment的兼容",
            replaceWith = ReplaceWith("allLocalSongs"))
    val allSongs: LiveData<List<SongVO>>
        get() {
            //懒惰加载
            if (allSongsLiveData == null) {
                //从repo获取DO形式的歌曲，并提供一个映射函数
                allSongsLiveData = Transformations.map(__songRepository.all  //对每个DO依次转化
                ) { input: List<SongDO?> ->
                    val songVOs: MutableList<SongVO> = ArrayList()
                    for (songDO in input) songVOs.add(voUtil.toVO(songDO))
                    songVOs
                }
            }
            return allSongsLiveData!!
        }

    @Deprecated(message = "暂时为了SearchFragment的兼容")
    fun songsMatch(sequence: List<Char?>?): List<SongVO> {
        //TODO 以序列为前缀匹配歌曲（查数据库）
        return ArrayList()
    }

    @Deprecated(message = "暂时为了SearchFragment的兼容")
    fun songListsContain(songVO: SongVO?): List<SongListVO> {
        //TODO 查找所有包含这个歌曲的歌单（查数据库）
        return ArrayList()
    }


}