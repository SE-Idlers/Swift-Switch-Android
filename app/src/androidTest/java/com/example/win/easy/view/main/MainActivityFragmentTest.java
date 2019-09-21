package com.example.win.easy.view.main;

import android.widget.ImageButton;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.R;
import com.example.win.easy.web.service.LoginService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityFragmentTest {

    /**
     * <p>测试顶栏的功能：</p>
     * <ol>
     *     <li>标题显示是不是正常</li>
     *     <li>前往登陆界面的按钮是不是正常</li>
     * </ol>
     */
    @Test
    public void testTopBarFunctionality(){

        verifyTopBarTitleDisplayed();

        clickButtonToLogin();
        verifyNavigationToLoginFragment(1);

        login();

        clickButtonToLogin();
        verifyNavigationToLoginFragment(1);
    }

    private void verifyTopBarTitleDisplayed() {
        onView(withText(textOnTopBar)).check(matches(isDisplayed()));
    }

    /**
     * <p>测试所有歌单、歌曲item的功能（导航）</p>
     */
    @Test
    public void testItemNavigation(){
        clickItemToAllSong();
        verifyNavigationToAllSongFragment();

        clickItemToAllSongList();
        verifyNavigationToAllSongListFragment();

        clickButtonToAddLocalSong();
        verifyNavigationToAddLocalSongFragment();

        clickButtonToCreateSongList();
        verifyNavigationToSongListCreationFragment();
    }

    private void clickItemToAllSong(){
        onView(withText(textOnItemToAllSong)).perform(click());
    }

    private void clickItemToAllSongList(){
        onView(withText(textOnItemToAllSongList)).perform(click());
    }

    private void clickButtonToAddLocalSong(){
        onView(allOf(instanceOf(ImageButton.class),isDescendantOfA(withChild(withChild(withText(textOnItemToAllSong)))))).perform(click());
    }

    private void clickButtonToCreateSongList(){
        onView(allOf(instanceOf(ImageButton.class),isDescendantOfA(withChild(withChild(withText(textOnItemToAllSongList)))))).perform(click());
    }

    private void clickButtonToLogin(){
        onView(withId(R.id.loginButtonId)).perform(click());
    }

    private void verifyNavigationToAllSongFragment(){
        NavDirections ac=MainActivityFragmentDirections.actionMainActivityFragmentToAllSongsFragment();
        verify(navController,times(1)).navigate(ac);
    }

    private void verifyNavigationToAllSongListFragment(){
        verify(navController,times(1)).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToAllSongListsFragment());
    }

    private void verifyNavigationToAddLocalSongFragment(){
        verify(navController,times(1)).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToPlaceholder());
    }

    private void verifyNavigationToSongListCreationFragment(){
        verify(navController,times(1)).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToSongListCreationFragment());
    }

    private void verifyNavigationToLoginFragment(int times){
        verify(navController,times(times)).navigate(MainActivityFragmentDirections.actionMainActivityFragmentToLoginFragment());
    }

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mockFragmentFactory();
        launchTestFragment();
    }

    private void mockFragmentFactory(){
        when(fragmentFactory.instantiate(any(ClassLoader.class),eq(MainActivityFragment.class.getName()))).thenReturn(mainActivityFragment);
    }

    private void launchTestFragment(){
        FragmentScenario<MainActivityFragment> scenario=FragmentScenario.launchInContainer(MainActivityFragment.class,null, R.style.AppTheme,fragmentFactory);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),navController));
    }

    private void login(){
        doReturn(true).when(loginService).hasLogin();
    }

    private void logout(){
        doReturn(false).when(loginService).hasLogin();
    }

    @InjectMocks MainActivityFragment mainActivityFragment;
    @Mock NavController navController;
    @Mock FragmentFactory fragmentFactory;
    @Mock LoginService loginService;

    private String textOnItemToAllSong="所有歌曲";
    private String textOnItemToAllSongList="所有歌单";
    private String textOnTopBar="我的";
}