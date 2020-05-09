package com.example.win.easy.dagger.module;

import android.content.Context;

import androidx.room.Room;

import com.example.win.easy.repository.SongListDto;
import com.example.win.easy.repository.SongListRepository;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.web.service.LoginService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lombok.val;

@Module
public class RepositoryModule {

    private final Context applicationContext;

    public RepositoryModule(Context applicationContext){
        this.applicationContext=applicationContext;
    }
    @Provides @Singleton
    OurDatabase provideOurDatabase(){
        return Room.databaseBuilder(applicationContext, OurDatabase.class,"ourDatabase")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides @Singleton static SongDao provideSongPojoDao(OurDatabase ourDatabase){
        return ourDatabase.songDao();
    }

    @Provides @Singleton static SongListDao provideSongListPojoDao(OurDatabase ourDatabase){
        return ourDatabase.songListDao();
    }

    @Provides @Singleton static SongXSongListDao provideSongXSongListDao(OurDatabase ourDatabase){
        return ourDatabase.songXSongListDao();
    }

    @Provides@Singleton static SongListRepository provideSongListRepository(SongListDto songListDto,
                                                                            SongListDao songListDao,
                                                                            LoginService loginService){
        return new SongListRepository(songListDto,songListDao,loginService);
    }

}
