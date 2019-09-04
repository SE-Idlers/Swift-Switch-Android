package com.example.win.easy.view.fragment;

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

import com.example.win.easy.R;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.viewmodel.SimpleViewModel;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>用于展示所有歌单的界面</p>
 */
public class AllSongListsFragment extends ListFragment {

    private SimpleViewModel viewModel;
    private ViewModelProvider.Factory factory;
    private LiveData<List<SongListDO>> allSongLists;
    private LiveData<List<SongXSongListDO>> allRelation;
    private QMUIGroupListView.Section section;

    public AllSongListsFragment(ViewModelProvider.Factory factory){
        this.factory=factory;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View thisView=super.onCreateView(inflater,container,savedInstanceState);
        //设置标题
        setTopBarTitle("所有歌单");
        //右上角按钮用于创建新的歌单
        setRightImageButtonOnClickListener(v->{
            //TODO 点击按钮创建歌单
            Toast.makeText(getContext(),"待实现：点击添加歌单",Toast.LENGTH_SHORT).show();
        });
        return thisView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //注册数据监听
        viewModel= ViewModelProviders.of(this,factory).get(SimpleViewModel.class);
        allSongLists=viewModel.getAllSongLists();
        allRelation=viewModel.getAllRelation();
        allRelation.observe(this,songXSongLists->update(allSongLists.getValue()==null?new ArrayList<>():allSongLists.getValue(),songXSongLists));
        allSongLists.observe(this,songListPojos -> update(songListPojos,allRelation.getValue()==null?new ArrayList<>():allRelation.getValue()));
    }

    /**
     * 根据最新的歌单及关系数据刷新视图
     * @param songListDOS 最新的歌单数据
     * @param allRelation 最新的关系数据
     */
    public void update(List<SongListDO> songListDOS, List<SongXSongListDO> allRelation){
        //每次刷新时都重新创建section
        if (section!=null)
            section.removeFrom(groupListView);
        section=QMUIGroupListView.newSection(getContext());
        //对每个歌单都生成一个itemView
        for (SongListDO songListDO : songListDOS) {
            QMUICommonListItemView itemView=groupListView.createItemView(LinearLayout.VERTICAL);
            //显示歌单名字
            itemView.setText(songListDO.getName());
            //显示歌单默认头像，如果后续发现有下载好的头像，则替换
            itemView.setImageDrawable(getResources().getDrawable(R.drawable.ase16));
            //item右侧显示歌单内歌曲数量
            itemView.setDetailText(String.valueOf(sizeOf(songListDO,allRelation)));
            //item最右侧显示">"
            itemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
            //若歌单已下载自己的头像，则发起一个解码的异步任务（因为解码耗时较长，会阻塞主线程）,解码结束后自动更新头像
            if (songListDO.getAvatarPath()!=null)
                new DecodeImageAsyncTask(itemView,getResources()).execute(songListDO.getAvatarPath());
            //设置item点击监听
            itemView.setOnClickListener(v -> {
                //TODO 触发切换Fragment
                Toast.makeText(getContext(),"待实现：触发切换Fragment",Toast.LENGTH_SHORT).show();
            });
            section.addItemView(itemView,null);
        }
        section.addTo(groupListView);
    }

    /**
     * 根据ManyToMany的关系列表计算某个歌单中歌曲的数量
     * @param songListDO 歌单
     * @param allRelation 关系表
     * @return 该歌单中歌曲的数量
     */
    private int sizeOf(SongListDO songListDO, List<SongXSongListDO> allRelation){
        int size=0;
        for (SongXSongListDO songXSongListDO :allRelation) {
            if (songXSongListDO.getSongListId() == songListDO.getId())
                size++;
        }
        return size;
    }
}