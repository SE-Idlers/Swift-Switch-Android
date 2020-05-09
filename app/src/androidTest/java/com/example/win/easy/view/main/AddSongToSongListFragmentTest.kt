package com.example.win.easy.view.main

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.win.easy.R
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.value_object.SongListVO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.viewmodel.RelationViewModel
import com.example.win.easy.viewmodel.SongListViewModel
import com.example.win.easy.viewmodel.SongViewModel
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import java.util.*

@RunWith(AndroidJUnit4ClassRunner::class)
class AddSongToSongListFragmentTest {
    /**
     *
     * 测试依次点击三首歌，点击添加后，能够正确添加到歌单
     *
     * tips:“能够正确添加到歌单”的验证是通过验证对viewModel的正确调用来进行的
     */
    @Test
    fun testClickSongsToAdd() {
        click(song1)
        click(song3)
        clickAddButton()
        verifySongsAdd(song1, song3)
        verifyNavigationToSongListFragment()
    }

    /**
     *
     * 测试当同一首歌被点击两次后会被取消添加
     */
    @Test
    fun testClickThenCancelAdd() {
        click(song1)
        click(song2)
        click(song2)
        clickAddButton()
        verifySongsAdd(song1)
        verifyNavigationToSongListFragment()
    }

    /**
     *
     * 测试标题栏正常显示
     */
    @Test
    fun testTopBarDisplayed() {
        Espresso.onView(ViewMatchers.withText(topBarTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun click(song: SongDO) {
        Espresso.onView(ViewMatchers.withText(song.name)).perform(ViewActions.click())
    }

    private fun clickAddButton() {
        Espresso.onView(CoreMatchers.instanceOf(QMUIAlphaImageButton::class.java)).perform(ViewActions.click())
    }

    private fun verifySongsAdd(vararg songDOs: SongDO) {
        verify(exactly = 1) { relationViewModel.addSongsTo(listOf(*songDOs), testSongList) }
    }

    private fun verifyNavigationToSongListFragment() {
        verify(exactly = 1) { navController.navigate(AddSongToSongListFragmentDirections.actionAddSongToSongListFragmentToSongListFragment(testSongList)) }
        navController= mockkClass(NavController::class)
        scenario.onFragment{fragment -> Navigation.setViewNavController(fragment.requireView(),navController)}
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockViewModel()
        mockViewModelFactory()
        mockFragmentFactory()
        launchTestFragment()
    }

    private fun mockViewModel(){
        every { songViewModel.launchLoadSongsExclude(testSongList,captureLambda()) } answers {
            lambda<(List<SongDO>)->Unit>().captured(songsNotInTestSongList)
        }
    }

    private fun mockFragmentFactory() {
        every { fragmentFactory.instantiate(any(),AddSongToSongListFragment::class.java.name) } returns addSongToSongListFragment
    }

    private fun mockViewModelFactory() {
        every { viewModelFactory.create(SongViewModel::class.java) } returns songViewModel
        every { viewModelFactory.create(RelationViewModel::class.java) } returns relationViewModel
    }

    private fun launchTestFragment() {
        scenario = FragmentScenario.launchInContainer(AddSongToSongListFragment::class.java, bundleToPass, R.style.AppTheme, fragmentFactory)
        scenario.onFragment { fragment: AddSongToSongListFragment -> Navigation.setViewNavController(fragment.requireView(), navController) }
    }

    @InjectMockKs lateinit var addSongToSongListFragment: AddSongToSongListFragment
    @RelaxedMockK lateinit var fragmentFactory: FragmentFactory
    @RelaxedMockK lateinit var viewModelFactory: ViewModelProvider.Factory
    @RelaxedMockK lateinit var songViewModel: SongViewModel
    @RelaxedMockK lateinit var relationViewModel: RelationViewModel
    @RelaxedMockK lateinit var navController: NavController

    private lateinit var scenario: FragmentScenario<AddSongToSongListFragment>
    private val topBarTitle = "选择要添加的歌曲"
    private val song1 = SongDO(name="球球你了")
    private val song2 = SongDO(name="我他妈实在是")
    private val song3 = SongDO(name="编不出来啦i-i")
    private val testSongList = SongListDO(name="艰难的人生")
    private val songsNotInTestSongList: MutableList<SongDO> = arrayListOf(song1,song2,song3)
    private val bundleToPass = Bundle().apply { putSerializable("songListSongsAddedTo", testSongList) }
}