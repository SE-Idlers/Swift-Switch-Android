package com.example.win.easy.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;

import java.util.Collection;
import java.util.List;

@Dao
public interface SongXSongListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SongXSongList songXSongList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Collection<SongXSongList> songXSongLists);

    @Query("SELECT * " +
            "FROM songpojo INNER JOIN songxsonglist " +
            "ON songpojo.id=songxsonglist.songId " +
            "WHERE songxsonglist.songListId=:songListId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<SongPojo> findAllSongsForSongListById(long songListId);
//    LiveData<List<SongPojo>> findAllSongsForSongListById(long songListId);

    @Query("SELECT * " +
            "FROM songlistpojo INNER JOIN songxsonglist " +
            "ON songlistpojo.id=songxsonglist.songListId " +
            "WHERE songxsonglist.songId=:songId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<SongListPojo> findAllSongListsForSongById(long songId);
//    LiveData<List<SongListPojo>> findAllSongListsForSongById(long songId);


}
