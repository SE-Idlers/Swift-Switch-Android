package com.example.win.easy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.win.easy.repository.RelationRepository
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO

class RelationViewModel(private val relationRepository: RelationRepository): ViewModel() {

    fun addSongsTo(songs: List<SongDO>,songList: SongListDO) {

    }
}