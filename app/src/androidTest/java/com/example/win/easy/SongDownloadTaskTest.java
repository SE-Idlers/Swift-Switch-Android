package com.example.win.easy;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.task.SongDownloadTask;
import com.example.win.easy.repository.web.domain.NetworkSong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SongDownloadTaskTest {

    private SongPojoDao songPojoDao;
    private NetworkSong networkSong;
    private SongPojo songPojo;
    private Executor executor;
    @Before
    public void prepare(){
        Context context= ApplicationProvider.getApplicationContext();
        OurDatabase ourDatabase= Room.inMemoryDatabaseBuilder(context,OurDatabase.class).build();
        songPojoDao=ourDatabase.songPojoDao();
        networkSong=NetworkSong.builder()
                .songUrl("http://m10.music.126.net/20190512175905/47bc363d8b12b5b6dbc95fb366ecb0ec/ymusic/d8ce/08c3/986c/844405c5672efe9b10076bab25d7bce2.mp3")
                .author("陈奕迅")
                .avatarUrl(null)
                .extensionName(".mp3")
                .name("最佳损友")
                .totalName("陈奕迅 - 最佳损友")
                .source(DataSource.WangYiYun)
                .uid("1846879130")
                .remoteId("23333333")
                .build();
        songPojo=new SongPojo(networkSong);
        executor= Executors.newSingleThreadExecutor();
    }

    @Test
    public void test() throws InterruptedException {
        long songId=songPojoDao.insert(songPojo);
        SongDownloadTask songDownloadTask=new SongDownloadTask(networkSong,songId);
        executor.execute(songDownloadTask);

        Thread.sleep(10000);
        SongPojo song=songPojoDao.findDataById(songId);
        System.out.println(song);
    }


}
