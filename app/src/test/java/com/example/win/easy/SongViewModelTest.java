package com.example.win.easy;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.repo.SongRepository;
import com.example.win.easy.rule.MockAndroidEnvironmentMainLooperRule;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.viewmodel.SongViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SongViewModel.class)
public class SongViewModelTest {

    //强制本来异步发送给主线程执行的的postValue方法立即执行
    @Rule InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();
    //构造一个安卓下的主线程
    @Rule MockAndroidEnvironmentMainLooperRule mockAndroidEnvironmentMainLooperRule =new MockAndroidEnvironmentMainLooperRule();

    @InjectMocks SongViewModel songViewModel;
    @Mock LifecycleOwner lifecycleOwner;
    @Mock SongRepository songRepository;
    private LifecycleRegistry lifecycleRegistry;
    private MutableLiveData<List<SongDO>> mockDatabaseAllSongDOs;
    private LiveData<List<SongVO>> allSongVOs;

    private List<SongDO> mockFirstSongDOs =new ArrayList<>();
    private List<SongDO> mockSecondSongDOs =new ArrayList<>();

    private String currentSongString;
    private String firstStringResult;
    private String secondStringResult;


    /**
     * <p>在独立于toVO实现的条件下测试LiveData能工作包括：</p>
     * <ul>
     *     <li>数据变化时能通知给它的观测者</li>
     *     <li>
     *         <p>观测者生命周期变化时能正常工作</p>
     *         <ol>
     *             <li>注册后显示有观测者</li>
     *             <li>观测者Paused等等时显示nonactive</li>
     *             <li>观测者nonactive时不通知更新</li>
     *             <li>观测者由nonactive转为active时更新数据</li>
     *         </ol>
     *     </li>
     * </ul>
     * <p>为了查看变动，每次的回调都会把所有songVO的名字连接成一个字符串，歌曲变动时字符串也相应变动</p>
     */
    @Test
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
        assertHasObserverButNotActive();

        secondDatabaseUpdate();
        assertSecondUpdateDoesNotWork();

        lifecycleResumed();
        assertSecondUpdateWorks();

        lifecycleDestroyed();
        assertDoNotHasObserver();
    }

    @Before
    public void before() throws Exception {

        //初始化mock注解注入
        MockitoAnnotations.initMocks(this);

        //spy readerViewModel使之可以部分mock
        songViewModel = PowerMockito.spy(songViewModel);

        //mock数据库
        prepareDOandVO();
        mockDatabaseAllSongDOs=new MutableLiveData<>();
        doReturn(mockDatabaseAllSongDOs).when(songRepository).getAll();

        //mock lifecycle监听
        lifecycleRegistry=new LifecycleRegistry(lifecycleOwner);
        doReturn(lifecycleRegistry).when(lifecycleOwner).getLifecycle();

        //设置正确的测试结果
        prepareResult();
    }

    private void prepareDOandVO() throws Exception {
        SongDO songDO1= SongDO.builder().name("黑白照-邓丽欣").build();
        SongDO songDO2= SongDO.builder().name("验伤-卫兰").build();
        SongDO songDO3= SongDO.builder().name("野孩子-杨千嬅").build();
        SongDO songDO4= SongDO.builder().name("倒带人生-陈奕迅").build();

        SongVO songVO1= SongVO.builder().name("邓丽欣-黑白照").build();
        SongVO songVO2= SongVO.builder().name("卫兰-验伤").build();
        SongVO songVO3= SongVO.builder().name("杨千嬅-野孩子").build();
        SongVO songVO4= SongVO.builder().name("陈奕迅-倒带人生").build();

        mockFirstSongDOs.add(songDO1);
        mockFirstSongDOs.add(songDO2);

        mockSecondSongDOs.add(songDO3);
        mockSecondSongDOs.add(songDO4);

        //Mock从DO的转换方法，因为VO类的定义还没确定
        PowerMockito.when(songViewModel,"toVO",songDO1).thenReturn(songVO1);
        PowerMockito.when(songViewModel,"toVO",songDO2).thenReturn(songVO2);
        PowerMockito.when(songViewModel,"toVO",songDO3).thenReturn(songVO3);
        PowerMockito.when(songViewModel,"toVO",songDO4).thenReturn(songVO4);
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

            for (SongVO songVO:songVOs)
                currentSongString =songVO.getName().concat(currentSongString);
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
    private void assertSecondUpdateDoesNotWork(){ assertEquals(firstStringResult,currentSongString); }
    private void assertSecondUpdateWorks(){ assertEquals(secondStringResult,currentSongString); }
}



