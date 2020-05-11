package com.example.win.easy.repository

import com.example.win.easy.repository.db.dao.SongXSongListDao
import com.example.win.easy.repository.db.data_object.SongXSongListDO

class RelationRepository(private val songXSongListDao: SongXSongListDao) {

    suspend fun insert(songXSongListDOs: List<SongXSongListDO>) = songXSongListDao.insert(songXSongListDOs)

}