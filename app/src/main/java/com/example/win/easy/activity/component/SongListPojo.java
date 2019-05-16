package com.example.win.easy.activity.component;

/*
 *模拟SongListPoj数据结构
 */

import android.graphics.drawable.Drawable;


public class SongListPojo {
    String name;
    int times;
    String source;
    Drawable pic;
    String path;

    private boolean readPicFromPath(){return false;}

    public SongListPojo(){
        name="";
        times=0;
        source="";
        pic=null;
    }

    public SongListPojo(String name,int times,String source,String path){
        this.name=name;
        this.times=times;
        this.source=source;
        this.path=path;
        this.pic=null;
    }
    public String getName(){return name;}
    public int getTimes(){return times;}
    public String getSource(){return source;}
    public Drawable getPic(){return pic;}

    public void setTimes(int t){times=t;}
    public void setPic(Drawable d){pic=d;}
}
