package com.example.win.easy.view.interfaces;

import com.example.win.easy.song.Song;

/**
 * 搜索完毕，选择某一首歌曲以后，将由该接口负责将选择的歌曲所在的歌单呈现出来
 */
public interface SongListView {

    /**
     * 将展示内容由先前的搜索结果变为选择的歌曲所在的所有歌单，替换展示内容的同时更新屏幕监听
     * @param song 选择的歌曲
     */
    void update(Song song);

}
