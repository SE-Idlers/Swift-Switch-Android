package com.example.win.easy.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.win.easy.R;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.EntityItem;
import com.example.win.easy.view.OnClickFunc;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class AddSongToSongListFragment extends ListFragment {

    private String topBarTitle="不在这个歌单里的歌曲";
    private ViewModelProvider.Factory viewModelFactory;
    private SongListViewModel songListViewModel;
    private SongListVO thisSongList;
    private List<SongVO> songsNotInThisSongList;
    private List<SongVO> songsWillAdd =new ArrayList<>();

    public AddSongToSongListFragment(ViewModelProvider.Factory viewModelFactory){
        this.viewModelFactory=viewModelFactory;
    }

    /**
     * <ol>
     *     <li>获取传递进来的歌单</li>
     *     <li>获取viewModel</li>
     *     <li>获取不在这个歌单里的歌曲（也就是这个视图要显示的）</li>
     *     <li>刷新页面视图</li>
     *     <li>设置右上角“添加”按钮</li>
     *     <li>设置页面标题</li>
     * </ol>
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View thisView=super.onCreateView(inflater, container, savedInstanceState);

        thisSongList=getPassedSongList();
        songListViewModel= getSongListViewModel();
        songsNotInThisSongList= getSongsNotIn(thisSongList);

        updateViewWith(songsNotInThisSongList);
        setUpAddButton();
        setUpTopBarTitle();
        return thisView;
    }

    private SongListVO getPassedSongList(){
        return AddSongToSongListFragmentArgs.fromBundle(getArguments()).getSongListSongsAddedTo();
    }

    private SongListViewModel getSongListViewModel() {
        return ViewModelProviders.of(this,viewModelFactory).get(SongListViewModel.class);
    }

    private List<SongVO> getSongsNotIn(SongListVO songListVO){
        return songListViewModel.songsNotIn(songListVO);
    }

    private void updateViewWith(List<SongVO> songsNotInThisSongList){
        List<QMUICommonListItemView> songItemViews=new ArrayList<>();
        for (SongVO songVO: songsNotInThisSongList){
            songItemViews.add(item(songVO,(thisSongItem,v)->{
                thisSongItem.clicked();

                if (thisSongItem.isSelected()) songsWillAdd.add(thisSongItem.getSongVO());
                else songsWillAdd.remove(thisSongItem.getSongVO());

                updateSelectionView(thisSongItem.isSelected(),v);
            }));
        }
        setItemViews(songItemViews);
    }

    private SongAddItem item(SongVO songVO,OnClickFunc<SongWithSelectedInfo> onClickFunc){
        return new SongAddItem(new SongWithSelectedInfo(songVO),onClickFunc);
    }

    private void updateSelectionView(boolean selected,QMUICommonListItemView itemView){
        if (selected) itemView.setImageDrawable(getResources().getDrawable(R.drawable.ase16));//显示歌曲默认头像，如果后续发现有下载好的头像，则替换
        else itemView.setImageDrawable(null);
    }

    private void setUpAddButton() {
        setRightImageButtonOnClickListener(v-> {
            songListViewModel.addSongsTo(songsWillAdd,thisSongList);
            Navigation.findNavController(getView()).navigate(AddSongToSongListFragmentDirections.actionAddSongToSongListFragmentToSongListFragment(thisSongList));
        });
    }

    private void setUpTopBarTitle() {
        setTopBarTitle(topBarTitle);
    }

    class SongAddItem extends EntityItem<SongWithSelectedInfo>{

        SongAddItem(SongWithSelectedInfo entity, OnClickFunc<SongWithSelectedInfo> onClickFunc) {
            super(AddSongToSongListFragment.this.getContext(),entity , onClickFunc);
            setText(entity.getSongVO().getName());//显示歌曲名字
            updateSelectionView(entity.isSelected(),this);
        }

    }
}

/**
 * <p>带着被选择信息的歌曲对象</p>
 * <p>ps:纯粹是出于点击需要</p>
 */
@Data
class SongWithSelectedInfo {
    private SongVO songVO;
    private boolean selected;

    //初始化的时候没被选择
    SongWithSelectedInfo(SongVO songVO){
        this.songVO=songVO;
        this.selected=false;
    }

    void clicked(){
        setSelected(!selected);
    }
}
