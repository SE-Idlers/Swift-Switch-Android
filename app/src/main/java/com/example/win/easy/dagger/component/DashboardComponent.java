package com.example.win.easy.dagger.component;

import com.example.win.easy.view.activity.LockActivity;
import com.example.win.easy.dagger.module.ListenerModule;
import com.example.win.easy.dagger.scope.DashboardScope;

import dagger.Component;

@Component(dependencies = ViewModelComponent.class,modules = ListenerModule.class)
@DashboardScope
public interface DashboardComponent {

    void inject(LockActivity lockActivity);

}
