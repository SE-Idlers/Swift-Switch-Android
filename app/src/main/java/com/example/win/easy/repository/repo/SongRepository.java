package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.web.domain.NetworkSong;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SongRepository extends Repository<SongPojo, NetworkSong> {

    private SongPojoDao songPojoDao;
    private Executor diskIO;

    @Inject
    public SongRepository(@Named("dbAccess")Executor diskIO,
                          SongPojoDao songPojoDao,
                          LoginManager loginManager){
        super(loginManager);
        this.diskIO= diskIO;
        this.songPojoDao= songPojoDao;
    }


    @Override
    public void insert(SongPojo localData) {
        diskIO.execute(()->{
            SongPojo result=songPojoDao.findAllBySongPath(localData.songPath);
            System.out.println(result);
            if (result==null)
                songPojoDao.insert(localData);
        });
    }

    @Override
    public void delete(SongPojo data) {
        diskIO.execute(()->songPojoDao.delete(data));
    }

    @Override
    public void update(SongPojo data) {
        diskIO.execute(()->songPojoDao.update(data));
    }

    @Override
    public void fetchAllByUid(String uid) {

    }

    @Override
    protected boolean shouldFetch() {
        return true;
    }

    @Override
    protected LiveData<List<SongPojo>> loadAll() {
        return songPojoDao.findAllSongPojos();
    }
}
