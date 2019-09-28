package com.example.win.easy;

import android.test.UiThreadTest;

import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.base.MainThread;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.repository.repo.Repo;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.service.SongListWebService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RepoTestOnAndroid {

    public void addToDB(){
        allSong.add(songDO1);
        allSong.add(songDO2);
        allSongList.add(songListDO1);
        allSongList.add(songListDO2);
        allSongXSongList.add(relation_1);//1-1
        allSongXSongList.add(relation_2);//2-2
        //本地数据库添加数据
    }

    public void addToSongMap(){
        songMap.clear();
        List2.clear();//2-2,3
        List2.add(songDO2);
        List2.add(songDO3);
        List3.clear();//3-3
        List3.add(songDO3);
        songMap.put(songListDO2, List2);
        songMap.put(songListDO3, List3);
        //添加网络数据
    }
    public void reset(){
        songMap = new HashMap<>();
        List1 = new ArrayList<>();
        List2 = new ArrayList<>();
        List3 = new ArrayList<>();
        allSong = new ArrayList<>();
        allSongList = new ArrayList<>();
        allSongXSongList = new ArrayList<>();
        addToDB();
        //定义本地数据库
        addToSongMap();
        //初始化网络数据
    }

    @Test
    @UiThreadTest
    public void testInsert() {
        reset();
        repo.getAllSong();//fetch

        verify(songDao, times(1)).insert(songDO3);
        verify(songListDao, times(1)).insert(songListDO3);
        verify(songXSongListDao, times(2)).insert(any(SongXSongListDO.class));
    }

    @Test
    @UiThreadTest
    public void testDelete(){
        reset();
        repo.getAllSongList();//先fetch一次

        songMap.clear();List2.clear();List3.clear();
        List2.add(songDO2);
        List2.add(songDO3);
        songMap.put(songListDO2, List2);//重新构建一个songMap

        repo.getAllSongList();//再fetch一次

        verify(songDao, times(0)).delete(any(SongDO.class));
        verify(songListDao, times(1)).delete(songListDO3);
        verify(songXSongListDao, times(1)).delete(any(SongXSongListDO.class));
    }

    @Test
    @UiThreadTest
    public void testUpdate(){
        reset();
        repo.getAllSongList();

        songMap.clear();List2.clear();List3.clear();
        List2.add(songDO3);
        List3.add(songDO2);
        songMap.put(songListDO2, List2);
        songMap.put(songListDO3, List3);

        repo.getAllSong();

        verify(songDao, times(0)).delete(any(SongDO.class));
        verify(songListDao, atLeastOnce()).findAllDataOnWeb();
        verify(songXSongListDao, times(2)).delete(any(SongXSongListDO.class));
    }

    @Test
    @UiThreadTest
    public void testNullDB(){
        reset();
        allSong.clear();
        allSongList.clear();
        allSongXSongList.clear();

        repo.getAllSongList();

        verify(songDao, atLeastOnce()).getAllSong();
        verify(songDao, atLeastOnce()).findAllDataOnWeb();
    }

    @Test
    @UiThreadTest
    public void testSongOf(){
        reset();
        repo.getAllSong();
        List<SongDO> result1 = repo.songsOf(songListDO2);
        List<SongDO> result2 = repo.getSongNotIn(songListDO3);

        verify(songXSongListDao, atLeastOnce()).getSongsOf(any(Long.class));
    }

    @Before
    @UiThreadTest
    public void before() {
        reset();
        MockitoAnnotations.initMocks(this);


        //mock 模拟网络请求
        doAnswer(invocation -> {
            OnReadyFunc<Map<SongListDO, List<SongDO>>> onReadyFunc = invocation.getArgument(0);
            //抓取网络
            //获得数据
            onReadyFunc.onReady(songMap);
            return null;
        }).when(songListWebService).getAllSongLists(any(OnReadyFunc.class));

        //mock 根据Rid查询歌曲
        doAnswer(invocation -> {
            Long songId = invocation.getArgument(0);
            for (SongDO item : allSong) {
                if(item.remoteId==null) continue;
                if ((item.remoteId.longValue())==(songId.longValue())) {
                    return item;
                }
            }
            return null;
        }).when(songDao).findByRemoteId(any(Long.class));

        //mock 查询网络歌曲
        doAnswer(invocation -> {
            List<SongDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                if (item.remoteId != null)
                    answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songDao).findAllDataOnWeb();

        //mock 查询本地歌曲
        doAnswer(invocation -> {
            List<SongDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                if (item.remoteId == null)
                    answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songDao).findAllDataOnLocal();

        //mock 获取所有歌曲
        doAnswer(invocation -> {
            List<SongDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songDao).getAllSong();

        //mock 根据Rid查询歌单
        doAnswer(invocation -> {
            Long songListId = invocation.getArgument(0);
            for (SongListDO item : allSongList) {
                if(item.remoteId==null) continue;
                if ((item.remoteId.longValue())==(songListId.longValue())) {
                    return item;
                }
            }
            return null;
        }).when(songListDao).findByRemoteId(any(Long.class));

        //mock 查询网络歌单
        doAnswer(invocation -> {
            List<SongListDO> answer = new ArrayList<>();
            for (SongListDO item : allSongList) {
                if (item.remoteId != null)
                    answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songListDao).findAllDataOnWeb();

        //mock 查询本地歌单
        doAnswer(invocation -> {
            List<SongListDO> answer = new ArrayList<>();
            for (SongListDO item : allSongList) {
                if (item.remoteId == null)
                    answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songListDao).findAllDataOnLocal();

        //mock 获取所有歌单
        doAnswer(invocation -> {
            List<SongListDO> answer = new ArrayList<>();
            for (SongListDO item : allSongList) {
                answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songListDao).getAllSongList();

        //mock 根据s_id sl_id查询关联键
        doAnswer(invocation -> {
            Long songId = invocation.getArgument(0);
            Long songListId = invocation.getArgument(1);
            for (SongXSongListDO item: allSongXSongList) {
                if((item.songId.longValue())==(songId.longValue()) && (item.songListId.longValue())==(songListId.longValue())) {
                    return item;
                }
            }
            return null;
        }).when(songXSongListDao).findById(any(Long.class), any(Long.class));

        //mock 查询网络关联键
        doAnswer(invocation -> {
            List<SongXSongListDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                if (item.remoteId != null) {
                    for (SongXSongListDO item2 : allSongXSongList) {
                        if (item.id.longValue() == item2.songId.longValue()) {
                            answer.add(item2);
                        }
                    }
                }
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songXSongListDao).findAllDataOnWeb();

        //mock 查询本地关联键
        doAnswer(invocation -> {
            List<SongXSongListDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                if (item.remoteId == null) {
                    for (SongXSongListDO item2 : allSongXSongList) {
                        if (item.id.longValue() == item2.songId.longValue()) {
                            answer.add(item2);
                        }
                    }
                }
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songXSongListDao).findAllDataOnLocal();

        //mock 获取所有关联键
        doAnswer(invocation -> {
            List<SongXSongListDO> answer = new ArrayList<>();
            for(SongXSongListDO item: allSongXSongList){
                answer.add(item);
            }
            return answer.isEmpty()
                    ?null
                    :answer;
        }).when(songXSongListDao).getAllRelation();

        //mock 模拟歌曲插入
        doAnswer(invocation -> {
            SongDO song = invocation.getArgument(0);
            songIdRecord = songIdRecord + 1L;
            song.setId(songIdRecord);
            allSong.add(song);
            return songIdRecord;
        }).when(songDao).insert(any(SongDO.class));

        //mock 模拟歌曲删除
        doAnswer(invocation -> {
            SongDO song = invocation.getArgument(0);
            allSong.remove(song);
            return null;
        }).when(songDao).delete(any(SongDO.class));

        //mock 模拟歌单插入
        doAnswer(invocation -> {
            SongListDO songList = invocation.getArgument(0);
            songListIdRecord = songListIdRecord + 1L;
            songList.setId(songListIdRecord);
            allSongList.add(songList);
            return songListIdRecord;
        }).when(songListDao).insert(any(SongListDO.class));

        //mock 模拟歌单删除
        doAnswer(invocation -> {
            SongListDO songList = invocation.getArgument(0);
            allSongList.remove(songList);
            return null;
        }).when(songListDao).delete(any(SongListDO.class));

        //mock 模拟关联键插入
        doAnswer(invocation -> {
            SongXSongListDO songX = invocation.getArgument(0);
            allSongXSongList.add(songX);
            return null;
        }).when(songXSongListDao).insert(any(SongXSongListDO.class));

        //mock 模拟关联键删除
        doAnswer(invocation -> {
            SongXSongListDO songX = invocation.getArgument(0);
            allSongXSongList.remove(songX);
            return null;
        }).when(songXSongListDao).delete(any(SongXSongListDO.class));

        //mock 查询歌单中的歌曲
        doAnswer(invocation ->{
            Long songListId = invocation.getArgument(0);
            List<SongDO> tmp = new ArrayList<>();
            for(SongXSongListDO relation: allSongXSongList){
                if(relation.songListId.longValue() == songListId){
                    for(SongDO songDO: allSong){
                        if(relation.songId.longValue() == songDO.id.longValue()){
                            tmp.add(songDO);
                            break;
                        }
                    }
                }
            }
            if (tmp.isEmpty())
                return null;
            else
                return tmp;
        }).when(songXSongListDao).getSongsOf(any(Long.class));
    }

    @InjectMocks
    private Repo repo;
    @Mock
    private SongDao songDao;
    @Mock
    private SongListDao songListDao;
    @Mock
    private SongXSongListDao songXSongListDao;
    @Mock
    private SongListWebService songListWebService;
    @Mock
    Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    };

    private SongDO songDO1 = SongDO.builder().id(new Long(1)).name("某人-本地").build();
    private SongDO songDO2 = SongDO.builder().id(new Long(2)).remoteId(new Long(1)).name("某人-网络").build();
    private SongDO songDO3 = SongDO.builder().remoteId(new Long(2)).name("新-网络").build();
    private Long songIdRecord = new Long(2);

    private SongListDO songListDO1 = SongListDO.builder().id(new Long(1)).name("本地歌单").build();
    private SongListDO songListDO2 = SongListDO.builder().id(new Long(2)).remoteId(new Long(1)).name("网络歌单").build();
    private SongListDO songListDO3 = SongListDO.builder().remoteId(new Long(2)).name("新网络歌单").build();
    private Long songListIdRecord = new Long(2);

    private SongXSongListDO relation_1 = SongXSongListDO.builder().songId(new Long(1)).songListId(new Long(1)).build();
    private SongXSongListDO relation_2 = SongXSongListDO.builder().songId(new Long(2)).songListId(new Long(2)).build();

    private List<SongDO> List1;
    private List<SongDO> List2;
    private List<SongDO> List3;

    private List<SongDO> allSong;
    private List<SongListDO> allSongList;
    private List<SongXSongListDO> allSongXSongList;

    private Map<SongListDO, List<SongDO>> songMap;
}