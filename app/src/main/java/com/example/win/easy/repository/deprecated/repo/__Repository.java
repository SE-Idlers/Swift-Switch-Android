package com.example.win.easy.repository.deprecated.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.web.service.LoginService;

import java.util.List;

public abstract class __Repository<LocalType,NetworkType> {

    protected LoginService loginService;

    public __Repository(LoginService loginService){
        this.loginService = loginService;
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
        if(!loginService.hasLogin())
            return;
    }

    protected abstract boolean shouldFetch();

    protected abstract LiveData<List<LocalType>> loadAll();

}
