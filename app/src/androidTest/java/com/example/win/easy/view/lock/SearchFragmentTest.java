package com.example.win.easy.view.lock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.R;
import com.example.win.easy.display.DisplayServiceAdapter;
import com.example.win.easy.tool.SongListWithSongs;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.example.win.easy.viewmodel.SongViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.win.easy.enumeration.DataSource.Local;
import static com.example.win.easy.enumeration.DataSource.WangYiYun;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SearchFragmentTest {

    @Test
    public void testNormalSelection(){
        search(testSequence);
        search(testSequence);
        verifySongListDisplayed(localMatchedSongs,neteaseMatchedSongs);

        clickTab(neteaseMatchedSongs);
        clickSong(songInHateAndNormal);
        verifySongListDisplayed(hate,normal);
        verifyMediaIsPlaying(songInHateAndNormal,hate);

        clickTab(normal);
        verifyMediaIsPlaying(songInHateAndNormal,normal);

        clickSong(songInNormalAndLove);
        verifySongListDisplayed(normal,love);
        verifyMediaIsPlaying(songInNormalAndLove,normal);

        clickTab(love);
        verifyMediaIsPlaying(songInNormalAndLove,love);
    }

    @Test
    public void testSequenceNoSongsMatch(){
        search(sequenceNoSongsMatch);
    }

    @Test
    public void testSelectSongNotInAnySongList(){
        search(testSequence);
        verifySongListDisplayed(localMatchedSongs,neteaseMatchedSongs);

        clickSong(songNotInAnySongList);
        verifySongListDisplayed(songListOfAllSong);
        verifyMediaIsPlaying(songNotInAnySongList,songListOfAllSong);
    }

    private void verifyMediaIsPlaying(SongVO beingPlayedSong, SongListWithSongs beingPlayedSongList) {
        assertEquals(beingPlayedSong,displayServiceAdapter.getCurrentDisplayedSong());
        assertEquals(beingPlayedSongList.getSongs(),displayServiceAdapter.getCurrentDisplayedSongList());
    }

    private void search(List<Character> sequence){
        scenario.onFragment(fragment -> fragment.search(sequence));
    }

    private void verifySongListDisplayed(SongListWithSongs... filledSongLists){
        SongListWithSongs theFirstOne=filledSongLists[0];
        int length=filledSongLists.length;
        for (int songListIndex=0;songListIndex<length;songListIndex++){
            SongListWithSongs songListWithSongs=filledSongLists[songListIndex];
            onView(withText(songListWithSongs.getSongList().getName())).check(matches(isDisplayed()));
            clickTab(songListWithSongs);


//            List<SongVO> songs=songListWithSongs.getSongs();
//            int songListSize=songs.size();
//            for (int songIndex=0;songIndex<songListSize;songIndex++) {
//                SongVO songVO=songs.get(songIndex);
//                onView(allOf(withText(songVO.getName()),isDescendantOfA(nthChildOf(nthChildOf(hasSibling(withChild(withChild(withChild(withText(songListWithSongs.getSongList().getName()))))),songListIndex),songIndex+1)))).check(matches(isDisplayed()));
//            }
        }
        clickTab(theFirstOne);
    }

    private void clickTab(SongListWithSongs songListWithSongs){
        onView(withText(songListWithSongs.getSongList().getName())).perform(click());
    }

    private void clickSong(SongVO song){
        onView(withText(song.getName())).perform(click());
    }

    @Before
    public void setUp() throws FailToReplaceSongListException {
        MockitoAnnotations.initMocks(this);

        setUpData();

        mockDisplayService();
        mockViewModel();
        mockViewModelFactory();
        mockFragmentFactory();

        launchTestFragment();
    }

    private void setUpData(){
        songListOfAllSong.getSongs().add(songNotInAnySongList);
        songListOfAllSong.getSongs().add(songOnlyInHate);
        songListOfAllSong.getSongs().add(songInHateAndNormal);
        songListOfAllSong.getSongs().add(songInNormalAndLove);

        localMatchedSongs.getSongs().add(songNotInAnySongList);
        localMatchedSongs.getSongs().add(songOnlyInHate);

        neteaseMatchedSongs.getSongs().add(songInHateAndNormal);
        neteaseMatchedSongs.getSongs().add(songInNormalAndLove);

        hate.getSongs().add(songOnlyInHate);
        hate.getSongs().add(songInHateAndNormal);

        normal.getSongs().add(songInHateAndNormal);
        normal.getSongs().add(songInNormalAndLove);

        love.getSongs().add(songInNormalAndLove);

        allSongsInRepoLiveData.postValue(songListOfAllSong.getSongs());
    }

    private void mockDisplayService() throws FailToReplaceSongListException {
        doAnswer(invocation -> {
            SongVO songArg=invocation.getArgument(0);
            List<SongVO> songListArg=invocation.getArgument(1);

            doReturn(songArg).when(displayServiceAdapter).getCurrentDisplayedSong();
            doReturn(songListArg).when(displayServiceAdapter).getCurrentDisplayedSongList();
            return null;
        }).when(displayServiceAdapter).startWith(any(SongVO.class),any(List.class));
        doAnswer(invocation -> {
            List<SongVO> songListArg=invocation.getArgument(0);

            doReturn(songListArg).when(displayServiceAdapter).getCurrentDisplayedSongList();
            return null;
        }).when(displayServiceAdapter).replaceSongList(any(List.class));
    }

    private void mockViewModel(){
        when(songViewModel.songsMatch(testSequence)).thenReturn(songListOfAllSong.getSongs());
        when(songViewModel.songsMatch(sequenceNoSongsMatch)).thenReturn(new ArrayList<>());

        when(songViewModel.songListsContain(songNotInAnySongList)).thenReturn(new ArrayList<>());
        when(songViewModel.songListsContain(songOnlyInHate)).thenReturn(Collections.singletonList(hate.getSongList()));
        when(songViewModel.songListsContain(songInHateAndNormal)).thenReturn(Arrays.asList(hate.getSongList(),normal.getSongList()));
        when(songViewModel.songListsContain(songInNormalAndLove)).thenReturn(Arrays.asList(normal.getSongList(),love.getSongList()));

        doReturn(allSongsInRepoLiveData).when(songViewModel).getAllSongs();

        when(songListViewModel.songsOf(hate.getSongList())).thenReturn(hate.getSongs());
        when(songListViewModel.songsOf(normal.getSongList())).thenReturn(normal.getSongs());
        when(songListViewModel.songsOf(love.getSongList())).thenReturn(love.getSongs());
    }

    private void mockViewModelFactory(){
        when(viewModelFactory.create(SongViewModel.class)).thenReturn(songViewModel);
        when(viewModelFactory.create(SongListViewModel.class)).thenReturn(songListViewModel);
    }

    private void mockFragmentFactory(){
        when(fragmentFactory.instantiate(any(ClassLoader.class),eq(SearchFragment.class.getName()))).thenReturn(searchFragment);
    }

    private void launchTestFragment(){
        scenario=FragmentScenario.launchInContainer(SearchFragment.class,null, R.style.AppTheme,fragmentFactory);
    }

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();
    @InjectMocks SearchFragment searchFragment;
    @Mock FragmentFactory fragmentFactory;
    @Mock SongListViewModel songListViewModel;
    @Mock SongViewModel songViewModel;
    @Mock ViewModelProvider.Factory viewModelFactory;
    @Mock DisplayServiceAdapter displayServiceAdapter;

    private FragmentScenario<SearchFragment> scenario;

    private SongListWithSongs songListOfAllSong=SongListWithSongs.builder().songList(SongListVO.builder().name("所有歌曲").build()).songs(new ArrayList<>()).build();
    private SongListWithSongs localMatchedSongs=SongListWithSongs.builder().songList(SongListVO.builder().name(Local.name()).dataSource(Local).build()).songs(new ArrayList<>()).build();
    private SongListWithSongs neteaseMatchedSongs=SongListWithSongs.builder().songList(SongListVO.builder().name(WangYiYun.name()).dataSource(WangYiYun).build()).songs(new ArrayList<>()).build();
    private SongListWithSongs hate=SongListWithSongs.builder().songList(SongListVO.builder().name("讨厌").dataSource(Local).build()).songs(new ArrayList<>()).build();
    private SongListWithSongs normal=SongListWithSongs.builder().songList(SongListVO.builder().name("一般般").dataSource(WangYiYun).build()).songs(new ArrayList<>()).build();
    private SongListWithSongs love=SongListWithSongs.builder().songList(SongListVO.builder().name("喜欢！").dataSource(WangYiYun).build()).songs(new ArrayList<>()).build();

    private SongVO songNotInAnySongList=SongVO.builder().name("无法地带").source(Local).build();
    private SongVO songOnlyInHate=SongVO.builder().name("只在讨厌里").source(Local).build();
    private SongVO songInHateAndNormal =SongVO.builder().name("在讨厌和一般般里").source(WangYiYun).build();
    private SongVO songInNormalAndLove=SongVO.builder().name("在一般般和喜欢里").source(WangYiYun).build();

    private MutableLiveData<List<SongVO>> allSongsInRepoLiveData=new MutableLiveData<>();
    private List<Character> sequenceNoSongsMatch= Arrays.asList('A','B','C');
    private List<Character> testSequence= Arrays.asList('E','F','G');
}