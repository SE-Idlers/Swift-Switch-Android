package com.example.win.easy.repository.deprecated.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.web.service.LoginService;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.dto.SongListDTO;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class __SongListRepository extends __Repository<SongListDO, SongListDTO> {

    private Executor diskIO;
    private SongListDao songListDao;

    @Inject
    public __SongListRepository(@Named("dbAccess") Executor diskIO,
                                SongListDao songListDao,
                                LoginService loginService){
        super(loginService);
        this.diskIO=diskIO;
        this.songListDao = songListDao;
    }

    @Override
    public void insert(SongListDO localData) {
        diskIO.execute(()-> {
            List<SongListDO> result= songListDao.findAllByNameAndSource(localData.getName(),localData.getSource().toString());
            if (result==null||result.size()==0)
                songListDao.insert(localData);
        });
    }

    @Override
    public void delete(SongListDO data) {
        diskIO.execute(()-> songListDao.delete(data));
    }

    @Override
    public void update(SongListDO data) {
        diskIO.execute(()-> songListDao.update(data));
    }

    @Override
    protected boolean shouldFetch() {
        //TODO 检查是否是第一次抓取、设置刷新时间等等
        return true;
    }

    @Override
    protected LiveData<List<SongListDO>> loadAll() {
        //TODO 从本地数据库加载数据，异步或同步有待考虑
        return songListDao.findAllSongListDOs();
    }
}
