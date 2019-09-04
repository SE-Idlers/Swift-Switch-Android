package com.example.win.easy;

import android.test.UiThreadTest;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.download.DownloadServiceAdapter;
import com.example.win.easy.repository.UpdateService;
import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.download.DownloadService;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.web.callback.OnReadyFunc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DownloadServiceAdapterInvocationTest {

    /**
     * <p>模拟有一个调用者要调用这个Adapter下载歌曲，下载完毕后要播放</p>
     * <p>按照设想的逻辑：{@link DownloadServiceAdapter#download(SongVO, OnReadyFunc)}，测试要验证的如下</p>
     * <ol>
     *     <li>发起任务到下载成功前，不会有任何影响</li>
     *     <li>数据库更新的影响是同步的而且跟物理数据库的更新有没有结束没有关系，也就是说，即便物理数据库的更新还没完，LiveData的更新效果也是直接同步地传递到activity的</li>
     *     <li>回调函数被正确执行</li>
     * </ol>
     * <p>为了控制这个过程的进行，人为地进行了一下同步，包括：</p>
     * <ol>
     *     <li>下载是被模拟的，就是有个东西一直阻塞着子线程，直到主线程通知它，他才继续进行，这个“通知”就是模拟下载完成的。
     *     所以一开始发起下载后，就先阻塞，然后验证初始状态正确，而后发出信号，作为下载完成的控制
     *     </li>
     *     <li>在上面那个控制结束后，就要开始更新数据库了。同步这样进行：异步更新物理数据库的那个子线程，就一直阻塞着，模拟一个很慢的数据库更新。
     *     然后主线程那边在这种物理数据库并没有更新的条件下验证LiveData的更新直接同步通知到了activity（也就是那个mock的调用者），再验证一下传进去的
     *     回调被正确执行了
     *     </li>
     *     <li>在上面那个验证完了以后，主线程再通知更新物理数据库的子线程，让它停止阻塞，接着往下执行（其实就是退出），
     *     然后主线程这边等待子线程退出之后自己也退出
     *     </li>
     * </ol>
     * <li>所以这里是模拟了一个调用者，模拟了一个repo发起更新LiveData和物理数据库</li>
     */
    @UiThreadTest
    @Test
    public void testDownload() {

        //模拟调用者调用
        //在这个过程中已经进行了第一个验证，具体的逻辑在setUp里面（mock下载服务的代码里）
        invoker.simulateInvokingDownloadService();

        //运行到这里时物理数据库还没有更新完（还阻塞着呢）。在这个时间点进行第二个和第三个验证
        verifyLiveDataUpdateSuccessfullyAndCallbackIsInvokedRespectively();

        //通知数据库更新线程退出
        notifyDatabaseUpdateToFinish();
    }



    @UiThreadTest
    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        //初始化invoker和songRepository
        invoker=spy(new MockDownloadServiceInvoker(lifecycleOwner,downloadServiceAdapter));
        doReturn(invoker).when(lifecycleOwner).getLifecycle();
        invoker.handleLifecycleEvent(Lifecycle.Event.ON_START);
        invoker.observe(mockSongRepository.getASong());
        mockSongRepository.initialLiveData(mockInitialSongVO);


        //mock当调用下载服务时它的行为
        doAnswer(invocation -> {

            // 获取参数（调用参数没法直接传递进来，要一个个get）
            // 并新建一个线程模拟下载的子线程
            SongDO songDO=invocation.getArgument(0);
            OnReadyFunc<SongDO> onReadyFunc=invocation.getArgument(1);
            Executor networkThread= Executors.newSingleThreadExecutor();

            //网络下载线程的行为：等待下载结束，然后获取数据，最后通知主线程数据准备好了
            networkThread.execute(()->{
                waitForMainThreadSignalToSimulateDownloadFinished();
                songDO.setSongPath(mockDownloadedFilePath);//这就是所谓的“获取数据”。。
                notifyMainThreadNetworkDataIsReady();
            });

            //验证初始状态（没下载完的时候）
            verifyInitialState();
            //模拟控制下载结束
            simulateDownloadIsFinished();
            //触发回调
            onReadyFunc.onReady(songDO);
            return null;
        }).when(downloadService).download(any(SongDO.class),any(OnReadyFunc.class));



        //mock 转化的行为
        when(voUtil.toDO(any(SongVO.class))).thenAnswer(invocation -> {
            SongVO songVO= invocation.getArgument(0);
            return SongDO.builder()
                    .name(songVO.getName())
                    .songPath(songVO.getSongFile()==null?null:songVO.getSongFile().getAbsolutePath())
                    .avatarPath(songVO.getAvatarFile()==null?null:songVO.getAvatarFile().getAbsolutePath())
                    .build();
        });
        when(voUtil.toVO(any(SongDO.class))).thenAnswer(invocation -> {
            SongDO songDO=invocation.getArgument(0);
            return SongVO.builder()
                    .name(songDO.name)
                    .songFile(songDO.getSongPath()==null?null:new File(songDO.getSongPath()))
                    .avatarFile(songDO.getAvatarPath()==null?null:new File(songDO.getAvatarPath()))
                    .build();
        });
    }

    private void verifyInitialState() {
        assertNull(invoker.getMySong().getSongFile());
        verify(invoker,times(0)).displaySong(any(SongVO.class));
    }

    private void verifyLiveDataUpdateSuccessfullyAndCallbackIsInvokedRespectively() {
        assertNotNull(invoker.getMySong().getSongFile());
        verify(invoker,times(1)).displaySong(any(SongVO.class));
    }

    private void waitForMainThreadSignalToSimulateDownloadFinished() {
        synchronized (mainThreadSignalToIndicateDownloadShouldBeFinished) {
            synchronized (downloadThreadHasBeenBlocked) {
                downloadThreadHasBeenBlocked.setReady(true);
            }
            try {
                mainThreadSignalToIndicateDownloadShouldBeFinished.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void notifyMainThreadNetworkDataIsReady(){
        synchronized (networkDataIsReady) {
            networkDataIsReady.notifyAll();
        }
    }
    private void simulateDownloadIsFinished() throws InterruptedException {
        while (true){
            synchronized (downloadThreadHasBeenBlocked){
                if (downloadThreadHasBeenBlocked.isReady())
                    break;
            }
        }

        synchronized (networkDataIsReady) {
            synchronized (mainThreadSignalToIndicateDownloadShouldBeFinished) {
                mainThreadSignalToIndicateDownloadShouldBeFinished.notifyAll();
            }
            networkDataIsReady.wait();
        }
    }
    private void notifyDatabaseUpdateToFinish(){
        while (true){
            synchronized (subThreadHasBeenPretendingToBeUpdatingDatabase){
                if (subThreadHasBeenPretendingToBeUpdatingDatabase.isReady())
                    break;
            }
        }

        synchronized (mainThreadSignalToIndicateDatabaseUpdateShouldBeFinished){
            mainThreadSignalToIndicateDatabaseUpdateShouldBeFinished.notifyAll();
        }
    }
    private void notifyMainThreadThatSubThreadBeginsToUpdateDatabaseAndThenPretendToBeUpdatingDatabaseUntilSignalFromMainThread() {
        synchronized (mainThreadSignalToIndicateDatabaseUpdateShouldBeFinished){
            synchronized (subThreadHasBeenPretendingToBeUpdatingDatabase){
                subThreadHasBeenPretendingToBeUpdatingDatabase.setReady(true);
            }
            try {
                mainThreadSignalToIndicateDatabaseUpdateShouldBeFinished.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>mock一个类似activity的调用者（lifecycle组件）</p>
     * <p>主要的方法就是那个{@link DownloadServiceAdapterInvocationTest.MockDownloadServiceInvoker#simulateInvokingDownloadService()}模拟一次调用</p>
     * <p>同时这个activity还有一个observe的数据，主要是用来验证下载过程中对LiveData的更新也会影响到这个activity</p>
     */
    class MockDownloadServiceInvoker extends LifecycleRegistry {

        private LifecycleOwner myself;
        private SongVO songToObserve;
        private DownloadServiceAdapter downloadServiceAdapter;

        MockDownloadServiceInvoker(@NonNull LifecycleOwner provider, DownloadServiceAdapter downloadServiceAdapter) {
            super(provider);
            this.myself =provider;
            this.downloadServiceAdapter = downloadServiceAdapter;
        }

        void observe(LiveData<SongVO> songToObserveLiveData){
            songToObserveLiveData.observe(myself,newSongVO-> songToObserve = newSongVO);
        }
        SongVO getMySong(){return songToObserve;}


        void simulateInvokingDownloadService(){

            //选择（内定）要下载的歌曲
            SongVO songToDownloadAndDisplay= songToObserve;

            //调用下载模块，并且下载成功（文件不是null）后就开始播放
            downloadServiceAdapter.download(songToDownloadAndDisplay, readySongVO->{
                if (readySongVO.getSongFile()!=null)
                    displaySong(readySongVO);
            });
        }

        void displaySong(SongVO songVO){
            //啥也不干,假装播放歌曲
        }
    }

    /**
     * mock一个歌曲的Repository，假装能对歌曲数据库更新
     */
    @SuppressWarnings("ConstantConditions")
    class MockSongRepository implements UpdateService {

        private MutableLiveData<SongVO> songVOMLD=new MutableLiveData<>();
        private Executor dbAccess= Executors.newSingleThreadExecutor();

        @Override
        public void update(SongDO songDO) {
            //模拟更新LiveData
            SongVO data= songVOMLD.getValue();
            data.setSongFile(new File(songDO.getSongPath()));
            songVOMLD.setValue(data);

            //模拟更新物理数据
            dbAccess.execute(DownloadServiceAdapterInvocationTest.this::notifyMainThreadThatSubThreadBeginsToUpdateDatabaseAndThenPretendToBeUpdatingDatabaseUntilSignalFromMainThread);

        }

        void initialLiveData(SongVO initialSongVO){songVOMLD.setValue(initialSongVO);}
        LiveData<SongVO> getASong(){ return songVOMLD; }
    }

    private MockDownloadServiceInvoker invoker;
    @InjectMocks DownloadServiceAdapter downloadServiceAdapter;
    @Mock LifecycleOwner lifecycleOwner;
    @Mock DownloadService downloadService;
    @Mock VOUtil voUtil;
    @Spy MockSongRepository mockSongRepository;

    //用于测试的简单数据
    private SongVO mockInitialSongVO=SongVO.builder().name("啥啊这是").songFile(null).avatarFile(null).build();
    private String mockDownloadedFilePath="/1/2/3/4/5/6/7/8/9.mp3";

    //用于同步的信号量
    private final BooleanSemaphore downloadThreadHasBeenBlocked=new BooleanSemaphore(false);
    private final Object mainThreadSignalToIndicateDownloadShouldBeFinished =new Object();
    private final BooleanSemaphore subThreadHasBeenPretendingToBeUpdatingDatabase =new BooleanSemaphore(false);
    private final Object mainThreadSignalToIndicateDatabaseUpdateShouldBeFinished =new Object();
    private final Object networkDataIsReady=new Object();

}
