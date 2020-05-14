package com.example.win.easy.display

import android.media.MediaPlayer
import com.example.win.easy.exception.DisplayStateInconsistentException
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import java.io.IOException

class DisplayServiceImpl(private var mediaPlayer: MediaPlayer) : DisplayService {

    private var currentSongList: SongListDO? = null
    private lateinit var displayList: List<SongDO>
    private var displayMode: DisplayMode? = null
    private var currentSong: SongDO? = null
    private var currentSongIndex = 0

    override fun next() {
        next(mediaPlayer)
    }

    override fun previous() {
        previous(mediaPlayer)
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun start() {
        if (currentSong != null) mediaPlayer.start()
    }

    override fun whetherPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun configMode(mode: DisplayMode) {
        displayMode = mode
    }

    @Throws(DisplayStateInconsistentException::class)
    override fun configDisplayList(songListDO: SongListDO, songDOs: List<SongDO>){
        if(!songDOs.contains(currentSong))
            throw DisplayStateInconsistentException("Wrong SongList")
        displayList = songDOs
        currentSongList= songListDO
        currentSongIndex = songDOs.indexOf(currentSong)
    }

    override fun displayByIndex(index: Int) {
        currentSongIndex = index
        displayByIndex(index, mediaPlayer)
    }

    @Throws(DisplayStateInconsistentException::class)
    override fun restartWith(songDO: SongDO, songListDO: SongListDO, songDOs: List<SongDO>){
        //设置播放列表，同时更新index
        configDisplayList(songListDO,songDOs)
        //设置当前歌曲
        currentSong = songDO
        //开始播放
        display(songDO, mediaPlayer)
    }

    override fun currentSong(): SongDO?=currentSong
    override fun currentSongList(): SongListDO?=currentSongList

    override fun currentSongs(): List<SongDO>?=displayList

    private fun display(songDO: SongDO?, mediaPlayer: MediaPlayer) {
        try {
            // 切歌之前先重置，释放掉之前的资源
            mediaPlayer.reset()
            // 设置播放源
            mediaPlayer.setDataSource(songDO!!.songPath)
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare()
            // 开始播放
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun next(mediaPlayer: MediaPlayer) {
        currentSongIndex = ++currentSongIndex % displayList.size
        displayByIndex(currentSongIndex, mediaPlayer)
    }

    private fun previous(mediaPlayer: MediaPlayer) {
        currentSongIndex = --currentSongIndex % displayList.size
        displayByIndex(currentSongIndex, mediaPlayer)
    }

    private fun displayByIndex(songIndex: Int, mediaPlayer: MediaPlayer) {
        //设置当前主体
        currentSong = displayList[songIndex]
        //播放
        display(currentSong, mediaPlayer)
    }

}