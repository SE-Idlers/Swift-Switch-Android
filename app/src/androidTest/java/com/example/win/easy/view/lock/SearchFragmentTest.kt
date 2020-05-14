package com.example.win.easy.view.lock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.R
import com.example.win.easy.TestCoroutineRule
import com.example.win.easy.display.DisplayService
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.tool.SongListWithSongs
import com.example.win.easy.viewmodel.SongListViewModel
import com.example.win.easy.viewmodel.SongViewModel
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class SearchFragmentTest {
    @Test
    fun testNormalSelection()= runBlocking {
        search(testSequence)
        verifySongListDisplayed(mLocalMatchedSongsC, mOnlineMatchedSongsC)
        clickTab(mOnlineMatchedSongsC)
        clickSong(songInHateAndNormal)
        verifySongListDisplayed(mNormalC,mHateC)
        clickTab(mHateC)
        verifyMediaIsPlaying(songInHateAndNormal, mHateC)
        clickTab(mNormalC)
        verifyMediaIsPlaying(songInHateAndNormal, mNormalC)
        clickSong(songInNormalAndLove)
        verifySongListDisplayed(mNormalC, mLoveC)
        verifyMediaIsPlaying(songInNormalAndLove, mNormalC)
        clickTab(mLoveC)
        verifyMediaIsPlaying(songInNormalAndLove, mLoveC)
    }

    @Test
    fun testSequenceNoSongsMatch() {
        search(sequenceNoSongsMatch)
    }

    @Test
    fun testSelectSongNotInAnySongList() {
        search(testSequence)
        verifySongListDisplayed(mLocalMatchedSongsC, mOnlineMatchedSongsC)
        clickSong(songNotInAnySongList)
        verifySongListDisplayed(mAllSongsC)
    }

    private fun verifyMediaIsPlaying(beingPlayedSong: SongDO, beingPlayedSongList: SongListWithSongs) {
        assertEquals(beingPlayedSong, displayService.currentSong())
        assertEquals(beingPlayedSongList.songList, displayService.currentSongList())
        assertEquals(beingPlayedSongList.songs, displayService.currentSongs())
    }

    private fun search(sequence: List<Char>) {
        scenario.onFragment { fragment: SearchFragment -> fragment.search(sequence) }
    }

    private fun verifySongListDisplayed(vararg filledSongLists: SongListWithSongs) {
        for (songListWithSongs in filledSongLists) {
            Espresso.onView(ViewMatchers.withText(songListWithSongs.name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            clickTab(songListWithSongs)
        }
        clickTab(filledSongLists[0])
    }

    private fun clickTab(songListWithSongs: SongListWithSongs) {
        Espresso.onView(ViewMatchers.withText(songListWithSongs.name)).perform(ViewActions.click())
    }

    private fun clickSong(song: SongDO) {
        Espresso.onView(ViewMatchers.withText(song.name)).perform(ViewActions.click())
    }

    @Before
    @Throws(FailToReplaceSongListException::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockDisplayService()
        mockViewModelFactory()
        mockViewModel()
        mockFragmentFactory()
        launchTestFragment()
    }

    private val songCap=CapturingSlot<SongDO>()
    private val songListCap=CapturingSlot<SongListDO>()
    private val songsCap=CapturingSlot<List<SongDO>>()
    private val replaceListCap=CapturingSlot<SongListDO>()
    private val replaceSongsCap=CapturingSlot<List<SongDO>>()

    @Throws(FailToReplaceSongListException::class)
    private fun mockDisplayService() {
        every { displayService.restartWith(capture(songCap),capture(songListCap),capture(songsCap)) }answers {
            every { displayService.currentSong() } returns songCap.captured
            every { displayService.currentSongList() } returns songListCap.captured
            every { displayService.currentSongs() } returns songsCap.captured
        }
        every { displayService.configDisplayList(capture(replaceListCap),capture(replaceSongsCap)) } answers {
            every { displayService.currentSongList() } returns replaceListCap.captured
            every { displayService.currentSongs() } returns replaceSongsCap.captured
        }
    }

    private val songDOCap=CapturingSlot<(List<SongDO>)->Unit>()
    private val hateCap=CapturingSlot<(List<SongListDO>)->Unit>()
    private val hateAndNormalCap=CapturingSlot<(List<SongListDO>)->Unit>()
    private val normalAndLoveCap=CapturingSlot<(List<SongListDO>)->Unit>()
    private val listOfSongListCap=CapturingSlot<(List<SongListDO>)>()
    private val songsInListsBlockCap=CapturingSlot<(List<List<SongDO>>)->Unit>()

    private fun mockViewModel() {
        every { songViewModel.launchLoadSongsBySeq(testSequence,capture(songDOCap)) } answers{songDOCap.captured(mAllSongsC.songs)}
        every { songViewModel.launchLoadSongsBySeq(sequenceNoSongsMatch,captureLambda()) } answers{ lambda<(List<SongDO>)->Unit>().captured(ArrayList())}

        every { songListViewModel.launchLoadSongListsBySong(songNotInAnySongList,captureLambda()) } answers{lambda<(List<SongDO>)->Unit>().captured(ArrayList())}
        every { songListViewModel.launchLoadSongListsBySong(songOnlyInHate,capture(hateCap)) } answers{hateCap.captured(listOf(mHateC.songList))}
        every { songListViewModel.launchLoadSongListsBySong(songInHateAndNormal,capture(hateAndNormalCap)) } answers{hateAndNormalCap.captured(listOf(mHateC.songList, mNormalC.songList))}
        every { songListViewModel.launchLoadSongListsBySong(songInNormalAndLove,capture(normalAndLoveCap)) } answers{normalAndLoveCap.captured(listOf(mNormalC.songList, mLoveC.songList))}

        every { songViewModel.allSongs } returns allSongsInRepoLiveData

        every { songViewModel.launchLoadSongsBySongLists(capture(listOfSongListCap),capture(songsInListsBlockCap)) }.answers{
            mutableListOf<List<SongDO>>().run {
                for (songList in listOfSongListCap.captured)
                    add(when(songList){
                        mHateC.songList-> mHateC.songs
                        mNormalC.songList-> mNormalC.songs
                        mLoveC.songList-> mLoveC.songs
                        mAllSongsC.songList-> mAllSongsC.songs
                        mLocalMatchedSongsC.songList-> mLocalMatchedSongsC.songs
                        mOnlineMatchedSongsC.songList-> mOnlineMatchedSongsC.songs
                        else -> listOf()
                    })
                songsInListsBlockCap.captured(this)
            }
        }
    }

    private fun mockViewModelFactory() {
        every { viewModelFactory.create(SongViewModel::class.java)} returns (songViewModel)
        every { viewModelFactory.create(SongListViewModel::class.java)} returns (songListViewModel)
    }

    private fun mockFragmentFactory() {
        every { fragmentFactory.instantiate(any(), any())} returns (searchFragment)
    }

    private fun launchTestFragment() {
        scenario = FragmentScenario.launchInContainer(SearchFragment::class.java, null, R.style.AppTheme, fragmentFactory)
    }

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val testCoroutineRule = TestCoroutineRule()

    @InjectMockKs lateinit var searchFragment: SearchFragment
    @MockK lateinit var fragmentFactory: FragmentFactory
    @RelaxedMockK lateinit var songListViewModel: SongListViewModel
    @RelaxedMockK lateinit var songViewModel: SongViewModel
    @MockK lateinit var viewModelFactory: ViewModelProvider.Factory
    @MockK lateinit var displayService: DisplayService

    private lateinit var scenario: FragmentScenario<SearchFragment>

    private val songNotInAnySongList = SongDO(name = "无法地带", source = DataSource.Local)
    private val songOnlyInHate = SongDO(name = "只在讨厌里", source = DataSource.Local)
    private val songInHateAndNormal = SongDO(name = "在讨厌和一般般里", source = DataSource.WangYiYun,uid=1,remoteId = 3)
    private val songInNormalAndLove = SongDO(name = "在一般般和喜欢里", source = DataSource.WangYiYun,uid=1,remoteId = 4)

    private val mAllSongsC = SongListWithSongs(songList= SongListDO(name = "所有歌曲", source = DataSource.Local),songs= listOf(songNotInAnySongList,songOnlyInHate,songInHateAndNormal,songInNormalAndLove))
    private val mLocalMatchedSongsC = SongListWithSongs(songList= SongListDO(name = DataSource.Local.name, source = DataSource.Local),songs= listOf(songNotInAnySongList,songOnlyInHate))
    private val mOnlineMatchedSongsC = SongListWithSongs(songList= SongListDO(name = DataSource.WangYiYun.name, source = DataSource.WangYiYun),songs= listOf(songInHateAndNormal,songInNormalAndLove))
    private val mHateC = SongListWithSongs(songList= SongListDO(name = "讨厌", source = DataSource.Local),songs= listOf(songOnlyInHate,songInHateAndNormal))
    private val mNormalC = SongListWithSongs(songList= SongListDO(name = "一般般", source = DataSource.WangYiYun,uid=1,remoteId = 1),songs= listOf(songInHateAndNormal,songInNormalAndLove))
    private val mLoveC = SongListWithSongs(songList= SongListDO(name = "喜欢！", source = DataSource.WangYiYun,uid=1,remoteId = 2),songs= listOf(songInNormalAndLove))


    private val allSongsInRepoLiveData = MutableLiveData<List<SongDO>>().apply { postValue(mAllSongsC.songs) }

    private val sequenceNoSongsMatch = listOf('A', 'B', 'C')
    private val testSequence = listOf('E', 'F', 'G')
}