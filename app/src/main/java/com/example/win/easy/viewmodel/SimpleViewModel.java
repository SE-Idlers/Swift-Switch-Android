package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.repo.SongListRepository;
import com.example.win.easy.repository.repo.SongRepository;
import com.example.win.easy.repository.repo.SongXSongListRepository;

import java.util.List;

public class SimpleViewModel extends ViewModel {

    private SongListRepository songListRepository=SongListRepository.getInstance();
    private SongRepository songRepository=SongRepository.getInstance();
    private SongXSongListRepository songXSongListRepository=SongXSongListRepository.getInstance();
    private LiveData<List<SongPojo>> allSongs;
    private LiveData<List<SongListPojo>> allSongLists;
    private LiveData<Integer> songAmount;
    private LiveData<Integer> songListAmount;

    public LiveData<List<SongPojo>> getAllSongs(){
        if (allSongs==null)
            allSongs=songRepository.getAll();
        return allSongs;
    }

    public LiveData<List<SongListPojo>> getAllSongLists(){
        if (allSongLists==null)
            allSongLists=songListRepository.getAll();
        return allSongLists;
    }

    public LiveData<Integer> getSongAmount(){
        if (songAmount==null)
            songAmount=Transformations.map(getAllSongs(),List::size);
        return songAmount;
    }

    public LiveData<Integer> getSongListAmount(){
        if (songListAmount==null)
            songListAmount=Transformations.map(getAllSongLists(),List::size);
        return songListAmount;
    }

    public void insert(SongPojo songPojo){songRepository.insert(songPojo);}
    public void insert(SongListPojo songListPojo){songListRepository.insert(songListPojo);}
    public void insertNewSongAndToSongLists(SongPojo newSong,List<SongListPojo> songListPojos){
        songXSongListRepository.insertNewSongAndToSongLists(newSong,songListPojos);
    }
}
