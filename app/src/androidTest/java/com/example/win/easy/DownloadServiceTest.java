package com.example.win.easy;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.win.easy.download.DownloadService;
import com.example.win.easy.download.FileService;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.callback.OnReadyFunc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static com.example.win.easy.enumeration.DataSource.WangYiYun;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DownloadServiceTest {
    private ThreadPoolExecutor poolExecutor;

    private DownloadService downloadService;

    private FileService fileService;

    private MediaPlayer mp ;

    @Before
    public void prepare() throws  Exception{
       poolExecutor = new ThreadPoolExecutor(1, 2, 1000, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
       fileService = new FileService();
       downloadService = new DownloadService(poolExecutor,fileService);
        mp = new MediaPlayer();
    }


    @Test
    public void TestDownloadMusic(){
        System.out.println("Testing: Music Downloading......");
        SongDO songDO = new SongDO();
        songDO.uid = 1111L;
        songDO.name = "七子之歌";
        songDO.author = "fake";
        songDO.source = WangYiYun;
        //歌曲的获取方式为：把歌曲播放页的url中id提取并替换到以下链接的id后，则可以直接下载
        songDO.songUrl = "http://music.163.com/song/media/outer/url?id=33337258.mp3";
        songDO.avatarUrl = "https://p3fx.kgimg.com/stdmusic/20160908/20160908071335142033.jpg";

        downloadService.download(songDO, new OnReadyFunc<SongDO>() {
            @Override
            public void onReady(SongDO songDO) {
                Log.e("Flag", "Music Downloaded");
                try{
                    mp.reset();
                    mp.setDataSource(songDO.songPath);
                    mp.prepare();
                    mp.start();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        downloadService.download(songDO, new OnReadyFunc<SongDO>() {
            @Override
            public void onReady(SongDO songDO) {
                Log.e("Flag", "Music Downloaded");
                try{
                    mp.reset();
                    mp.setDataSource(songDO.songPath);
                    mp.prepare();
                    mp.start();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Test
    public void TestDownloadSongList(){
        System.out.println("Testing: Music Downloading......");
        SongListDO songListDO = new SongListDO();
        songListDO.uid = 222221L;
        songListDO.name = "经典1";
        songListDO.source = WangYiYun;
        songListDO.avatarUrl = "https://p3fx.kgimg.com/stdmusic/20160908/20160908071335142033.jpg";

        downloadService.download(songListDO, new OnReadyFunc<SongListDO>() {
            @Override
            public void onReady(SongListDO songListDO) {
                Log.e("Flag", "SongList Downloaded");
            }
        });

    }

}
