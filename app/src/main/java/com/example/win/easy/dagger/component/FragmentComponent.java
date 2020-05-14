package com.example.win.easy.dagger.component;

import androidx.fragment.app.FragmentFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.win.easy.dagger.module.FragmentModule;
import com.example.win.easy.dagger.scope.FragmentScope;
import com.example.win.easy.display.DisplayService;
import com.example.win.easy.network.LoginService;

import dagger.Component;

@Component(dependencies = ViewModelComponent.class,modules = FragmentModule.class)
@FragmentScope
public interface FragmentComponent {

    FragmentFactory getFragmentFactory();

    ViewModelProvider.Factory getViewModelFactory();

    DisplayService getDisplayManager();

    LoginService getLoginService();
}
