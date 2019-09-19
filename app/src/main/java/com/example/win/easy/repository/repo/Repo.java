package com.example.win.easy.repository.repo;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.web.service.SongListWebService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repo {
    protected SongDao songDao;
    protected SongListDao songListDao;
    protected SongXSongListDao songXSongListDao;
    protected SongListWebService songListWebService;
    protected ExecutorService executorService; // 处理多次fetch
    protected Executor executor;

    protected MutableLiveData<List<SongDO>> AllSong;
    protected MutableLiveData<List<SongListDO>> AllSongList;

    protected List<SongDO> allSong;
    protected List<SongListDO> allSongList;
    protected List<SongXSongListDO> allRelation;
    protected List<SongDO> songOnWeb;
    protected List<SongListDO> songListOnWeb;
    protected List<SongXSongListDO> songXSongListOnWeb;
    protected List<SongDO> songOnLocal;
    protected List<SongListDO> songListOnLocal;
    protected List<SongXSongListDO> songXSongListOnLocal;

    protected List<SongDO> songOf;
    protected List<SongDO> songNotIn;

    public Repo(SongDao songDao,
                SongListDao songListDao,
                SongXSongListDao songXSongListDao,
                SongListWebService songListWebService) {
        this.songDao = songDao;
        this.songListDao = songListDao;
        this.songXSongListDao = songXSongListDao;
        this.songListWebService = songListWebService;

        executorService = Executors.newSingleThreadExecutor();//使用单线程化线程池，按照制定顺序执行Repo_task
        AllSong = new MutableLiveData<>();
        AllSongList = new MutableLiveData<>();

        //在构造函数就把所有数据缓存好
        songOf = new ArrayList<>();
        songNotIn = new ArrayList<>();

        executor = new Executor() { //测试用
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        //这里已经避免List为null了，后续应该检测List时候isEmpty
    }

    private AsyncTask Make_A_fetch_Task(){
        return new Repo_AsyncTask(this.songDao,this.songListDao,this.songXSongListDao,
                this.songListWebService, this.AllSong, this.AllSongList, this.allSong, this.allSongList,
                this.allRelation, this.songOnWeb, this.songListOnWeb, this.songXSongListOnWeb,
                this.songOnLocal, this.songListOnLocal, this.songXSongListOnLocal);
    }

    public LiveData<List<SongDO>> getAllSong() {
        Make_A_fetch_Task().executeOnExecutor(executorService);
        return AllSong;
    }

    public LiveData<List<SongListDO>> getAllSongList() {
        Make_A_fetch_Task().executeOnExecutor(executorService);
        return AllSongList;
    }

    public List<SongDO> songsOf(SongListDO songList) {
        songOf = songXSongListDao.getSongsOf(songList.id);
        return songOf;
    }

    public List<SongDO> getSongNotIn(SongListDO songList) {
        songOf = songXSongListDao.getSongsOf(songList.id);
        songNotIn = new ArrayList<>();
        for(SongDO item_1: allSong){
            boolean flag = true;
            for(SongDO item_2: songOf){
                if(item_1.id.longValue() == item_2.id.longValue()){
                    flag = false;
                    break;
                }
            }
            if(flag)
                songNotIn.add(item_1);
        }
        return songNotIn;
    }

    //TODO:更新
    //TODO:get方法
}
