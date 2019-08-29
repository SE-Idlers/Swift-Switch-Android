package com.example.win.easy.repository.deprecated.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.web.service.LoginService;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.web.dto.SongDTO;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class __SongRepository extends __Repository<SongDO, SongDTO> {

    private SongDao songDao;
    private Executor diskIO;

    @Inject
    public __SongRepository(@Named("dbAccess")Executor diskIO,
                            SongDao songDao,
                            LoginService loginService){
        super(loginService);
        this.diskIO= diskIO;
        this.songDao = songDao;
    }


    @Override
    public void insert(SongDO localData) {
        diskIO.execute(()->{
            SongDO result= songDao.findAllBySongPath(localData.songPath);
            System.out.println(result);
            if (result==null)
                songDao.insert(localData);
        });
    }

    @Override
    public void delete(SongDO data) {
        diskIO.execute(()-> songDao.delete(data));
    }

    @Override
    public void update(SongDO data) {
        diskIO.execute(()-> songDao.update(data));
    }

    @Override
    protected boolean shouldFetch() {
        return true;
    }

    @Override
    protected LiveData<List<SongDO>> loadAll() {
        return songDao.findAllSongDOs();
    }
}
