package com.example.win.easy

import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO
import com.example.win.easy.db.SongXSongListDO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SongDaoTest {

    @get:Rule val databaseRule=DatabaseRule()
    private val songDao=databaseRule.songDao
    private val songListDao=databaseRule.songListDao
    private val relationDao=databaseRule.songXSongListDao

    @Before
    fun setUp()= runBlocking{
        songDao.insert(seq1)
        songDao.insert(seq2)
        songDao.insert(seq3)

        val songId=songDao.insert(song)
        val listId1=songListDao.insert(songList1)
        val listId2=songListDao.insert(songList2)

        relationDao.insert(SongXSongListDO(songId, listId1))
        relationDao.insert(SongXSongListDO(songId, listId2))

        songList1.id=listId1
        songList2.id=listId2
    }

    private val song = SongDO(name = "song")
    private val songList1 = SongListDO(name = "1")
    private val songList2 = SongListDO(name = "2")

    @Test
    fun testLoadExcludeSongList()= runBlocking{
        val result=songDao.loadExcludeSongList(songList1.id!!)
        assertEquals(1,result.size)
        assertEquals("song",result[0].name)
    }

    private val seq1 = SongDO(name = "seq1", sequence = listOf('A'))
    private val seq2 = SongDO(name = "seq2", sequence = listOf('A', 'B'))
    private val seq3 = SongDO(name = "seq3", sequence = listOf('-', 'A'))

    @Test
    fun testLoadBySeq()= runBlocking{
        val result=songDao.loadBySeq(String(listOf('A').toCharArray()))
        assertEquals(2,result.size)
        assertEquals("seq1",result[0].name)
        assertEquals("seq2",result[1].name)
    }

}