package com.example.win.easy.repository.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.win.easy.repository.db.pojo.SongPojo;

import java.util.Collection;
import java.util.List;

@Dao
public interface SongPojoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SongPojo songPojo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Collection<SongPojo> songPojos);

    @Query("SELECT * FROM songpojo")
    LiveData<List<SongPojo>> findAllSongPojos();

    @Query("SELECT * FROM songpojo")
    List<SongPojo> findAllDataSongPojos();

    @Query("SELECT * FROM songpojo WHERE id=:songId")
//    SongPojo findById(long songId);
    LiveData<SongPojo> findById(long songId);

    @Query("SELECT * FROM songpojo WHERE id=:songId")
    SongPojo findDataById(long songId);


    @Query("SELECT * FROM songpojo WHERE source=:dataSource")
//    List<SongPojo> findByDataSource(String dataSource);
    LiveData<List<SongPojo>> findByDataSource(String dataSource);

    @Query("SELECT * FROM songpojo WHERE source!=:dataSource")
    List<SongPojo> findAllSongsExceptByDataSource(String dataSource);

    @Query("SELECT * FROM songpojo WHERE name=:name AND author=:author AND source=:source AND uid=:uid AND remoteId=:remoteId")
    SongPojo findLocalRecordOfNetworkSong(String name,String author,String source,String uid,String remoteId);

    @Query("SELECT * FROM songpojo WHERE songPath=:songPath")
    SongPojo findAllBySongPath(String songPath);

    @Delete
    void delete(SongPojo songPojo);

    @Delete
    void delete(Collection<SongPojo> songPojos);

    @Update
    void update(SongPojo songPojo);

    @Update
    void update(Collection<SongPojo> songPojos);
}
