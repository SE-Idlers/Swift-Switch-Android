package com.example.win.easy;

import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.dto.SongListDTO;
import com.example.win.easy.web.network.AllSongListNetworkFetchService;
import com.example.win.easy.web.network.NetworkFetchService;
import com.example.win.easy.web.request.BackendRequestService;
import com.example.win.easy.web.service.LoginService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("ALL")
@RunWith(MockitoJUnitRunner.class)
public class SongListFetchServiceTest {

    /**
     * 用于测试的网络通信数据，具体描述见{@link SongListFetchServiceTest#fetchTestWithLogin()}
     */
    private String songListNameForTest="Test";
    private int songListSizeForTest=5;
    private String testUid="1846879130";

    //测试的配置
    private String serverUrl="http://106.53.93.41:9000/";

    //用于同步的信号量
    private final BooleanSemaphore mainTestThreadHasBeenWaitingForMyAssertionToBeFinished=new BooleanSemaphore(false);
    private final Object assertionIsFinished=new Object();

    //主要被mock的对象，具体mock见setUp
    private OnReadyFunc<List<SongListDTO>> onReadyFuncForTest;

    /**
     * <p>测试思路：网络资源获取成功后能够正确调用准备好的onReadyFunc</p>
     * <p>这里设置的onReadyFunc非常简单，只是一个简单的assert语句，assert成功后通知主线程</p>
     * <p>逻辑是：测试的Service发起Call=>收到Response=></p>
     * <p>LambdaCallback（用lambda函数简单封装的Callback）拿着响应中的listData调用onReadyFuncTest(listData)</p>
     * <p>=>onReadyFuncTest执行assert</p>
     * <p>tips: 这里测试的数据是真实存在的，是我自己存在网易云上的一个测试歌单，assert过程是，从返回的所有歌单中找到名为{@link SongListFetchServiceTest#songListNameForTest}的歌单</p>
     * <p>比对这个歌单大小是否等于{@link SongListFetchServiceTest#songListSizeForTest}，当然啦，这两个值都要自己手动设置，一旦有变动就要修改一下，不然测试是没法通过的</p>
     * <p>所以很大程度上来说这个测试依赖于我网易云那个用来测试的歌单有没有改-.-||（好吧，这确实是个X一样的测试设计，但是没办法，我没想出来办法mock网络请求。。）</p>
     */
    @Test
    public void fetchTestWithLogin() throws InterruptedException {
        setUpLoginState(true);
        networkFetchService.fetch(onReadyFuncForTest);

        //等待子测试线程的assertion结束以完成测试
        waitForSubThreadAssertion();
    }



    /**
     * <p>未登录状态下的测试，获取uid的方法应该没有被调用</p>
     * <p>（其实按理说应当verify的是网络请求没有发起，但是{@link BackendRequestService}这个类是自动生成的，是个final类</p>
     * <p>而mockito没法spy一个final类对象，所以只能退而求其次，verify一下loginManger的方法没有被调用了）</p>
     */
    @Test
    public void fetchTestWithoutLogin(){
        setUpLoginState(false);
        networkFetchService.fetch(onReadyFuncForTest);

        verify(mockLoginService,times(0)).getCurrentUid();
    }


    @Before
    public void setUp(){
        //mock依赖注入
        spyBackendRequestService = new Retrofit.Builder().baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BackendRequestService.class);
        networkFetchService =new AllSongListNetworkFetchService(spyBackendRequestService, mockLoginService);




        //用于测试的onReadyFuncTest，简单地assert一下测试歌单的大小，确保通信成功
        onReadyFuncForTest=songListsFromResponse -> {

            //找到那个我专门用来测试的歌单
            SongListDTO songListDedicatedForTest=null;
            for (SongListDTO songListDTO:songListsFromResponse)
                if (songListDTO.getName().equals(songListNameForTest)) {
                    songListDedicatedForTest = songListDTO;
                    break;
                }

            //没找到就直接手动让测试不通过（好吧，这个assert很沙雕。。）
            if (songListDedicatedForTest==null)
                assertTrue(false);

            //找到后进行assert
            assertEquals(songListSizeForTest,songListDedicatedForTest.getSongDTOs().size());

            //直到确保主线程已经开始等待assertion成功后，才能assert并通知主线程
            notifyMainThread();

        };



        //mock 当前用户（如果当前mock登陆状态是已登陆）的uid
        doReturn(testUid).when(mockLoginService).getCurrentUid();
    }
    private void setUpLoginState(boolean hasLogin){doReturn(hasLogin).when(mockLoginService).hasLogin();}
    private void waitForSubThreadAssertion() throws InterruptedException {
        //发起网络请求后，设置自己已经准备好要开始等待assertion了，然后开始等待assertion
        synchronized (assertionIsFinished){
            synchronized (mainTestThreadHasBeenWaitingForMyAssertionToBeFinished){
                mainTestThreadHasBeenWaitingForMyAssertionToBeFinished.setReady(true);
            }
            assertionIsFinished.wait();
        }
    }

    private void notifyMainThread(){
        while (true){
            synchronized (mainTestThreadHasBeenWaitingForMyAssertionToBeFinished){
                if (mainTestThreadHasBeenWaitingForMyAssertionToBeFinished.isNotReady())
                    continue;
            }

            synchronized (assertionIsFinished){
                assertionIsFinished.notifyAll();
            }
            break;
        }
    }

    private NetworkFetchService<List<SongListDTO>> networkFetchService;
    private BackendRequestService spyBackendRequestService;
    private LoginService mockLoginService =Mockito.mock(LoginService.class);

}
