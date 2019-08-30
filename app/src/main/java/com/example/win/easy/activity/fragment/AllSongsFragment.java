package com.example.win.easy.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.application.SwiftSwitchApplication;
import com.example.win.easy.factory.__SongFactory;
import com.example.win.easy.tool.UriProcessTool;
import com.example.win.easy.repository.db.CustomTypeConverters;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.viewmodel.SimpleViewModel;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

/**
 * 展示所有歌曲的Fragment
 */
public class AllSongsFragment extends ListFragment {

    @Inject ViewModelProvider.Factory factory;
    @Inject
    __SongFactory songFactory;
    private SimpleViewModel viewModel;
    private LiveData<List<SongDO>> allSongs;
    private QMUIGroupListView.Section section;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View thisView=super.onCreateView(inflater,container,savedInstanceState);
        //设置标题
        setTopBarTitle("所有歌曲");
        //右上角按钮用于添加本地歌曲
        setRightImageButtonOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            startActivityForResult(intent, Constants.READ_REQUEST_CODE);
        });
        return thisView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        //从查看文件夹Activity,即请求码为READ_REQUEST_CODE返回
        if (requestCode ==Constants.READ_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                &&resultData!=null) {
            //添加歌曲到本地数据库
            viewModel.insert(songFactory.create(new File(UriProcessTool.getPathByUri4kitkat(getContext(),resultData.getData()))));
            //提示添加成功
            Toast.makeText(getContext(),"歌曲添加成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //注册数据监听
        SwiftSwitchApplication.application.getViewModelComponent().inject(this);
        viewModel = ViewModelProviders.of(this,factory).get(SimpleViewModel.class);
        allSongs=viewModel.getAllSongs();
        allSongs.observe(this, this::update);
    }

    /**
     * 根据最新的歌曲数据刷新视图
     * @param songDOs 最新的歌曲数据
     */
    public void update(List<SongDO> songDOs){
        //每次刷新时都重新创建section
        if (section!=null)
            section.removeFrom(groupListView);
        section=QMUIGroupListView.newSection(getContext());
        //对每个歌曲都生成一个itemView
        for (SongDO songDO : songDOs) {
            QMUICommonListItemView itemView=groupListView.createItemView(LinearLayout.VERTICAL);
            //显示歌曲名称
            itemView.setText(songDO.getName());
            //显示歌曲默认头像
            itemView.setImageDrawable(getResources().getDrawable(R.drawable.ase16));
            //itemView最右侧显示自定义的视图（一张图片）
            itemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
            itemView.setOnClickListener(v -> {
                //TODO 触发歌曲播放
                Toast.makeText(getContext(),"待实现：点击播放歌曲",Toast.LENGTH_SHORT).show();
            });
            //如果歌曲已经下载好，则显示它的sequence，同时在最右侧显示一张音符图片
            if (songDO.getSongPath()!=null){
                QMUIRadiusImageView imageView=new QMUIRadiusImageView(getContext());
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_music));
                itemView.addAccessoryCustomView(imageView);
                itemView.setDetailText(CustomTypeConverters.characterList2string(songDO.getSequence()));
            }
            //如果歌曲有下载好的头像，则发起一个异步任务，读取本地图片文件并解码图片，完成后自动更新视图
            if (songDO.getAvatarPath()!=null)
                new DecodeImageAsyncTask(itemView,getResources()).execute(songDO.getAvatarPath());
            section.addItemView(itemView,null);
        }
        section.addTo(groupListView);
    }




}
