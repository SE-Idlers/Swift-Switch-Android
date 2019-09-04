package com.example.win.easy.dagger.component;

import androidx.lifecycle.ViewModelProvider;

import com.example.win.easy.dagger.module.ViewModelModule;
import com.example.win.easy.dagger.scope.ViewModelScope;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.factory.__SongFactory;
import com.example.win.easy.parser.filter.FilterStrategy;

import java.util.List;

import dagger.Component;

@Component(dependencies = AppComponent.class,modules = ViewModelModule.class)
@ViewModelScope
public interface ViewModelComponent {

    ViewModelProvider.Factory getViewModelFactory();

    DisplayManager getDisplayManager();

    FilterStrategy<List<Character>> getFilterStrategy();

    __SongFactory getSongFactory();

}
