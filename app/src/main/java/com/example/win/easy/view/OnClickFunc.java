package com.example.win.easy.view;

import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

public interface OnClickFunc<E> {

    void onclick(E entity, QMUICommonListItemView thisItemView);
}
