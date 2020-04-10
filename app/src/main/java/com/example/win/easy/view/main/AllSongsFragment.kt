package com.example.win.easy.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.win.easy.Constants
import com.example.win.easy.R
import com.example.win.easy.factory.__SongFactory
import com.example.win.easy.repository.db.CustomTypeConverters
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.tool.UriProcessTool
import com.example.win.easy.view.EntityItem
import com.example.win.easy.view.OnClickFunc
import com.example.win.easy.viewmodel.SimpleViewModel
import com.qmuiteam.qmui.widget.QMUIRadiusImageView
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import java.io.File
import kotlin.collections.ArrayList

/**
 *
 * 展示所有歌曲的界面
 */
class AllSongsFragment(
        private val factory: ViewModelProvider.Factory,
        private val songFactory: __SongFactory) : ListFragment() {

    private lateinit var viewModel: SimpleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory)[SimpleViewModel::class.java]
        viewModel.allSongs.observe(this, Observer { songDOs: MutableList<SongDO> -> refresh(songDOs) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            super.onCreateView(inflater, container, savedInstanceState).also {
                setTopBarTitle("所有歌曲")
                setRightImageButton()
            }

    private fun setRightImageButton(){
        setRightImageButtonOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*"
            startActivityForResult(intent, Constants.READ_REQUEST_CODE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        //从查看文件夹Activity,即请求码为READ_REQUEST_CODE返回
        if (requestCode == Constants.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
            //添加歌曲到本地数据库
            viewModel.insert(songFactory.create(File(UriProcessTool.getPathByUri4kitkat(context, resultData.data))))
            //提示添加成功
            Toast.makeText(context, "歌曲添加成功", Toast.LENGTH_SHORT).show()
        }
    }



    /**
     * 根据最新的歌曲数据刷新视图
     * @param songDOs 最新的歌曲数据
     */
    private fun refresh(songDOs: MutableList<SongDO>) {
        ArrayList<QMUICommonListItemView>().run {
            for (i in songDOs.indices)
                add(SongItem(songDOs[i],OnClickFunc{ _, _->
                    //TODO
                    Toast.makeText(context, "待实现：点击播放歌曲", Toast.LENGTH_SHORT).show()
                }))
            setItemViews (this)
        }
    }

    internal inner class SongItem(entity: SongDO, onClickFunc: OnClickFunc<SongDO>) : EntityItem<SongDO>(this@AllSongsFragment.context, entity, onClickFunc) {
        init {
            //TODO
            // 1. 修改代码布局，有点乱
            // 2. SongDO类java转kotlin，直接访问field不合适
            // 3. default drawable的问题
            // 4. avatar的显示
            text = entity.name // 歌曲名称
            setImageDrawable(resources.getDrawable(R.drawable.ase16)) // 默认头像
            accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM // item访问类型
            entity.songPath?.let { // 如果歌曲已经下载好，显示seq，右侧显示音符
                QMUIRadiusImageView(context).run {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_action_music))
                    addAccessoryCustomView(this)
                }
                detailText = CustomTypeConverters.characterList2string(entity.sequence)
            }
            entity.avatarPath?.let { // 如果歌曲有下载好的头像，读取、解码图片，更新视图
//                new DecodeImageAsyncTask(this,getResources()).execute(entity.getAvatarPath());
            }
        }
    }

}