package com.example.win.easy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongXSongListDO
import com.example.win.easy.dto.SongDto
import com.example.win.easy.dto.SongListDto
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.network.LoginService
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.SongRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Collections.addAll

class _SyncTest {

    @get:Rule val dbRule = DatabaseRule()
    @get:Rule val instantTaskExecutorRule=InstantTaskExecutorRule()
    @SpyK var songDao = dbRule.songDao
    @SpyK var relationDao = dbRule.songXSongListDao
    @SpyK var songListDao = dbRule.songListDao

    @RelaxedMockK lateinit var songDto: SongDto
    @RelaxedMockK lateinit var songListDto: SongListDto
    @RelaxedMockK lateinit var loginService: LoginService

    @InjectMockKs lateinit var songRepository: SongRepository
    @InjectMockKs lateinit var songListRepository: SongListRepository

    @Before
    fun setUp() = runBlocking{
        MockKAnnotations.init(this@_SyncTest)
        listId = songListDao.insert(SongListDO())
        oSong1.run { id = songDao.insert(this); relationDao.insert(SongXSongListDO(id!!, listId)) }
        oSong2.run { id = songDao.insert(this); relationDao.insert(SongXSongListDO(id!!, listId)) }
        oSong3.run { id = songDao.insert(this); relationDao.insert(SongXSongListDO(id!!, listId)) }
        oSong4.run { id = songDao.insert(this); relationDao.insert(SongXSongListDO(id!!, listId)) }

        oList1.run { id=songListDao.insert(this); }
        oList2.run { id=songListDao.insert(this); }
        oList3.run { id=songListDao.insert(this); }
        oList4.run { id=songListDao.insert(this); }
        oSongIn3.run { id=songDao.insert(this) }
        relationDao.insert(SongXSongListDO(oSongIn3.id!!,oList3.id!!))

        Unit
    }

    // 交集
    private val oSong1= SongDO(name="1", author = "a1", songUrl = "su1",avatarUrl = "au1",
            source = DataSource.WangYiYun,uid = 1,remoteId = 1)
    private val oSong2= SongDO(name="2", author = "a2", songUrl = "su2",avatarUrl = "au2",songPath = "sp2",
            source = DataSource.WangYiYun,uid = 1,remoteId = 2)
    // 本地独有
    private val oSong3= SongDO(name="3", author = "a3", songUrl = "su3",avatarUrl = "au3",
            source = DataSource.WangYiYun,uid = 1,remoteId = 3)
    private val oSong4= SongDO(name="4", author = "a4", songUrl = "su4",avatarUrl = "au4",songPath = "sp4",
            source = DataSource.WangYiYun,uid = 1,remoteId = 4)

    // 交集
    private val nSong1= SongDO(name="1", author = "a1", songUrl = "su1",avatarUrl = "au1",
            source = DataSource.WangYiYun,uid = 1,remoteId = 1)
    private val nSong2= SongDO(name="2", author = "a2", songUrl = "su2",avatarUrl = "au2",
            source = DataSource.WangYiYun,uid = 1,remoteId = 2)
    // Online独有
    private val nSong5= SongDO(name="5", author = "a5", songUrl = "su5",avatarUrl = "au5",
            source = DataSource.WangYiYun,uid = 1,remoteId = 5)
    private val nSong6= SongDO(name="6", author = "a6", songUrl = "su6",avatarUrl = "au6",
            source = DataSource.WangYiYun,uid = 1,remoteId = 6)
    private var listId = 0L

    @Test
    fun testSongSync()= runBlocking{
        songRepository.syncSongsInSongList(SongListDO(id=listId), listOf(nSong1,nSong2,nSong5,nSong6))
        assertEquals(listOf(oSong1,oSong2,nSong5,nSong6),songDao.loadBySongList(listId))
    }

    // 交集
    private val oList1=SongListDO(name="1", avatarUrl = "au1",
            source = DataSource.WangYiYun,uid = 1,remoteId = 1)
    private val oList2=SongListDO(name="2", avatarUrl = "au2", avatarPath = "ap2",
            source = DataSource.WangYiYun,uid = 1,remoteId = 2)
    // 本地独有
    private val oList3=SongListDO(name="3", avatarUrl = "au3",
            source = DataSource.WangYiYun,uid = 1,remoteId = 3)
    private val oList4=SongListDO(name="4", avatarUrl = "au4", avatarPath = "ap4",
            source = DataSource.WangYiYun,uid = 1,remoteId = 4)
    private val oSongIn3 = SongDO(name="33", author = "a33", songUrl = "su33",avatarUrl = "au33",
            source = DataSource.WangYiYun,uid = 1,remoteId = 33)
    // 交集
    private val nList1=SongListDO(name="1", avatarUrl = "au1",
            source = DataSource.WangYiYun,uid = 1,remoteId = 1)
    private val nList2=SongListDO(name="2", avatarUrl = "au2",
            source = DataSource.WangYiYun,uid = 1,remoteId = 2)
    // Online独有
    private val nList5=SongListDO(name="5", avatarUrl = "au5",
            source = DataSource.WangYiYun,uid = 1,remoteId = 5)
    private val nList6=SongListDO(name="6", avatarUrl = "au6",
            source = DataSource.WangYiYun,uid = 1,remoteId = 6)

    @Test
    fun testSongListSync()= runBlocking{
        assertEquals(1,songDao.loadBySongList(oList3.id!!).size)
        songListRepository.syncSongLists(listOf(oList1,oList2,oList3,oList4),listOf(nList1,nList2,nList5,nList6))
        assertEquals(listOf(oList1,oList2,nList5,nList6),songListDao.loadBySourceExclude(DataSource.Local))
        assertEquals(0,songDao.loadBySongList(oList3.id!!).size)
    }
}