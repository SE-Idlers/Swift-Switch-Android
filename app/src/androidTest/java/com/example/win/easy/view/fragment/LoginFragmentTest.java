package com.example.win.easy.view.fragment;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.win.easy.BooleanSemaphore;
import com.example.win.easy.R;
import com.example.win.easy.enumeration.LoginType;
import com.example.win.easy.view.activity.MainActivity;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.service.LoginService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Executors;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.win.easy.enumeration.LoginType.Phone;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginFragmentTest {

    /**
     * <p>测试一次正常的手机号登陆</p>
     * <p>模拟输入账号，密码，然后点击登陆，进行两次验证：</p>
     * <ol>
     *     <li>在登陆过程中验证“正在登陆”的提示框正常显示</li>
     *     <li>在登陆完成之后验证“正在登陆”的提示框消失而且登陆状态正确，并导航到主界面</li>
     * </ol>
     */
    @Test
    public void testSuccessfulPhoneLogin() throws InterruptedException {

        typePhoneUsername();
        typePassword(truePassword);
        clickLogin();

        verifySuccessfulLogin();

        tearDown();
    }

    /**
     * <p>测试登陆方式转换正常</p>
     * <p>先测试手机登陆切换到Email登陆，再测试Email登陆切换到手机登陆，对每次切换都验证：</p>
     * <ol>
     *     <li>输入框的提示词(hint)换了</li>
     *     <li>切换按钮上的文字换了（切换到xx登陆）</li>
     * </ol>
     * <p>但是其实还应该测试输入框的textType由手机类型到邮箱类型的相互切换，但是这个做不到，只好实际跑起来的时候测试了</p>
     */
    @Test
    public void testLoginTypeSwitch(){
        testPhone2EmailSwitch();
        testEmail2PhoneSwitch();
    }

    /**
     * <p>测试正常的邮箱登陆</p>
     * <p>流程跟手机登陆基本一样，主要是要测试下切换登录方式后仍然可以成功登陆</p>
     */
    @Test
    public void testSuccessfulEmailLogin() throws InterruptedException {
        switchToEmailLogin();

        typeEmailUsername();
        typePassword(truePassword);
        clickLogin();

        verifySuccessfulLogin();

        tearDown();
    }

    /**
     * <p>测试登陆失败</p>
     */
    @Test
    public void testUnsuccessfulLogin() {
//        typePhoneUsername();
//        typePassword(wrongPassword);
//        clickLogin();
        //TODO 想办法测试“登陆失败”的dialog显示后过一下就消失
    }

    private void typePhoneUsername(){
        onView(withHint(phoneAccountHint)).perform(typeText(phoneUsername));
    }

    private void typePassword(String password){
        onView(withHint(passwordHint)).perform(typeText(password));
    }

    private void clickLogin(){
        onView(withText(loginButtonText)).perform(click());
    }

    private void verifySuccessfulLogin() throws InterruptedException {
        verifyLoginingDialogDisplayed();
        waitForLoginFinished();

        verifyLoginingDialogDismiss();
        verifySuccessfulLoginState();
        verifyNavigationToMainFragment();
    }

    private void verifyLoginingDialogDisplayed(){
        onView(withText(loadingHint)).check(matches(isDisplayed()));
    }

    private void verifyLoginingDialogDismiss(){
        onView(withText(loadingHint)).check(doesNotExist());
    }

    private void verifySuccessfulLoginState() {
        assertTrue(loginService.hasLogin());
        assertEquals(uid,loginService.getCurrentUid());
    }

    private void verifyNavigationToMainFragment() {
        verify(navController,times(1)).navigate(LoginFragmentDirections.actionLoginFragmentToMainActivityFragment());
    }

    private void testPhone2EmailSwitch(){
        verifyPhoneLoginHintDisplayed();
        switchToEmailLogin();
        verifyEmailLoginHintDisplayed();
    }

    private void verifyPhoneLoginHintDisplayed(){
        onView(withHint(phoneAccountHint)).check(matches(isDisplayed()));
        onView(withText(swithToEmailLoginText)).check(matches(isDisplayed()));
    }

    private void switchToEmailLogin(){
        onView(withText(swithToEmailLoginText)).perform(click());
    }

    private void verifyEmailLoginHintDisplayed(){
        onView(withHint(emailAccountHint)).check(matches(isDisplayed()));
        onView(withText(swithToPhoneLoginText)).check(matches(isDisplayed()));
    }

    private void testEmail2PhoneSwitch(){
        verifyEmailLoginHintDisplayed();
        switchToPhoneLogin();
        verifyPhoneLoginHintDisplayed();
    }

    private void switchToPhoneLogin(){
        onView(withText(swithToPhoneLoginText)).perform(click());
    }

    private void typeEmailUsername(){
        onView(withHint(emailAccountHint)).perform(typeText(emailUsername));
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setUpLoginFragment();
        mockLoginService();
        mockFragmentFactory();

        launchTestFragment();
    }

    private void setUpLoginFragment() {
        loginFragment=new LoginFragment(loginService,loginType);
    }

    private void mockLoginService(){
        Answer<Object> mockLoginInvocation=invocation -> {
            String password=invocation.getArgument(1);
            OnReadyFunc<Boolean> onReadyFunc=invocation.getArgument(2);

            networkThreadExecute(()->{
                waitForLoginDialogVerification();

                logining();
                processLoginResult(password,onReadyFunc);

                notifyLoginFinished();
            });
            return null;
        };
        doAnswer(mockLoginInvocation).when(loginService).loginByPhone(eq(phoneUsername),anyString(),any(OnReadyFunc.class));
        doAnswer(mockLoginInvocation).when(loginService).loginByEmail(eq(emailUsername),anyString(),any(OnReadyFunc.class));
    }

    private void networkThreadExecute(Runnable runnable){
        Executors.newSingleThreadExecutor().execute(runnable);
    }

    private void logining(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processLoginResult(String password,OnReadyFunc<Boolean> onReadyFunc){
        try {
            runCallbackInMainThread(()->{
                if (isCorrect(password)){
                    setSuccessfulLoginState();
                    onReadyFunc.onReady(true);
                }
                else onReadyFunc.onReady(false);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void setSuccessfulLoginState(){
        doReturn(true).when(loginService).hasLogin();
        doReturn(uid).when(loginService).getCurrentUid();
    }

    private void runCallbackInMainThread(Runnable runnable) throws Throwable {
        activityActivityTestRule.runOnUiThread(runnable);
    }

    private boolean isCorrect(String password){
        return password.equals(truePassword);
    }

    private void mockFragmentFactory(){
        when(fragmentFactory.instantiate(any(ClassLoader.class),eq(LoginFragment.class.getName()))).thenReturn(loginFragment);
    }

    private void launchTestFragment(){
        FragmentScenario<LoginFragment> scenario=FragmentScenario.launchInContainer(LoginFragment.class,null, R.style.AppTheme,fragmentFactory);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(),navController));
    }



    private void tearDown() {
        doReturn(false).when(loginService).hasLogin();
        doReturn(null).when(loginService).getCurrentUid();
    }

    private void waitForLoginFinished() throws InterruptedException {
        synchronized (loginFinished) {
            synchronized (loginDialogVerification) {
                loginDialogVerification.setReady(true);
            }
            loginFinished.wait();
        }
    }

    private void waitForLoginDialogVerification(){
        while (true){
            synchronized (loginDialogVerification){
                if(loginDialogVerification.isReady())
                    break;
            }
        }
    }

    private void notifyLoginFinished(){
        synchronized (loginFinished){
            loginFinished.notifyAll();
        }
    }

    @Spy LoginService loginService=new LoginService(null);
    private LoginFragment loginFragment;
    private LoginType loginType=Phone;
    @Mock FragmentFactory fragmentFactory;
    @Mock NavController navController;
    @Rule public ActivityTestRule<MainActivity>  activityActivityTestRule=new ActivityTestRule<>(MainActivity.class);

    private String phoneAccountHint ="手机号";
    private String emailAccountHint="邮箱";
    private String passwordHint="密码";

    private String loginButtonText ="登陆";
    private String swithToEmailLoginText="切换到邮箱登陆";
    private String swithToPhoneLoginText="切换到手机登陆";

    private String loadingHint="正在登陆";

    private String phoneUsername="15564278737";
    private String emailUsername="15564278737@163.com";

    private String truePassword="zxc486251379";
    private String wrongPassword="123456";

    private String uid="777777";

    private final BooleanSemaphore loginDialogVerification=new BooleanSemaphore(false);
    private final Object loginFinished=new Object();
}