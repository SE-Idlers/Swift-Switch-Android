package com.example.win.easy.dagger.module

import android.content.Context
import androidx.room.Room
import com.example.win.easy.repository.RelationRepository
import com.example.win.easy.repository.SongListDto
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.SongRepository
import com.example.win.easy.repository.db.dao.SongDao
import com.example.win.easy.repository.db.dao.SongListDao
import com.example.win.easy.repository.db.dao.SongXSongListDao
import com.example.win.easy.repository.db.database.OurDatabase
import com.example.win.easy.web.service.LoginService
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
    fun provideSongRepository(songDao: SongDao): SongRepository = SongRepository(songDao)


    @Provides
    @Singleton
    fun provideSongListRepository(songListDto: SongListDto,
                                  songListDao: SongListDao,
                                  loginService: LoginService): SongListRepository = SongListRepository(songListDto, songListDao, loginService)

    @Provides
    @Singleton
    fun provideRelationRepository(relationDao: SongXSongListDao): RelationRepository = RelationRepository(relationDao)
}