package com.example.win.easy.activity;

/*
*github相关类的网址：
* https://github.com/SE-Idlers/Swift-Switch-Android/blob/dev/app/src/main/java/com/example/win/easy/repository/db/pojo/SongPojo.java
* https://github.com/SE-Idlers/Swift-Switch-Android/blob/dev/app/src/main/java/com/example/win/easy/repository/db/pojo/SongListPojo.java
* https://github.com/SE-Idlers/Swift-Switch-Android/tree/dev/app/src/main/java/com/example/win/easy/repository/db/pojo
* */

/*
 *总述：
 * 数据存储在ViewModel
 *
 * 界面放有测试按钮，点击触发ViewModel更新；
 * ViewModel更新触发界面更新
 */

/*备注：viewModel需要提供的函数
 * 歌单：
 * 所有歌单图片更新结束 : boolean
 * 播放次数发生更新 : 歌单id，播放次数int
 * 播放顺序发生改变 : 未写
 *
 * 歌曲：
 * 某一首歌曲下载完成 : 歌曲id
 * 歌曲播放次数发生改变 : 歌曲id，播放次数int
 * 播放顺序发生改变 : 未写
 * */

/* 代码块
 * 1. 定义
 * 2. 调用初始化函数
 * 3. 测试用的控件的初始化：调用时更新ViewModel数据，模拟后台数据更新
 * 4. 函数：界面控件响应、观察者模式响应、界面更新行为注册
 */

import com.example.win.easy.R;
import com.example.win.easy.activity.component.*;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


public class AppListOne extends AppCompatActivity {
    //测试控件
    Button btn_io;//模拟后台读入图片
    Button btn_picLoaded;//模拟更改LiveData从而通知刷新图片
    Button btn_exchange;//模拟后台更新列表播放次数
    Button btn_SongListTimes;//模拟后台更新歌曲播放次数
    Button btn_SongLoad;//模拟后台完成歌曲下载

    /*……后期可制为底边栏*/
    Button btn_back;
    Button btn_songlist;
    Button btn_songs;

    EditText etx_songlist;//搜索

    ViewModel mViewModel;

    //歌单+歌曲共用的view控件
    ListView listView;
    SongListAdapter mSongListAdapter;
    SongAdapter mSongAdapter;
    int songOrList=0;//song; 1=List

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applistone);

        //初始化
        init();

 /****测试控件初始化****/
        btn_io=(Button)findViewById(R.id.btn_io);
        btn_picLoaded=(Button) findViewById(R.id.btn_picLoaded);
        btn_SongListTimes=(Button)findViewById(R.id.btn_SongListTimes);

        btn_exchange=(Button)findViewById(R.id.btn_exchange);
        btn_SongLoad=(Button) findViewById(R.id.btn_SongLoad);

        //【歌曲】
        btn_SongLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//模拟后台完成歌曲下载
                if(listView.getAdapter() instanceof SongAdapter) {
                    mViewModel.updateDownLoadedASong(listView,4);//提交最新完成下载歌曲的id，暂时以在资源中的位置代替
                    //正常来说，应该更新第4个数据，但是为了显示更多变化，将在viewModel的数据更改中实现为更新“视图中的第四个数据”
                    //viewModel的数据更改代码中包括正常情况下的代码，取消注释即可
                    //Log.d("SongList","DownLoad inButton");
                }
            }
        });

        btn_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//模拟后台更新了歌曲的播放次数
                if(listView.getAdapter() instanceof SongAdapter){
                    mViewModel.updateSongTimes(listView,0,100);//更新歌曲的播放次数
                }
            }
        });


        //【歌单】
        btn_io.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//模拟后台读入图片,异步加载
                if(listView.getAdapter() instanceof SongListAdapter){
                    mViewModel.updateImage(AppListOne.this);
                }
            }
        });

        btn_picLoaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//更新LiveData：模拟更新后，引发显示图片
                if(listView.getAdapter() instanceof SongListAdapter){
                mViewModel.updateImageDone() ;//更新LiveData<Boolean> mSongListPicDownLoaded
                }
            }
        });
        btn_SongListTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//模拟后台更新了列表的播放次数
                if(listView.getAdapter() instanceof SongListAdapter) {
                    mViewModel.updateSongListTimes(listView,1, 100);//更新列表的播放次数
                }
            }
        });


