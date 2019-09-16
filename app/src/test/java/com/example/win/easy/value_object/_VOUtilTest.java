package com.example.win.easy.value_object;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static com.example.win.easy.enumeration.DataSource.Local;
import static com.example.win.easy.enumeration.DataSource.WangYiYun;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class _VOUtilTest {

    @Test
    public void testSongVO2DO() {
        //TODO
//        assertEquals(songDO,voUtil.testSongDTO2DO(expectedSongVO));
    }

    @Test
    public void testSongDO2VO() {
        assertEquals(expectedSongVO,voUtil.toVO(songDO));
    }

    @Test
    public void testSongListVO2DO() {
        //TODO
//        assertEquals(songListDO,voUtil.testSongDTO2DO(expectedSongListVO));
    }

    @Test
    public void testSongListDO2VO() {
        assertEquals(expectedSongListVO,voUtil.toVO(songListDO));
    }

    @Before
    public void setUp() {
        expectedSongVO = SongVO.builder()
                .id(songDO.getId())
                .name(songDO.getName())
                .songFileUrl(songDO.getSongUrl())
                .songFilePath(songDO.getSongPath())
                .avatarUrl(songDO.getAvatarUrl())
                .avatarPath(songDO.getAvatarPath())
                .build();
        expectedSongListVO = SongListVO.builder()
                .id(songListDO.getId())
                .name(songListDO.getName())
                .avatarUrl(songListDO.getAvatarUrl())
                .avatarPath(songListDO.getAvatarPath())
                .build();
    }

    private SongDO songDO=SongDO.builder()
            .id(36L)
            .name("黑白照 - 邓丽欣")
            .uid(1111L)
            .remoteId(84L)
            .sequence(Arrays.asList('A','B','C'))
            .source(Local)
            .author("邓丽欣")
            .songUrl("/google.com/song.mp3")
            .songPath("/data/user/0/song.mp3")
            .avatarUrl("/google.com/pic.jpeg")
            .avatarPath("/data/user/0/pic.jpeg")
            .build();
    private SongListDO songListDO= SongListDO.builder()
            .id(7777777L)
            .name("female as a singer")
            .uid(1111L)
            .remoteId(333333L)
            .source(WangYiYun)
            .avatarUrl("/google.com/123.jpg")
            .avatarPath("/data/user/0/123.jpg")
            .build();

    @InjectMocks VOUtil voUtil;

    private SongVO expectedSongVO;
    private SongListVO expectedSongListVO;
}