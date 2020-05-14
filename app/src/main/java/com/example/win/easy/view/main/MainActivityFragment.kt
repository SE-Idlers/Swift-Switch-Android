package com.example.win.easy.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.example.win.easy.R
import com.example.win.easy.network.LoginService
import com.qmuiteam.qmui.widget.QMUITopBar
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView

/**
 *
 * 点app图标进来后见到的主界面
 */
class MainActivityFragment(private val loginService: LoginService) : Fragment() {
    @BindView(R.id.listview)
    lateinit var itemGroup: QMUIGroupListView

    @BindView(R.id.topbar)
    lateinit var qmuiTopBar: QMUITopBar

    @BindString(R.string.textOnItemToAllSong)
    lateinit var textItemAllSong: String

    @BindString(R.string.textOnItemToAllSongList)
    lateinit var textItemAllSongList: String

    @BindString(R.string.mainActivityFragmentTopBarTitle)
    lateinit var topBarTitle: String

    /**
     *  主界面的初始化工作：
     *  1. 顶栏标题
     *  2. 登录按钮
     *  3. 前往所有歌曲/歌单的item
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main_activity, container, false)
                    .also {
                        ButterKnife.bind(this, it)
                        setTopBar(topBarTitle)
                        setButtonLogin()
                        setItems(createItemAllSong(), createItemAllSongList())
                    }

    /**
     * 设置顶栏标题
     */
    private fun setTopBar(title: String) {
        qmuiTopBar.setTitle(title)
    }

    /**
     * 设置前往登录界面的按钮
     */
    private fun setButtonLogin(){
        qmuiTopBar.addLeftImageButton(R.drawable.ic_action_cloud, R.id.loginButtonId).run {
            setOnClickListener{
                synchronized(loginService) {
                    if (!loginService.hasLogin())
                        Navigation.findNavController(view!!)
                                .navigate(MainActivityFragmentDirections.actionMainActivityFragmentToLoginFragment())
                }
            }
        }
    }

    /**
     * 创建前往所有歌曲的item
     */
    private fun createItemAllSong() =
            newItem(textItemAllSong, View.OnClickListener {
                Navigation.findNavController(view!!)
                        .navigate(MainActivityFragmentDirections.actionMainActivityFragmentToAllSongsFragment())
            }).also {
                addButtonTo(createButtonAddSong(),it)
            }

    /**
     * 创建新的item
     */
    private fun newItem(text: String?, onClickListener: View.OnClickListener) =
            itemGroup.createItemView(text).apply {
                accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
                setOnClickListener(onClickListener)
            }

    /**
     * 创建添加歌曲的按钮
     */
    private fun createButtonAddSong()=
            newImageButton(R.drawable.ic_action_name, View.OnClickListener {
                Navigation.findNavController(view!!)
                        .navigate(MainActivityFragmentDirections.actionMainActivityFragmentToPlaceholder())
            })


    /**
     * 创建新的带图片的按钮
     */
    private fun newImageButton(imageResId: Int, onClickListener: View.OnClickListener)=
            ImageButton(context).apply{
                setImageResource(imageResId)
                setOnClickListener(onClickListener)
            }

    /**
     * 将按钮attach到item上
     */
    private fun addButtonTo(imageButton: ImageButton, itemView: QMUICommonListItemView) {
        itemView.addAccessoryCustomView(imageButton)
    }

    /**
     * 创建前往所有歌单的item
     */
    private fun createItemAllSongList() =
        newItem(textItemAllSongList,View.OnClickListener {
            Navigation.findNavController(view!!)
                    .navigate(MainActivityFragmentDirections.actionMainActivityFragmentToAllSongListsFragment())
        }).also {
            addButtonTo(createButtonAddSongList(),it)
        }

    /**
     * 创建添加歌单的按钮
     */
    private fun createButtonAddSongList()=
            newImageButton(
                    R.drawable.ic_action_name, View.OnClickListener {
                        Navigation.findNavController(view!!)
                                .navigate(MainActivityFragmentDirections.actionMainActivityFragmentToSongListCreationFragment())
            })

    /**
     * 设置并显示主界面的items
     */
    private fun setItems(vararg itemViews: QMUICommonListItemView) {
        QMUIGroupListView.newSection(context).run {
            for (itemView in itemViews)
                addItemView(itemView, null)
            addTo(itemGroup)
        }
    }

}