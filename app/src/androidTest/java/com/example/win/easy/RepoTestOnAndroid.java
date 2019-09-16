package com.example.win.easy;

import android.test.UiThreadTest;

import androidx.lifecycle.MutableLiveData;
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
        allSongXSongList.add(relation_1);
        allSongXSongList.add(relation_2);
        //本地数据库添加数据
    }

    public void reset(){
        songMap = new HashMap<>();
        List2 = new ArrayList<>();
        List3 = new ArrayList<>();
        allSong = new ArrayList<>();
        allSongList = new ArrayList<>();
        allSongXSongList = new ArrayList<>();
        addToDB();
        songDOS = new MutableLiveData<>();
        songListDOS = new MutableLiveData<>();
        songXSongListDOS = new MutableLiveData<>();
        songDOS.setValue(allSong);
        songListDOS.setValue(allSongList);
        songXSongListDOS.setValue(allSongXSongList);
        //定义本地数据库
        songMap.clear();
        List2.clear();List3.clear();
        List2.add(songDO2);
        List2.add(songDO3);
        List3.add(songDO3);
        songMap.put(songListDO2, List2);
        songMap.put(songListDO3, List3);
        //公共变量初始化
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
        songDOS.setValue(null);
        songListDOS.setValue(null);
        songXSongListDOS.setValue(null);
        repo.getAllSongList();

        verify(songDao, atLeastOnce()).findAllSongDOs();
        verify(songDao, atLeastOnce()).findAllDataOnWeb();
    }

    @Test
    @UiThreadTest
    public void testSongOf(){
        reset();
        repo.fetch();
        List<SongDO> result1 = repo.songsOf(songListDO2);
        List<SongDO> result2 = repo.getSongNotIn(songListDO3);
    }

    @Before
    @UiThreadTest
    public void before() {
        MockitoAnnotations.initMocks(this);

        reset();

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
                    MutableLiveData<SongDO> result = new MutableLiveData<>();
                    result.setValue(item);
                    return result;
                }
            }
            return new MutableLiveData<SongListDO>();
        }).when(songDao).findByRemoteId(any(Long.class));

        //mock 查询网络歌曲
        doAnswer(invocation -> {
            MutableLiveData<List<SongDO>> result = new MutableLiveData<>();
            List<SongDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                if (item.remoteId != null)
                    answer.add(item);
            }
            result.setValue(answer);
            return result;
        }).when(songDao).findAllDataOnWeb();

        //mock 获取所有歌曲
        doAnswer(invocation -> {
            MutableLiveData<List<SongDO>> result = new MutableLiveData<>();
            List<SongDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                answer.add(item);
            }
            result.setValue(answer);
            return result;
        }).when(songDao).findAllSongDOs();

        //mock 根据Rid查询歌单
        doAnswer(invocation -> {
            Long songListId = invocation.getArgument(0);
            for (SongListDO item : allSongList) {
                if(item.remoteId==null) continue;
                if ((item.remoteId.longValue())==(songListId.longValue())) {
                    MutableLiveData<SongListDO> result = new MutableLiveData<>();
                    result.setValue(item);
                    return result;
                }
            }
            return new MutableLiveData<SongListDO>();
        }).when(songListDao).findByRemoteId(any(Long.class));

        //mock 查询网络歌单
        doAnswer(invocation -> {
            MutableLiveData<List<SongListDO>> result = new MutableLiveData<>();
            List<SongListDO> answer = new ArrayList<>();
            for (SongListDO item : allSongList) {
                if (item.remoteId != null)
                    answer.add(item);
            }
            result.setValue(answer);
            return result;
        }).when(songListDao).findAllDataOnWeb();

        //mock 获取所有歌单
        doAnswer(invocation -> {
            MutableLiveData<List<SongListDO>> result = new MutableLiveData<>();
            List<SongListDO> answer = new ArrayList<>();
            for (SongListDO item : allSongList) {
                answer.add(item);
            }
            result.setValue(answer);
            return result;
        }).when(songListDao).findAllSongListDOs();

        //mock 根据s_id sl_id查询关联键
        doAnswer(invocation -> {
            Long songId = invocation.getArgument(0);
            Long songListId = invocation.getArgument(1);
            for (SongXSongListDO item: allSongXSongList) {
                if((item.songId.longValue())==(songId.longValue()) && (item.songListId.longValue())==(songListId.longValue())) {
                    MutableLiveData<SongXSongListDO> result = new MutableLiveData<>();
                    result.setValue(item);
                    return result;
                }
            }
            return new MutableLiveData<SongXSongListDO>();
        }).when(songXSongListDao).findbyID(any(Long.class), any(Long.class));

        //mock 查询网络关联键
        doAnswer(invocation -> {
            MutableLiveData<List<SongXSongListDO>> result = new MutableLiveData<>();
            List<SongXSongListDO> answer = new ArrayList<>();
            for (SongDO item : allSong) {
                if (item.remoteId != null) {
                    for (SongXSongListDO item2 : allSongXSongList) {
                        if (item.id.equals( item2.songId)) {
                            answer.add(item2);
                        }
                    }
                }
            }
            result.setValue(answer);
            return result;
        }).when(songXSongListDao).findAllDataOnWeb();

        //mock 模拟歌曲插入
        doAnswer(invocation -> {
            SongDO song = invocation.getArgument(0);
            songIdRecord = new Long(songIdRecord.intValue()+1);
            song.setId(songIdRecord);
            allSong.add(song);
            songDOS.setValue(allSong);
            return songIdRecord;
        }).when(songDao).insert(any(SongDO.class));

        //mock 模拟歌曲删除
        doAnswer(invocation -> {
            SongDO song = invocation.getArgument(0);
            allSong.remove(song);
            songDOS.setValue(allSong);
            return null;
        }).when(songDao).delete(any(SongDO.class));

        //mock 模拟歌单插入
        doAnswer(invocation -> {
            SongListDO songList = invocation.getArgument(0);
            songListIdRecord = new Long(songListIdRecord.intValue()+1);
            songList.setId(songListIdRecord);
            allSongList.add(songList);
            songListDOS.setValue(allSongList);
            return songListIdRecord;
        }).when(songListDao).insert(any(SongListDO.class));

        //mock 模拟歌单删除
        doAnswer(invocation -> {
            SongListDO songList = invocation.getArgument(0);
            allSongList.remove(songList);
            songListDOS.setValue(allSongList);
            return null;
        }).when(songListDao).delete(any(SongListDO.class));

        //mock 模拟关联键插入
        doAnswer(invocation -> {
            SongXSongListDO songX = invocation.getArgument(0);
            allSongXSongList.add(songX);
            songXSongListDOS.setValue(allSongXSongList);
            return null;
        }).when(songXSongListDao).insert(any(SongXSongListDO.class));

        //mock 模拟关联键删除
        doAnswer(invocation -> {
            SongXSongListDO songX = invocation.getArgument(0);
            allSongXSongList.remove(songX);
            songXSongListDOS.setValue(allSongXSongList);
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
        }).when(songXSongListDao).findAllSongsDataForSongListById(any(Long.class));
    }

    @InjectMocks
    private Repo repo;
//    @Spy//未使用
//    private Executor diskIO = new Executor() {
//
//        private Executor executor = Executors.newSingleThreadExecutor();
//
//        @Override
//        public void execute(Runnable command) {
//            executor.execute(command);
//        }
//    };
    @Mock
    private SongDao songDao;
    @Mock
    private SongListDao songListDao;
    @Mock
    private SongXSongListDao songXSongListDao;
    @Mock
    private SongListWebService songListWebService;

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

    //private List<SongDO> List1;
    private List<SongDO> List2;
    private List<SongDO> List3;

    private List<SongDO> allSong;
    private List<SongListDO> allSongList;
    private List<SongXSongListDO> allSongXSongList;

    private MutableLiveData<List<SongDO>> songDOS;
    private MutableLiveData<List<SongListDO>> songListDOS;
    private MutableLiveData<List<SongXSongListDO>> songXSongListDOS;

    private Map<SongListDO, List<SongDO>> songMap;
}