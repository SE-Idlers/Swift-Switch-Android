package com.example.win.easy;

import android.Manifest;
import android.os.Environment;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.GrantPermissionRule;

import com.example.win.easy.download.FileService;
import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ALL")
@RunWith(AndroidJUnit4ClassRunner.class)
public class FileServiceTest {

    /**
     * <p>测试service提供的各个方法可以正确生成文件，这个所谓的“正确生成文件”包括：</p>
     * <ol>
     *     <li>生成的文件名是正确的（与预期相符合）</li>
     *     <li>返回的文件确确实实创建了，而不是只new了个File对象</li>
     * </ol>
     * <li>这个只是测试没有文件冲突（已经存在）时的情况，冲突的在另一个测试方法里</li>
     */
    @Test
    public void testFileCorrectCreation() throws IOException {
        //清除所有可能存在的文件
        tearDown();

        //获取实际调用结果
        File actualSongFile=fileService.file(testSong);
        File actualSongAvatarFile=fileService.avatar(testSong);
        File actualSongListAvatarFile=fileService.avatar(testSongList);

        //首先确保路径正确
        assertEquals(expectedSongFile.getAbsolutePath(),actualSongFile.getAbsolutePath());
        assertEquals(expectedSongAvatarFile.getAbsolutePath(),actualSongAvatarFile.getAbsolutePath());
        assertEquals(expectedSongListAvatarFile.getAbsolutePath(),actualSongListAvatarFile.getAbsolutePath());

        //而后确保真的创建了，在文件系统中存在
        assertTrue(expectedSongFile.exists());
        assertTrue(expectedSongAvatarFile.exists());
        assertTrue(expectedSongListAvatarFile.exists());
    }

    /**
     * <p>测试当要测试的文件已经存在时，应当删掉重新创建</p>
     * <p>怎么知道是重新创建的呢？就比较一下最后修改</p>
     */
    @Test
    public void testWhenFileAlreadyExists() throws IOException {
        setUpSongFileAlreadyExistsCondition();
        File actualSongFile=fileService.file(testSong);

        //验证文件名是正确的
        assertEquals(expectedSongFile.getAbsolutePath(),actualSongFile.getAbsolutePath());

        //验证确实是删掉重建的
        assertFalse(expectedSongFile==actualSongFile);
    }

    @Before
    public void setUp() {
        Long uid=5456L;

        //设置歌曲信息
        String songName="陈奕迅 - Last Order";

        String songUrl="https://www.google.com/songs/kyu5j6hg5hgj156t.mp3";
        String songExtension=".mp3";

        String songAvatarUrlPrefix="https://www.google.com/pictures/";
        String songAvatarUri="fds5sd456d4sfd16.jpg";
        String songAvatarUrl=songAvatarUrlPrefix+songAvatarUri;

        //设置歌单信息
        DataSource songListSource=DataSource.WangYiYun;
        String songListAvatarUrlPrefix="https://www.google.com/pictures/";
        String songListAvatarUri="a489wee4eh5fg1h.jpg";
        String songListAvatarUrl=songListAvatarUrlPrefix+songListAvatarUri;

        //用于测试的歌曲和歌单对象
        testSong =SongDO.builder().uid(uid).name(songName).songUrl(songUrl).avatarUrl(songAvatarUrl).build();
        testSongList = SongListDO.builder().uid(uid).source(songListSource).avatarUrl(songListAvatarUrl).build();


        //{rootDir}/{songDir}/{uid}/{songFilename}{songExtension}
        expectedSongFile=new File(rootDir+songDir+d+uid+d+songName+songExtension);

        //{rootDir}/{pictureDir}/{uid}/{songDir}/{pictureWebUri}
        expectedSongAvatarFile=new File(rootDir+pictureDir+d+uid+songDir+d+songAvatarUri);

        //{rootDir}/{pictureDir}/{uid}/{songListDir}/{dataSource}/{pictureWebUri}
        expectedSongListAvatarFile=new File(rootDir+pictureDir+d+uid+songListDir+d+songListSource.toString()+d+songListAvatarUri);
    }

    @After
    public void tearDown(){
        cleanUp(expectedSongFile);
        cleanUp(expectedSongAvatarFile);
        cleanUp(expectedSongListAvatarFile);
    }

    private void setUpSongFileAlreadyExistsCondition() throws IOException {
        if (!expectedSongFile.exists()){
            if(expectedSongFile.getParentFile().exists()) assertTrue(expectedSongFile.createNewFile());
            else assertTrue(expectedSongFile.mkdirs());
        }
    }

    private void cleanUp(File file){
        if (file.exists())
            for (File itr=file;!itr.getAbsolutePath().equals(rootDir);itr=itr.getParentFile())
                if (itr.isFile()) assertTrue(itr.delete());
                else {
                    if (itr.listFiles().length==0)
                        assertTrue(itr.delete());
                    else break;
                }
    }

    //获取SD卡的读写权限
    @Rule public GrantPermissionRule grantPermissionRule=GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private FileService fileService=new FileService();

    private SongDO testSong;
    private SongListDO testSongList;

    private File expectedSongFile;
    private File expectedSongAvatarFile;
    private File expectedSongListAvatarFile;

    private String rootDir=Environment.getExternalStorageDirectory().getAbsolutePath();
    private String songDir="/songs";
    private String songListDir="/songLists";
    private String pictureDir="/pictures";
    private String d="/";

}