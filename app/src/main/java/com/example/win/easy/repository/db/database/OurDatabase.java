package com.example.win.easy.repository.db.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.win.easy.repository.db.CustomTypeConverters;
import com.example.win.easy.repository.db.dao.IInformationDao;
import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.pojo.IInformation;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;

@Database(
        entities = {
                SongPojo.class,
                SongListPojo.class,
                SongXSongList.class,
                IInformation.class
        },
        version = 1
)
@TypeConverters(value = {
        CustomTypeConverters.class
})
public abstract class OurDatabase extends RoomDatabase {
    public abstract SongPojoDao songPojoDao();
    public abstract SongListPojoDao songListPojoDao();
    public abstract SongXSongListDao songXSongListDao();
    public abstract IInformationDao iInformationDao();
}
