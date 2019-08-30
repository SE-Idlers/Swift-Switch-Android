package com.example.win.easy.web.service;


import com.example.win.easy.web.DTOUtil;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.dto.SongDTO;
import com.example.win.easy.web.dto.SongListDTO;
import com.example.win.easy.web.network.NetworkFetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>在一层层向上层传递网络资源的过程中，这个类作为DTO到DO的适配器工作</p>
 * <p>也就是说，它其实只做了下转化的逻辑，其他啥也没干。下层已经拿到DTO数据了，而上层需要DO数据，因此它来做这个转化工作</p>
 * <p>当然了，这是一个异步的过程，因此<b>调用时不会直接返回结果</b>，而应该在调用时传入一个lambda函数</p>
 * <p>用来表示成功时应当执行的工作，一旦成功Service将自动调用</p>
 * <p><b>但是lambda函数的行为必须是不包含IO的、不会阻塞主线程的</b>，因为传入的lambda函数执行是在主线程进行的</p>
 * <p>需要IO时可以在lambda函数体中使用AsyncTask等等的异步任务（其实和本来执行异步的写法是一样的，只是这个封装在了lambda函数中当作了回调而已）</p>
 */
public class SongListWebService {

    private DTOUtil dtoUtil;
    private NetworkFetchService<List<SongListDTO>> networkFetchService;

    public SongListWebService(DTOUtil dtoUtil,NetworkFetchService<List<SongListDTO>> networkFetchService){
        this.dtoUtil=dtoUtil;
        this.networkFetchService = networkFetchService;
    }

    /**
     * <p><b>这个类不会检查登陆状态</b></p>
     * <p>获取所有的歌单和其中包含的歌曲，把它们以DO的形式返回</p>
     * <p>可以保证：</p>
     * <ol>
     *     <li>只有要get的数据获取到时才触发回调且每次获得的SongList和Song都是同一次请求的结果，不会出现两者不匹配不一致的情况</li>
     *     <li>返回的数据已经恰当地转化为了DO对象（关于这个“恰当地”到底多恰当，可以参考{@link DTOUtil}）</li>
     * </ol>
     * @param onReadyFunc SongListDOs和SongDOs都准备完毕时触发的回调
     */
    public void getAllSongLists(OnReadyFunc<Map<SongListDO,List<SongDO>>> onReadyFunc) {
        networkFetchService.fetch(songListDTOs -> {

            //将DTO转化为一个歌单到歌曲列表的映射
            Map<SongListDO,List<SongDO>> songListToSongsMap=new HashMap<>();
            for (SongListDTO songListDTO:songListDTOs){
                SongListDO songListDO=dtoUtil.toDO(songListDTO);
                List<SongDO> songsInThisSongList=new ArrayList<>();
                for (SongDTO songDTO:songListDTO.getSongDTOs())
                    songsInThisSongList.add(dtoUtil.toDO(songDTO));
                songListToSongsMap.put(songListDO,songsInThisSongList);
            }

            //准备结束时触发回调
            onReadyFunc.onReady(songListToSongsMap);
        });
    }
}
