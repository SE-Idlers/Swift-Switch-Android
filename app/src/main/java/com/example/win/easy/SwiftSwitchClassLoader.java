package com.example.win.easy;

import android.content.Context;

import androidx.room.Room;

import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.repository.web.BackendResourceWebService;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.thread.AppExecutors;
import com.example.win.easy.view.DashboardView;
import com.example.win.easy.view.MediaPlayerView;
import com.example.win.easy.view.SongListPanelView;
import com.example.win.easy.view.SongPanelView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//防止部分类懒惰加载，提前实例化静态变量
public class SwiftSwitchClassLoader {

    private static SwiftSwitchClassLoader instance=new SwiftSwitchClassLoader();
    public static SwiftSwitchClassLoader getInstance(){return instance;}
    private SwiftSwitchClassLoader(){}

    private static OurDatabase ourDatabase;
    private static BackendResourceWebService backendResourceWebService;

    public static void init(){
        SongManagerImpl.getInstance();
        SongListMangerImpl.getInstance();
        DashboardView.getInstance();
        MediaPlayerView.getInstance();
        SongListPanelView.getInstance();
        SongPanelView.getInstance();
        AppExecutors.getInstance();
        Context context=MainActivity.mainActivity.getApplicationContext();
        ourDatabase=Room.databaseBuilder(context,OurDatabase.class,"ourDatabase").build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://guohere.com:9000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        backendResourceWebService=retrofit.create(BackendResourceWebService.class);
        LoginManager.init(backendResourceWebService);
        LoginManager.loginByPhone("15564278737","zxc486251379");
    }

    public static OurDatabase getOurDatabase(){
        return ourDatabase;
    }
    public static BackendResourceWebService getBackendResourceWebService(){return backendResourceWebService;}
}
