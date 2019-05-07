package com.example.win.easy.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.win.easy.repository.db.pojo.SongListPojo;

import java.util.List;

@Dao
public interface SongListPojoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SongListPojo songListPojo);

    @Query("SELECT * FROM songlistpojo")
    List<SongListPojo> findAllSongListPojos();
//    LiveData<List<SongListPojo>> findAllSongListPojos();

    @Query("SELECT * FROM songlistpojo WHERE id=:songListId")
    SongListPojo findById(long songListId);
//    LiveData<SongListPojo> findById(long songListId);

}
