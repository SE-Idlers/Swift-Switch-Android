package com.example.win.easy.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.enumeration.DataSource

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
interface SongListDao {
    @Query("SELECT * FROM SongListDO")
    fun loadAll(): LiveData<List<SongListDO>>

    @Query("SELECT * FROM SongListDO join SongXSongListDO WHERE  songId=:songId")
    suspend fun loadBySong(songId: Long): List<SongListDO>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(songListDO: SongListDO): Long

    @Query("SELECT * FROM SongListDO WHERE  source!=:source")
    suspend fun loadBySourceExclude(source: DataSource): List<SongListDO>

    @Delete
    suspend fun deleteAll(songListDOs: Collection<SongListDO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songListDOs: Collection<SongListDO>): List<Long>

    //以下为Repo涉及方法
    @Query("SELECT * FROM SongListDO")
    fun allSongList(): List<SongListDO?>?

    @Query("SELECT * FROM SongListDO WHERE remoteId=:songListRemoteId")
    fun findByRemoteId(songListRemoteId: Long?): SongListDO?

    @Query("SELECT * FROM SongListDO WHERE remoteId is not null")
    fun findAllDataOnWeb(): List<SongListDO?>?

    @Query("SELECT * FROM SongListDO WHERE remoteId is null")
    fun findAllDataOnLocal(): List<SongListDO?>?

    @Query("SELECT * FROM SongDO join SongXSongListDO WHERE songListId=:songListId")
    fun findSongsInSongList(songListId: Long): LiveData<List<SongDO>?>

    /** */
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