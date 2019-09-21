package com.example.win.easy.view.main;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.R;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.viewmodel.SongListViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.win.easy.enumeration.DataSource.Local;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ALL")
@RunWith(AndroidJUnit4ClassRunner.class)
public class SongListCreationFragmentTest {

    /**
     * <p>测试成功的歌单创建：</p>
     * <ol>
     *     <li>成功调用viewModel方法</li>
     *     <li>成功后页面导航</li>
     * </ol>
     */
    @Test
    public void testSuccessfulSongListCreation() throws SongListToCreateAlreadyExistLocallyException {

        typeSongListName(newSongListName);
        clickSongListCreationButton();

        verifySuccessfulCreation();
        verifyNavigationAfterSuccessfulCreation();
    }

    /**
     * <p>测试创建重名的歌单：</p>
     * <p>验证viewModel用错误的参数被调用</p>
     */
    @Test
    public void testUnsuccessfulSongListCreationForAlreadyExisted() throws SongListToCreateAlreadyExistLocallyException {

        typeSongListName(existedSongListName);
        clickSongListCreationButton();

        verifyUnsuccessfulCreationForAlreadyExisted();
    }

    /**
     * <p>测试不输入名字直接创建歌单：</p>
     * <p>验证viewModel不被调用</p>
     */
    @Test
    public void testUnsuccessfulSongListCreationForEmpty() throws SongListToCreateAlreadyExistLocallyException {

        clickSongListCreationButton();

        verifyUnsuccessfulCreationForEmpty();
    }

    private void typeSongListName(String songListName){
        onView(withHint(newSongListCreationHint)).perform(replaceText(songListName));
    }

    private void clickSongListCreationButton(){
        onView(withText(textOnNewSongListCreationButton)).perform(click());
    }

    private void verifySuccessfulCreation() throws SongListToCreateAlreadyExistLocallyException {
        verify(songListViewModel,times(1)).create(newSongListVO);
    }

    private void verifyNavigationAfterSuccessfulCreation(){
        verify(navController,times(1)).navigateUp();
    }

    private void verifyUnsuccessfulCreationForAlreadyExisted() throws SongListToCreateAlreadyExistLocallyException {
        verify(songListViewModel,times(1)).create(existedSongListVO);
    }

    private void verifyUnsuccessfulCreationForEmpty() throws SongListToCreateAlreadyExistLocallyException {
        verify(songListViewModel,times(0)).create(emptySongListVO);
    }

    @Before
    public void setUp() throws SongListToCreateAlreadyExistLocallyException {
        MockitoAnnotations.initMocks(this);

        setUpData();

        mockViewModel();
        mockVieModelFactory();
        mockFragmentFactory();

        launchTestFragment();
    }

    private void setUpData(){
        newSongListVO= SongListVO.builder()
                .name(newSongListName)
                .dataSource(Local)
                .build();
        existedSongListVO= SongListVO.builder()
                .name(existedSongListName)
                .dataSource(Local)
                .build();
        emptySongListVO= SongListVO.builder()
                .name("")
                .dataSource(Local)
                .build();
    }

    private void mockViewModel() throws SongListToCreateAlreadyExistLocallyException {
        doThrow(SongListToCreateAlreadyExistLocallyException.class).when(songListViewModel).create(existedSongListVO);
    }

    private void mockVieModelFactory() {
        when(viewModelFactory.create(SongListViewModel.class)).thenReturn(songListViewModel);
    }

    private void mockFragmentFactory(){
        when(fragmentFactory.instantiate(any(ClassLoader.class),eq(SongListCreationFragment.class.getName()))).thenReturn(songListCreationFragment);
    }

    private void launchTestFragment(){
        FragmentScenario<SongListCreationFragment> scenario=FragmentScenario.launchInContainer(SongListCreationFragment.class,null, R.style.AppTheme,fragmentFactory);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),navController));
    }

    @InjectMocks SongListCreationFragment songListCreationFragment;
    @Mock FragmentFactory fragmentFactory;
    @Mock NavController navController;
    @Mock SongListViewModel songListViewModel;
    @Mock ViewModelProvider.Factory viewModelFactory;

    private String newSongListName="乐队有很多";
    private String existedSongListName="但新裤子只有一条";

    private SongListVO newSongListVO;
    private SongListVO existedSongListVO;
    private SongListVO emptySongListVO;

    private String newSongListCreationHint="我的新歌单已经饥渴难耐了";
    private String textOnNewSongListCreationButton="给我建他娘的！";
}