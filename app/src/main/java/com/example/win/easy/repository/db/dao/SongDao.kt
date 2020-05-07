package com.example.win.easy.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.win.easy.repository.db.data_object.SongDO

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songDO: SongDO): Long

    @Query("SELECT * FROM SongDO WHERE songPath is not null")
    fun loadAllLocal(): LiveData<List<SongDO>?>

    @Query("SELECT * FROM SongDO WHERE songPath=:path")
    suspend fun findByPath(path: String): List<SongDO>

    @Update
    suspend fun update(songDO: SongDO)

    //以下为Repo涉及方法
    @Query("SELECT * FROM SongDO")
    fun findAllSong(): List<SongDO>

    @Query("SELECT * FROM SongDO WHERE remoteId=:RemoteId")
    fun findByRemoteId(RemoteId: Long?): SongDO?

    @Query("SELECT * FROM SongDO WHERE remoteId is not null")
    fun findAllDataOnWeb(): List<SongDO?>?

    @Query("SELECT * FROM SongDO WHERE remoteId is null")
    fun findAllDataOnLocal(): List<SongDO?>?


    /** */
    @Query("SELECT * FROM SongDO")
    fun findAllSongDOs(): LiveData<List<SongDO?>?>?

    @Query("SELECT * FROM SongDO WHERE songPath=:songPath")
    fun findAllBySongPath(songPath: String?): SongDO?

    @Delete
    fun delete(songDO: SongDO?)


}