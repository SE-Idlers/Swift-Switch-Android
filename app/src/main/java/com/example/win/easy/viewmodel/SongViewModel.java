package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.value_object.SongVO;

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends ViewModel {

    private __SongRepository songRepository;
    private VOUtil voUtil;

    public SongViewModel(__SongRepository songRepository,VOUtil voUtil){
        this.songRepository=songRepository;
        this.voUtil=voUtil;
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
                    songVOs.add(voUtil.toVO(songDO));
                return  songVOs;
            });
        }
        return allSongsLiveData;
    }

}
