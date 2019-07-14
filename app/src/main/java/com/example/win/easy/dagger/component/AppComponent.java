package com.example.win.easy.dagger.component;

import com.example.win.easy.dagger.module.DisplayModule;
import com.example.win.easy.dagger.module.DownloadModule;
import com.example.win.easy.dagger.module.NetworkModule;
import com.example.win.easy.dagger.module.ParserModule;
import com.example.win.easy.dagger.module.RepositoryModule;
import com.example.win.easy.dagger.module.ThreadModule;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.factory.SongFactory;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.repo.SongListRepository;
import com.example.win.easy.repository.repo.SongRepository;
import com.example.win.easy.repository.repo.SongXSongListRepository;

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

    SongRepository getSongRepository();

    SongListRepository getSongListRepository();

    SongXSongListRepository getSongXSongListRepository();

    SongPojoDao getSongPojoDao();

    SongFactory getSongFactory();

    FilterStrategy<List<Character>> getFilterStrategy();

    @Named("dbAccess") Executor diskIO();
    @Named("mainThread") Executor mainThread();

}
