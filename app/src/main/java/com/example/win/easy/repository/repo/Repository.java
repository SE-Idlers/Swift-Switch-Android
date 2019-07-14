package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.LoginManager;

import java.util.List;

public abstract class Repository<LocalType,NetworkType> {

    protected LoginManager loginManager;

    public Repository(LoginManager loginManager){
        this.loginManager=loginManager;
    }

    /**
     * 对外提供一个简单的接口，获取所有歌曲<br/>
     * 执行流程:<br/>
     * 是否需要从远程抓取->需要->发起<b>异步</b>网络请求，同时<b>同步</b>返回本地数据库数据<br/>
     * &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;|->不需要->直接<b>同步</b>返回本地数据库数据
     * @return
     */
    public LiveData<List<LocalType>> getAll(){
        if (shouldFetch())
            fetchAll();
        return loadAll();
    }

    public abstract void insert(LocalType localData);

    public abstract void delete(LocalType data);

    public abstract void update(LocalType data);

    public void fetchAll(){
        //未登录时取消抓取
        if(!loginManager.hasLogin())
            return;
        //获取uid并据此发起网络请求并抓取
        fetchAllByUid(loginManager.getCurrentUid());
    }

    public abstract void fetchAllByUid(String uid);

    protected abstract boolean shouldFetch();

    protected abstract LiveData<List<LocalType>> loadAll();

}
