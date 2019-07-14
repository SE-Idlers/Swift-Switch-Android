package com.example.win.easy.application;

import android.app.Application;

import com.example.win.easy.dagger.component.AppComponent;
import com.example.win.easy.dagger.component.DaggerAppComponent;
import com.example.win.easy.dagger.component.DaggerViewModelComponent;
import com.example.win.easy.dagger.component.ViewModelComponent;
import com.example.win.easy.dagger.module.RepositoryModule;

public class SwiftSwitchApplication extends Application {

    AppComponent appComponent;
    ViewModelComponent viewModelComponent;

    public static SwiftSwitchApplication application;
    @Override
    public void onCreate(){
        super.onCreate();
        application=this;
        appComponent= DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public ViewModelComponent getViewModelComponent(){
        if (viewModelComponent==null){
            viewModelComponent= DaggerViewModelComponent.builder()
                    .appComponent(appComponent)
                    .build();
        }
        return viewModelComponent;
    }
}
