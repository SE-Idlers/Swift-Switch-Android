package com.example.win.easy.display.interfaces

import com.example.win.easy.display.DisplayMode
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.tool.SongListWithSongs

interface DisplayService {
    operator fun next()
    fun previous()
    fun pause()
    fun start()
    fun whetherPlaying(): Boolean
    fun configMode(mode: DisplayMode)
    fun configDisplayList(songListDO: SongListDO, songDOs: List<SongDO>)
    fun displayByIndex(index: Int)
    fun restartWith(songDO: SongDO, songListDO: SongListDO, songDOs: List<SongDO>)
    fun currentSong(): SongDO?
    fun currentSongList(): SongListDO?
    fun currentSongs(): List<SongDO>?
}