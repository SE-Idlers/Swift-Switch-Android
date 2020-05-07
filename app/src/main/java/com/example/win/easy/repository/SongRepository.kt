package com.example.win.easy.repository

import com.example.win.easy.repository.db.dao.SongDao
import com.example.win.easy.repository.db.data_object.SongDO

class SongRepository(private val songDao: SongDao) {

    val allLocalData=songDao.loadAllLocal()

    suspend fun insertOnUniquePath(songDO: SongDO){
        if (songDao.findByPath(songDO.songPath).isEmpty())
            songDao.insert(songDO)
    }

    suspend fun update(songDO: SongDO)=songDao.update(songDO)
}