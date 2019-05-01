package com.example.win.easy.view.interfaces;

import com.example.win.easy.songList.SongList;

import java.util.List;

/**
 * 管理视图接口，用于呈现搜索结果
 */
public interface SearchingView {

    /**
     * 将所有的搜索结果呈现出来，管理显示的内容以及其相应的监听等等
     * @param sortedIndices 所有搜索结果的下标列表
     */
    void update(List<Integer> sortedIndices );

    SongList getPrincipal();

    void setPrincipal(SongList principal);

}
