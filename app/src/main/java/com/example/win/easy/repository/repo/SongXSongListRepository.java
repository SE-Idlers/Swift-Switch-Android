package com.example.win.easy.repository.repo;

import androidx.lifecycle.LiveData;

import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.thread.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class SongXSongListRepository extends Repository<SongXSongList,Void> {

    private static SongXSongListRepository instance=new SongXSongListRepository();
    public static SongXSongListRepository getInstance(){return instance;}
    private SongXSongListRepository(){
        this.diskIO= AppExecutors.getInstance().diskIO();
        this.songXSongListDao= SwiftSwitchClassLoader.getOurDatabase().songXSongListDao();
    }

    private Executor diskIO;
    private SongXSongListDao songXSongListDao;

    public LiveData<List<SongPojo>> getAllSongsForSongList(SongListPojo songListPojo){
        return songXSongListDao.findAllSongsForSongListById(songListPojo.getId());
    }

    @Override
    public void insert(SongXSongList localData) {
        diskIO.execute(()->songXSongListDao.insert(localData));
    }

    @Override
    public void delete(SongXSongList data) {
        diskIO.execute(()->songXSongListDao.delete(data));
    }

    @Override
    public void update(SongXSongList data) {
    }

    @Override
    public void fetchAllByUid(String uid) {

    }

    @Override
    protected boolean shouldFetch() {
        return true;
    }

    @Override
    protected LiveData<List<SongXSongList>> loadAll() {
        return null;
    }

    public void insertNewSongAndToSongLists(SongPojo newSong, List<SongListPojo> songListPojos){
        diskIO.execute(new InsertNewSongAndToSongListsTask(newSong,songListPojos) );
    }

    private class InsertNewSongAndToSongListsTask implements Runnable{

        private SongPojo newSong;
        private List<SongListPojo> songListPojos;
        private SongPojoDao songPojoDao;
        private SongXSongListDao songXSongListDao;
        InsertNewSongAndToSongListsTask(SongPojo newSong, List<SongListPojo> songListPojos){
            this.newSong =newSong;
            this.songListPojos=songListPojos;
            this.songPojoDao=SwiftSwitchClassLoader.getOurDatabase().songPojoDao();
            this.songXSongListDao=SwiftSwitchClassLoader.getOurDatabase().songXSongListDao();
        }
        @Override
        public void run() {
            SongPojo result=songPojoDao.findAllBySongPath(newSong.songPath);
            long newSongId;
            if (result==null)
                newSongId=songPojoDao.insert(newSong);
            else
                newSongId=result.getId();
            List<SongXSongList> songXSongLists=new ArrayList<>();
            List<SongListPojo> existingLists=songXSongListDao.findAllSongListsForSongById(newSongId);
            for (SongListPojo songListPojo:songListPojos)
                if (!existingLists.contains(songListPojo))
                    songXSongLists.add(new SongXSongList(newSongId,songListPojo.getId()));
            songXSongListDao.insert(songXSongLists);
        }
    }
}
