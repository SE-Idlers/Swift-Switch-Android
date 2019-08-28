package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.value_object.SongVO;

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends ViewModel {

    private __SongRepository songRepository;

    public SongViewModel(__SongRepository songRepository){
        this.songRepository=songRepository;
    }

    private LiveData<List<SongVO>> allSongsLiveData;

    /**
     * 获取VO形式的所有歌曲
     * @return 所有歌曲的LiveData
     */
    public LiveData<List<SongVO>> getAllSongs(){
        //懒惰加载
        if (allSongsLiveData==null){
            //从repo获取DO形式的歌曲，并提供一个映射函数
            allSongsLiveData=Transformations.map(songRepository.getAll(),
                    //对每个DO依次转化
                    input -> {
                List<SongVO> songVOs=new ArrayList<>();
                for(SongDO songDO:input)
                    songVOs.add(toVO(songDO));
                return  songVOs;
            });
        }
        return allSongsLiveData;
    }

    /**
     * 歌曲DO到歌曲VO的转化函数（也就是适配器）
     * @param songDO DO形式的song
     * @return VO形式的song
     */
    private SongVO toVO(SongDO songDO){
        //TODO SongDO 到 SongVO的转化
        return null;
    }
}
