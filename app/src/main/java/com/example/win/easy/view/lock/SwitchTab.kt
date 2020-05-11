package com.example.win.easy.view.lock

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.example.win.easy.R
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.tool.SongListWithSongs
import com.example.win.easy.view.OnClickFunc
import com.qmuiteam.qmui.layout.QMUILinearLayout
import com.qmuiteam.qmui.widget.QMUITabSegment
import java.util.*

class SwitchTab:QMUILinearLayout{

    @BindView(R.id.tab_segment) lateinit var tabSegment: QMUITabSegment
    @BindView(R.id.view_pager) lateinit var viewPager: ViewPager
    private lateinit var currentAllSongListWithSongs: List<SongListWithSongs>
    private val pageGroup: PageGroup
    private val listeners: MutableList<QMUITabSegment.OnTabSelectedListener> = ArrayList()

    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet?):super(context,attributeSet)
    constructor(context: Context,attributeSet: AttributeSet?,defStyleAttr: Int):super(context,attributeSet,defStyleAttr)

    init{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val thisView = inflater.inflate(R.layout.switch_tab, this)
        ButterKnife.bind(this, thisView)
        pageGroup = PageGroup(context)
        viewPager.adapter = pageGroup
        tabSegment.setupWithViewPager(viewPager, false)
    }

    fun setPagesWithTab(allSongListWithSongs: List<SongListWithSongs>, tabOnClickFunc: OnClickFunc<SongListWithSongs>, songOnClickFunc: OnClickFunc<SongDO>) {
        clear()
        currentAllSongListWithSongs = allSongListWithSongs
        for (songListWithSong in allSongListWithSongs) {
            addTab(songListWithSong.songList, tabOnClickFunc)
            addPage(songListWithSong.songs, songOnClickFunc)
        }
        flush()
    }

    private fun clear() {
        clearListeners()
        tabSegment.reset()
        pageGroup.clear()
    }

    private fun clearListeners() {
        for (listener in listeners)
            tabSegment.removeOnTabSelectedListener(listener)
        listeners.clear()
    }

    private fun addTab(songList: SongListDO, tabOnClickFunc: OnClickFunc<SongListWithSongs>) {
        tabSegment.addTab(QMUITabSegment.Tab(songList.name))
        createListener(tabOnClickFunc).let {
            tabSegment.addOnTabSelectedListener(it)
            listeners.add(it)
        }
    }

    private fun createListener(tabOnClickFunc: OnClickFunc<SongListWithSongs>)= object : QMUITabSegment.OnTabSelectedListener {
        override fun onTabSelected(index: Int) {
            tabOnClickFunc.onclick(currentAllSongListWithSongs[index], null)
        }
        override fun onTabUnselected(index: Int) {}
        override fun onTabReselected(index: Int) {}
        override fun onDoubleTap(index: Int) {}
    }

    private fun addPage(songsInList: List<SongDO>, songOnClickFunc: OnClickFunc<SongDO>)=
            pageGroup.addPage(songsInList, songOnClickFunc)


    private fun flush() {
        pageGroup.flush()
        tabSegment.notifyDataChanged()
    }
}