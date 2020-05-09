package com.example.win.easy.dagger.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.win.easy.factory.ViewModelFactory;
import com.example.win.easy.repository.SongListRepository;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.example.win.easy.viewmodel.SongListViewModelImpl;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

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
    @ViewModelKey(SongListViewModel.class)
    static ViewModel provideSongListViewModel(SongListRepository songListRepository){
        return new SongListViewModelImpl(songListRepository);
    }

    @Provides
    static ViewModelProvider.Factory provideViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap){
        return new ViewModelFactory(providerMap);
    }

}
