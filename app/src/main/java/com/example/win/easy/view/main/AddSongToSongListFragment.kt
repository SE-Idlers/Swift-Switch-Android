package com.example.win.easy.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.win.easy.R
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.view.EntityItem
import com.example.win.easy.view.OnClickFunc
import com.example.win.easy.viewmodel.RelationViewModel
import com.example.win.easy.viewmodel.SongListViewModel
import com.example.win.easy.viewmodel.SongViewModel
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import java.util.*

class AddSongToSongListFragment(private val viewModelFactory: ViewModelProvider.Factory) : ListFragment() {
    private lateinit var songViewModel: SongViewModel
    private lateinit var relationViewModel: RelationViewModel
    private lateinit var thisSongList: SongListDO

    private val topBarTitle = "选择要添加的歌曲"
    private lateinit var songsNotInThisSongList: List<SongDO>
    private val songsToAdd: MutableList<SongDO> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songViewModel = ViewModelProviders.of(this,viewModelFactory).get(SongViewModel::class.java)
        relationViewModel = ViewModelProviders.of(this,viewModelFactory).get(RelationViewModel::class.java)
    }

    /**
     *  获取传递进来的歌单并获取不在这个歌单里的歌曲，设置右上角“添加”按钮，设置页面标题
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            super.onCreateView(inflater, container, savedInstanceState).apply {
                thisSongList = AddSongToSongListFragmentArgs.fromBundle(arguments!!).songListSongsAddedTo
                songViewModel.launchLoadSongsExclude(thisSongList){songs ->
                    songsNotInThisSongList=songs
                    refresh(songs)
                }
                setUpAddButton()
                setTopBarTitle(topBarTitle)
            }

    private fun refresh(songsNotInThisSongList: List<SongDO>) =
            ArrayList<QMUICommonListItemView>().run {
                for (song in songsNotInThisSongList) {
                    add(SongAddItem(SongWithSelectedInfo(song), OnClickFunc { thisSongItem: SongWithSelectedInfo, v: QMUICommonListItemView ->
                        thisSongItem.clicked()
                        if (thisSongItem.selected)
                            songsToAdd.add(thisSongItem.songDO)
                        else
                            songsToAdd.remove(thisSongItem.songDO)
                        refresh(thisSongItem.selected, v)
                    }))
                }
                setItemViews(this)
            }

    private fun refresh(selected: Boolean, itemView: QMUICommonListItemView) =
            itemView.setImageDrawable(if (selected)resources.getDrawable(R.drawable.ase16)else null)

    private fun setUpAddButton() = setRightImageButtonOnClickListener {
        relationViewModel.addSongsTo(songsToAdd,thisSongList)
        Navigation.findNavController(view!!)
                .navigate(AddSongToSongListFragmentDirections.actionAddSongToSongListFragmentToSongListFragment(thisSongList))
    }

    internal inner class SongAddItem(
            entity: SongWithSelectedInfo,
            onClickFunc: OnClickFunc<SongWithSelectedInfo>)
        : EntityItem<SongWithSelectedInfo>(this@AddSongToSongListFragment.context, entity, onClickFunc) {
        init {
            text = entity.songDO.name //显示歌曲名字
            refresh(entity.selected, this)
        }
    }

    /**
     * 带着被选择信息的歌曲对象
     * ps:纯粹是出于点击需要
     */
    internal class SongWithSelectedInfo(val songDO: SongDO) {
        var selected = false
        fun clicked() { selected=!selected }
    }
}

