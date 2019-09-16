package com.example.win.easy.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.BooleanSemaphore;
import com.example.win.easy.R;
import com.example.win.easy.display.DisplayServiceAdapter;
import com.example.win.easy.download.DownloadServiceAdapter;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SongListFragmentTest {


    @InjectMocks SongListFragment songListFragment;
    @Mock DisplayServiceAdapter displayServiceAdapter;
    @Mock DownloadServiceAdapter downloadServiceAdapter;
    @Mock FragmentFactory fragmentFactory;
    @Mock ViewModelProvider.Factory viewModelFactory;
    @Mock SongListViewModel songListViewModel;
    @Mock NavController mockNavController;


    /**
     * <p>简陋地测试下界面是不是正常显示</p>
     * <p>把带字的、特定类型的元素确认下isDisplayed就完了</p>
     */
    @Test
    public void testDisplay(){
        onView(allOf(isDescendantOfA(instanceOf(QMUITopBar.class)),withText(selectedSongList.getName()))).check(matches(isDisplayed()));
        onView(instanceOf(QMUIAlphaImageButton.class)).check(matches(isDisplayed()));
        onView(withText(downloadedSong.getName())).check(matches(isDisplayed()));
        onView(withText(nonDownloadedSongWithUrl.getName())).check(matches(isDisplayed()));
    }

    /**
     * <p>测试当点击一首已经下载好的歌曲时，直接播放</p>
     */
    @Test
    public void testClickDownloadedSong(){
        onView(withText(downloadedSong.getName())).perform(click());
        verify(displayServiceAdapter,times(1)).startWith(downloadedSong,songsInSelectedSongListLiveData.getValue());
    }


    /**
     * <p>测试当点击一首还没下载的歌曲时，先触发下载，然后等下载结束后，触发播放</p>
     */
    @Test
    public void testClickDownloadedSongWith(){
        onView(withText(nonDownloadedSongWithUrl.getName())).perform(click());
        verify(downloadServiceAdapter,times(1)).download(eq(nonDownloadedSongWithUrl),any(OnReadyFunc.class));
        verify(displayServiceAdapter,times(1)).startWith(downloadedSongFromUrl,songsInSelectedSongListLiveData.getValue());
    }

    /**
     * <p>测试当点击右上角的按钮时，触发到“添加歌曲到歌单”fragment的导航</p>
     */
    @Test
    public void testAddSongToSongList(){
        onView(instanceOf(QMUIAlphaImageButton.class)).perform(click());
        verify(mockNavController,times(1)).navigate(SongListFragmentDirections.actionSongListFragmentToAddSongToSongListFragment(selectedSongList));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpData();

        mockDownloadService();
        mockLiveDataFromViewModel();
        mockViewModelFactory();
        mockFragmentFactory();

        launchTestedFragment();
    }

    private void setUpData(){
        setUpLiveData();
        setUpBundleData();
        setUpDownloadedData();
    }

    private void setUpLiveData(){
        List<SongVO> songsInSelectedSongList=new ArrayList<>();
        songsInSelectedSongList.add(downloadedSong);
        songsInSelectedSongList.add(nonDownloadedSongWithUrl);
        songsInSelectedSongListLiveData.postValue(songsInSelectedSongList);
    }

    private void setUpBundleData(){
        bundlePassedFromSelection.putSerializable("selectedSongList", selectedSongList);
    }

    private void setUpDownloadedData(){
        downloadedSongFromUrl=SongVO.builder()
                .name(nonDownloadedSongWithUrl.getName())
                .songFileUrl(nonDownloadedSongWithUrl.getSongFileUrl())
                .avatarPath(nonDownloadedSongWithUrl.getAvatarPath())
                .songFilePath("/data/user/0/downloaded.mp3")
                .build();
    }

    private void mockDownloadService() {
        doAnswer(invocation -> {

            //获取参数
            SongVO songToDownload=invocation.getArgument(0);
            OnReadyFunc<SongVO> onReadyFunc=invocation.getArgument(1);

            //模拟发起请求
            Executors.newSingleThreadExecutor().execute(this::mockDownloadTask);

            waitForDownloadFinished();
            onReadyFunc.onReady(downloadedSongFromUrl);

            return null;
        }).when(downloadServiceAdapter).download(any(SongVO.class),any(OnReadyFunc.class));
    }

    private void mockDownloadTask(){
        downloading();
        notifyDownloadFinished();
    }

    private void downloading(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void mockLiveDataFromViewModel() {
        when(songListViewModel.songsOf(selectedSongList)).thenReturn(songsInSelectedSongListLiveData);
    }

    private void mockViewModelFactory() {
        when(viewModelFactory.create(SongListViewModel.class)).thenReturn(songListViewModel);
    }

    private void mockFragmentFactory() {
        when(fragmentFactory.instantiate(any(ClassLoader.class),eq(SongListFragment.class.getName()))).thenReturn(songListFragment);
    }

    private void launchTestedFragment(){
        FragmentScenario<SongListFragment> scenario = FragmentScenario.launchInContainer(SongListFragment.class, bundlePassedFromSelection, R.style.AppTheme, fragmentFactory);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), mockNavController));
    }

    private void waitForDownloadFinished(){
        synchronized (downloadFinished) {
            synchronized (mainThreadWaiting) {
                mainThreadWaiting.setReady(true);
            }
            try {
                downloadFinished.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyDownloadFinished(){
        while (true){
            synchronized (mainThreadWaiting){
                if (mainThreadWaiting.isReady())
                    break;
            }
        }
        synchronized (downloadFinished){
            downloadFinished.notifyAll();
        }
    }

    //LiveData和这个界面的数据
    private SongListVO selectedSongList=SongListVO.builder().name("体测").build();
    private SongVO downloadedSong=SongVO.builder().name("电灯胆 - 邓丽欣").songFilePath("/songs/d.mp3").build();
    private SongVO nonDownloadedSongWithUrl=SongVO.builder().name("藏在贴纸相背后 - 陈奕迅").songFileUrl("https://www.google.com/songs/song.mp3").build();
    private MutableLiveData<List<SongVO>> songsInSelectedSongListLiveData=new MutableLiveData<>();


    //Bundle数据
    private Bundle bundlePassedFromSelection=new Bundle();

    //下载的数据
    private SongVO downloadedSongFromUrl;

    //用于同步的数据
    private final BooleanSemaphore mainThreadWaiting =new BooleanSemaphore(false);
    private final Object downloadFinished=new Object();
}