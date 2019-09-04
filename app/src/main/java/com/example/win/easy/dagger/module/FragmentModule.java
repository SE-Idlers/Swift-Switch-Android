package com.example.win.easy.dagger.module;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.win.easy.factory.DaggerFragmentFactory;
import com.example.win.easy.view.fragment.MainActivityFragment;
import com.example.win.easy.view.fragment.AllSongListsFragment;
import com.example.win.easy.view.fragment.AllSongsFragment;
import com.example.win.easy.factory.__SongFactory;

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
public class FragmentModule {

    @MapKey
    @Target(ElementType.METHOD)
    @Documented
    @interface FragmentKey{
        Class<? extends Fragment> value();
    }

    @Provides
    @IntoMap
    @FragmentKey(MainActivityFragment.class)
    static Fragment provideMainActivityFragment(ViewModelProvider.Factory factory, __SongFactory songFactory){
        return new MainActivityFragment(factory,songFactory);
    }

    @Provides
    @IntoMap
    @FragmentKey(AllSongsFragment.class)
    static Fragment provideAllSongsFragment(ViewModelProvider.Factory factory, __SongFactory songFactory){
        return new AllSongsFragment(factory,songFactory);
    }

    @Provides
    @IntoMap
    @FragmentKey(AllSongListsFragment.class)
    static Fragment provideAllSongListsFragment(ViewModelProvider.Factory factory){
        return new AllSongListsFragment(factory);
    }

    @Provides
    @IntoMap
    @FragmentKey(NavHostFragment.class)
    static Fragment provideNavHostFragment(){
        return new NavHostFragment();
    }

    @Provides
    static FragmentFactory provideFragmentFactory(Map<Class<? extends Fragment>, Provider<Fragment>> providerMap){
        return new DaggerFragmentFactory(providerMap);
    }
}
