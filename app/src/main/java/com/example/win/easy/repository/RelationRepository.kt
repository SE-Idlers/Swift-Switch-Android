package com.example.win.easy.repository

import com.example.win.easy.dao.SongXSongListDao
import com.example.win.easy.db.SongXSongListDO

class RelationRepository(private val songXSongListDao: SongXSongListDao) {

    suspend fun insert(songXSongListDOs: List<SongXSongListDO>) = songXSongListDao.insert(songXSongListDOs)

}