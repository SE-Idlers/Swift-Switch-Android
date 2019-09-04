package com.example.win.easy.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Provider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap;

    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap){
        this.providerMap=providerMap;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) providerMap.get(modelClass).get();
    }
}
