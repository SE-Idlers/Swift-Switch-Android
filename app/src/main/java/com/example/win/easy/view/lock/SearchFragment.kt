package com.example.win.easy.view.lock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.example.win.easy.R
import com.example.win.easy.display.interfaces.DisplayService
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.tool.SongListWithSongs
import com.example.win.easy.view.OnClickFunc
import com.example.win.easy.viewmodel.SongListViewModel
import com.example.win.easy.viewmodel.SongViewModel
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment(private val viewModelFactory: ViewModelProvider.Factory, private val displayService: DisplayService) : Fragment() {

    @BindView(R.id.switch_tab) lateinit var switchTab: SwitchTab
    @BindString(R.string.defaultNameOfSongListOfAllSongs) lateinit var defaultSongListName: String

    private lateinit var songViewModel: SongViewModel
    private lateinit var songListViewModel: SongListViewModel

    private var lastSelectedSong: SongDO? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val thisView = inflater.inflate(R.layout.fragment_search, container, false)
        ButterKnife.bind(this, thisView)
        songViewModel = ViewModelProviders.of(this, viewModelFactory).get(SongViewModel::class.java)
        songListViewModel = ViewModelProviders.of(this, viewModelFactory).get(SongListViewModel::class.java)
        return thisView
    }

    /**
     * 搜索歌曲并根据结果设置相应视图
     */
    fun search(sequence: List<Char>)=songViewModel.launchLoadSongsBySeq(sequence){ songsMatched ->
        switchTab.setPagesWithTab(
                groupBySource(songsMatched),
                tabOnClickFuncWhenSearching(),
                songOnClickFunc())
    }

    private fun songOnClickFunc() = OnClickFunc { song: SongDO, _: QMUICommonListItemView? -> toSwitchMode(song) }

    private fun toSwitchMode(selectedSong: SongDO) {
        lastSelectedSong = selectedSong
        songListViewModel.launchLoadSongListsBySong(selectedSong){songListsContainIt->
            if (songListsContainIt.isNotEmpty())
                songViewModel.launchLoadSongsBySongLists(songListsContainIt){ songsInSongLists->
                    displayService.restartWith(selectedSong,songListsContainIt[0],songsInSongLists[0])
                    switchTab.setPagesWithTab(
                            combine(songListsContainIt,songsInSongLists),
                            tabOnClickFuncWhenSwitching(),
                            songOnClickFunc())

                }
            else
                defaultSongListWithSongs().run {
                    displayService.restartWith(selectedSong,songList,songs)
                    switchTab.setPagesWithTab(
                            listOf(this),
                            tabOnClickFuncWhenSwitching(),
                            songOnClickFunc())
                }
        }
    }

    private fun combine(songLists: List<SongListDO>,songsInLists: List<List<SongDO>>) =
            ArrayList<SongListWithSongs>().apply {
                for(i in songLists.indices)
                    add(SongListWithSongs(songList = songLists[i],songs = songsInLists[i]))
            }

    private fun tabOnClickFuncWhenSwitching(): OnClickFunc<SongListWithSongs> =
        OnClickFunc { songListWithSongs, _: QMUICommonListItemView? ->
            if (ableToReplaceSongList())
                displayService.configDisplayList(songListWithSongs.songList,songListWithSongs.songs)
        }

    private fun ableToReplaceSongList()= displayService.currentSong() == lastSelectedSong
    private fun defaultSongListWithSongs()=SongListWithSongs(songList = defaultSongList(),songs = songViewModel.allSongs.value!!)
    private fun defaultSongList()=SongListDO(name = defaultSongListName,source = DataSource.Local)
    private fun tabOnClickFuncWhenSearching() = OnClickFunc { _: SongListWithSongs, _: QMUICommonListItemView? -> }


    private fun groupBySource(songs: List<SongDO>): List<SongListWithSongs> {
        val source2ListMap: MutableMap<DataSource, MutableList<SongDO>> = EnumMap(DataSource::class.java)
        val songSources = listOf(*DataSource.values())

        for (songSrc in songSources)
            source2ListMap[songSrc] = ArrayList()
        for (song in songs)
            source2ListMap[song.source]!!.add(song)

        return ArrayList<SongListWithSongs>().apply {
            for (source in songSources)
                if (source2ListMap[source]!!.isNotEmpty())
                    add(SongListWithSongs(songList=SongListDO().apply { name=source.name; this.source =source },songs=source2ListMap[source]!!))
        }
    }
}