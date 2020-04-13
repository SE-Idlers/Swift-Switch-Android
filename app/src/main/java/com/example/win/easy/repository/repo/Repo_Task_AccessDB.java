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
import java.util.Map;

public class Repo_Task_AccessDB extends AsyncTask<Object, Object, Long> {
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

    Map<SongListDO,List<SongDO>> songMap;

    private MutableLiveData<List<SongXSongListDO>> AllRelation;

    public Repo_Task_AccessDB(SongDao songDao,
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
                              List<SongXSongListDO> songXSongListOnLocal,
                              Map<SongListDO,List<SongDO>> songMap,
                              MutableLiveData<List<SongXSongListDO>> AllRelation) {
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

        this.songMap = songMap;

        this.AllRelation = AllRelation;
    }

    @Override
    protected void onPostExecute(Long aLong) {

        AllSong.postValue(allSong);
        AllSongList.postValue(allSongList);
        AllRelation.postValue(allRelation);
    }

    private void beforeUpdate() {
        //fetch之前从新获取缓存数据
        allSong = songDao.getAllSong()==null?new ArrayList<>():songDao.getAllSong();
        allSongList = songListDao.allSongList() ==null?new ArrayList<>(): songListDao.allSongList();
        allRelation = songXSongListDao.getAllRelation()==null?new ArrayList<>():songXSongListDao.getAllRelation();
        songOnWeb = songDao.findAllDataOnWeb()==null?new ArrayList<>():songDao.findAllDataOnWeb();
        songListOnWeb = songListDao.findAllDataOnWeb()==null?new ArrayList<>():songListDao.findAllDataOnWeb();
        songXSongListOnWeb = songXSongListDao.findAllDataOnWeb()==null?new ArrayList<>():songXSongListDao.findAllDataOnWeb();
        songOnLocal = songDao.findAllDataOnLocal()==null?new ArrayList<>():songDao.findAllDataOnLocal();
        songListOnLocal = songListDao.findAllDataOnLocal()==null?new ArrayList<>():songListDao.findAllDataOnLocal();
        songXSongListOnLocal = songXSongListDao.findAllDataOnLocal()==null?new ArrayList<>():songXSongListDao.findAllDataOnLocal();
    }

    @Override
    protected Long doInBackground(Object... params){
        beforeUpdate();
        int i = 1;
        Iterator iterator = songMap.keySet().iterator();
        SongListDO songListDO;
        while (iterator.hasNext()) {
            songListDO = (SongListDO) iterator.next();
            boolean end_one = songMap.keySet().size() == i;
            List<SongDO> allSongDO = songMap.get(songListDO);//要在id更新语句之前取出来
            Long songListId = UpDaTeSoNgLiSt(songListDO, end_one == true);
            int j = 1;
            for (SongDO songDO : allSongDO) {
                boolean end_two = allSongDO.size() == j;
                Long songId = UpDaTeSoNg(songDO, end_one && end_two);
                UpDaTeSoNgXSoNgLiSt(
                        SongXSongListDO.builder().songId(songId).songListId(songListId).build(),
                        end_one && end_two);
                j++;
            }
            i++;
        }
        allSong = songDao.getAllSong()==null?new ArrayList<>():songDao.getAllSong();
        allSongList = songListDao.allSongList() ==null?new ArrayList<>(): songListDao.allSongList();
        allRelation = songXSongListDao.getAllRelation()==null
                ?new ArrayList<>()
                :songXSongListDao.getAllRelation();

        return null;
    }

    private Long UpDaTeSoNg(SongDO data, boolean isEnd) {
        Long songId = insertSong(data);
        //手动迭代
        int index = songOnWeb.size() - 1;
        for (SongDO hasIt; index >= 0; index--) {
            hasIt = songOnWeb.get(index);
            if (hasIt.remoteId.longValue() == data.remoteId.longValue()) {
                songOnWeb.remove(index);//remove可行性存疑
                break;
            }
        }
        if (isEnd == true && songOnWeb.isEmpty() == false) {
            for (SongDO item : songOnWeb) {
                deleteSong(item);
            }
        }
        return songId;
    }

    private Long UpDaTeSoNgLiSt(SongListDO data, boolean isEnd) {
        Long songListId = insertSongList(data);
        //手动迭代
        int index = songListOnWeb.size() - 1;
        for (SongListDO hasIt; index >= 0; index--) {
            hasIt = songListOnWeb.get(index);
            if (hasIt.remoteId.longValue() == data.remoteId.longValue()) {
                songListOnWeb.remove(index);
                break;
            }
        }
        if (isEnd == true && songListOnWeb.isEmpty() == false) {
            for (SongListDO item : songListOnWeb) {
                deleteSongList(item);
            }
        }
        return songListId;
    }

    private void UpDaTeSoNgXSoNgLiSt(SongXSongListDO data, boolean isEnd) {
        insertSongXSongList(data);
        int index = songXSongListOnWeb.size() - 1;
        for (SongXSongListDO hasIt; index >= 0; index--) {
            hasIt = songXSongListOnWeb.get(index);
            if (hasIt.songId.longValue() == data.songId.longValue()
                    && hasIt.songListId.longValue() == data.songListId.longValue()) {
                songXSongListOnWeb.remove(index);
                break;
            }
        }
        if (isEnd == true && songXSongListOnWeb.isEmpty() == false) {
            for (SongXSongListDO item : songXSongListOnWeb) {
                deleteSongXSongList(item);
            }
        }
    }

    private Long insertSong(SongDO data) {
        SongDO result = songDao.findByRemoteId(data.remoteId);
        if (result == null)
            return songDao.insert(data);
        else
            return result.id;
    }

    private void deleteSong(SongDO data) {
        SongDO result = songDao.findByRemoteId(data.remoteId);
        if (result != null)
            songDao.delete(data);
    }

    private Long insertSongList(SongListDO data) {
        SongListDO result = songListDao.findByRemoteId(data.remoteId);
        if (result == null)
            return songListDao.insert(data);
        else
            return result.id;
    }

    private void deleteSongList(SongListDO data) {
        SongListDO result = songListDao.findByRemoteId(data.remoteId);
        if (result != null)
            songListDao.delete(data);
    }

    private void insertSongXSongList(SongXSongListDO data) {
        SongXSongListDO result = songXSongListDao.findById(data.songId, data.songListId);
        if (result == null)
            songXSongListDao.insert(data);
    }

    private void deleteSongXSongList(SongXSongListDO data) {
        SongXSongListDO result = songXSongListDao.findById(data.songId, data.songListId);
        if (result != null)
            songXSongListDao.delete(data);
    }
}
