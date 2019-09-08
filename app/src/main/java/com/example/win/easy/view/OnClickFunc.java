package com.example.win.easy.view;

import android.view.View;

public interface OnClickFunc<E> {

    void onclick(E entity,View thisView);
}
