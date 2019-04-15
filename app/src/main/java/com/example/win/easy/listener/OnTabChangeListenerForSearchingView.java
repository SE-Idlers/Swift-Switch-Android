package com.example.win.easy.listener;

import android.widget.TabHost;

public class OnTabChangeListenerForSearchingView implements TabHost.OnTabChangeListener {

    private TabHost tabHost;
    public OnTabChangeListenerForSearchingView(TabHost tabHost){this.tabHost=tabHost;}
    @Override
    public void onTabChanged(String tabId) {
        tabHost.setCurrentTabByTag(tabId);
    }
}
