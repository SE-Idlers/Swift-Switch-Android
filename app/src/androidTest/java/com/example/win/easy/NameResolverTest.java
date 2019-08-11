package com.example.win.easy;

import android.util.Log;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.web.DownloadFilenameResolver;
import com.example.win.easy.repository.web.dto.SongDTO;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4ClassRunner.class)
public class NameResolverTest {


    @Test
    public void test(){
        String root=DownloadFilenameResolver.root();
        String tempSuffix=DownloadFilenameResolver.tempSuffix();
        Log.d("Root directory",root);
        Log.d("Temp suffix",tempSuffix);
        SongDTO songDTO = SongDTO.builder()
                .songUrl("http://2333.com")
                .author("陈奕迅")
                .avatarUrl("http://nbnbnbnb.com")
                .extensionName(".mp3")
                .name("烟味")
                .totalName("陈奕迅 - 烟味")
                .source(DataSource.WangYiYun)
                .uid("123456")
                .remoteId("4567890")
                .build();
        String filenameDue=root+"/123456/song/陈奕迅 - 烟味.mp3";
        assertEquals(filenameDue,DownloadFilenameResolver.finishSongFilePath(songDTO));
        assertEquals(filenameDue+tempSuffix,DownloadFilenameResolver.tempSongFilePath(songDTO));
        Log.d("Finish name",DownloadFilenameResolver.finishSongFilePath(songDTO));
        Log.d("Downloading name",DownloadFilenameResolver.tempSongFilePath(songDTO));
    }

}
