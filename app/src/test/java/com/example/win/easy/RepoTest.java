package com.example.win.easy;

import android.test.UiThreadTest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.repository.repo.Repo;
import com.example.win.easy.tool.SongList;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.service.SongListWebService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepoTest {
    @InjectMocks
    private Repo repo;
    @Mock
    private Executor diskIO;
    @Mock
    private SongDao songDao;
    @Mock
    private SongListDao songListDao;
    @Mock
    private SongXSongListDao songXSongListDao;
    @Mock
    private SongListWebService songListWebService;

    private SongDO songDO1 = SongDO.builder().id(1).remoteId("1").name("某人-网络本地").build();
    private SongDO songDO2 = SongDO.builder().id(2).remoteId(null).name("某人-本地").build();
    private SongDO new_Web_song = SongDO.builder().id(3).remoteId("2").name("新-网络本地").build();

    private SongListDO songListDO1 = SongListDO.builder().id(1).remoteId("1").name("网络歌单").build();
    private SongListDO songListDO2 = SongListDO.builder().id(2).remoteId(null).name("本地歌单").build();
    private SongListDO new_Web_songList = SongListDO.builder().id(3).remoteId("3").name("新歌单").build();

    private SongXSongListDO WebList1 = SongXSongListDO.builder().songId(1).songListId(1).build();
    private SongXSongListDO LocalList1 = SongXSongListDO.builder().songId(2).songListId(2).build();
    private SongXSongListDO new_Web_List1 = SongXSongListDO.builder().songId(3).songListId(3).build();
    private SongXSongListDO new_Web_List2 = SongXSongListDO.builder().songId(3).songListId(1).build();

    private List<SongDO> List1;
    private List<SongDO> List2;
    private List<SongDO> List3;

    private List<Long> songIdList;
    private List<Long> songListIdList;
    private List<String> songXSongListIdList;

    private List<SongDO> allSong;
    private List<SongListDO> allSongList;
    private List<SongXSongListDO> allSongXSongList;

    private MutableLiveData<List<SongDO>> songDOS;
    private MutableLiveData<List<SongListDO>> songListDOS;
    private MutableLiveData<List<SongXSongListDO>> songXSongListDOS;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        new_Web_List1 = SongXSongListDO.builder().songId(3).songListId(1).build();
    }

    @Test
    @UiThreadTest
    public void testSongUpdate(){
        Map<SongListDO, List<SongDO>> songMap = new HashMap<>();
        List1 = new ArrayList<>();
        List1.add(songDO1);
        List2 = new ArrayList<>();
        List2.add(songDO2);
        List3 = new ArrayList<>();
        List3.add(new_Web_song);
        List3.add(songDO1);
        songMap.put(songListDO1, List1);
        songMap.put(songListDO2, List2);
        songMap.put(new_Web_songList, List3);
        songIdList = new ArrayList<>();
        songListIdList = new ArrayList<>();
        songXSongListIdList = new ArrayList<>();
        songIdList.add((long)(1));songIdList.add((long)(2));
        songListIdList.add((long)(1));songListIdList.add((long)(2));
        songXSongListIdList.add("1"+" "+"1");
        songXSongListIdList.add("2"+" "+"2");
        allSong = new ArrayList<>();
        allSongList = new ArrayList<>();
        allSongXSongList = new ArrayList<>();
        allSong.add(songDO1);
        allSong.add(songDO2);
        allSongList.add(songListDO1);
        allSongList.add(songListDO2);
        allSongXSongList.add(WebList1);
        allSongXSongList.add(LocalList1);
        songDOS = new MutableLiveData<>();
        songListDOS = new MutableLiveData<>();
        songXSongListDOS = new MutableLiveData<>();
        songDOS.setValue(allSong);
        songListDOS.setValue(allSongList);
        songXSongListDOS.setValue(allSongXSongList);
        doAnswer(invocation -> {
            OnReadyFunc<Map<SongListDO,List<SongDO>>> onReadyFunc=invocation.getArgument(0);

            //抓取网络
            //获得数据
            onReadyFunc.onReady(songMap);
            return null;
        }).when(songListWebService).getAllSongLists(any(OnReadyFunc.class));
        doAnswer(invocation -> {
            long songId = invocation.getArgument(0);
            if(songIdList.contains(songId)){
                MutableLiveData<SongDO> result = new MutableLiveData<>();
                result.setValue(SongDO.builder().build());
                return result;
            }
            else
                return null;
        }).when(songDao).findById(any());
        doAnswer(invocation -> {
            MutableLiveData<List<SongDO>> result = new MutableLiveData<>();
            List<SongDO> answer = new ArrayList<>();
            for(SongDO item: allSong){
                if(item.remoteId != null)
                    answer.add(item);
            }
            result.setValue(answer);
            return result;
        }).when(songDao).findAllDataOnWeb();
        doAnswer(invocation -> {
            long songListId = invocation.getArgument(0);
            if(songListIdList.contains(songListId)) {
                MutableLiveData<SongListDO> result = new MutableLiveData<>();
                result.setValue(SongListDO.builder().build());
                return result;
            }
            else
                return null;
        }).when(songListDao).findById(any());
        doAnswer(invocation -> {
            MutableLiveData<List<SongListDO>> result = new MutableLiveData<>();
            List<SongListDO> answer = new ArrayList<>();
            for(SongListDO item: allSongList){
                if(item.remoteId != null)
                    answer.add(item);
            }
            result.setValue(answer);
            return result;
        }).when(songListDao).findAllDataOnWeb();
        doAnswer(invocation -> {
            long songId = invocation.getArgument(0);
            long songListId = invocation.getArgument(1);
            if(songXSongListIdList.contains((char)(songId+48)+" "+(char)(songListId+48))) {
                MutableLiveData<SongXSongListDO> result = new MutableLiveData<>();
                result.setValue(SongXSongListDO.builder().build());
                return result;
            }
            else
                return null;
        }).when(songXSongListDao).findbyID(any(), any());
        doAnswer(invocation -> {
            MutableLiveData<List<SongXSongListDO>> result = new MutableLiveData<>();
            List<SongXSongListDO> answer = new ArrayList<>();
            for(SongDO item: allSong){
                if(item.remoteId!=null){
                    for(SongXSongListDO item2: allSongXSongList){
                        if(item.id == item2.songId){
                            answer.add(item2);
                        }
                    }
                }
            }
            result.setValue(answer);
            return result;
        }).when(songXSongListDao).findAllDataOnWeb();
        doAnswer(invocation -> {
            SongDO song = invocation.getArgument(0);
            songIdList.add(song.id);
            allSong.add(song);
            songDOS.setValue(allSong);
            return null;
        }).when(songDao).insert(any(SongDO.class));
        doAnswer(invocation -> {
            SongDO song = invocation.getArgument(0);
            songIdList.remove(song.id);
            allSong.remove(song);
            songDOS.setValue(allSong);
            return null;
        }).when(songDao).delete(any(SongDO.class));
        doAnswer(invocation -> {
            SongListDO songList = invocation.getArgument(0);
            songIdList.add(songList.id);
            allSongList.add(songList);
            songListDOS.setValue(allSongList);
            return null;
        }).when(songListDao).insert(any(SongListDO.class));
        doAnswer(invocation -> {
            SongListDO songList = invocation.getArgument(0);
            songIdList.remove(songList.id);
            allSongList.remove(songList);
            songListDOS.setValue(allSongList);
            return null;
        }).when(songListDao).delete(any(SongListDO.class));
        doAnswer(invocation -> {
            SongXSongListDO songX = invocation.getArgument(0);
            songXSongListIdList.add((char)(songX.songId+48)+" "+(char)(songX.songListId+48));
            allSongXSongList.add(songX);
            songXSongListDOS.setValue(allSongXSongList);
            return null;
        }).when(songXSongListDao).insert(any(SongXSongListDO.class));
        doAnswer(invocation -> {
            SongXSongListDO songX = invocation.getArgument(0);
            songXSongListIdList.remove((char)(songX.songId+48)+" "+(char)(songX.songListId+48));
            allSongXSongList.remove(songX);
            songXSongListDOS.setValue(allSongXSongList);
            return null;
        }).when(songXSongListDao).delete(any(SongXSongListDO.class));

        System.out.print(repo.getAllSong());

    }
}
