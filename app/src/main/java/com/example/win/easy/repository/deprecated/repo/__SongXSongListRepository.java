package com.example.win.easy.repository.deprecated.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.web.service.LoginService;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import lombok.AllArgsConstructor;

@Singleton
public class __SongXSongListRepository extends __Repository<SongXSongListDO,Void> {

    private Executor diskIO;
    private SongDao songDao;
    private SongXSongListDao songXSongListDao;

    @Inject
    public __SongXSongListRepository(@Named("dbAccess")Executor diskIO,
                                     SongDao songDao,
                                     SongXSongListDao songXSongListDao,
                                     LoginService loginService){
        super(loginService);
        this.diskIO= diskIO;
        this.songDao = songDao;
        this.songXSongListDao= songXSongListDao;
    }

    public LiveData<List<SongDO>> getAllSongsForSongList(SongListDO songListDO){
        return songXSongListDao.findAllSongsForSongListById(songListDO.getId());
    }

    @Override
    public void insert(SongXSongListDO localData) {
        diskIO.execute(()->songXSongListDao.insert(localData));
    }

    @Override
    public void delete(SongXSongListDO data) {
        diskIO.execute(()->songXSongListDao.delete(data));
    }

    @Override
    public void update(SongXSongListDO data) {
    }

    @Override
    protected boolean shouldFetch() {
        return true;
    }

    @Override
    protected LiveData<List<SongXSongListDO>> loadAll() {
        return songXSongListDao.findAllSongXSongLists();
    }

    public void insertNewSongAndToSongLists(SongDO newSong, List<SongListDO> songListDOS){
        diskIO.execute(create(newSong, songListDOS) );
    }

    @AllArgsConstructor
    private class InsertNewSongToSongListsTask implements Runnable{

        private SongDO newSong;
        private List<SongListDO> songListDOS;
        private SongDao songDao;
        private SongXSongListDao songXSongListDao;

        @Override
        public void run() {
            SongDO result= songDao.findAllBySongPath(newSong.getSongPath());
            long newSongId;
            if (result==null)
                newSongId= songDao.insert(newSong);
            else
                newSongId=result.getId();
            List<SongXSongListDO> songXSongListDOs =new ArrayList<>();
            List<SongListDO> existingLists=songXSongListDao.findAllSongListsForSongById(newSongId);
            for (SongListDO songListDO : songListDOS)
                if (!existingLists.contains(songListDO))
                    songXSongListDOs.add(new SongXSongListDO(newSongId, songListDO.getId()));
            songXSongListDao.insert(songXSongListDOs);
        }
    }

    public InsertNewSongToSongListsTask create(SongDO newSong, List<SongListDO> songListDOS){
        return new InsertNewSongToSongListsTask(newSong, songListDOS, songDao,songXSongListDao);
    }
}
