package com.example.win.easy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.deprecated.repo.__SongRepository
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.value_object.VOUtil
import kotlinx.coroutines.launch
import java.util.*

class SongViewModel(
        private val songRepository: __SongRepository,
        private val voUtil: VOUtil) : ViewModel() {
    private var allSongsLiveData: LiveData<List<SongVO>>? = null//对每个DO依次转化//从repo获取DO形式的歌曲，并提供一个映射函数//懒惰加载

    /**
     * 获取VO形式的所有歌曲
     * @return 所有歌曲的LiveData
     */
    val allSongs: LiveData<List<SongVO>>
        get() {
            //懒惰加载
            if (allSongsLiveData == null) {
                //从repo获取DO形式的歌曲，并提供一个映射函数
                allSongsLiveData = Transformations.map(songRepository.all  //对每个DO依次转化
                ) { input: List<SongDO?> ->
                    val songVOs: MutableList<SongVO> = ArrayList()
                    for (songDO in input) songVOs.add(voUtil.toVO(songDO))
                    songVOs
                }
            }
            return allSongsLiveData!!
        }

    fun songsMatch(sequence: List<Char?>?): List<SongVO> {
        //TODO 以序列为前缀匹配歌曲（查数据库）
        return ArrayList()
    }

    fun songListsContain(songVO: SongVO?): List<SongListVO> {
        //TODO 查找所有包含这个歌曲的歌单（查数据库）
        return ArrayList()
    }

    fun update(songDO: SongDO)=viewModelScope.launch {

    }
}