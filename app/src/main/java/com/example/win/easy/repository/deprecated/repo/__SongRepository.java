package com.example.win.easy.repository.deprecated.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.web.service.LoginService;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.web.dto.SongDTO;

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
    protected boolean shouldFetch() {
        return true;
    }

    @Override
    protected LiveData<List<SongDO>> loadAll() {
        return songDao.findAllSongDOs();
    }
}
