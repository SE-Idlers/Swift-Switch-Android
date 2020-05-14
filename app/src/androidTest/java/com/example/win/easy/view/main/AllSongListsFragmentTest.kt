package com.example.win.easy.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import androidx.test.rule.ActivityTestRule
import com.example.win.easy.R
import com.example.win.easy.db.SongListDO
import com.example.win.easy.view.ImageService
import com.example.win.easy.viewmodel.SongListViewModel
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AllSongListsFragmentTest {
    /**
     * 测试当点击一个歌单的时候会触发进入歌单，具体来说，需要验证下面这些：
     *  1. 触发了导航并导航到正确的fragment
     *  1. 传递了正确的参数（选择的歌单）
     * 当然啦，这些东西用一句verify就可以了
     */
    @Test
    fun testNavigationToSongListFragment() {
        //点击歌单名为name1的显示项
        Espresso.onView(ViewMatchers.withText(name1)).perform(ViewActions.click())
        verify(exactly = 1) { mockNavController.navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToSongListFragment(songList1)) }
    }

    /**
     * 测试对于那种头像路径不空的，能发起解码任务解码图片
     */
    @Test
    fun testAvatarDecode() {
        verify(exactly = 1) { imageService.decode(songList1.avatarPath) }
    }

    /**
     * 测试当所有歌单的LiveData更新时，视图（显示的那一个个歌单Item）能相应地更新
     */
    @Test
    @Throws(Throwable::class)
    fun testDataUpdate() {

        //验证更新前的状态
        Espresso.onView(ViewMatchers.withText(name2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(newName)).check(ViewAssertions.doesNotExist())


        //在UI线程中更新LiveData
        rule.runOnUiThread { changeSongListNameTo(newName) }


        //验证更新后的状态
        Espresso.onView(ViewMatchers.withText(name2)).check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withText(newName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * 测试当点击右上角的图标想创建歌单时能正确导航到创建歌单的fragment
     */
    @Test
    fun testNavigationToSongListCreation() {
        Espresso.onView(CoreMatchers.instanceOf(QMUIAlphaImageButton::class.java)).perform(ViewActions.click())
        verify(exactly = 1) { mockNavController.navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToPlaceholder()) }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        //mock掉LiveData,ViewModel的工厂还有Fragment的工厂
        every { mockSongListViewModel.loadAll() } returns allSongListsLiveData
        every { mockViewModelFactory.create(SongListViewModel::class.java) } returns mockSongListViewModel
        every { mockFragmentFactory.instantiate(any(),AllSongListsFragment::class.java.name) } returns allSongListsFragment

        //启起来要测试的Fragment（因为没有要传给他的参数，所以arg是null，而主题是AppTheme要设置，最后设置用来初始化它的工厂类）
        val scenario = FragmentScenario.launchInContainer(AllSongListsFragment::class.java, null, R.style.AppTheme, mockFragmentFactory)
        //把原来的NavController替换成mock的NavController
        scenario.onFragment { fragment: AllSongListsFragment -> Navigation.setViewNavController(fragment.requireView(), mockNavController) }
    }

    private fun changeSongListNameTo(newName: String) {
        songList2.name=newName
        allSongListsLiveData.postValue(allSongLists)
    }

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule var rule = ActivityTestRule(MainActivity::class.java)

    @InjectMockKs lateinit var allSongListsFragment: AllSongListsFragment
    @RelaxedMockK lateinit var mockViewModelFactory: ViewModelProvider.Factory
    @RelaxedMockK lateinit var mockFragmentFactory: FragmentFactory
    @RelaxedMockK lateinit var mockSongListViewModel: SongListViewModel
    @RelaxedMockK lateinit var mockNavController: NavController
    @RelaxedMockK lateinit var imageService: ImageService

    private val name1 = "背心尊者大战美国队长"
    private val name2 = "从何时开始怕遥望星辰"
    private val newName = "享受却不知情"
    private var songList1 = SongListDO(id = 1, name = name1, avatarPath = "/data/user/0")
    private var songList2 = SongListDO(id = 2, name = name2)
    private var allSongLists = arrayListOf(songList1,songList2)
    private val allSongListsLiveData = MutableLiveData<List<SongListDO>>().apply { postValue(allSongLists) }
}