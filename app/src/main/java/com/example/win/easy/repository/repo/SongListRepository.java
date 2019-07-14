package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.factory.CallbackFactory;
import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.web.BackendResourceWebService;
import com.example.win.easy.repository.web.domain.NetworkSongList;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class SongListRepository extends Repository<SongListPojo, NetworkSongList> {

    private Executor diskIO;
    private SongListPojoDao songListPojoDao;
    private BackendResourceWebService webService;
    private CallbackFactory callbackFactory;

    @Inject
    public SongListRepository(@Named("dbAccess") Executor diskIO,
                              SongListPojoDao songListPojoDao,
                              BackendResourceWebService backendResourceWebService,
                              LoginManager loginManager,
                              CallbackFactory callbackFactory){
        super(loginManager);
        this.diskIO=diskIO;
        this.songListPojoDao= songListPojoDao;
        this.webService=backendResourceWebService;
        this.callbackFactory=callbackFactory;
    }

    @Override
    public void insert(SongListPojo localData) {
        diskIO.execute(()-> {
            List<SongListPojo> result=songListPojoDao.findAllByNameAndSource(localData.getName(),localData.getSource().toString());
            if (result==null||result.size()==0)
                songListPojoDao.insert(localData);
        });
    }

    @Override
    public void delete(SongListPojo data) {
        diskIO.execute(()->songListPojoDao.delete(data));
    }

    @Override
    public void update(SongListPojo data) {
        diskIO.execute(()->songListPojoDao.update(data));
    }

    @Override
    protected boolean shouldFetch() {
        //TODO 检查是否是第一次抓取、设置刷新时间等等
        return true;
    }

    @Override
    protected LiveData<List<SongListPojo>> loadAll() {
        //TODO 从本地数据库加载数据，异步或同步有待考虑
        return songListPojoDao.findAllSongListPojos();
    }
}
