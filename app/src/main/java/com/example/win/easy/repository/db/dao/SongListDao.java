package com.example.win.easy.repository.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.win.easy.repository.db.data_object.SongListDO;

import java.util.Collection;
import java.util.List;

@Dao
public interface SongListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(SongListDO songListDO);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insert(Collection<SongListDO> songListDOs);

    @Query("SELECT * FROM SongListDO")
    List<SongListDO> findAllDataSongListDOs();

    @Query("SELECT * FROM SongListDO")
    LiveData<List<SongListDO>> findAllSongListDOs();

    @Query("SELECT * FROM SongListDO WHERE id=:songListId")
//    SongListDO findById(Long songListId);
    LiveData<SongListDO> findById(Long songListId);

    @Query("SELECT * FROM SongListDO WHERE id=:songListRemoteId")
    LiveData<SongListDO> findByRemoteId(Long songListRemoteId);

    @Query("SELECT * FROM SongListDO WHERE remoteId is not null")
    LiveData<List<SongListDO>> findAllDataOnWeb();

    @Query("SELECT * FROM SongListDO WHERE source=:dataSource")
    List<SongListDO> findByDataSource(String dataSource);
//    LiveData<List<SongListDO>> findByDataSource(String dataSource);

    @Query("SELECT * FROM SongListDO WHERE name=:name AND source=:source AND uid=:uid AND remoteId=:remoteId")
    SongListDO findLocalRecordOfNetworkSongList(String name, String source, String uid, String remoteId);

    @Query("SELECT * FROM SongListDO WHERE name=:name AND source=:source")
    List<SongListDO> findAllByNameAndSource(String name, String source);

    @Delete
    void delete(SongListDO songListDO);

    @Delete
    void delete(Collection<SongListDO> songListDOs);

    @Update
    void update(SongListDO songListDO);

    @Update
    void update(Collection<SongListDO> songListDOs);
}
