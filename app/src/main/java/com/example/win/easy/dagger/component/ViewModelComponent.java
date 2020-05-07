package com.example.win.easy.dagger.component;

import androidx.lifecycle.ViewModelProvider;

import com.example.win.easy.dagger.module.ViewModelModule;
import com.example.win.easy.dagger.scope.ViewModelScope;
import com.example.win.easy.display.interfaces.DisplayService;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.recognization.interfaces.RecognitionService;
import com.example.win.easy.web.service.LoginService;

import java.util.List;

import dagger.Component;

@Component(dependencies = AppComponent.class,modules = ViewModelModule.class)
@ViewModelScope
public interface ViewModelComponent {

    ViewModelProvider.Factory getViewModelFactory();

    DisplayService getDisplayManager();

    FilterStrategy<List<Character>> getFilterStrategy();

    RecognitionService getRecognitionService();

    LoginService getLoginService();
}
