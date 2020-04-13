package com.example.win.easy.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.win.easy.repository.db.data_object.SongListDO

@Dao
interface SongListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songListDO: SongListDO?): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(songListDOs: Collection<SongListDO?>?): Array<Long?>?

    //以下为Repo涉及方法
    @Query("SELECT * FROM SongListDO")
    fun allSongList(): List<SongListDO?>?

    @Query("SELECT * FROM SongListDO WHERE remoteId=:songListRemoteId")
    fun findByRemoteId(songListRemoteId: Long?): SongListDO?

    @Query("SELECT * FROM SongListDO WHERE remoteId is not null")
    fun findAllDataOnWeb(): List<SongListDO?>?

    @Query("SELECT * FROM SongListDO WHERE remoteId is null")
    fun findAllDataOnLocal(): List<SongListDO?>?

    /** */
    @Query("SELECT * FROM SongListDO")
    fun loadAll(): LiveData<List<SongListDO>>

    @Query("SELECT * FROM SongListDO WHERE id=:songListId")
    fun  //    SongListDO findById(Long songListId);
            findById(songListId: Long?): LiveData<SongListDO?>?

    @Query("SELECT * FROM SongListDO WHERE source=:dataSource")
    fun findByDataSource(dataSource: String?): List<SongListDO?>?

    //    LiveData<List<SongListDO>> findByDataSource(String dataSource);
    @Query("SELECT * FROM SongListDO WHERE name=:name AND source=:source")
    fun findAllByNameAndSource(name: String?, source: String?): List<SongListDO?>?

    @Delete
    fun delete(songListDO: SongListDO?)

    @Delete
    fun delete(songListDOs: Collection<SongListDO?>?)

    @Update
    fun update(songListDO: SongListDO?)

    @Update
    fun update(songListDOs: Collection<SongListDO?>?)
}