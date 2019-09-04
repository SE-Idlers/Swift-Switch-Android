package com.example.win.easy.dagger.component;

import androidx.fragment.app.FragmentFactory;

import com.example.win.easy.dagger.module.FragmentModule;
import com.example.win.easy.dagger.scope.FragmentScope;

import dagger.Component;

@Component(dependencies = ViewModelComponent.class,modules = FragmentModule.class)
@FragmentScope
public interface FragmentComponent {

    FragmentFactory getFragmentFactory();

}
