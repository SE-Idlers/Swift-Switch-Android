package com.example.win.easy.dagger.component;

import com.example.win.easy.dagger.module.ListenerModule;
import com.example.win.easy.dagger.scope.DashboardScope;
import com.example.win.easy.view.lock.LockActivity;

import dagger.Component;

@Component(dependencies = FragmentComponent.class,modules = ListenerModule.class)
@DashboardScope
public interface DashboardComponent {

    void inject(LockActivity lockActivity);

}
