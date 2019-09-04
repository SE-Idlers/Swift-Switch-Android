package com.example.win.easy;


import com.example.win.easy.web.request.BackendRequestService;
import com.example.win.easy.web.service.LoginService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ALL")
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    //真正的我的网易云id
    private String actualMyUid="1846879130";

    //登陆手机号和密码
    private String loginPhone="15564278737";
    private String truePassword ="zxc486251379";
    private String wrongPassword="123456";

    //测试的配置
    private String serverUrl="http://106.53.93.41:9000/";


    /**
     * <p>测试手机登陆功能</p>
     * <p>测试的想法是：验证成功登陆后，hasLogin为true，当前用户id是我的id（好吧，依然是拿了实际的我的网易云号码来测试），同时验证回调函数被正确执行</p>
     * <p>前两个的验证比较直观，关于第三个回调函数有没有执行，这里设置的是给主线程发送一个信号，不然主线程就会一直等待，所以只要主线程能顺利执行，那就说明onReadyFunc这个回调被执行了</p>
     * <p>当然，由于在jvm中callback会在background线程中执行，测试中部分代码还要拿来做同步</p>
     * <p>还有就是为了防止两个Test方法测试时相互影响，每次测试开始前都要重置一部分变量</p>
     * @see retrofit2.Callback
     */
    @Test
    public void testLoginByPhone() throws InterruptedException {
        resetLoginState();

        loginService.loginByPhone(loginPhone, truePassword,success-> notifyMainThreadLoginReady());

        waitForLoginReady();

        verifySuccessfulLogin(loginService);
    }


    /**
     * <p>跟{@link LoginServiceTest#testLoginByPhone()}差不多，验证hasLogin为false，验证当前id是null，其他的都一样</p>
     */
    @Test
    public void testFailLogin() throws InterruptedException {
        resetLoginState();

        loginService.loginByPhone(loginPhone,wrongPassword,success-> notifyMainThreadLoginReady());

        waitForLoginReady();

        verifyFailLogin(loginService);
    }

    @Before
    public void setUp() {
        //初始化请求服务
        backendRequestService = new Retrofit.Builder().baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BackendRequestService.class);
    }

    private void resetLoginState(){
        loginService=new LoginService(backendRequestService);
        mainTestThreadHasBeenWaitingForMyAssertionToBeFinished.setReady(false);
    }

    private void waitForLoginReady() throws InterruptedException {
        synchronized (loginCallbackFinished){
            synchronized (mainTestThreadHasBeenWaitingForMyAssertionToBeFinished){
                mainTestThreadHasBeenWaitingForMyAssertionToBeFinished.setReady(true);
            }
            loginCallbackFinished.wait();
        }
    }

    private void notifyMainThreadLoginReady(){
        while (true){
            synchronized (mainTestThreadHasBeenWaitingForMyAssertionToBeFinished){
                if(mainTestThreadHasBeenWaitingForMyAssertionToBeFinished.isNotReady())
                    continue;
            }

            synchronized (loginCallbackFinished){
                loginCallbackFinished.notifyAll();
                break;
            }
        }
    }
    private void verifySuccessfulLogin(LoginService loginService) {
        assertTrue(loginService.hasLogin());
        assertEquals(actualMyUid,loginService.getCurrentUid());
    }
    private void verifyFailLogin(LoginService loginService) {
        assertFalse(loginService.hasLogin());
        assertNull(loginService.getCurrentUid());
    }

    private BackendRequestService backendRequestService;
    private LoginService loginService;

    private final BooleanSemaphore mainTestThreadHasBeenWaitingForMyAssertionToBeFinished=new BooleanSemaphore(false);
    private final Object loginCallbackFinished=new Object();

}
