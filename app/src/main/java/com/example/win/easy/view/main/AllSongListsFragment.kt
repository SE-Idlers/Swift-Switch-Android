package com.example.win.easy.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import butterknife.BindString
import com.example.win.easy.R
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.view.EntityItem
import com.example.win.easy.view.ImageService
import com.example.win.easy.view.OnClickFunc
import com.example.win.easy.viewmodel.SongListViewModel
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import java.util.*

/**
 *
 * 用于展示所有歌单的界面
 */
class AllSongListsFragment(private val factory: ViewModelProvider.Factory, private val imageService: ImageService) : ListFragment() {

    @BindString(R.string.allSongListsFragmentTopBarTitle)
    lateinit var barTitle: String

    /**
     * 首次创建时注册监听
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(this, factory).get(SongListViewModel::class.java).run {
            loadAll().observe(this@AllSongListsFragment, Observer { refresh(it) })
        }
    }

    /**
     * 每次刷新视图时：
     * 1. 设置标题栏
     * 2. 设置右上角添加歌单按钮
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)=
            super.onCreateView(inflater, container, savedInstanceState).also {
                setTopBarTitle(barTitle)
                setRightImageButtonOnClickListener {
                    Navigation.findNavController(it!!)
                            .navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToPlaceholder())
                }
            }

    /**
     * 根据最新的歌单数据刷新视图
     */
    private fun refresh(songLists: List<SongListDO>) {
        ArrayList<QMUICommonListItemView>().run {
            //对每个歌单都生成一个itemView
            for (songList in songLists)
                add(SongListItem(songList, OnClickFunc { songListPassed: SongListDO, _: QMUICommonListItemView ->
                    Navigation.findNavController(view!!)
                            .navigate(AllSongListsFragmentDirections.actionAllSongListsFragmentToSongListFragment(songListPassed))
                }))
            //显示之
            setItemViews(this)
        }
    }

    /**
     * 列表页面中的每一个Item对应的对象
     */
    internal inner class SongListItem(entity: SongListDO, onClickFunc: OnClickFunc<SongListDO>) : EntityItem<SongListDO>(this@AllSongListsFragment.context, entity, onClickFunc) {
        init {
            text = entity.name // 歌单名字
            setImageDrawable(resources.getDrawable(R.drawable.ase16)) // 默认头像
            accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON // item最右侧显示">"
            entity.avatarPath?.let { // 如果头像已下载，发起解码，成功后设置头像
                imageService.decode(it)
            }
        }
    }

}