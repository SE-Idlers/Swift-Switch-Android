package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import lombok.AllArgsConstructor;

@Singleton
public class SongXSongListRepository extends Repository<SongXSongList,Void> {

    private Executor diskIO;
    private SongPojoDao songPojoDao;
    private SongXSongListDao songXSongListDao;

    @Inject
    public SongXSongListRepository(@Named("dbAccess")Executor diskIO,
                                   SongPojoDao songPojoDao,
                                   SongXSongListDao songXSongListDao,
                                   LoginManager loginManager){
        super(loginManager);
        this.diskIO= diskIO;
        this.songPojoDao=songPojoDao;
        this.songXSongListDao= songXSongListDao;
    }

    public LiveData<List<SongPojo>> getAllSongsForSongList(SongListPojo songListPojo){
        return songXSongListDao.findAllSongsForSongListById(songListPojo.getId());
    }

    @Override
    public void insert(SongXSongList localData) {
        diskIO.execute(()->songXSongListDao.insert(localData));
    }

    @Override
    public void delete(SongXSongList data) {
        diskIO.execute(()->songXSongListDao.delete(data));
    }

    @Override
    public void update(SongXSongList data) {
    }

    @Override
    protected boolean shouldFetch() {
        return true;
    }

    @Override
    protected LiveData<List<SongXSongList>> loadAll() {
        return songXSongListDao.findAllSongXSongLists();
    }

    public void insertNewSongAndToSongLists(SongPojo newSong, List<SongListPojo> songListPojos){
        diskIO.execute(create(newSong,songListPojos) );
    }

    @AllArgsConstructor
    private class InsertNewSongToSongListsTask implements Runnable{

        private SongPojo newSong;
        private List<SongListPojo> songListPojos;
        private SongPojoDao songPojoDao;
        private SongXSongListDao songXSongListDao;

        @Override
        public void run() {
            SongPojo result=songPojoDao.findAllBySongPath(newSong.songPath);
            long newSongId;
            if (result==null)
                newSongId=songPojoDao.insert(newSong);
            else
                newSongId=result.getId();
            List<SongXSongList> songXSongLists=new ArrayList<>();
            List<SongListPojo> existingLists=songXSongListDao.findAllSongListsForSongById(newSongId);
            for (SongListPojo songListPojo:songListPojos)
                if (!existingLists.contains(songListPojo))
                    songXSongLists.add(new SongXSongList(newSongId,songListPojo.getId()));
            songXSongListDao.insert(songXSongLists);
        }
    }

    public InsertNewSongToSongListsTask create(SongPojo newSong, List<SongListPojo> songListPojos){
        return new InsertNewSongToSongListsTask(newSong,songListPojos,songPojoDao,songXSongListDao);
    }
}
