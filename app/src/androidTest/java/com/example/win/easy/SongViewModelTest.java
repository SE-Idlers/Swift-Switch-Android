package com.example.win.easy;

import android.test.UiThreadTest;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.viewmodel.SongViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SongViewModelTest {

    /**
     * <p>在独立于toVO实现的条件下测试LiveData能工作包括：</p>
     * <ul>
     *     <li>数据变化时能通知给它的观测者</li>
     *     <li>
     *         <p>观测者生命周期变化时能正常工作</p>
     *         <ol>
     *             <li>注册后显示有观测者</li>
     *             <li>观测者Paused等等时显示active</li>
     *             <li>观测者nonactive时不通知更新</li>
     *             <li>观测者由nonactive转为active时更新数据</li>
     *         </ol>
     *     </li>
     * </ul>
     * <p>为了查看变动，每次的回调都会把所有songVO的名字连接成一个字符串，歌曲变动时字符串也相应变动</p>
     */
    @Test
    @UiThreadTest
    public void testLiveDataWorksIndependentOfToVOMethod(){

        initAllSongsLiveData();
        assertDoNotHasObserver();

        registerNonactiveObserver();
        assertHasObserverButNotActive();

        lifecycleResumed();
        assertHasObserverAndIsActive();

        firstDatabaseUpdate();
        assertFirstUpdateWorks();

        lifecyclePaused();
        assertHasObserverAndIsActive();

        secondDatabaseUpdate();
        assertSecondUpdateWorks();

        lifecycleResumed();
        assertSecondUpdateWorks();

        lifecycleDestroyed();
        assertDoNotHasObserver();
    }

    @Before
    @UiThreadTest
    public void before() {

        //初始化mock注解注入
        MockitoAnnotations.initMocks(this);

        //spy readerViewModel使之可以部分mock
        songViewModel = spy(songViewModel);

        //mock数据库
        prepareDOandVO();
        mockDatabaseAllSongDOs=new MutableLiveData<>();
        doReturn(mockDatabaseAllSongDOs).when(songRepository).getAll();

        //mock lifecycle监听
        lifecycleRegistry=new LifecycleRegistry(lifecycleOwner);
        doReturn(lifecycleRegistry).when(lifecycleOwner).getLifecycle();

        //mock转化DO到VO的方法
        when(voUtil.toVO(any(SongDO.class))).thenAnswer(invocation -> {
            SongDO songDO=invocation.getArgument(0);

            return SongVO.builder()
                    .name(songDO.getName())
                    .songFile(songDO.getSongPath()==null?null:new File(songDO.getSongPath()))
                    .avatarFile(songDO.getAvatarPath()==null?null:new File(songDO.getAvatarPath()))
                    .build();
        });
        prepareResult();
    }

    private void prepareDOandVO() {
        SongDO songDO1= SongDO.builder().name("邓丽欣-黑白照").build();
        SongDO songDO2= SongDO.builder().name("卫兰-验伤").build();
        SongDO songDO3= SongDO.builder().name("杨千嬅-野孩子").build();
        SongDO songDO4= SongDO.builder().name("陈奕迅-倒带人生").build();

        mockFirstSongDOs.add(songDO1);
        mockFirstSongDOs.add(songDO2);

        mockSecondSongDOs.add(songDO3);
        mockSecondSongDOs.add(songDO4);

    }

    private void prepareResult(){
        firstStringResult="卫兰-验伤"+"邓丽欣-黑白照";
        secondStringResult="陈奕迅-倒带人生"+"杨千嬅-野孩子";
    }

    private void initAllSongsLiveData(){ allSongVOs = songViewModel.getAllSongs(); }
    private void registerNonactiveObserver(){
        allSongVOs.observe(
                lifecycleOwner,
                songVOs -> {
                    currentSongString="";//重置

                    for (int i=0;i<songVOs.size();i++)
                        currentSongString =songVOs.get(i).getName().concat(currentSongString);
                });
    }
    private void lifecyclePaused(){ lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE); }
    private void lifecycleResumed(){ lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME); }
    private void lifecycleDestroyed(){ lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY); }

    private void firstDatabaseUpdate(){ mockDatabaseAllSongDOs.postValue(mockFirstSongDOs); }
    private void secondDatabaseUpdate(){ mockDatabaseAllSongDOs.postValue(mockSecondSongDOs); }

    private void assertHasObserverButNotActive(){
        assertTrue(allSongVOs.hasObservers());
        assertFalse(allSongVOs.hasActiveObservers());

    }
    private void assertHasObserverAndIsActive(){
        assertTrue(allSongVOs.hasObservers());
        assertTrue(allSongVOs.hasActiveObservers());
    }
    private void assertDoNotHasObserver(){
        assertFalse(allSongVOs.hasObservers());
        assertFalse(allSongVOs.hasActiveObservers());
    }
    private void assertFirstUpdateWorks(){ assertEquals(firstStringResult,currentSongString); }
    private void assertSecondUpdateWorks(){ assertEquals(secondStringResult,currentSongString); }

    //强制本来异步发送给主线程执行的的postValue方法立即执行
    @Rule public InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();

    @InjectMocks SongViewModel songViewModel;
    @Mock LifecycleOwner lifecycleOwner;
    @Mock __SongRepository songRepository;
    @Mock VOUtil voUtil;

    private LifecycleRegistry lifecycleRegistry;
    private MutableLiveData<List<SongDO>> mockDatabaseAllSongDOs;
    private LiveData<List<SongVO>> allSongVOs;

    private List<SongDO> mockFirstSongDOs =new ArrayList<>();
    private List<SongDO> mockSecondSongDOs =new ArrayList<>();

    private String currentSongString;
    private String firstStringResult;
    private String secondStringResult;
}
