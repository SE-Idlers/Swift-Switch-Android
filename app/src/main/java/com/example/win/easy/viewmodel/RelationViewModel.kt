package com.example.win.easy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.win.easy.repository.RelationRepository
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongXSongListDO
import kotlinx.coroutines.launch

class RelationViewModel(private val relationRepository: RelationRepository): ViewModel() {

    fun addSongsTo(songs: List<SongDO>, songList: SongListDO) = viewModelScope.launch {
        ArrayList<SongXSongListDO>().run{
            for (song in songs)
                add(SongXSongListDO(songId = song.id!!, songListId = songList.id!!))
            relationRepository.insert(this)
        }
    }
}