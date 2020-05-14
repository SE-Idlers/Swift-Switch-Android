package com.example.win.easy.view.main

import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.R
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.exception.SongListToCreateAlreadyExistLocallyException
import com.example.win.easy.db.SongListDO
import com.example.win.easy.viewmodel.SongListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SongListCreationFragmentTest {

    /**
     * 测试成功的歌单创建：
     *  1. 成功调用viewModel方法
     *  1. 成功后页面导航
     */
    @Test
    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    fun testSuccessfulSongListCreation() {
        typeSongListName(newSongListName)
        clickSongListCreationButton()
        verifySuccessfulCreation()
        verifyNavigationAfterSuccessfulCreation()
    }

    /**
     * 测试创建重名的歌单：
     * 验证viewModel用错误的参数被调用
     */
    @Test
    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    fun testUnsuccessfulSongListCreationForAlreadyExisted() {
        typeSongListName(existedSongListName)
        clickSongListCreationButton()
        verifyUnsuccessfulCreationForAlreadyExisted()
    }

    /**
     *
     * 测试不输入名字直接创建歌单：
     *
     * 验证viewModel不被调用
     */
    @Test
    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    fun testUnsuccessfulSongListCreationForEmpty() {
        clickSongListCreationButton()
        verifyUnsuccessfulCreationForEmpty()
    }

    private fun typeSongListName(songListName: String) {
        Espresso.onView(ViewMatchers.withHint(newSongListCreationHint)).perform(ViewActions.replaceText(songListName))
    }

    private fun clickSongListCreationButton() {
        Espresso.onView(ViewMatchers.withText(textOnNewSongListCreationButton)).perform(ViewActions.click())
    }

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    private fun verifySuccessfulCreation() {
        verify(exactly = 1) { songListViewModel.insert(newSongList) }
    }

    private fun verifyNavigationAfterSuccessfulCreation() {
        verify(exactly = 1) { navController.navigateUp() }
    }

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    private fun verifyUnsuccessfulCreationForAlreadyExisted() {
        verify(exactly = 1) { songListViewModel.insert(existedSongList) }
    }

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    private fun verifyUnsuccessfulCreationForEmpty() {
        verify(exactly = 0) { songListViewModel.insert(emptySongList) }
    }

    @Before
    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mockViewModel()
        mockVieModelFactory()
        mockFragmentFactory()
        launchTestFragment()
    }

    @Throws(SongListToCreateAlreadyExistLocallyException::class)
    private fun mockViewModel() {
        every { songListViewModel.insert(existedSongList) } throws SongListToCreateAlreadyExistLocallyException()
    }

    private fun mockVieModelFactory() {
        every { viewModelFactory.create(SongListViewModel::class.java) } returns songListViewModel
    }

    private fun mockFragmentFactory() {
        every { fragmentFactory.instantiate(any(),SongListCreationFragment::class.java.name) } returns songListCreationFragment
    }

    private fun launchTestFragment() {
        val scenario = FragmentScenario.launchInContainer(SongListCreationFragment::class.java, null, R.style.AppTheme, fragmentFactory)
        scenario.onFragment { fragment: SongListCreationFragment -> Navigation.setViewNavController(fragment.requireView(), navController) }
    }

    @InjectMockKs lateinit var songListCreationFragment: SongListCreationFragment
    @RelaxedMockK lateinit var fragmentFactory: FragmentFactory
    @RelaxedMockK lateinit var navController: NavController
    @RelaxedMockK lateinit var songListViewModel: SongListViewModel
    @RelaxedMockK lateinit var viewModelFactory: ViewModelProvider.Factory

    private val newSongListName = "乐队有很多"
    private val existedSongListName = "但新裤子只有一条"
    private var newSongList = SongListDO(name = newSongListName, source = DataSource.Local)
    private var existedSongList = SongListDO(name = existedSongListName, source = DataSource.Local)
    private var emptySongList = SongListDO(name = "", source = DataSource.Local)
    private val newSongListCreationHint = "我的新歌单已经饥渴难耐了"
    private val textOnNewSongListCreationButton = "给我建他娘的！"
}