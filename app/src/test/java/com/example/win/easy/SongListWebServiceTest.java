package com.example.win.easy;

import com.example.win.easy.web.DTOUtil;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.dto.SongDTO;
import com.example.win.easy.web.dto.SongListDTO;
import com.example.win.easy.web.network.NetworkFetchService;
import com.example.win.easy.web.service.SongListWebService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SuppressWarnings("ALL")
@RunWith(PowerMockRunner.class)
public class SongListWebServiceTest {

    @InjectMocks SongListWebService songListWebService;
    @Mock DTOUtil dtoUtil;
    private NetworkFetchService mockSongListNetworkFetchService;
    private final Object mockRequestProcess=new Object();
    private final BooleanSemaphore requestHasBeenWaiting=new BooleanSemaphore(false);
    private final BooleanSemaphore dataConvertHasBeenFinished=new BooleanSemaphore(false);

    /**
     * <ol>
     *      <li>
     *        <p>测试中通过同步代码严格控制主线程和web线程的执行顺序，确保测试能够正常进行</p>
     *        <p>顺序为：准备（主线程）->开启一个子线程（主线程）->子线程阻塞模拟请求进行中->主线程通知子线程解除其阻塞，模拟请求完成-></p>
     *        <p>子线程准备数据->主线程assert结果的正确性</p>
     *      </li>
     *      <li>同时，测试是独立于DTO到DO实现来进行的(可以看到DTO到DO的转化util是被mock了的)</li>
     * </ol>
     *
     */
    @Test
    public void testGet() throws InterruptedException {
        setUpCallbackToInvokeWhenSongDOsFromWebServiceIsReady();
        assertDataIsNotReadyAndCallbackIsNotInvoked();

        triggerRequestProcessFinish();
        assertDataIsReadyAndCallbackIsInvoked();
    }

    @Before
    public void before() throws Exception {
        //手动初始化一个spy，初始化注解mock注入
        mockNetworkService();
        MockitoAnnotations.initMocks(this);
        songListWebService =PowerMockito.spy(songListWebService);

        //mock数据
        mockSongListDTOFromBackend();
        mock_DTO_to_DO_methods();
        prepareResult();
    }

