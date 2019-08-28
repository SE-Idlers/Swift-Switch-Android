package com.example.win.easy;


import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
@Ignore
public class LoginManagerTest {

//    private BackendResourceWebService webService;
//
//    @Before
//    public void constructService(){
//        Retrofit retrofit=new Retrofit.Builder()
//                .baseUrl("http://127.0.0.1:9000/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        webService=retrofit.create(BackendResourceWebService.class);
//        LoginManager.init(webService);
//    }
//
//    @Test
//    public void testInitialState(){
//        assertFalse(LoginManager.hasLogin());
//        assertNull(LoginManager.getCurrentUid());
//        assertFalse(LoginManager.isLogining());
//    }
//
//    @Test
//    public void testLoginByPhone() throws InterruptedException {
//        LoginManager.loginByPhone("15564278737","zxc486251379");
//        assertTrue(LoginManager.isLogining());
//        while (LoginManager.isLogining())
//            Thread.sleep(100);
//        while (!LoginManager.hasLogin())
//            Thread.sleep(100);
//        assertEquals("1849339365",LoginManager.getCurrentUid());
//    }

}
