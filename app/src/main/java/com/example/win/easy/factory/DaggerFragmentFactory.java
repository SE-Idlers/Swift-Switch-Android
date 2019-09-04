package com.example.win.easy.factory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import java.util.Map;

import javax.inject.Provider;

public class DaggerFragmentFactory extends FragmentFactory {

    private Map<Class<? extends Fragment>, Provider<Fragment>> providerMap;

    public DaggerFragmentFactory(Map<Class<? extends Fragment>, Provider<Fragment>> providerMap){
        this.providerMap=providerMap;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        Class<? extends Fragment> fragmentClass=loadFragmentClass(classLoader,className);
        return providerMap.get(fragmentClass).get();
    }
}
