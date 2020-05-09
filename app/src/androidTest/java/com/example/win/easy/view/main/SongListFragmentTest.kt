package com.example.win.easy.view.main

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.win.easy.BooleanSemaphore
import com.example.win.easy.R
import com.example.win.easy.TestCoroutineRule
import com.example.win.easy.display.interfaces.DisplayService
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.viewmodel.SongListViewModelImpl
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.widget.QMUITopBar
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class SongListFragmentTest {
    @InjectMockKs lateinit var songListFragment: SongListFragment
    @RelaxedMockK lateinit var displayService: DisplayService
    @MockK lateinit var downloadServiceAdapter: DownloadServiceAdapter
    @MockK lateinit var fragmentFactory: FragmentFactory
    @MockK lateinit var viewModelFactory: ViewModelProvider.Factory
    private val songListViewModel= mockkClass(SongListViewModelImpl::class,relaxed = true)
    @MockK(relaxUnitFun = true) lateinit var mockNavController: NavController

    @get:Rule
    val testRule=TestCoroutineRule()

    /**
     * 测试当点击右上角的按钮时，触发到“添加歌曲到歌单”fragment的导航
     */
    @Test
    fun testAddSongToSongList() {
        scenario.onFragment { fragment: SongListFragment -> Navigation.setViewNavController(fragment.requireView(), mockNavController) }
        Espresso.onView(CoreMatchers.instanceOf(QMUIAlphaImageButton::class.java)).perform(ViewActions.click())
        verify{ mockNavController.navigate(SongListFragmentDirections.actionSongListFragmentToAddSongToSongListFragment(selectedSongList)) }
    }

    private val mSpinner=MutableLiveData(false)
    private val mSnackbar=MutableLiveData("")
    private val onlineSongsLiveData=MutableLiveData<List<SongDO>>()

    /**
     * 测试界面启动
     * 1. 测试界面元素、本地歌单的正常显示
     * 2. 网络歌单测试：
     *      1. 加载时的状态
     *      2. 加载成功时的界面
     */
    @Test
    fun testFragmentLaunch(): Unit =runBlocking{

        // 本地歌单以及界面元素测试
        every { songListViewModel.loadSongsIn(selectedSongList) } returns songsInSelectedSongList
        scenario.recreate()
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDescendantOfA(CoreMatchers.instanceOf(QMUITopBar::class.java)), ViewMatchers.withText(selectedSongList.name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(CoreMatchers.instanceOf(QMUIAlphaImageButton::class.java)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        checkItemWithName(downloadedSong.name)
        checkItemWithName(nonDownloadedSongWithUrl.name)

        // 网络歌单测试

        // 加载中
        every { songListViewModel.loadSongsIn(selectedSongList) }.answers{
            mSpinner.postValue(false)
            onlineSongsLiveData
        }
        scenario.recreate()
        Espresso.onView(CoreMatchers.instanceOf(ProgressBar::class.java)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // 加载完
        mSpinner.postValue(true)
        onlineSongsLiveData.postValue(listOf(downloadedSong,nonDownloadedSongWithUrl))
        checkItemWithName(downloadedSong.name)
        checkItemWithName(nonDownloadedSongWithUrl.name)
    }

    private fun checkItemWithName(name: String){
        Espresso.onView(ViewMatchers.withText(name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private var blockCap = CapturingSlot<()->Unit>()
    private var songCap = CapturingSlot<SongDO>()

    /**
     * 测试点击下载，且下载后触发播放
     */
    @Test
    fun testClickSongToDownload()= testRule.testCoroutineDispatcher.runBlockingTest{
        every { downloadServiceAdapter.download(capture(songCap),capture(blockCap)) } answers {
            launch(testRule.testCoroutineDispatcher) {
                yield()
                songCap.captured.songPath="Test Path"
                withContext(Dispatchers.Main){
                    blockCap.captured()
                }
            }
        }
        every { songListViewModel.loadSongsIn(selectedSongList) } returns songsInSelectedSongList
        scenario.recreate()
        Espresso.onView(ViewMatchers.withText(nonDownloadedSongWithUrl.name)).perform(ViewActions.click())
        assertEquals(null,nonDownloadedSongWithUrl.songPath)
        verify(exactly = 0) { displayService.restartWith(nonDownloadedSongWithUrl,selectedSongList,songsInSelectedSongList.value!!) }

        runCurrent()

        assertEquals("Test Path",nonDownloadedSongWithUrl.songPath)
        verify(exactly = 1) { displayService.restartWith(nonDownloadedSongWithUrl,selectedSongList,songsInSelectedSongList.value!!) }

        nonDownloadedSongWithUrl.songPath=null
    }

    @Before
    fun setUp() {
        every { songListViewModel.spinner } returns mSpinner
        every { songListViewModel.snackbar } returns mSnackbar

        MockKAnnotations.init(this)

        every { downloadServiceAdapter.spinner } returns mSpinner
        every { downloadServiceAdapter.snackbar } returns mSnackbar

        every { viewModelFactory.create(SongListViewModelImpl::class.java) } returns songListViewModel
        every { fragmentFactory.instantiate(any(),SongListFragment::class.java.name) } returns songListFragment
        scenario = FragmentScenario.launchInContainer(SongListFragment::class.java, bundlePassedFromSelection, R.style.AppTheme, fragmentFactory)
    }


    //LiveData和这个界面的数据
    private val selectedSongList = SongListDO().apply {
        name="体测"
    }
    private val downloadedSong = SongDO().apply{
        name="电灯胆 - 邓丽欣"
        songPath="/songs/d.mp3"
    }
    private val nonDownloadedSongWithUrl = SongDO().apply{
        name="藏在贴纸相背后 - 陈奕迅"
        songUrl="https://www.google.com/songs/song.mp3"
    }

    //下载的数据
    private var downloadedSongFromUrl=SongDO().apply {
        name=nonDownloadedSongWithUrl.name
        songUrl=nonDownloadedSongWithUrl.songUrl
        avatarPath=nonDownloadedSongWithUrl.avatarPath
        songPath="/data/user/0/downloaded.mp3"
    }

    //Bundle数据
    private val bundlePassedFromSelection = Bundle().apply { putSerializable("selectedSongList", selectedSongList) }

    private val songsInSelectedSongList=MutableLiveData(listOf(downloadedSong,nonDownloadedSongWithUrl))
    private lateinit var scenario: FragmentScenario<SongListFragment>
}