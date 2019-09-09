package com.example.win.easy.view.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.win.easy.R;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.view.EntityItem;
import com.example.win.easy.view.ImageService;
import com.example.win.easy.view.OnClickFunc;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>用于展示所有歌单的界面</p>
 */
public class AllSongListsFragment extends ListFragment {

    private ViewModelProvider.Factory factory;
    private String barTitle="所有歌单";
    private ImageService imageService;

    public AllSongListsFragment(ViewModelProvider.Factory factory,ImageService imageService){
        this.factory=factory;
        this.imageService=imageService;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View thisView=super.onCreateView(inflater,container,savedInstanceState);
        //设置标题
        setTopBarTitle(barTitle);
        //右上角按钮用于创建新的歌单
        setRightImageButtonOnClickListener(v-> Navigation.findNavController(v).navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToPlaceholder()));
        SongListViewModel songListViewModel=ViewModelProviders.of(this,factory).get(SongListViewModel.class);
        songListViewModel.getAll().observe(this,this::update);
        return thisView;
    }

    /**
     * <p>根据最新的歌单及关系数据刷新视图</p>
     * @param songLists 最新的歌单数据
     */
    public void update(List<SongListVO> songLists){
        List<QMUICommonListItemView> songListItemViews=new ArrayList<>();

        //对每个歌单都生成一个itemView
        for (int i=0;i<songLists.size();i++){
            songListItemViews.add(new SongListItem(
                    songLists.get(i),
                    (songList,view)-> Navigation.findNavController(getView()).navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToSongListFragment(songList)))
            );
        }

        //显示之
        setItemViews(songListItemViews);
    }

    /**
     * <p>这个列表页面中的每一个Item对应的对象</p>
     */
    class SongListItem extends EntityItem<SongListVO> {
        public SongListItem(SongListVO entity, OnClickFunc<SongListVO> onClickFunc) {
            super(AllSongListsFragment.this.getContext(), entity, onClickFunc);

            setText(entity.getName());//显示歌单名字
            setImageDrawable(getResources().getDrawable(R.drawable.ase16));//显示歌单默认头像，如果后续发现有下载好的头像，则替换
            setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);//item最右侧显示">"
            if(entity.getAvatarPath()!=null)//如果歌单已经下载自己的头像，则发起一个解码，成功后设置头像
                imageService.decode(entity.getAvatarPath(),bitmap -> setImageDrawable(new BitmapDrawable(getResources(),bitmap)));
        }
    }
}