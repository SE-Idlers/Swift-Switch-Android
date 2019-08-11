package com.example.win.easy.repository.db.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.win.easy.repository.db.CustomTypeConverters;
import com.example.win.easy.repository.db.dao.IInformationDao;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.IInformation;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;

@Database(
        entities = {
                SongDO.class,
                SongListDO.class,
                SongXSongListDO.class,
                IInformation.class
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
    public abstract IInformationDao iInformationDao();
}
