package com.example.win.easy.repository.repo;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.web.service.SongListWebService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Repo_AsyncTask extends AsyncTask<Object, Object, Long> {
    private SongDao songDao;
    private SongListDao songListDao;
    private SongXSongListDao songXSongListDao;
    private SongListWebService songListWebService;

    private MutableLiveData<List<SongDO>> AllSong;
    private MutableLiveData<List<SongListDO>> AllSongList;

    private List<SongDO> allSong;
    private List<SongListDO> allSongList;
    private List<SongXSongListDO> allRelation;
    private List<SongDO> songOnWeb;
    private List<SongListDO> songListOnWeb;
    private List<SongXSongListDO> songXSongListOnWeb;
    private List<SongDO> songOnLocal;
    private List<SongListDO> songListOnLocal;
    private List<SongXSongListDO> songXSongListOnLocal;

    public Repo_AsyncTask(SongDao songDao,
                          SongListDao songListDao,
                          SongXSongListDao songXSongListDao,
                          SongListWebService songListWebService,
                          MutableLiveData<List<SongDO>> AllSong,
                          MutableLiveData<List<SongListDO>> AllSongList,
                          List<SongDO> allSong,
                          List<SongListDO> allSongList,
                          List<SongXSongListDO> allRelation,
                          List<SongDO> songOnWeb,
                          List<SongListDO> songListOnWeb,
                          List<SongXSongListDO> songXSongListOnWeb,
                          List<SongDO> songOnLocal,
                          List<SongListDO> songListOnLocal,
                          List<SongXSongListDO> songXSongListOnLocal) {
        this.songDao = songDao;
        this.songListDao = songListDao;
        this.songXSongListDao = songXSongListDao;
        this.songListWebService = songListWebService;

        this.AllSong = AllSong;
        this.AllSongList = AllSongList;
        this.allSong = allSong;
        this.allSongList = allSongList;
        this.allRelation = allRelation;
        this.songOnWeb = songOnWeb;
        this.songListOnWeb = songListOnWeb;
        this.songXSongListOnWeb = songXSongListOnWeb;
        this.songOnLocal = songOnLocal;
        this.songListOnLocal = songListOnLocal;
        this.songXSongListOnLocal = songXSongListOnLocal;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Long doInBackground(Object... params){
        fetch();
        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {

    }


    public void fetch() {
        songListWebService.getAllSongLists((songMap) -> {
            AsyncTask task = new Repo_Task_AccessDB(this.songDao, this.songListDao, this.songXSongListDao,
                    this.songListWebService, this.AllSong, this.AllSongList, this.allSong, this.allSongList,
                    this.allRelation, this.songOnWeb, this.songListOnWeb, this.songXSongListOnWeb,
                    this.songOnLocal, this.songListOnLocal, this.songXSongListOnLocal,
                    songMap);
            task.execute();
        });
    }
}
