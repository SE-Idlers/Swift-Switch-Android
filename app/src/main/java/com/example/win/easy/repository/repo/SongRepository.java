package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.web.domain.NetworkSong;
import com.example.win.easy.thread.AppExecutors;

import java.util.List;
import java.util.concurrent.Executor;

public class SongRepository extends Repository<SongPojo, NetworkSong> {

    private static SongRepository instance=new SongRepository();
    public static SongRepository getInstance(){return instance;}
    private SongRepository(){
        this.songPojoDao= SwiftSwitchClassLoader.getOurDatabase().songPojoDao();
        this.diskIO= AppExecutors.getInstance().diskIO();
    }

    private SongPojoDao songPojoDao;
    private Executor diskIO;

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
    protected void fetchAllByUid(String uid) {

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
