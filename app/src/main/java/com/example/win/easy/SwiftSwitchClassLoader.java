package com.example.win.easy;

import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.repository.web.BackendResourceWebService;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.thread.AppExecutors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//防止部分类懒惰加载，提前实例化静态变量
public class SwiftSwitchClassLoader {
    private SwiftSwitchClassLoader(){}

    private static OurDatabase ourDatabase;
    private static BackendResourceWebService backendResourceWebService;

    public static void init(){
        SongManagerImpl.getInstance();
        SongListMangerImpl.getInstance();
        AppExecutors.getInstance();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://guohere.com:9000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        backendResourceWebService=retrofit.create(BackendResourceWebService.class);
        LoginManager.init(backendResourceWebService);
        LoginManager.loginByPhone("15564278737","zxc486251379");
    }

    public static void setOurDatabase(OurDatabase _ourDatabase){
        ourDatabase=_ourDatabase;
    }
    public static OurDatabase getOurDatabase(){
        return ourDatabase;
    }
    public static BackendResourceWebService getBackendResourceWebService(){return backendResourceWebService;}
}
