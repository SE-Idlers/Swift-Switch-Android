package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.web.domain.NetworkSongList;

import java.util.List;
import java.util.concurrent.Executor;

public class SongListRepository extends Repository<SongListPojo, NetworkSongList> {

    private Executor diskIO;
    private SongListPojoDao songListPojoDao;
    private LoginManager loginManager;

    public SongListRepository(Executor diskIO){
        this.diskIO=diskIO;
    }


    @Override
    public void insert(SongListPojo localData) {
        diskIO.execute(()-> songListPojoDao.insert(localData));
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
    protected void fetchAllByUid(String uid) {
        //根据Uid抓取歌单，获得网络数据后设定同步歌单的异步IO任务
        //-----------------------------------------------------
        //tips:发起网络请求是在主线程，网络请求的处理是在子线程
        //     而网络请求完成后，回调函数的执行是在主线程，但是回调函数的任务只是
        //     发起一个异步的在子线程执行的歌单同步任务，不会造成主线程的阻塞
        //TODO 根据uid发起网络请求抓取歌曲
    }

    @Override
    protected boolean shouldFetch() {
        //TODO 检查是否是第一次抓取、设置刷新时间等等
        return true;
    }

    @Override
    protected LiveData<List<SongListPojo>> loadAll() {
        //TODO 从本地数据库加载数据，异步或同步有待考虑
        return null;
    }
}
