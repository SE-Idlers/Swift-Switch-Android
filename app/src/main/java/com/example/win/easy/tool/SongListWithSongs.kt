package com.example.win.easy.tool

import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import lombok.Builder

data class SongListWithSongs(
        val songList: SongListDO,
        val songs: List<SongDO>) {
    val name: String
        get() = songList.name!!

    val source: DataSource
        get() = songList.source!!
}