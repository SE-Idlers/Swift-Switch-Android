package com.example.win.easy.view.main;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.win.easy.R;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.view.ImageService;
import com.example.win.easy.viewmodel.SongListViewModel;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class AllSongListsFragmentTest {


    /**
     * <p>测试当点击一个歌单的时候会触发进入歌单，具体来说，需要验证下面这些：</p>
     * <ol>
     *     <li>触发了导航并导航到正确的fragment</li>
     *     <li>传递了正确的参数（选择的歌单）</li>
     *      * </ol>
     * <p>当然啦，这些东西用一句verify就可以了</p>
     */
    @Test
    public void testNavigationToSongListFragment(){

        //点击歌单名为name1的显示项
        onView(withText(name1)).perform(click());

        //确认一下确实触发了导航，而且带着正确的参数
        verify(mockNavController,times(1))
                .navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToSongListFragment(songList1));
    }

    /**
     * <p>测试对于那种头像路径不空的，能发起解码任务解码图片</p>
     */
    @Test
    public void testAvatarDecode(){
        verify(imageService, times(1)).decode(eq(songList1.getAvatarPath()),any(OnReadyFunc.class));
    }

    /**
     * <p>测试当所有歌单的LiveData更新时，视图（显示的那一个个歌单Item）能相应地更新</p>
     */
    @Test
    public void testDataUpdate() throws Throwable {

        //验证更新前的状态
        onView(withText(name2)).check(matches(isDisplayed()));
        onView(withText(newName)).check(doesNotExist());


        //在UI线程中更新LiveData
        rule.runOnUiThread(()->changeSongListNameTo(newName));


        //验证更新后的状态
        onView(withText(name2)).check(doesNotExist());
        onView(withText(newName)).check(matches(isDisplayed()));

    }

    /**
     * <p>测试当点击右上角的图标想创建歌单时能正确导航到创建歌单的fragment</p>
     */
    @Test
    public void testNavigationToSongListCreation(){
        onView(instanceOf(QMUIAlphaImageButton.class)).perform(click());
        verify(mockNavController,times(1)).navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToPlaceholder());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        //设置测试使用的数据
        setUpData();

        //mock掉LiveData,ViewModel的工厂还有Fragment的工厂
        doReturn(allSongListsLiveData).when(mockSongListViewModel).loadAll();
        when(mockViewModelFactory.create(SongListViewModel.class)).thenReturn(mockSongListViewModel);
        when(mockFragmentFactory.instantiate(any(ClassLoader.class),eq(AllSongListsFragment.class.getName()))).thenReturn(allSongListsFragment);

        //启起来要测试的Fragment（因为没有要传给他的参数，所以arg是null，而主题是AppTheme要设置，最后设置用来初始化它的工厂类）
        FragmentScenario<AllSongListsFragment> scenario=FragmentScenario.launchInContainer(AllSongListsFragment.class,null,R.style.AppTheme,mockFragmentFactory);

        //把原来的NavController替换成mock的NavController
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),mockNavController));
    }
    private void setUpData(){
        songList1=SongListVO.builder().id(1).name(name1).avatarPath("/data/user/0").build();
        songList2=SongListVO.builder().id(2).name(name2).build();
        allSongLists=new ArrayList<>();
        allSongLists.add(songList1);
        allSongLists.add(songList2);
        allSongListsLiveData.postValue(allSongLists);
    }
    private void changeSongListNameTo(String newName) {
        songList2.setName(newName);
        allSongListsLiveData.postValue(allSongLists);
    }

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule=new InstantTaskExecutorRule();
    @Rule public ActivityTestRule<MainActivity> rule=new ActivityTestRule<>(MainActivity.class);

    @InjectMocks AllSongListsFragment allSongListsFragment;
    @Mock ViewModelProvider.Factory mockViewModelFactory;
    @Mock FragmentFactory mockFragmentFactory;
    @Mock SongListViewModel mockSongListViewModel;
    @Mock NavController mockNavController;
    @Mock ImageService imageService;

    private List<SongListVO> allSongLists;
    private SongListVO songList1;
    private SongListVO songList2;

    private String name1="背心尊者大战美国队长";
    private String name2="从何时开始怕遥望星辰";
    private String newName="享受却不知情";

    private MutableLiveData<List<SongListVO>> allSongListsLiveData=new MutableLiveData<>();
}

