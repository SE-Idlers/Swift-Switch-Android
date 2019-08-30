package com.example.win.easy.dagger.component;

import com.example.win.easy.dagger.module.DisplayModule;
import com.example.win.easy.dagger.module.DownloadModule;
import com.example.win.easy.dagger.module.NetworkModule;
import com.example.win.easy.dagger.module.ParserModule;
import com.example.win.easy.dagger.module.RepositoryModule;
import com.example.win.easy.dagger.module.ThreadModule;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.factory.__SongFactory;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.deprecated.repo.__SongListRepository;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.repository.deprecated.repo.__SongXSongListRepository;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ThreadModule.class,
        NetworkModule.class,
        DisplayModule.class,
        RepositoryModule.class,
        ParserModule.class,
        DownloadModule.class
})
@Singleton
public interface AppComponent {

    DisplayManager getDisplayManager();

    __SongRepository getSongRepository();

    __SongListRepository getSongListRepository();

    __SongXSongListRepository getSongXSongListRepository();

    SongDao getSongPojoDao();

    __SongFactory getSongFactory();

    FilterStrategy<List<Character>> getFilterStrategy();

    @Named("dbAccess") Executor diskIO();
    @Named("mainThread") Executor mainThread();

}
