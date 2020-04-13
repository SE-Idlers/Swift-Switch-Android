package com.example.win.easy.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.win.easy.R;
import com.example.win.easy.display.DisplayServiceAdapter;
import com.example.win.easy.download.DownloadServiceAdapter;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.EntityItem;
import com.example.win.easy.view.OnClickFunc;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.List;

import kotlin.NotImplementedError;

public class SongListFragment extends ListFragment {

    private SongListVO thisSongList;//这个歌单
    private List<SongVO> songsInThisSongList;//歌单里的所有歌

    private DisplayServiceAdapter displayServiceAdapter;
    private DownloadServiceAdapter downloadServiceAdapter;
    private ViewModelProvider.Factory factory;
    private SongListViewModel songListViewModel;

    public SongListFragment(DisplayServiceAdapter displayServiceAdapter,DownloadServiceAdapter downloadServiceAdapter, ViewModelProvider.Factory factory){
        this.displayServiceAdapter = displayServiceAdapter;
        this.downloadServiceAdapter=downloadServiceAdapter;
        this.factory=factory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=super.onCreateView(inflater, container, savedInstanceState);

        songListViewModel=getViewModel();
        thisSongList=getPassedSongList();
        songsInThisSongList=getSongsIn(thisSongList);

        updateViewWith(songsInThisSongList);
        setTopBarTitle(thisSongList.getName());
        setUpRightImageButton();

        return view;
    }

    private SongListViewModel getViewModel() {
        return ViewModelProviders.of(this,factory).get(SongListViewModel.class);
    }

    private SongListVO getPassedSongList(){
        throw new NotImplementedError();
        // TODO
//        return SongListFragmentArgs.fromBundle(getArguments()).getSelectedSongList();
    }

    private List<SongVO> getSongsIn(SongListVO songListVO) {
        return songListViewModel.songsOf(songListVO);
    }

    private void updateViewWith(List<SongVO> newSongs){
        List<QMUICommonListItemView> songItems=createSongItemViews(newSongs);
        setItemViews(songItems);
    }

    private List<QMUICommonListItemView> createSongItemViews(List<SongVO> songVOs){
        List<QMUICommonListItemView> songItems=new ArrayList<>();
        for (SongVO songVO:songVOs)
            songItems.add(item(songVO));
        return songItems;
    }

    private SongItem item(SongVO songVO){
        //主要是设置监听事件
        return new SongItem(songVO, (songOfTheItem,itemView)->{
            if (songOfTheItem.songFileHasBeenDownloaded())//如果歌曲文件已经下载就直接播放
                display(songOfTheItem);
            else if(songOfTheItem.songFileCanBeDownloaded())//如果没有下载，那就下载后再播放
                downloadThenDisplay(songOfTheItem);
        });
    }

    private void display(SongVO songVO){
        displayServiceAdapter.startWith(songVO, songsInThisSongList);
    }

    private void downloadThenDisplay(SongVO songVO){
        downloadServiceAdapter.download(songVO, this::display);
    }

    private void setUpRightImageButton(){
        setRightImageButtonOnClickListener(v-> Navigation.findNavController(getView()).navigate(SongListFragmentDirections.actionSongListFragmentToAddSongToSongListFragment(thisSongList)));
    }

    class SongItem extends EntityItem<SongVO> {

        public SongItem(SongVO entity, OnClickFunc<SongVO> onClickFunc) {
            super(SongListFragment.this.getContext(), entity, onClickFunc);
            setText(entity.getName());//显示歌曲名字
            setImageDrawable(getResources().getDrawable(R.drawable.ase16));//显示歌曲默认头像，如果后续发现有下载好的头像，则替换
            if (!entity.songFileHasBeenDownloaded())
                setBackgroundColor(getResources().getColor(R.color.app_color_description));
        }
    }
}
