package com.example.win.easy.repository.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;

import java.util.Collection;
import java.util.List;

@Dao
public interface SongXSongListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SongXSongListDO songXSongListDO);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Collection<SongXSongListDO> songXSongListDOs);

    @Query("SELECT * FROM SongXSongListDO")
    LiveData<List<SongXSongListDO>> findAllSongXSongLists();

    @Query("SELECT * FROM SongXSongListDO")
    List<SongXSongListDO> findAllRelation();

    @Query("SELECT * " +
            "FROM SongDO INNER JOIN SongXSongListDO " +
            "ON SongDO.id=SongXSongListDO.songId " +
            "WHERE SongXSongListDO.songListId=:songListId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<SongDO> findAllSongsDataForSongListById(Long songListId);
//    LiveData<List<SongDO>> findAllSongsDataForSongListById(Long songListId);

    @Query("SELECT * " +
            "FROM SongDO INNER JOIN SongXSongListDO " +
            "ON SongDO.id=SongXSongListDO.songId " +
            "WHERE SongXSongListDO.songListId=:songListId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    LiveData<List<SongDO>> findAllSongsForSongListById(Long songListId);

    @Query("SELECT * " +
            "FROM SongListDO INNER JOIN SongXSongListDO " +
            "ON SongListDO.id=SongXSongListDO.songListId " +
            "WHERE SongXSongListDO.songId=:songId")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<SongListDO> findAllSongListsForSongById(Long songId);
//    LiveData<List<SongListDO>> findAllSongListsForSongById(Long songId);

    @Query("SELECT * FROM SongXSongListDO " +
            "WHERE SongXSongListDO.songId=:songId " +
            "AND SongXSongListDO.songListId=:songListId")
    LiveData<SongXSongListDO> findbyID(Long songId, Long songListId);

    @Query("SELECT songId,songListId FROM SongXSongListDO NATURAL JOIN SongDO " +
            "WHERE SongDO.remoteId != null")
    LiveData<List<SongXSongListDO>> findAllDataOnWeb();

    @Delete
    void delete(SongXSongListDO songXSongListDO);

}