/****测试控件初始化结束****/


        //确认当前视图
        Intent intent=getIntent();
        songOrList=0;//=intent.getIntExtra("SongOrList",0);//【最终代码】跳转目标从主页面来
        if(songOrList==0)
        {
            listView.setAdapter(mSongAdapter);
        }else if(songOrList==1){
            listView.setAdapter(mSongListAdapter);
        }else{

        }
    }

    /*******************************初始化:界面控件响应、观察者模式响应、界面更新行为注册【由观察者模式响应内部调用】**********************************/

    void init(){
        init_ctl();
        init_viewModel_action();
    }
    /*******************初始化界面控件******************/
    void init_ctl(){
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_songlist=(Button)findViewById(R.id.btn_songlist);
        btn_songs=(Button)findViewById(R.id.btn_songs);
        etx_songlist=(EditText)findViewById(R.id.etx_songlist);
        listView=(ListView)findViewById(R.id.listView);

        btn_songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songOrList==0) return;
                //跳转到歌曲列表视图
                songOrList=0;
                listView.setAdapter(mSongAdapter);
                mSongAdapter.notifyDataSetChanged();
            }
        });

        btn_songlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songOrList==1) return;
                //跳转到歌单列表视图
                songOrList=1;
                listView.setAdapter(mSongListAdapter);
                mSongListAdapter.notifyDataSetChanged();
            }
        });
    }


    /*****************初始化观察者模式响应*******************/
    void init_viewModel_action(){
        mViewModel =ViewModelProviders.of(AppListOne.this).get(ViewModel.class);

        //初始化歌单列表
             //【测试用】
             /*List<SongListPojo> mData2=test_SongList_init();
              mViewModel.setSongListData(mData2);*/
        List<SongListPojo> mData2=mViewModel.getSongListData();//从ViewModel处获得

        //适配器初始化
        mSongListAdapter = new SongListAdapter(mData2, AppListOne.this);
        listView.setAdapter(mSongListAdapter);

        register_songlist();//songListAdapter下的对LiveData观察者行为绑定


        //初始化歌曲列表
            //【测试用】
            /*List<SongPojo> mData1=test_Songs_init();
              mViewModel.setSongData(mData1);*/
        List<SongPojo> mData1=mViewModel.getSongData(); //从ViewModel处获得

        //适配器初始化
        mSongAdapter=new SongAdapter(mData1,AppListOne.this);
        listView.setAdapter(mSongAdapter);

        register_songs();//songAdapter下的对LiveData观察者行为绑定
    }


    /*******************界面更新行为注册**************/
    private void register_songlist(){/****歌单界面更新行为***/
        // 所有歌单图片更新结束 ：LiveData for图片下载结束的标识
        mViewModel.getSongListPicDownLoadState().observe(AppListOne.this,new Observer<Boolean>(){
            @Override
            public void onChanged(@Nullable Boolean mDownLoaded){
                if(mDownLoaded) {
                    SongListAdapter songListAdapter = (SongListAdapter) listView.getAdapter();
                    songListAdapter.updateItemPhoto(listView);
                }
            }});

        // 某个歌单播放次数发生更新 : LiveData for{歌单id（暂时是在资源中的position）,播放次数}
        mViewModel.getSongListReplayTimes().observe(AppListOne.this,new Observer<ArrayList<Integer>>(){
            @Override
            public void onChanged(@Nullable ArrayList<Integer> atrr){
                SongListAdapter songListAdapter=(SongListAdapter)listView.getAdapter();
                songListAdapter.updateItemTimes(listView,atrr.get(0),atrr.get(1));
            }
        });
    }

    private void register_songs(){ /****歌曲界面更新行为***/
        //某首歌下载：LiveData for最新下载歌曲的id
        mViewModel.getCurDownloadedSong().observe(AppListOne.this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (listView.getAdapter() instanceof SongAdapter) {
                    SongAdapter songAdapter = (SongAdapter) listView.getAdapter();
                    songAdapter.updateItemDownLoad(listView, integer, true);
                    //Log.d("SongList","DownLoad inViewModel");
                }
            }
        });

        //播放次数：LiveData for{歌曲id（暂时是在资源中的position）,播放次数}
        mViewModel.getSongReplayTimes().observe(AppListOne.this,new Observer<ArrayList<Integer>>(){
            @Override
            public void onChanged(@Nullable ArrayList<Integer> atrr) {
                if (listView.getAdapter() instanceof SongAdapter){
                    SongAdapter songAdapter = (SongAdapter) listView.getAdapter();
                    songAdapter.updateItemTimes(listView,atrr.get(0),atrr.get(1));
                }
            }
        });

    }

 /*
    //测试样例
    List<SongListPojo> test_SongList_init(){
        //初始化并默认赋值
        List<SongListPojo> mData = new ArrayList<>();
        mData.add(new SongListPojo("歌单一",1,"来源一","未知"));
        mData.add(new SongListPojo("歌单二",2,"来源二","未知"));
        mData.add(new SongListPojo("歌单三",3,"来源三","未知"));
        mData.add(new SongListPojo("歌单四",4,"来源四","未知"));
        mData.add(new SongListPojo("歌单五",1,"来源五","未知"));
        mData.add(new SongListPojo("歌单六",2,"来源六","未知"));
        mData.add(new SongListPojo("歌单七",3,"来源七","未知"));
        mData.add(new SongListPojo("歌单八",4,"来源八","未知"));
        mData.add(new SongListPojo("歌单九",1,"来源九","未知"));
        mData.add(new SongListPojo("歌单十",2,"来源十","未知"));
        mData.add(new SongListPojo("歌单一",1,"来源一","未知"));
        mData.add(new SongListPojo("歌单二",2,"来源二","未知"));
        mData.add(new SongListPojo("歌单三",3,"来源三","未知"));
        mData.add(new SongListPojo("歌单四",4,"来源四","未知"));
        mData.add(new SongListPojo("歌单五",1,"来源五","未知"));
        mData.add(new SongListPojo("歌单六",2,"来源六","未知"));
        mData.add(new SongListPojo("歌单七",3,"来源七","未知"));
        mData.add(new SongListPojo("歌单八",4,"来源八","未知"));
        mData.add(new SongListPojo("歌单九",1,"来源九","未知"));
        mData.add(new SongListPojo("歌单十",2,"来源十","未知"));
        return mData;

    }

      List<SongPojo> test_Songs_init(){
        List<SongPojo>mData=new ArrayList<>();
        mData.add(new SongPojo("歌曲一",1,1,"歌手一","专辑一",false));
        mData.add(new SongPojo("歌曲二",2,2,"歌手二","专辑二",false));
        mData.add(new SongPojo("歌曲三",3,3,"歌手三","专辑三",false));
        mData.add(new SongPojo("歌曲四",1,1,"歌手四","专辑四",false));
        mData.add(new SongPojo("歌曲五",2,2,"歌手五","专辑五",false));
        mData.add(new SongPojo("歌曲六",3,3,"歌手六","专辑六",false));
        mData.add(new SongPojo("歌曲七",1,1,"歌手七","专辑七",false));
        mData.add(new SongPojo("歌曲八",2,2,"歌手八","专辑八",false));
        mData.add(new SongPojo("歌曲九",3,3,"歌手九","专辑九",false));
        mData.add(new SongPojo("歌曲十",1,1,"歌手十","专辑十",false));
        mData.add(new SongPojo("歌曲一",1,1,"歌手一","专辑一",false));
        mData.add(new SongPojo("歌曲二",2,2,"歌手二","专辑二",false));
        mData.add(new SongPojo("歌曲三",3,3,"歌手三","专辑三",false));
        mData.add(new SongPojo("歌曲四",1,1,"歌手四","专辑四",false));
        mData.add(new SongPojo("歌曲五",2,2,"歌手五","专辑五",false));
        mData.add(new SongPojo("歌曲六",3,3,"歌手六","专辑六",false));
        mData.add(new SongPojo("歌曲七",1,1,"歌手七","专辑七",false));
        mData.add(new SongPojo("歌曲八",2,2,"歌手八","专辑八",false));
        mData.add(new SongPojo("歌曲九",3,3,"歌手九","专辑九",false));
        mData.add(new SongPojo("歌曲十",1,1,"歌手十","专辑十",false));
        return mData;
    }
  */


}