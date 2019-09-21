package com.example.win.easy.view.lock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.win.easy.R;
import com.example.win.easy.display.DisplayServiceAdapter;
import com.example.win.easy.tool.SongListWithSongs;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.OnClickFunc;
import com.example.win.easy.viewmodel.SongViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.example.win.easy.enumeration.DataSource.Local;

public class SearchFragment extends Fragment {

    private ViewModelProvider.Factory viewModelFactory;
    private SongViewModel songViewModel;
    private DisplayServiceAdapter displayServiceAdapter;
    private SwitchTab switchTab;
    private String defaultSongListName="所有歌曲";
    private SongVO lastSelectedSong;

    public SearchFragment(ViewModelProvider.Factory viewModelFactory,DisplayServiceAdapter displayServiceAdapter){
        this.viewModelFactory=viewModelFactory;
        this.displayServiceAdapter=displayServiceAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView=inflater.inflate(R.layout.fragment_search,container,false);
        ButterKnife.bind(this,thisView);

        songViewModel= ViewModelProviders.of(this,viewModelFactory).get(SongViewModel.class);
        switchTab=new SwitchTab(getContext());
        return thisView;
    }

    public void search(List<Character> sequence){
        //TODO 从viewModel获取
        List<SongVO> songsMatched=new ArrayList<>();

        List<SongListWithSongs> filledSongListGroupedBySource=groupBySource(songsMatched);
        OnClickFunc<SongVO> songOnClickFunc= songOnClickFunc();
        OnClickFunc<SongListWithSongs> tabOnClickFunc=tabOnClickFuncWhenSearching();

        switchTab.setPagesWithTab(filledSongListGroupedBySource,tabOnClickFunc,songOnClickFunc);
    }

    private OnClickFunc<SongVO> songOnClickFunc(){
        return (song,itemView)->toSwitchMode(song);
    }

    private void toSwitchMode(SongVO selectedSong){
        record(selectedSong);
        //TODO
        List<SongListWithSongs> filledSongListsContainSelectedSong= filledSongListsContain(selectedSong);
        display(selectedSong,filledSongListsContainSelectedSong);

        OnClickFunc<SongVO> songOnClickFunc= songOnClickFunc();
        OnClickFunc<SongListWithSongs> tabOnClickFunc=tabOnClickFuncWhenSwitching();

        switchTab.setPagesWithTab(filledSongListsContainSelectedSong,tabOnClickFunc,songOnClickFunc);
    }

    private void display(SongVO selectedSong, List<SongListWithSongs> filledSongListsContainIt) {
        List<SongVO> defaultSongListChosen=chooseFirst(filledSongListsContainIt);
        displayServiceAdapter.startWith(selectedSong,defaultSongListChosen);
    }

    private List<SongVO> chooseFirst(List<SongListWithSongs> filledSongListsContainSelectedSong) {
        return filledSongListsContainSelectedSong.get(0).getSongs();
    }

    private void record(SongVO selectedSong) {
        lastSelectedSong=selectedSong;
    }

    private OnClickFunc<SongListWithSongs> tabOnClickFuncWhenSwitching(){
        return (songListWithSongs,itemView)->{
            if (ableToReplaceSongList())
                tryToReplaceSongListWith(songListWithSongs.getSongs());
        };
    }

    private void tryToReplaceSongListWith(List<SongVO> songList){
        try {
            displayServiceAdapter.replaceSongList(songList);
        } catch (FailToReplaceSongListException e) {
            e.printStackTrace();
        }
    }

    private boolean ableToReplaceSongList(){
        return displayServiceAdapter.getCurrentDisplayedSong().equals(lastSelectedSong);
    }


    private List<SongListWithSongs> filledSongListsContain(SongVO songVO){
        //TODO 从viewModel获取
        List<SongListVO> songListsContainIt=new ArrayList<>();

        return isEmpty(songListsContainIt)
                ? defaultSingleFilledSongList()
                : filledSongLists(songListsContainIt);
    }

    private boolean isEmpty(List<SongListVO> songLists){
        return songLists.size()==0;
    }

    private List<SongListWithSongs> defaultSingleFilledSongList(){
        List<SongListWithSongs> singleSongListList=new ArrayList<>();

        //TODO 从viewModel获取
        List<SongVO> allSongs=new ArrayList<>();
        SongListVO defaultSongList=createDefaultSongList();

        singleSongListList.add(SongListWithSongs.builder().songList(defaultSongList).songs(allSongs).build());

        return singleSongListList;
    }

    private SongListVO createDefaultSongList(){
        return SongListVO.builder()
                .name(defaultSongListName)
                .dataSource(Local)
                .build();
    }

    private List<SongListWithSongs> filledSongLists(List<SongListVO> songLists){
        List<SongListWithSongs> filledSongLists=new ArrayList<>();
        for (SongListVO songList:songLists){
            //TODO 从viewModel中获取
            List<SongVO> songsIn=new ArrayList<>();

            SongListWithSongs filledSongLit=SongListWithSongs.builder().songList(songList).songs(songsIn).build();
            filledSongLists.add(filledSongLit);
        }
        return filledSongLists;
    }

    private OnClickFunc<SongListWithSongs> tabOnClickFuncWhenSearching(){
        return (songListWithSongs,itemView)->{};
    }

    private List<SongListWithSongs> groupBySource(List<SongVO> songs){
        //TODO
        return null;
    }
}
