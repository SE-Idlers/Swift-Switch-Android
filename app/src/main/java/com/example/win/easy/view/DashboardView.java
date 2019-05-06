package com.example.win.easy.view;

import android.app.Activity;

import com.example.win.easy.DashBoard;
import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.song.Song;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.TemporaryListGenerator;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.interfaces.SearchingView;
import com.example.win.easy.view.interfaces.SongListView;

import java.util.List;

public class DashboardView extends Activity
        implements SongListView, SearchingView {

    private TemporaryListGenerator tool = TemporaryListGenerator.getInstance();
    private SongListManager songListManager=SongListMangerImpl.getInstance();
    private DashBoard dashBoard = MainActivity.mainActivity.findViewById(R.id.dash_board);

    private static DashboardView instance = new DashboardView();

    public static DashboardView getInstance() {
        return instance;
    }

    private DashboardView() {
    }

    @Override
    public void update(Song song) {
        //获取歌曲出现过的所有歌单
        List<SongList> appearanceLists =songListManager.appearanceListsOf(song);
        //设置dashBoard
        dashBoard.setup(appearanceLists, DashBoard.DashBoardType.SwitchSongList);
    }

    @Override
    public void update(List<Integer> sortedIndices) {
        //获得按来源得到的结果列表
        List<SongList> candidates = tool.toSearchResult(sortedIndices);
        //设置dashBoard
        dashBoard.setup(candidates, DashBoard.DashBoardType.SelectingSong);
    }
}
