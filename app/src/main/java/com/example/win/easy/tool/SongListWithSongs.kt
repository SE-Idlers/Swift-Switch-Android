package com.example.win.easy.tool

import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO

data class SongListWithSongs(
        val songList: SongListDO,
        val songs: List<SongDO>) {
    val name: String
        get() = songList.name!!

    val source: DataSource
        get() = songList.source!!
}