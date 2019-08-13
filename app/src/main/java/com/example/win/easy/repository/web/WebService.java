package com.example.win.easy.repository.web;

public abstract class WebService<T> {

    abstract void get(OnReadyFunc<T> onReadyFunc);

}
