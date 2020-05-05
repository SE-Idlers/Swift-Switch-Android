package com.example.win.easy.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.win.easy.R
import com.example.win.easy.display.DisplayServiceAdapter
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.value_object.SongVO
import com.example.win.easy.view.EntityItem
import com.example.win.easy.view.OnClickFunc
import com.example.win.easy.viewmodel.SongListViewModel
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import kotlin.collections.ArrayList

class SongListFragment(private val displayServiceAdapter: DisplayServiceAdapter, private val downloadServiceAdapter: DownloadServiceAdapter, private val factory: ViewModelProvider.Factory) : ListFragment() {
    private lateinit var thisSongList: SongListDO
    private lateinit var songsInThisSongList: LiveData<List<SongDO>?>
    private lateinit var songListViewModel: SongListViewModel

    /**
     * 首次创建注册ViewModel
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songListViewModel=ViewModelProviders.of(this, factory).get(SongListViewModel::class.java)
    }

    /**
     * 每次创建视图时：
     *  1. 重新获取歌单内容livedata
     *  2. 监听livedata，更新时刷新视图
     *  3. 设置标题
     *  4. 设置右上角按钮导航
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        thisSongList = retrievePassedSongList()
        songsInThisSongList = loadSongsIn(thisSongList)
        songsInThisSongList.observe(this, androidx.lifecycle.Observer { refresh(it) })
        setTopBarTitle(thisSongList.name)
        setUpRightImageButton()
        return view
    }

    private fun retrievePassedSongList()=SongListFragmentArgs.fromBundle(arguments!!).selectedSongList
    private fun loadSongsIn(songListDO: SongListDO)=songListViewModel.loadSongsIn(songListDO)

    /**
     * 新的数据到来时刷新视图
     */
    private fun refresh(newSongDOs: List<SongDO>?) {
        newSongDOs?.let {nonNullSongDOs->
            ArrayList<QMUICommonListItemView>().apply {
                for (songDO in nonNullSongDOs)
                    add(item(songDO))
                setItemViews(this)
            }
        }
    }

    private fun item(songDO: SongDO)= SongItem(songDO, OnClickFunc { songOfTheItem: SongDO, _: QMUICommonListItemView? ->
        songOfTheItem.songPath?.let {//如果歌曲文件已经下载就直接播放
            display(songOfTheItem)
        } ?:let {//如果没有下载，那就下载后再播放
            songOfTheItem.songUrl?.let {
                downloadThenDisplay(songOfTheItem)
            }
        }
    })

    private fun display(songDO: SongDO) {
        throw NotImplementedError()
        // TODO
//        displayServiceAdapter.startWith(songVO, songsInThisSongList.value!!)
    }

    private fun downloadThenDisplay(songDO: SongDO) {
        throw NotImplementedError()
        // TODO
//        downloadServiceAdapter.download(songVO) { songVO: SongVO -> display(songVO) }
    }

    /**
     * 设置右上角按钮，导航到“添加歌曲到歌单界面”
     */
    private fun setUpRightImageButton() {
        setRightImageButtonOnClickListener {
            Navigation.findNavController(view!!)
                    .navigate(SongListFragmentDirections.actionSongListFragmentToAddSongToSongListFragment(thisSongList))
        }
    }

    internal inner class SongItem(entity: SongDO, onClickFunc: OnClickFunc<SongDO>) : EntityItem<SongDO>(this@SongListFragment.context, entity, onClickFunc) {
        init {
            text = entity.name //显示歌曲名字
            setImageDrawable(resources.getDrawable(R.drawable.ase16)) //显示歌曲默认头像，如果后续发现有下载好的头像，则替换
            entity.songPath?: setBackgroundColor(resources.getColor(R.color.app_color_description))
        }
    }
}