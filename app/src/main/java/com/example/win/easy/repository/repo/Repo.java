package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.tool.SongList;
import com.example.win.easy.web.service.SongListWebService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class Repo {
    private SongDao songDao;
    private SongListDao songListDao;
    private SongXSongListDao songXSongListDao;
    private MutableLiveData<List<SongDO>> songOnWeb;
    private MutableLiveData<List<SongListDO>> songListOnWeb;
    private MutableLiveData<List<SongXSongListDO>> songXSongListOnWeb;
    private MutableLiveData<List<SongDO>> allSong;
    private MutableLiveData<List<SongListDO>> allSongList;
    private SongListWebService songListWebService;
    //private Executor diskIO; // unblock

    public Repo(SongDao songDao,
                SongListDao songListDao,
                SongXSongListDao songXSongListDao,
                SongListWebService songListWebService) {
        this.songDao = songDao;
        this.songListDao = songListDao;
        this.songXSongListDao = songXSongListDao;
        this.songListWebService = songListWebService;
        allSong = new MutableLiveData<>();
        allSongList = new MutableLiveData<>();
        songOnWeb = new MutableLiveData<>();
        songListOnWeb = new MutableLiveData<>();
        songXSongListOnWeb = new MutableLiveData<>();
    }

    private Long insertSong(SongDO data) {
        LiveData<SongDO> result = songDao.findByRemoteId(data.remoteId);
        if (result == null) return -1L; //error
        if (result.getValue() == null)
            return songDao.insert(data);
        else
            return result.getValue().id;
    }

    private void deleteSong(SongDO data) {
        LiveData<SongDO> result = songDao.findByRemoteId(data.remoteId);
        if (result != null && result.getValue() != null)
            songDao.delete(data);
    }

    private Long insertSongList(SongListDO data) {
        LiveData<SongListDO> result = songListDao.findByRemoteId(data.remoteId);
        if (result == null) return -1L; //error
        if (result.getValue() == null)
            return songListDao.insert(data);
        else
            return result.getValue().id;
    }

    private void deleteSongList(SongListDO data) {
        LiveData<SongListDO> result = songListDao.findByRemoteId(data.remoteId);
        if (result != null && result.getValue() != null)
            songListDao.delete(data);
    }

    private void insertSongXSongLIst(SongXSongListDO data) {
        LiveData<SongXSongListDO> result = songXSongListDao.findbyID(data.songId, data.songListId);
        if (result == null) return; //error
        if (result.getValue() == null)
            songXSongListDao.insert(data);
    }

    private void deleteSongXSongList(SongXSongListDO data) {
        LiveData<SongXSongListDO> result = songXSongListDao.findbyID(data.songId, data.songListId);
        if (result != null && result.getValue() != null)
            songXSongListDao.delete(data);
    }

    private void beforeUpdate() {
        songOnWeb.setValue(songDao.findAllDataOnWeb() == null
                ?new ArrayList<SongDO>()
                :songDao.findAllDataOnWeb().getValue()==null
                    ?new ArrayList<SongDO>()
                    :songDao.findAllDataOnWeb().getValue());
        songListOnWeb.setValue(songListDao.findAllDataOnWeb() == null
                ?new ArrayList<SongListDO>()
                :songListDao.findAllDataOnWeb().getValue()==null
                    ?new ArrayList<SongListDO>()
                    :songListDao.findAllDataOnWeb().getValue());
        songXSongListOnWeb.setValue(songXSongListDao.findAllDataOnWeb() == null
                ?new ArrayList<SongXSongListDO>()
                :songXSongListDao.findAllDataOnWeb().getValue()==null
                    ?new ArrayList<SongXSongListDO>()
                    :songXSongListDao.findAllDataOnWeb().getValue());
        allSong.setValue(songDao.findAllSongDOs()==null
                ?new ArrayList<SongDO>()
                :songDao.findAllSongDOs().getValue()==null
                    ?new ArrayList<SongDO>()
                    :songDao.findAllSongDOs().getValue());
        allSongList.setValue(songListDao.findAllSongListDOs()==null
                ?new ArrayList<SongListDO>()
                :songListDao.findAllSongListDOs().getValue()==null
                    ?new ArrayList<SongListDO>()
                    :songListDao.findAllSongListDOs().getValue());
    }

    private Long UpDaTeSoNg(SongDO data, boolean isEnd) {
        Long songId = insertSong(data);
        //手动迭代
        int index = songOnWeb.getValue().size() - 1;
        for (SongDO hasIt; index >= 0; index--) {
            hasIt = songOnWeb.getValue().get(index);
            if (hasIt.remoteId.longValue() == data.remoteId.longValue())
                songOnWeb.getValue().remove(songOnWeb.getValue().indexOf(data));//remove可行性存疑
        }
        if (isEnd == true && songOnWeb.getValue().isEmpty() == false) {
            for (SongDO item : songOnWeb.getValue()) {
                deleteSong(item);
            }
        }
        return songId;
    }

    private Long UpDaTeSoNgLiSt(SongListDO data, boolean isEnd) {
        Long songListId = insertSongList(data);
        //手动迭代
        int index = songListOnWeb.getValue().size() - 1;
        for (SongListDO hasIt; index >= 0; index--) {
            hasIt = songListOnWeb.getValue().get(index);
            if (hasIt.remoteId.longValue() == data.remoteId.longValue())
                songListOnWeb.getValue().remove(songListOnWeb.getValue().indexOf(data));
        }
        if (isEnd == true && songListOnWeb.getValue().isEmpty() == false) {
            for (SongListDO item : songListOnWeb.getValue()) {
                deleteSongList(item);
            }
        }
        return songListId;
    }

    private void UpDaTeSoNgXSoNgLiSt(SongXSongListDO data, boolean isEnd) {
        insertSongXSongLIst(data);
        int index = songXSongListOnWeb.getValue().size() - 1;
        for (SongXSongListDO hasIt; index >= 0; index--) {
            hasIt = songXSongListOnWeb.getValue().get(index);
            if (hasIt.songId.longValue() == data.songId.longValue()
                    && hasIt.songListId.longValue() == data.songListId.longValue())
                songXSongListOnWeb.getValue().remove(songXSongListOnWeb.getValue().indexOf(data));
        }
        if (isEnd == true && songXSongListOnWeb.getValue().isEmpty() == false) {
            for (SongXSongListDO item : songXSongListOnWeb.getValue()) {
                deleteSongXSongList(item);
            }
        }
    }

    public void fetch() {
        beforeUpdate();
        songListWebService.getAllSongLists((songMap) -> {
            //List<SongListDO> allSongListDO = new ArrayList<>(songMap.keySet());
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
            allSong.setValue(songDao.findAllSongDOs()==null
                    ?new ArrayList<SongDO>()
                    :songDao.findAllSongDOs().getValue()==null
                    ?new ArrayList<SongDO>()
                    :songDao.findAllSongDOs().getValue());
            allSongList.setValue(songListDao.findAllSongListDOs()==null
                    ?new ArrayList<SongListDO>()
                    :songListDao.findAllSongListDOs().getValue()==null
                    ?new ArrayList<SongListDO>()
                    :songListDao.findAllSongListDOs().getValue());
        });
    }

    public LiveData<List<SongDO>> getAllSong() {
        fetch();
        return allSong;
    }

    public LiveData<List<SongListDO>> getAllSongList() {
        fetch();
        return allSongList;
    }
}
