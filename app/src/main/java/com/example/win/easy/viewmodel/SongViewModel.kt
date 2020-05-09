package com.example.win.easy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.win.easy.repository.SongRepository
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class SongViewModel(
        private val songRepository: SongRepository) : ViewModel() {
    val allLocalSongs=songRepository.allLocalData
    val allSongs=songRepository.allData

    fun insertOnUniquePath(songDO: SongDO) =viewModelScope.launch {
        songRepository.insertOnUniquePath(songDO)
    }

    fun update(songDO: SongDO)=viewModelScope.launch {
        songRepository.update(songDO)
    }

    fun launchLoadSongsBySeq(sequence: List<Char>, block: (songs: List<SongDO>)->Unit){

    }

    fun launchLoadSongsExclude(songListDO: SongListDO,block: (songs: List<SongDO>) -> Unit){

    }

    fun launchLoadSongsBySongLists(songLists: List<SongListDO>, block: (songs: List<List<SongDO>>) -> Unit){
        viewModelScope.launch {
            ArrayList<List<SongDO>>().run {
                for (songList in songLists)
                    add(songRepository.loadSongsBySongList(songList))
                block(this)
            }
        }
    }
}