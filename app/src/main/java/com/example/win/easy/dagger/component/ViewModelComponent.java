package com.example.win.easy.dagger.component;

import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.activity.fragment.AllSongListsFragment;
import com.example.win.easy.activity.fragment.AllSongsFragment;
import com.example.win.easy.dagger.module.ViewModelModule;
import com.example.win.easy.dagger.scope.ViewModelScope;

import dagger.Component;

@Component(dependencies = AppComponent.class,modules = ViewModelModule.class)
@ViewModelScope
public interface ViewModelComponent {

    void inject(MainActivity mainActivity);
    void inject(AllSongsFragment allSongsFragment);
    void inject(AllSongListsFragment allSongListsFragment);
}
