package com.example.win.easy.dagger.module;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.win.easy.display.interfaces.DisplayService;
import com.example.win.easy.recognization.interfaces.RecognitionService;
import com.example.win.easy.view.lock.DisplayFragment;
import com.example.win.easy.view.lock.HandWritingFragment;
import com.example.win.easy.view.lock.SearchFragment;
import com.example.win.easy.factory.DaggerFragmentFactory;
import com.example.win.easy.view.ImageService;
import com.example.win.easy.view.main.AddSongToSongListFragment;
import com.example.win.easy.view.main.AllSongListsFragment;
import com.example.win.easy.view.main.AllSongsFragment;
import com.example.win.easy.view.main.LoginFragment;
import com.example.win.easy.view.main.MainActivityFragment;
import com.example.win.easy.view.main.SongListCreationFragment;
import com.example.win.easy.view.main.SongListFragment;
import com.example.win.easy.web.service.LoginService;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import static com.example.win.easy.enumeration.LoginType.Phone;

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
    @FragmentKey(NavHostFragment.class)
    static Fragment provideNavHostFragment(){
        return new NavHostFragment();
    }

    @Provides
    @IntoMap
    @FragmentKey(MainActivityFragment.class)
    static Fragment provideMainActivityFragment(LoginService loginService){
        return new MainActivityFragment(loginService);
    }

    @Provides
    @IntoMap
    @FragmentKey(AllSongsFragment.class)
    static Fragment provideAllSongsFragment(DisplayService displayService,ViewModelProvider.Factory factory){
        return new AllSongsFragment(displayService,null,null,factory);
    }

    @Provides
    @IntoMap
    @FragmentKey(AllSongListsFragment.class)
    static Fragment provideAllSongListsFragment(ViewModelProvider.Factory factory){
        return new AllSongListsFragment(factory,new ImageService());
    }

    @Provides
    @IntoMap
    @FragmentKey(SongListFragment.class)
    static Fragment provideSongListFragment(ViewModelProvider.Factory factory){
        return new SongListFragment(null,null,factory);
    }

    @Provides
    @IntoMap
    @FragmentKey(AddSongToSongListFragment.class)
    static Fragment provideAddSongToSongListFragment(ViewModelProvider.Factory viewModelFactory){
        return new AddSongToSongListFragment(viewModelFactory);
    }

    @Provides
    @IntoMap
    @FragmentKey(LoginFragment.class)
    static Fragment provideLoginFragment(LoginService loginService){
        return new LoginFragment(loginService,Phone);
    }

    @Provides
    @IntoMap
    @FragmentKey(SongListCreationFragment.class)
    static Fragment provideSongListCreationFragment(ViewModelProvider.Factory viewModelFactory){
        return new SongListCreationFragment(viewModelFactory);
    }

    @Provides
    @IntoMap
    @FragmentKey(SearchFragment.class)
    static Fragment provideSearchFragment(ViewModelProvider.Factory viewModelFactory){
        return new SearchFragment(viewModelFactory,null);
    }

    @Provides
    @IntoMap
    @FragmentKey(HandWritingFragment.class)
    static Fragment provideHandWritingFragment(RecognitionService recognitionService){
        return new HandWritingFragment(recognitionService);
    }

    @Provides
    @IntoMap
    @FragmentKey(DisplayFragment.class)
    static Fragment provideDisplayFragment(DisplayService displayService){
        return new DisplayFragment(displayService);
    }

    @Provides
    static FragmentFactory provideFragmentFactory(Map<Class<? extends Fragment>, Provider<Fragment>> providerMap){
        return new DaggerFragmentFactory(providerMap);
    }
}
