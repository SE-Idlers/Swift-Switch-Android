package com.example.win.easy.repository

import com.example.win.easy.dao.SongDao
import com.example.win.easy.dao.SongXSongListDao
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongXSongListDO

class SongRepository(private val songDao: SongDao,private val relationDao: SongXSongListDao) {

    val allLocalData=songDao.loadAllLocal()
    val allData=songDao.loadAll()

    suspend fun loadSongsExclude(songListDO: SongListDO) = songDao.loadExcludeSongList(songListDO.id!!)

    suspend fun loadSongsBySongList(songListDO: SongListDO)=songDao.loadBySongList(songListDO.id!!)

    suspend fun loadSongsBySeq(sequence: List<Char>) = songDao.loadBySeq(String(sequence.toCharArray()))

    suspend fun insertOnUniquePath(songDO: SongDO){
        if (songDao.findByPath(songDO.songPath!!).isEmpty())
            songDao.insert(songDO)
    }

    suspend fun syncSongsInSongList(songListDO: SongListDO,newSongs: List<SongDO>){
        val oldSongs=songDao.loadBySongList(songListDO.id!!)
        val toDelete=oldSongs.filterNot { it in newSongs }
        ArrayList<SongXSongListDO>().apply {
            for (song in toDelete)
                add(SongXSongListDO(song.id!!,songListDO.id!!))
            relationDao.deleteAll(this)
        }
        val toAddIds=songDao.insertAll(newSongs.filterNot { it in oldSongs })
        ArrayList<SongXSongListDO>().apply {
            for (songId in toAddIds)
                add(SongXSongListDO(songId,songListDO.id!!))
            relationDao.insertAll(this)
        }
    }
    suspend fun update(songDO: SongDO)=songDao.update(songDO)
}