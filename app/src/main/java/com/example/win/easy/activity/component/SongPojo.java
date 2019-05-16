package com.example.win.easy.activity.component;

/*
 *模拟SongPoj数据结构
 */

public class SongPojo {

    String name;
    int times;
    int order;
    String singer;
    String album;
    boolean downloaded;

    private boolean readPicFromPath(){return false;}


    public SongPojo(){
        name="";
        times=0;
        order=-1;
        singer="";
        album="";
        downloaded=false;
    }

    public SongPojo(String name,int times,int order,String singer,String album,boolean downloaded){
        this.name=name;
        this.times=times;
        this.order=order;
        this.singer=singer;
        this.album=album;
        this.downloaded=downloaded;
    }
    public String getName(){return name;}
    public int getTimes(){return times;}
    public int getOrder(){return order;}
    public String getSinger(){return singer;}
    public String getAlbum(){return album;}
    public boolean getDownLoaded(){return downloaded;}

    public void setDownLoaded(boolean state){downloaded=state;}
    public void setTimes(int t){times=t;}
}
