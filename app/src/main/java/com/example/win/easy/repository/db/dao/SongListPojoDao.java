package com.example.win.easy.repository.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.win.easy.repository.db.pojo.SongListPojo;

import java.util.Collection;
import java.util.List;

@Dao
public interface SongListPojoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SongListPojo songListPojo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Collection<SongListPojo> songListPojos);

    @Query("SELECT * FROM songlistpojo")
//    List<SongListPojo> findAllSongListPojos();
    LiveData<List<SongListPojo>> findAllSongListPojos();

    @Query("SELECT * FROM songlistpojo WHERE id=:songListId")
//    SongListPojo findById(long songListId);
    LiveData<SongListPojo> findById(long songListId);

    @Query("SELECT * FROM songlistpojo WHERE source=:dataSource")
    List<SongListPojo> findByDataSource(String dataSource);
//    LiveData<List<SongListPojo>> findByDataSource(String dataSource);

    @Query("SELECT * FROM songlistpojo WHERE name=:name AND source=:source AND uid=:uid AND remoteId=:remoteId")
    SongListPojo findLocalRecordOfNetworkSongList(String name,String source,String uid,String remoteId);

    @Delete
    void delete(SongListPojo songListPojo);

    @Delete
    void delete(Collection<SongListPojo> songListPojos);

    @Update
    void update(SongListPojo songListPojo);

    @Update
    void update(Collection<SongListPojo> songListPojos);
}
