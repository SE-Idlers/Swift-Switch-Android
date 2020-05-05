package com.example.win.easy.view.main

import android.os.Bundle
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
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.BooleanSemaphore
import com.example.win.easy.R
import com.example.win.easy.display.DisplayServiceAdapter
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.viewmodel.SongListViewModel
import com.example.win.easy.web.callback.OnReadyFunc
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import com.qmuiteam.qmui.widget.QMUITopBar
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.invocation.InvocationOnMock
import java.util.*
import java.util.concurrent.Executors

class SongListFragmentTest {
    @InjectMockKs lateinit var songListFragment: SongListFragment
    @MockK lateinit var displayServiceAdapter: DisplayServiceAdapter
    @MockK lateinit var downloadServiceAdapter: DownloadServiceAdapter
    @MockK lateinit var fragmentFactory: FragmentFactory
    @MockK lateinit var viewModelFactory: ViewModelProvider.Factory
    @MockK(relaxUnitFun = true) lateinit var songListViewModel: SongListViewModel
    @MockK(relaxUnitFun = true) lateinit var mockNavController: NavController

    /**
     *
     * 简陋地测试下界面是不是正常显示
     *
     * 把带字的、特定类型的元素确认下isDisplayed就完了
     */
    @Test
    fun testDisplay() {
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDescendantOfA(CoreMatchers.instanceOf(QMUITopBar::class.java)), ViewMatchers.withText(selectedSongList.getName()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(CoreMatchers.instanceOf(QMUIAlphaImageButton::class.java)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(downloadedSong.name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(nonDownloadedSongWithUrl.name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     *
     * 测试当点击右上角的按钮时，触发到“添加歌曲到歌单”fragment的导航
     */
    @Test
    fun testAddSongToSongList() {
        Espresso.onView(CoreMatchers.instanceOf(QMUIAlphaImageButton::class.java)).perform(ViewActions.click())
        verify{ mockNavController.navigate(SongListFragmentDirections.actionSongListFragmentToAddSongToSongListFragment(selectedSongList)) }
    }

    /**
     *
     * 测试当点击一首已经下载好的歌曲时，直接播放
     */
    //    @Test public
    fun testClickDownloadedSong() {
        // TODO
        Espresso.onView(ViewMatchers.withText(downloadedSong.name)).perform(ViewActions.click())
        //        verify(displayServiceAdapter,times(1)).startWith(downloadedSong, songsInSelectedSongList);
    }

    /**
     *
     * 测试当点击一首还没下载的歌曲时，先触发下载，然后等下载结束后，触发播放
     */
    //    @Test public
    fun testClickDownloadedSongWith() {
        // TODO
        Espresso.onView(ViewMatchers.withText(nonDownloadedSongWithUrl.name)).perform(ViewActions.click())
//        Mockito.verify(downloadServiceAdapter, Mockito.times(1))!!.download(ArgumentMatchers.eq(nonDownloadedSongWithUrl), ArgumentMatchers.any(OnReadyFunc::class.java))
        //        verify(displayServiceAdapter,times(1)).startWith(downloadedSongFromUrl, songsInSelectedSongList);
    }



    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockDownloadService()
        mockLiveDataFromViewModel()
        mockViewModelFactory()
        mockFragmentFactory()
        launchTestedFragment()
    }

    private fun mockDownloadService() {
//        Mockito.doAnswer { invocation: InvocationOnMock ->
//
//            //获取参数
//            val songToDownload = invocation.getArgument<SongVO>(0)
//            val onReadyFunc = invocation.getArgument<OnReadyFunc<SongVO?>>(1)
//
//            //模拟发起请求
//            Executors.newSingleThreadExecutor().execute { mockDownloadTask() }
//            waitForDownloadFinished()
//            onReadyFunc.onReady(downloadedSongFromUrl)
//            null
//        }.`when`(downloadServiceAdapter)!!.download(ArgumentMatchers.any(SongVO::class.java), ArgumentMatchers.any(OnReadyFunc::class.java))
    }

    private fun mockDownloadTask() {
        downloading()
        notifyDownloadFinished()
    }

    private fun downloading() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun mockLiveDataFromViewModel() {
        every { songListViewModel.loadSongsIn(selectedSongList) } returns songsInSelectedSongList
    }

    private fun mockViewModelFactory() {
        every { viewModelFactory.create(SongListViewModel::class.java) } returns songListViewModel
    }

    private fun mockFragmentFactory() {
        every { fragmentFactory.instantiate(any(),SongListFragment::class.java.name) } returns songListFragment
    }

    private fun launchTestedFragment() {
        val scenario = FragmentScenario.launchInContainer(SongListFragment::class.java, bundlePassedFromSelection, R.style.AppTheme, fragmentFactory)
        scenario.onFragment { fragment: SongListFragment -> Navigation.setViewNavController(fragment.requireView(), mockNavController) }
    }

    private fun waitForDownloadFinished() {
        synchronized(downloadFinished) {
            synchronized(mainThreadWaiting) { mainThreadWaiting.isReady = true }
            try {
//                downloadFinished.wait()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun notifyDownloadFinished() {
//        while (true) {
//            synchronized(mainThreadWaiting) { if (mainThreadWaiting.isReady) break }
//        }
//        synchronized(downloadFinished) { downloadFinished.notifyAll() }
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


    //用于同步的数据
    private val mainThreadWaiting = BooleanSemaphore(false)
    private val downloadFinished = Any()
}