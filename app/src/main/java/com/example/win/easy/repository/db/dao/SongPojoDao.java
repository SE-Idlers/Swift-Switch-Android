package com.example.win.easy.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.win.easy.repository.db.pojo.SongPojo;

import java.util.List;

@Dao
public interface SongPojoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SongPojo songPojo);

    @Query("SELECT * FROM songpojo")
    List<SongPojo> findAllSongPojos();
//    LiveData<List<SongPojo>> findAllSongPojos();

    @Query("SELECT * FROM songpojo WHERE id=:songId")
    SongPojo findById(long songId);
//    LiveData<SongPojo> findById(long songId);

}
