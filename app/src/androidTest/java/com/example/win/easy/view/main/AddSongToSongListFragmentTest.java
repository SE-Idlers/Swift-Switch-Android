package com.example.win.easy.view.main;

import android.os.Bundle;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.action.ViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.R;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.NotImplementedError;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class AddSongToSongListFragmentTest {

    /**
     * <p>测试依次点击三首歌，点击添加后，能够正确添加到歌单</p>
     * <p>tips:“能够正确添加到歌单”的验证是通过验证对viewModel的正确调用来进行的</p>
     */
    @Test
    public void testClickSongsToAdd(){
        click(song1);
        click(song3);
        clickAddButton();

        verifySongsAdd(song1,song3);
        verifyNavigationToSongListFragment();
    }

    /**
     * <p>测试当同一首歌被点击两次后会被取消添加</p>
     */
    @Test
    public void testClickThenCancelAdd(){
        click(song1);
        click(song2);
        click(song2);
        clickAddButton();

        verifySongsAdd(song1);
        verifyNavigationToSongListFragment();
    }

    /**
     * <p>测试标题栏正常显示</p>
     */
    @Test
    public void testTopBarDisplayed(){
        onView(withText(topBarTitle)).check(matches(isDisplayed()));
    }

    private void click(SongVO song){
        onView(withText(song.getName())).perform(ViewActions.click());
    }

    private void clickAddButton(){
        onView(instanceOf(QMUIAlphaImageButton.class)).perform(ViewActions.click());
    }

    private void verifySongsAdd(SongVO... songVOs){
        verify(songListViewModel,times(1)).addSongsTo(Arrays.asList(songVOs),testSongList);
    }

    private void verifyNavigationToSongListFragment(){
        throw new NotImplementedError();
        //TODO
//        verify(navController,times(1)).navigate(AddSongToSongListFragmentDirections.actionAddSongToSongListFragmentToSongListFragment(testSongList));

        //重置NavController（清除调用记录）
//        navController= Mockito.mock(NavController.class);
//        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),navController));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpData();

        mockViewModel();
        mockViewModelFactory();
        mockFragmentFactory();

        launchTestFragment();
    }

    private void setUpData(){
        setUpBundleToPass();
        setUpSongListData();
    }

    private void setUpBundleToPass(){
        bundleToPass.putSerializable("songListSongsAddedTo",testSongList);
    }

    private void setUpSongListData(){
        songsNotInTestSongList.add(song1);
        songsNotInTestSongList.add(song2);
        songsNotInTestSongList.add(song3);
    }

    private void mockViewModel(){
        when(songListViewModel.songsNotIn(testSongList)).thenReturn(songsNotInTestSongList);
    }

    private void mockFragmentFactory(){
        when(fragmentFactory.instantiate(any(ClassLoader.class), eq(AddSongToSongListFragment.class.getName()))).thenReturn(addSongToSongListFragment);
    }

    private void mockViewModelFactory(){
        when(viewModelFactory.create(SongListViewModel.class)).thenReturn(songListViewModel);
    }

    private void launchTestFragment(){
        scenario=FragmentScenario.launchInContainer(AddSongToSongListFragment.class, bundleToPass, R.style.AppTheme,fragmentFactory);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),navController));
    }

    @InjectMocks AddSongToSongListFragment addSongToSongListFragment;
    @Mock FragmentFactory fragmentFactory;
    @Mock ViewModelProvider.Factory viewModelFactory;
    @Mock SongListViewModel songListViewModel;
    @Mock NavController navController;

    private String topBarTitle="不在这个歌单里的歌曲";
    private FragmentScenario<AddSongToSongListFragment> scenario;
    private SongListVO testSongList=SongListVO.builder().name("艰难的人生").build();
    private List<SongVO> songsNotInTestSongList=new ArrayList<>();
    private SongVO song1=SongVO.builder().name("球球你了").build();
    private SongVO song2=SongVO.builder().name("我他妈实在是").build();
    private SongVO song3=SongVO.builder().name("编不出来啦i-i").build();
    private Bundle bundleToPass =new Bundle();
}