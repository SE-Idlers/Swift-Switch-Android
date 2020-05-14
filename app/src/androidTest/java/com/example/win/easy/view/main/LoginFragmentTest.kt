package com.example.win.easy.view.main

import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.win.easy.R
import com.example.win.easy.enumeration.LoginType
import com.example.win.easy.network.BackendRequestService
import com.example.win.easy.network.LoginService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class LoginFragmentTest {
    /**
     *
     * 测试一次正常的手机号登陆
     *
     * 模拟输入账号，密码，然后点击登陆，进行两次验证：
     *
     *  1. 在登陆过程中验证“正在登陆”的提示框正常显示
     *  1. 在登陆完成之后验证“正在登陆”的提示框消失而且登陆状态正确，并导航到主界面
     *
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSuccessfulPhoneLogin() = runBlocking{
        typePhoneUsername()
        typePassword(truePassword)
        clickLogin()
        yield()
        verifySuccessfulLogin()
        tearDown()
    }

    /**
     *
     * 测试登陆方式转换正常
     *
     * 先测试手机登陆切换到Email登陆，再测试Email登陆切换到手机登陆，对每次切换都验证：
     *
     *  1. 输入框的提示词(hint)换了
     *  1. 切换按钮上的文字换了（切换到xx登陆）
     *
     *
     * 但是其实还应该测试输入框的textType由手机类型到邮箱类型的相互切换，但是这个做不到，只好实际跑起来的时候测试了
     */
    @Test
    fun testLoginTypeSwitch() {
        testPhone2EmailSwitch()
        testEmail2PhoneSwitch()
    }

    /**
     *
     * 测试正常的邮箱登陆
     *
     * 流程跟手机登陆基本一样，主要是要测试下切换登录方式后仍然可以成功登陆
     */
    @Test
    @Throws(InterruptedException::class)
    fun testSuccessfulEmailLogin() {
        switchToEmailLogin()
        typeEmailUsername()
        typePassword(truePassword)
        clickLogin()
        verifySuccessfulLogin()
        tearDown()
    }

    /**
     *
     * 测试登陆失败
     */
    @Test
    fun testUnsuccessfulLogin() {
//        typePhoneUsername();
//        typePassword(wrongPassword);
//        clickLogin();
        //TODO 想办法测试“登陆失败”的dialog显示后过一下就消失
    }

    private fun typePhoneUsername() {
        Espresso.onView(ViewMatchers.withHint(phoneAccountHint)).perform(ViewActions.typeText(phoneUsername))
    }

    private fun typePassword(password: String) {
        Espresso.onView(ViewMatchers.withHint(passwordHint)).perform(ViewActions.typeText(password))
    }

    private fun clickLogin() {
        Espresso.onView(ViewMatchers.withText(loginButtonText)).perform(ViewActions.click())
    }

    private fun verifySuccessfulLogin() {
        verifySuccessfulLoginState()
        verifyNavigationToMainFragment()
    }

    private fun verifySuccessfulLoginState() {
        TestCase.assertTrue(loginService.hasLogin())
        TestCase.assertEquals(uid, loginService.currentUid)
    }

    private fun verifyNavigationToMainFragment() {
        verify(exactly = 1) { navController.navigateUp() }
    }

    private fun testPhone2EmailSwitch() {
        verifyPhoneLoginHintDisplayed()
        switchToEmailLogin()
        verifyEmailLoginHintDisplayed()
    }

    private fun verifyPhoneLoginHintDisplayed() {
        Espresso.onView(ViewMatchers.withHint(phoneAccountHint)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(switchToEmailLoginText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun switchToEmailLogin() {
        Espresso.onView(ViewMatchers.withText(switchToEmailLoginText)).perform(ViewActions.click())
    }

    private fun verifyEmailLoginHintDisplayed() {
        Espresso.onView(ViewMatchers.withHint(emailAccountHint)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(switchToPhoneLoginText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun testEmail2PhoneSwitch() {
        verifyEmailLoginHintDisplayed()
        switchToPhoneLogin()
        verifyPhoneLoginHintDisplayed()
    }

    private fun switchToPhoneLogin() {
        Espresso.onView(ViewMatchers.withText(switchToPhoneLoginText)).perform(ViewActions.click())
    }

    private fun typeEmailUsername() {
        Espresso.onView(ViewMatchers.withHint(emailAccountHint)).perform(ViewActions.typeText(emailUsername))
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setUpLoginFragment()
        mockLoginService()
        mockFragmentFactory()
        launchTestFragment()
    }

    private fun setUpLoginFragment() {
        loginFragment = LoginFragment(loginService, loginType)
    }

    private fun mockLoginService() {
        coEvery { loginService.login(phoneUsername,truePassword,LoginType.Phone) } coAnswers {setSuccessfulLoginState()}
        coEvery { loginService.login(emailUsername,truePassword,LoginType.Email) } coAnswers {setSuccessfulLoginState()}
        coEvery { loginService.login(phoneUsername,wrongPassword,LoginType.Phone) } throws Throwable("wrong password")
    }

    private fun setSuccessfulLoginState() {
        every { loginService.hasLogin() } returns true
        every { loginService.currentUid } returns uid
    }

    @Throws(Throwable::class)
    private fun runCallbackInMainThread(runnable: Runnable) {
        activityActivityTestRule.runOnUiThread(runnable)
    }

    private fun mockFragmentFactory() {
        every { fragmentFactory.instantiate(any(),LoginFragment::class.java.name) } returns loginFragment
    }

    private fun launchTestFragment() {
        val scenario = FragmentScenario.launchInContainer(LoginFragment::class.java, null, R.style.AppTheme, fragmentFactory)
        scenario.onFragment { fragment: LoginFragment -> Navigation.setViewNavController(fragment.requireView(), navController) }
    }

    private fun tearDown() {
        every { loginService.hasLogin() } returns false
        every { loginService.currentUid } returns null
    }

    @InjectMockKs lateinit var loginFragment: LoginFragment
    @SpyK var loginService = LoginService(mockkClass(BackendRequestService::class))
    @RelaxedMockK lateinit var fragmentFactory: FragmentFactory
    @RelaxedMockK lateinit var navController: NavController
    private val loginType = LoginType.Phone
    @get:Rule var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private val phoneAccountHint = "手机号"
    private val emailAccountHint = "邮箱"
    private val passwordHint = "密码"
    private val loginButtonText = "登陆"
    private val switchToEmailLoginText = "切换到邮箱登陆"
    private val switchToPhoneLoginText = "切换到手机登陆"
    private val loadingHint = "正在登陆"
    private val phoneUsername = "15564278737"
    private val emailUsername = "15564278737@163.com"
    private val truePassword = "zxc486251379"
    private val wrongPassword = "123456"
    private val uid = "777777"
}