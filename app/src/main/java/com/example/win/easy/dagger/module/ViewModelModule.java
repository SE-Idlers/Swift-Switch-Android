package com.example.win.easy.dagger.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.win.easy.factory.ViewModelFactory;
import com.example.win.easy.repository.SongListRepository;
import com.example.win.easy.repository.deprecated.repo.__SongListRepository;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.repository.deprecated.repo.__SongXSongListRepository;
import com.example.win.easy.repository.repo.Repo;
import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.viewmodel.SimpleViewModel;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.example.win.easy.viewmodel.SongListViewModelImpl;
import com.example.win.easy.viewmodel.SongViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import kotlin.NotImplementedError;

@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Documented
    @MapKey
    @interface ViewModelKey{
        Class<? extends ViewModel> value();
    }

    @Provides
    @IntoMap
    @ViewModelKey(SimpleViewModel.class)
    static ViewModel simpleViewModel(__SongRepository songRepository,
                                     __SongListRepository songListRepository,
                                     __SongXSongListRepository songXSongListRepository){
        return new SimpleViewModel(songRepository,songListRepository,songXSongListRepository);
    }

    @Provides
    @IntoMap
    @ViewModelKey(SongViewModel.class)
    static ViewModel songViewModel(__SongRepository songRepository){
        return new SongViewModel(songRepository,new VOUtil());
    }

    @Provides
    @IntoMap
    @ViewModelKey(SongListViewModel.class)
    static ViewModel provideSongListViewModel(SongListRepository songListRepository,Repo repo, VOUtil voUtil){
        return new SongListViewModelImpl(songListRepository,repo,voUtil);
    }

    @Provides
    static ViewModelProvider.Factory provideViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap){
        return new ViewModelFactory(providerMap);
    }

}
