package com.example.win.easy.persistence;

public interface ConfigurationPersistence<T>{

    void save(T entity);

    T load();

}
