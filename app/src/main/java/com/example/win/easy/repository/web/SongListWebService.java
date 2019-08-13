package com.example.win.easy.repository.web;


import com.example.win.easy.repository.SongListNetworkService;
import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.web.dto.SongDTO;
import com.example.win.easy.repository.web.dto.SongListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>向上层提供获取歌曲、歌单等等的网络服务</p>
 * <p>这是一个异步的过程，因此<b>调用时不会直接返回结果</b>，而应该在调用时传入一个lambda函数</p>
 * <p>用来表示成功时应当执行的工作，一旦成功Service将自动调用</p>
 * <p><b>但是lambda函数的行为必须是不包含IO的、不会阻塞主线程的</b>，因为传入的lambda函数执行是在主线程进行的</p>
 * <p>需要IO时可以在lambda函数体中使用AsyncTask等等的异步任务（其实和本来执行异步的写法是一样的，只是这个封装在了lambda函数中当作了回调而已）</p>
 */
@Singleton
public class SongListWebService {

    private LoginManager loginManager;
    private SongListNetworkService songListNetworkService;

    @Inject
    public SongListWebService(LoginManager loginManager,SongListNetworkService songListNetworkService){
        this.loginManager=loginManager;
        this.songListNetworkService=songListNetworkService;
    }

    /**
     * <p>获取所有的歌单和其中包含的歌曲，将其以DO的形式返回</p>
     * <p>可以保证：</p>
     * <ol>
     *     <li>只有登陆后才会做出响应，未登录时Service对调用不响应</li>
     *     <li>只有要get的数据获取到时才触发回调且每次获得的SongList和Song都是同一次请求的结果，不会出现两者不匹配不一致的情况</li>
     * </ol>
     * @param onBothReadyFunc SongListDOs和SongDOs都准备完毕时触发的回调
     */
    public void getAllSongLists(OnReadyFunc<Map<SongListDO,List<SongDO>>> onBothReadyFunc) {
        //未登录时忽略调用
        if (loginManager.hasLogin()){
            songListNetworkService.fetch(songListDTOs -> {

                //将DTO转化为一个歌单到歌曲列表的映射
                Map<SongListDO,List<SongDO>> songListToSongsMap=new HashMap<>();
                for (SongListDTO songListDTO:songListDTOs){
                    SongListDO songListDO=toDO(songListDTO);
                    List<SongDO> songsInThisSongList=new ArrayList<>();
                    for (SongDTO songDTO:songListDTO.getSongDTOs())
                        songsInThisSongList.add(toDO(songDTO));
                    songListToSongsMap.put(songListDO,songsInThisSongList);
                }

                //准备结束时触发回调
                onBothReadyFunc.onReady(songListToSongsMap);
            });
        }
    }

    private SongListDO toDO(SongListDTO songListDTO){
        //TODO SongListDTO到SongListDO的转化
        return null;
    }

    private SongDO toDO(SongDTO songDTO){
        //TODO SongDTO到SongDO的转化
        return null;
    }
}
