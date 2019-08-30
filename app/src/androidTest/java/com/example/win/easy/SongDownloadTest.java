package com.example.win.easy;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
@Ignore
public class SongDownloadTest {

//
//    @Test
//    public void test(){
//        SongDTO songDTO = SongDTO.builder()
//                .songUrl("http://m10.music.126.net/20190512145921/f1140875434dc3ad4d57e2914b98a8a7/ymusic/5258/0f5f/015c/e23eb784398544031837660e6d233a6e.mp3")
//                .author("陈奕迅")
//                .avatarUrl("http://nbnbnbnb.com")
//                .extensionName(".mp3")
//                .name("烟味")
//                .totalName("陈奕迅 - 烟味")
//                .source(DataSource.WangYiYun)
//                .uid("123456")
//                .remoteId("4567890")
//                .build();
//        String filename=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Swift Switch/最佳损友.mp3";
//        String fileTempName=filename+"~TEMP";
//        String url="http://m10.music.126.net/20190512153429/e59687a175039d205007e81a472dc2a6/ymusic/fcc0/f5a9/dbb2/6572c024438329e20cfcdd9fb3bda993.mp3";
//        SongDownloadTask songDownloadTask=new SongDownloadTask(songDTO,0);
//        songDownloadTask.download(url, fileTempName);
//        File file=new File(fileTempName);
//        file.renameTo(new File(filename));
//    }

}
