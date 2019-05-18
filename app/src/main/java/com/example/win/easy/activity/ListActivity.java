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

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.win.easy.R;
import com.example.win.easy.activity.fragment.AllSongListsFragment;
import com.example.win.easy.activity.fragment.AllSongsFragment;
import com.example.win.easy.activity.fragment.ListFragment;


public class ListActivity extends FragmentActivity {

    private FragmentManager fragmentManager=getSupportFragmentManager();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //获取要展示的内容类型是什么，歌曲or歌单
        int contentType= getIntent().getIntExtra("content",-1);
        //根据类型加载Fragment
        if (contentType== ListFragment.CONTENT_ALL_SONGS){
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_list_frame,new AllSongsFragment())
                    .commit();
        }else if (contentType==ListFragment.CONTENT_ALL_SONG_LISTS){
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_list_frame,new AllSongListsFragment())
                    .commit();
        }
    }

}
