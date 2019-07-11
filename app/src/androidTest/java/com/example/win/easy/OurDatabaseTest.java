package com.example.win.easy;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.repository.db.dao.IInformationDao;
import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.repository.db.pojo.IInformation;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4ClassRunner.class)
public class OurDatabaseTest {

    private OurDatabase ourDatabase;
    private SongPojoDao songPojoDao;
    private SongListPojoDao songListPojoDao;
    private SongXSongListDao songXSongListDao;
    private IInformationDao iInformationDao;
    private SongPojo songPojo1=new SongPojo();
    private IInformation iInformation1=new IInformation();
    private SongListPojo songListPojo1=new SongListPojo();
    private SongPojo songPojo2=new SongPojo();
    private IInformation iInformation2=new IInformation();
    private SongListPojo songListPojo2=new SongListPojo();

    @Before
    public void init(){
        Context context= ApplicationProvider.getApplicationContext();
        ourDatabase= Room.inMemoryDatabaseBuilder(context,OurDatabase.class).build();
        songPojoDao=ourDatabase.songPojoDao();
        songListPojoDao=ourDatabase.songListPojoDao();
        songXSongListDao=ourDatabase.songXSongListDao();
        iInformationDao=ourDatabase.iInformationDao();
    }

    @After
    public void free(){
        ourDatabase.close();
    }

    /**
     * 测试将歌曲添加到歌单
     */
    @Test
    public void testAddSongToSongList(){
        //设置上下文，两首歌，两个歌单
        createIInformation();
        createSongs();
        createSongLists();

        insertSongs();
        insertSongLists();

        List<SongPojo> songPojos=songPojoDao.findAllSongPojos();
        List<SongListPojo> songListPojos=songListPojoDao.findAllSongListPojos();

        //插入ManyToMany关系表
        for (SongPojo songPojo:songPojos){
            for (SongListPojo songListPojo:songListPojos)
                insert(songPojo,songListPojo);
        }

        //查看结果
        System.out.println("Find appearance SongLists of certain song:");
        for (SongPojo songPojo:songPojos)
            System.out.println("Appearance of "+songPojo+" ::: "+songXSongListDao.findAllSongListsForSongById(songPojo.getId()));
        System.out.println("Find songs of certain _SongList:");
        for (SongListPojo songListPojo:songListPojos)
            System.out.println("Songs of "+songListPojo+" ::: "+songXSongListDao.findAllSongsDataForSongListById(songListPojo.getId()));
    }

    private void insert(SongPojo songPojo, SongListPojo songListPojo){
        long songId=songPojo.getId();
        long songListId=songListPojo.getId();
        songXSongListDao.insert(new SongXSongList(songId,songListId));
    }

    private void createSongs(){
        List<Character> sequence1=new ArrayList<>();
        sequence1.add('D');
        sequence1.add('W');
        sequence1.add('G');
        sequence1.add('X');
        sequence1.add('D');
        sequence1.add('H');
        songPojo1.setName("第五个现代化");
        songPojo1.setAuthor("陈奕迅");
        songPojo1.setSequence(sequence1);
        songPojo1.setSource(DataSource.Local);
        songPojo1.setSongPath("here");

        List<Character> sequence2=new ArrayList<>();
        sequence2.add('H');
        sequence2.add('J');
        sequence2.add('S');
        sequence2.add('D');
        songPojo2.setName("黄金时代");
        songPojo2.setAuthor("陈奕迅");
        songPojo2.setSequence(sequence2);
        songPojo2.setSource(DataSource.Local);
        songPojo2.setSongPath("there");

    }

    private void createIInformation(){
        iInformation1.setMostRecentlyDisplayedDate(new Date(233));
        iInformation2.setMostRecentlyDisplayedDate(new Date(2333));
    }

    private void createSongLists(){
        songListPojo1.setName("Eason");
        songListPojo2.setName("代我感觉的唱片");
    }

    private void insertSongs(){
        long songId=songPojoDao.insert(songPojo1);
        iInformation1.setSongId(songId);
        iInformationDao.insert(iInformation1);

        songId=songPojoDao.insert(songPojo2);
        iInformation2.setSongId(songId);
        iInformationDao.insert(iInformation2);
    }

    private void insertSongLists(){
        songListPojoDao.insert(songListPojo1);
        songListPojoDao.insert(songListPojo2);
    }
}
