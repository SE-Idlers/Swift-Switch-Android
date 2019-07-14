package com.example.win.easy.dagger.component;

import androidx.lifecycle.ViewModelProvider;

import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.activity.fragment.AllSongListsFragment;
import com.example.win.easy.activity.fragment.AllSongsFragment;
import com.example.win.easy.dagger.module.ViewModelModule;
import com.example.win.easy.dagger.scope.ViewModelScope;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.parser.filter.FilterStrategy;

import java.util.List;

import dagger.Component;

@Component(dependencies = AppComponent.class,modules = ViewModelModule.class)
@ViewModelScope
public interface ViewModelComponent {

    ViewModelProvider.Factory getViewModelFactory();

    DisplayManager getDisplayManager();

    FilterStrategy<List<Character>> getFilterStrategy();

    void inject(MainActivity mainActivity);
    void inject(AllSongsFragment allSongsFragment);
    void inject(AllSongListsFragment allSongListsFragment);

}
