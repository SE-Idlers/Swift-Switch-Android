package com.example.win.easy.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongXSongListDO

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
interface SongXSongListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songXSongListDOs: Collection<SongXSongListDO>)

    @Delete
    suspend fun deleteAll(songXSongListDOs: Collection<SongXSongListDO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songXSongListDOs: Collection<SongXSongListDO>)

    //以下为Repo涉及方法
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songXSongListDO: SongXSongListDO): Long

    @get:Query("SELECT * FROM SongXSongListDO")
    val allRelation: List<SongXSongListDO?>?

    @Query("SELECT * FROM SongXSongListDO " +
            "WHERE SongXSongListDO.songId=:songId " +
            "AND SongXSongListDO.songListId=:songListId")
    fun findById(songId: Long?, songListId: Long?): SongXSongListDO?

    @Query("SELECT SongXSongListDO.songId, songListId FROM SongDO INNER JOIN SongXSongListDO " +
            "ON SongDO.id = SongXSongListDO.songId " +
            "WHERE SongDO.remoteId is not null")
    fun findAllDataOnWeb(): List<SongXSongListDO?>?

    @Query("SELECT SongXSongListDO.songId, songListId FROM SongDO INNER JOIN SongXSongListDO " +
            "ON SongDO.id = SongXSongListDO.songId " +
            "WHERE SongDO.remoteId is null")
    fun findAllDataOnLocal(): List<SongXSongListDO?>?

    @Query("SELECT * " +
            "FROM SongDO INNER JOIN SongXSongListDO " +
            "ON SongDO.id=SongXSongListDO.songId " +
            "WHERE SongXSongListDO.songListId=:songListId")
    fun getSongsOf(songListId: Long?): List<SongDO?>?

    /** */
    @Query("SELECT * FROM SongXSongListDO")
    fun findAllSongXSongLists(): LiveData<List<SongXSongListDO?>?>?

    @Query("SELECT * " +
            "FROM SongDO INNER JOIN SongXSongListDO " +
            "ON SongDO.id=SongXSongListDO.songId " +
            "WHERE SongXSongListDO.songListId=:songListId")
    fun findAllSongsForSongListById(songListId: Long?): LiveData<List<SongDO?>?>?

    @Query("SELECT * " +
            "FROM SongListDO INNER JOIN SongXSongListDO " +
            "ON SongListDO.id=SongXSongListDO.songListId " +
            "WHERE SongXSongListDO.songId=:songId")
    fun findAllSongListsForSongById(songId: Long?): List<SongListDO?>?

    @Delete
    fun delete(songXSongListDO: SongXSongListDO?)
}