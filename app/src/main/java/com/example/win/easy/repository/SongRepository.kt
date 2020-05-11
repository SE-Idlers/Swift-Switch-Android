package com.example.win.easy.repository

import com.example.win.easy.repository.db.dao.SongDao
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO

class SongRepository(private val songDao: SongDao) {

    val allLocalData=songDao.loadAllLocal()
    val allData=songDao.loadAll()

    suspend fun loadSongsExclude(songListDO: SongListDO) = songDao.loadExcludeSongList(songListDO.id!!)

    suspend fun loadSongsBySongList(songListDO: SongListDO)=songDao.loadBySongLists(songListDO.id!!)

    suspend fun loadSongsBySeq(sequence: List<Char>) = songDao.loadBySeq(String(sequence.toCharArray()))

    suspend fun insertOnUniquePath(songDO: SongDO){
        if (songDao.findByPath(songDO.songPath!!).isEmpty())
            songDao.insert(songDO)
    }

    suspend fun update(songDO: SongDO)=songDao.update(songDO)
}