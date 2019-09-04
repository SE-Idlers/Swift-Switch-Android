package com.example.win.easy.dagger.component;

import com.example.win.easy.view.activity.MainActivity;
import com.example.win.easy.dagger.scope.MainActivityScope;

import dagger.Component;

@Component(dependencies = FragmentComponent.class)
@MainActivityScope
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);

}
