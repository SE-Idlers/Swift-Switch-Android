package com.example.win.easy.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.factory.__SongFactory;
import com.example.win.easy.repository.db.CustomTypeConverters;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.tool.UriProcessTool;
import com.example.win.easy.view.EntityItem;
import com.example.win.easy.view.OnClickFunc;
import com.example.win.easy.viewmodel.SimpleViewModel;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>展示所有歌曲的界面</p>
 */
public class AllSongsFragment extends ListFragment {

    private ViewModelProvider.Factory factory;
    private __SongFactory songFactory;
    private SimpleViewModel viewModel;

    public AllSongsFragment(ViewModelProvider.Factory factory,__SongFactory songFactory){
        this.factory=factory;
        this.songFactory=songFactory;
    }

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
        viewModel = ViewModelProviders.of(this,factory).get(SimpleViewModel.class);
        viewModel.getAllSongs().observe(this, this::update);
    }

    /**
     * 根据最新的歌曲数据刷新视图
     * @param songDOs 最新的歌曲数据
     */
    public void update(List<SongDO> songDOs){
        if (songDOs.size()==0) {
            songDOs.add(SongDO.builder().id(78L).name("测试测试and测试 - 桂喜老师").source(DataSource.Local).sequence(new ArrayList<>()).build());
            songDOs.add(SongDO.builder().id(99L).name("One Punch").source(DataSource.Local).sequence(new ArrayList<>()).build());
        }
        List<QMUICommonListItemView> songItemViews=new ArrayList<>();
        //对每个歌单都生成一个itemView
        for (int i=0;i<songDOs.size();i++){
            songItemViews.add(new SongItem(songDOs.get(i),(song,view)->{
                //TODO 触发歌曲播放
                Toast.makeText(getContext(),"待实现：点击播放歌曲",Toast.LENGTH_SHORT).show();
            }));
        }
        setItemViews(songItemViews);
    }


    class SongItem extends EntityItem<SongDO> {

        public SongItem(SongDO entity, OnClickFunc<SongDO> onClickFunc) {
            super(AllSongsFragment.this.getContext(), entity, onClickFunc);
            //显示歌曲名称
            setText(entity.getName());
            //显示歌曲默认头像
            setImageDrawable(getResources().getDrawable(R.drawable.ase16));
            //itemView最右侧显示自定义的视图（一张图片）
            setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
            //如果歌曲已经下载好，则显示它的sequence，同时在最右侧显示一张音符图片
            if (entity.getSongPath()!=null){
                QMUIRadiusImageView imageView=new QMUIRadiusImageView(getContext());
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_music));
                addAccessoryCustomView(imageView);
                setDetailText(CustomTypeConverters.characterList2string(entity.getSequence()));
            }
            //如果歌曲有下载好的头像，则发起一个异步任务，读取本地图片文件并解码图片，完成后自动更新视图
//            if (entity.getAvatarPath()!=null)
//                new DecodeImageAsyncTask(this,getResources()).execute(entity.getAvatarPath());
        }
    }



}