    //mock网络请求服务
    private void mockNetworkService(){
        mockSongListNetworkFetchService = spy(new NetworkFetchService<List<SongListDTO>>() {
            /**
             * <p>网络请求是在其他线程中执行的，因此这里应当模拟一个后台的web请求线程<p/>
             * <p>并控制这个线程的阻塞与执行，相当于mock请求过程与请求完成</p>
             */
            Executor mockWebRequestExecutor=Executors.newSingleThreadExecutor();

            @Override
            public void fetch(OnReadyFunc<List<SongListDTO>> onBothReadyFunc) {
                mockWebRequestExecutor.execute(()->{

                    //在mock出来的网络请求线程被阻塞前主线程不应当发送“模拟请求完成”的信号
                    //此处是为了保证网路请求线程先阻塞，主线程再发送“请求完成”
                    synchronized (mockRequestProcess){
                        synchronized (requestHasBeenWaiting){
                            requestHasBeenWaiting.setReady(true);
                            requestHasBeenWaiting.notifyAll();
                        }
                        /*
                        通过暂时阻塞这个background线程来模拟请求正在进行,
                        外部可以调用这个对象的notify方法来控制模拟请求的完成，
                        从而触发后续的回调
                         */
                        try {
                            mockRequestProcess.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //请求完成，触发调用者传入的当DTO数据可用时要触发的回调
                    onBothReadyFunc.onReady(mockSongListDTOs);

                    //在数据准备完成前主线程不应当执行assert代码，现在数据已经准备完成，通知主线程继续执行assert
                    synchronized (dataConvertHasBeenFinished){
                        dataConvertHasBeenFinished.setReady(true);
                        dataConvertHasBeenFinished.notifyAll();
                    }
                });
            }
        });
    }
    private void setUpCallbackToInvokeWhenSongDOsFromWebServiceIsReady(){
        songListWebService.getAllSongLists((songListToSongsMap)->{

            //对歌单的verify就是简单的把歌单名字串在一起，确保是返回过数据的即可
            actualSongListString ="";
            List<SongListDO> allSongListDOs=new ArrayList<>(songListToSongsMap.keySet());
            for (SongListDO songListDO: allSongListDOs)
                actualSongListString = actualSongListString.concat(songListDO.getName());

            //对歌曲的verify复杂一些，因为不同歌单的歌曲可能会出现重叠，但这些重叠的部分在
            //递交上来的DO对象中应该都是相同对象在不同list里面的引用，这里的verify要确定这一点
            actualAllSongDOsSet =new HashSet<>();
            for (List<SongDO> songDOList: songListToSongsMap.values())
                actualAllSongDOsSet.addAll(songDOList);
        });
    }
    private void assertDataIsNotReadyAndCallbackIsNotInvoked(){
        assertNotEquals(trueSongListString, actualSongListString);
        assertNotEquals(trueAllSongDOsSet, actualAllSongDOsSet);
    }
    private void triggerRequestProcessFinish() throws InterruptedException {
        //确保网络请求线程已经被阻塞后才能模拟信号的发送
        synchronized (requestHasBeenWaiting){
            while (requestHasBeenWaiting.isNotReady())
                requestHasBeenWaiting.wait();
            synchronized (mockRequestProcess){
                mockRequestProcess.notifyAll();
            }
        }
    }
    private void assertDataIsReadyAndCallbackIsInvoked() throws InterruptedException {
        //确保数据准备完成后才能进行assert
        synchronized (dataConvertHasBeenFinished){
            while (dataConvertHasBeenFinished.isNotReady())
                dataConvertHasBeenFinished.wait();
        }
        assertEquals(trueSongListString, actualSongListString);
        assertEquals(trueAllSongDOsSet, actualAllSongDOsSet);
    }

    //以下是数据准备
    private void mockSongListDTOFromBackend(){
        songListDTO1=SongListDTO.builder()
                .name("无双")
                .songDTO(songDTO1).songDTO(songDTO2)
                .build();
        songListDTO2=SongListDTO.builder()
                .name("another excellent set!")
                .songDTO(songDTO2).songDTO(songDTO3)
                .build();

        mockSongListDTOs.add(songListDTO1);
        mockSongListDTOs.add(songListDTO2);
    }

    private void mock_DTO_to_DO_methods() throws Exception {
        when(dtoUtil.toDO(songDTO1)).thenReturn(songDO1);
        when(dtoUtil.toDO(songDTO2)).thenReturn(songDO2);
        when(dtoUtil.toDO(songDTO3)).thenReturn(songDO3);

        when(dtoUtil.toDO(songListDTO1)).thenReturn(songListDO1);
        when(dtoUtil.toDO(songListDTO2)).thenReturn(songListDO2);
    }

    private void prepareResult(){
        trueSongListString ="another excellent set!"+"无双";

        trueAllSongDOsSet=new HashSet<>();
        trueAllSongDOsSet.add(songDO1);
        trueAllSongDOsSet.add(songDO2);
        trueAllSongDOsSet.add(songDO3);
    }

    private String actualSongListString;
    private String trueSongListString;

    private Set<SongDO> actualAllSongDOsSet;
    private Set<SongDO> trueAllSongDOsSet;

    private SongListDTO songListDTO1;
    private SongListDTO songListDTO2;

    private SongDTO songDTO1=SongDTO.builder().name("不知所谓-陈奕迅").build();
    private SongDTO songDTO2=SongDTO.builder().name("野孩子-杨千嬅").build();
    private SongDTO songDTO3=SongDTO.builder().name("深爱着你-陈百强").build();

    private SongDO songDO1=SongDO.builder().name("陈奕迅-不知所谓").build();
    private SongDO songDO2=SongDO.builder().name("杨千嬅-野孩子").build();
    private SongDO songDO3=SongDO.builder().name("陈百强-深爱着你").build();

    private SongListDO songListDO1=SongListDO.builder().name("无双").build();
    private SongListDO songListDO2=SongListDO.builder().name("another excellent set!").build();

    private List<SongListDTO> mockSongListDTOs=new ArrayList<>();
}

