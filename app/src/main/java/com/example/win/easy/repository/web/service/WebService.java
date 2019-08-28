package com.example.win.easy.repository.web.service;

import com.example.win.easy.repository.web.callback.OnReadyFunc;

public abstract class WebService<T> {

    abstract void get(OnReadyFunc<T> onReadyFunc);

}
