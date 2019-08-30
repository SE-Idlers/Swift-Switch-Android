package com.example.win.easy;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
@Ignore
public class OurDatabaseTest {
//
//    private OurDatabase ourDatabase;
//    private SongDao songDao;
//    private SongListDao songListDao;
//    private SongXSongListDao songXSongListDao;
//    private IInformationDao iInformationDao;
//    private SongDO songPojo1=new SongDO();
//    private IInformation iInformation1=new IInformation();
//    private SongListDO songListPojo1=new SongListDO();
//    private SongDO songPojo2=new SongDO();
//    private IInformation iInformation2=new IInformation();
//    private SongListDO songListPojo2=new SongListDO();
//
//    @Before
//    public void init(){
//        Context context= ApplicationProvider.getApplicationContext();
//        ourDatabase= Room.inMemoryDatabaseBuilder(context,OurDatabase.class).build();
//        songDao=ourDatabase.songDao();
//        songListDao=ourDatabase.songListDao();
//        songXSongListDao=ourDatabase.songXSongListDao();
//        iInformationDao=ourDatabase.iInformationDao();
//    }
//
//    @After
//    public void free(){
//        ourDatabase.close();
//    }
//
//    /**
//     * 测试将歌曲添加到歌单
//     */
//    @Test
//    public void testAddSongToSongList(){
//        //设置上下文，两首歌，两个歌单
//        createIInformation();
//        createSongs();
//        createSongLists();
//
//        insertSongs();
//        insertSongLists();
//
//        List<SongDO> songPojos=songDao.findAllSongDOs();
//        List<SongListDO> songListPojos=songListDao.findAllSongListDOs();
//
//        //插入ManyToMany关系表
//        for (SongDO songDO:songPojos){
//            for (SongListDO songListPojo:songListPojos)
//                insert(songDO,songListPojo);
//        }
//
//        //查看结果
//        System.out.println("Find appearance SongLists of certain song:");
//        for (SongDO songDO:songPojos)
//            System.out.println("Appearance of "+songDO+" ::: "+songXSongListDao.findAllSongListsForSongById(songDO.getId()));
//        System.out.println("Find songs of certain _SongList:");
//        for (SongListDO songListPojo:songListPojos)
//            System.out.println("Songs of "+songListPojo+" ::: "+songXSongListDao.findAllSongsDataForSongListById(songListPojo.getId()));
//    }
//
//    private void insert(SongDO songDO, SongListDO songListPojo){
//        long songId=songDO.getId();
//        long songListId=songListPojo.getId();
//        songXSongListDao.insert(new SongXSongListDO(songId,songListId));
//    }
//
//    private void createSongs(){
//        List<Character> sequence1=new ArrayList<>();
//        sequence1.add('D');
//        sequence1.add('W');
//        sequence1.add('G');
//        sequence1.add('X');
//        sequence1.add('D');
//        sequence1.add('H');
//        songPojo1.setName("第五个现代化");
//        songPojo1.setAuthor("陈奕迅");
//        songPojo1.setSequence(sequence1);
//        songPojo1.setSource(DataSource.Local);
//        songPojo1.setSongPath("here");
//
//        List<Character> sequence2=new ArrayList<>();
//        sequence2.add('H');
//        sequence2.add('J');
//        sequence2.add('S');
//        sequence2.add('D');
//        songPojo2.setName("黄金时代");
//        songPojo2.setAuthor("陈奕迅");
//        songPojo2.setSequence(sequence2);
//        songPojo2.setSource(DataSource.Local);
//        songPojo2.setSongPath("there");
//
//    }
//
//    private void createIInformation(){
//        iInformation1.setMostRecentlyDisplayedDate(new Date(233));
//        iInformation2.setMostRecentlyDisplayedDate(new Date(2333));
//    }
//
//    private void createSongLists(){
//        songListPojo1.setName("Eason");
//        songListPojo2.setName("代我感觉的唱片");
//    }
//
//    private void insertSongs(){
//        long songId=songDao.insert(songPojo1);
//        iInformation1.setSongId(songId);
//        iInformationDao.insert(iInformation1);
//
//        songId=songDao.insert(songPojo2);
//        iInformation2.setSongId(songId);
//        iInformationDao.insert(iInformation2);
//    }
//
//    private void insertSongLists(){
//        songListDao.insert(songListPojo1);
//        songListDao.insert(songListPojo2);
//    }
}
