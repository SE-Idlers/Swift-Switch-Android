package com.example.win.easy.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.win.easy.db.SongDO;
import com.example.win.easy.db.SongListDO;
import com.example.win.easy.db.SongXSongListDO;

@Database(
        entities = {
                SongDO.class,
                SongListDO.class,
                SongXSongListDO.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters(value = {
        CustomTypeConverters.class
})
public abstract class OurDatabase extends RoomDatabase {
    public abstract SongDao songDao();
    public abstract SongListDao songListDao();
    public abstract SongXSongListDao songXSongListDao();
}
