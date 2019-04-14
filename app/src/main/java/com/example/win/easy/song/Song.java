package com.example.win.easy.song;

/**
 * 歌曲的各种信息及其set和get函数
 */

import android.support.annotation.VisibleForTesting;

import com.example.win.easy.feature.InternalInformation;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Song {

    private String name;

    private String author;

    private List<Character> sequence;

    DataSource source;

    InternalInformation internalInformation;

    public Song() {}

    public void setName(String nm){
        name = nm;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String ath){
        author = ath;
    }
    public String getAuthor() {
        return author;
    }

    public void setSequence(List<Character> songinf){
        sequence = songinf;
    }

    public List<Character> getSequence()
    {
        return sequence;
    }

}
