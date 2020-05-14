package com.example.win.easy.dagger.module

import android.content.Context
import androidx.room.Room
import com.example.win.easy.repository.*
import com.example.win.easy.dao.SongDao
import com.example.win.easy.dao.SongListDao
import com.example.win.easy.dao.SongXSongListDao
import com.example.win.easy.dao.OurDatabase
import com.example.win.easy.dto.SongDto
import com.example.win.easy.dto.SongListDto
import com.example.win.easy.network.LoginService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun provideOurDatabase(): OurDatabase =
            Room.databaseBuilder(applicationContext, OurDatabase::class.java, "ourDatabase")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideSongDao(ourDatabase: OurDatabase): SongDao = ourDatabase.songDao()

    @Provides
    @Singleton
    fun provideSongListDao(ourDatabase: OurDatabase): SongListDao = ourDatabase.songListDao()

    @Provides
    @Singleton
    fun provideSongXSongListDao(ourDatabase: OurDatabase): SongXSongListDao = ourDatabase.songXSongListDao()

    @Provides
    @Singleton
    fun provideSongRepository(songDao: SongDao,relationDao: SongXSongListDao): SongRepository = SongRepository(songDao,relationDao)


    @Provides
    @Singleton
    fun provideSongListRepository(songListDto: SongListDto,
                                  songDto: SongDto,
                                  songListDao: SongListDao,
                                  loginService: LoginService): SongListRepository = SongListRepository(songListDto, songDto, songListDao, loginService)

    @Provides
    @Singleton
    fun provideRelationRepository(relationDao: SongXSongListDao): RelationRepository = RelationRepository(relationDao)
}