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
import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.tool.SongListWithSongs;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.OnClickFunc;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.example.win.easy.viewmodel.SongViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.win.easy.enumeration.DataSource.Local;

public class SearchFragment extends Fragment {

    @BindView(R.id.switch_tab) SwitchTab switchTab;
    @BindString(R.string.defaultNameOfSongListOfAllSongs) String defaultSongListName;

    private ViewModelProvider.Factory viewModelFactory;
    private SongViewModel songViewModel;
    private SongListViewModel songListViewModel;
    private DisplayServiceAdapter displayServiceAdapter;
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
        songListViewModel=ViewModelProviders.of(this,viewModelFactory).get(SongListViewModel.class);
        return thisView;
    }

    public void search(List<Character> sequence){
        List<SongVO> songsMatched=songViewModel.songsMatch(sequence);
        List<SongListWithSongs> filledSongListGroupedBySource=groupBySource(songsMatched);

        switchTab.setPagesWithTab(
                filledSongListGroupedBySource,
                tabOnClickFuncWhenSearching(),
                songOnClickFunc());
    }

    private OnClickFunc<SongVO> songOnClickFunc(){
        return (song,itemView)->toSwitchMode(song);
    }

    private void toSwitchMode(SongVO selectedSong){
        record(selectedSong);

        List<SongListWithSongs> filledSongListsContainSelectedSong= filledSongListsContain(selectedSong);
        display(selectedSong,filledSongListsContainSelectedSong);

        switchTab.setPagesWithTab(
                filledSongListsContainSelectedSong,
                tabOnClickFuncWhenSwitching(),
                songOnClickFunc());
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
        List<SongListVO> songListsContainIt=songViewModel.songListsContain(songVO);

        return isEmpty(songListsContainIt)
                ? defaultSingleFilledSongList()
                : filledSongLists(songListsContainIt);
    }

    private boolean isEmpty(List<SongListVO> songLists){
        return songLists.size()==0;
    }

    private List<SongListWithSongs> defaultSingleFilledSongList(){
        List<SongListWithSongs> singleSongListList=new ArrayList<>();

        List<SongVO> allSongs=songViewModel.getAllSongs().getValue();
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
            List<SongVO> songsIn=songListViewModel.songsOf(songList);

            SongListWithSongs filledSongLit=SongListWithSongs.builder().songList(songList).songs(songsIn).build();
            filledSongLists.add(filledSongLit);
        }
        return filledSongLists;
    }

    private OnClickFunc<SongListWithSongs> tabOnClickFuncWhenSearching(){
        return (songListWithSongs,itemView)->{};
    }

    private List<SongListWithSongs> groupBySource(List<SongVO> songs){
        Map<DataSource,List<SongVO>> src2ListMap=new HashMap<>();
        List<DataSource> songSrcs= Arrays.asList(DataSource.values());
        for (DataSource songSrc:songSrcs)
            src2ListMap.put(songSrc,new ArrayList<>());
        for (SongVO song:songs){
            DataSource songSrc=song.getSource();
            src2ListMap.get(songSrc).add(song);
        }
        System.out.println(songSrcs);
        List<SongListWithSongs> groupedSongs=new ArrayList<>();
        for (DataSource src:songSrcs){
            SongListVO srcSongList= fromSrc(src);
            List<SongVO> songsFromSrc=src2ListMap.get(src);
            if (songsFromSrc.size()!=0)
                groupedSongs.add(SongListWithSongs.builder().songList(srcSongList).songs(songsFromSrc).build());
        }
        return groupedSongs;
    }

    private SongListVO fromSrc(DataSource src){
        return SongListVO.builder()
                .name(src.name())
                .dataSource(src)
                .build();
    }
}
