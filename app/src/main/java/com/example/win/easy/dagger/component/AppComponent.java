package com.example.win.easy.dagger.component;

import com.example.win.easy.dagger.module.BackendRequestModule;
import com.example.win.easy.dagger.module.RecognitionModule;
import com.example.win.easy.dagger.module.UtilModule;
import com.example.win.easy.dagger.module.DisplayModule;
import com.example.win.easy.dagger.module.DownloadModule;
import com.example.win.easy.dagger.module.LoginModule;
import com.example.win.easy.dagger.module.ParserModule;
import com.example.win.easy.dagger.module.RepositoryModule;
import com.example.win.easy.dagger.module.ThreadModule;
import com.example.win.easy.dagger.module.WebSongListModule;
import com.example.win.easy.display.interfaces.DisplayService;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.recognization.interfaces.RecognitionService;
import com.example.win.easy.repository.SongListRepository;
import com.example.win.easy.web.DTOUtil;
import com.example.win.easy.web.service.LoginService;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ThreadModule.class,
        BackendRequestModule.class,
        LoginModule.class,
        UtilModule.class,
        WebSongListModule.class,
        DisplayModule.class,
        RepositoryModule.class,
        ParserModule.class,
        DownloadModule.class,
        RecognitionModule.class
})
@Singleton
public interface AppComponent {

    DisplayService getDisplayManager();

    SongListRepository getSongListRepository();

    DTOUtil getDTOUtil();

    RecognitionService getRecognitionService();

    FilterStrategy<List<Character>> getFilterStrategy();

    LoginService getLoginService();



    @Named("dbAccess") Executor diskIO();
    @Named("mainThread") Executor mainThread();

}
