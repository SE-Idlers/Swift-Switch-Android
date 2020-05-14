package com.example.win.easy.view.lock

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.win.easy.db.SongDO
import com.example.win.easy.view.OnClickFunc
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import java.util.*

class PageGroup(private val context: Context) : PagerAdapter() {

    private val pages: MutableList<QMUIGroupListView> = ArrayList()

    fun clear() = pages.clear()
    fun addPage(songsInPage: List<SongDO>, songOnClickFunc: OnClickFunc<SongDO>) = pages.add(toPage(songsInPage,songOnClickFunc))
    fun flush() = notifyDataSetChanged()

    override fun getCount() = pages.size
    override fun isViewFromObject(view: View, `object`: Any) = view === `object`
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container.removeView(`object` as View) // 从容器中移除视图
    override fun getItemPosition(`object`: Any) = POSITION_NONE // 强制每次刷新视图
    override fun instantiateItem(container: ViewGroup, position: Int) =
            pages[position].also{
                //向容器中添加视图
                container.addView(it, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }

    private fun toPage(songsInPage: List<SongDO>, songOnClickFunc: OnClickFunc<SongDO>) =
            QMUIGroupListView(context).also {
                setItemsIn(it, songsInPage, songOnClickFunc)
            }

    private fun setItemsIn(page: QMUIGroupListView, songsInPage: List<SongDO>, songOnClickFunc: OnClickFunc<SongDO>) =
            QMUIGroupListView.newSection(context).run{
                //为这个歌单每一首歌构建一个Item，并把它们放入同一个Section中
                for (song in songsInPage)
                    item(song,page).let {
                        addItemView(it) { _: View? ->
                            songOnClickFunc.onclick(song,it)
                        }
                    }
                //将这个Section放入GroupListView中（每个GroupListView只设置一个Section，虽然实际上允许放多个Section）
                addTo(page)
            }

    private fun item(song: SongDO, page: QMUIGroupListView) =
            page.createItemView(
                null,
                song.name,
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE)
}