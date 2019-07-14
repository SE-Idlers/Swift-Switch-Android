package com.example.win.easy.dagger.module;

import android.content.Context;

import androidx.room.Room;

import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.database.OurDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    private final Context applicationContext;

    public RepositoryModule(Context applicationContext){
        this.applicationContext=applicationContext;
    }
    @Provides @Singleton
    OurDatabase provideOurDatabase(){
        return Room.databaseBuilder(applicationContext, OurDatabase.class,"ourDatabase").build();
    }

    @Provides @Singleton static SongPojoDao provideSongPojoDao(OurDatabase ourDatabase){
        return ourDatabase.songPojoDao();
    }

    @Provides @Singleton static SongListPojoDao provideSongListPojoDao(OurDatabase ourDatabase){
        return ourDatabase.songListPojoDao();
    }

    @Provides @Singleton static SongXSongListDao provideSongXSongListDao(OurDatabase ourDatabase){
        return ourDatabase.songXSongListDao();
    }

}
