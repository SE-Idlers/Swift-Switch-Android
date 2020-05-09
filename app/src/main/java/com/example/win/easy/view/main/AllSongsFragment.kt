package com.example.win.easy.view.main

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.win.easy.Constants
import com.example.win.easy.display.interfaces.DisplayService
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.enumeration.DataSource
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.tool.UriProcessTool
import com.example.win.easy.viewmodel.SongViewModel
import java.io.File

/**
 *
 * 展示所有歌曲的界面
 */
class AllSongsFragment(
        displayService: DisplayService,
        downloadServiceAdapter: DownloadServiceAdapter,
        private val parser: FilenameParser<Char>,
        private val factory: ViewModelProvider.Factory) : SongListFragment(displayService,downloadServiceAdapter,factory) {

    private lateinit var songViewModel: SongViewModel

    override fun initViewModel() {
        songViewModel=ViewModelProviders.of(this,factory).get(SongViewModel::class.java)
    }

    override fun initCreateView(){
        initSongList()

        // 设置标题、按钮
        setTopBarTitle(thisSongList.name)
        setUpRightImageButton()
    }

    override fun initSongList(){
        thisSongList= SongListDO(name = "本地歌曲",source = DataSource.Local)
        songsInThisSongList=songViewModel.allLocalSongs
    }

    /**
     * 设置右上角按钮，导航到“添加本地歌曲”
     */
    override fun setUpRightImageButton() {
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
            //添加歌曲到本地数据库e
            songViewModel.insertOnUniquePath(data2Song(resultData))
            //提示添加成功
            Toast.makeText(context, "歌曲添加成功", Toast.LENGTH_SHORT).show()
        }
    }

    private fun data2Song(intent: Intent): SongDO{
        val path=File(UriProcessTool.getPathByUri4kitkat(context, intent.data)).absolutePath
        return SongDO(
            name=path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.')),
            author=path.substring(path.lastIndexOf('/') + 1, path.indexOf('-')),
            source=DataSource.Local,
            sequence=parser.parse(path.substring(path.indexOf('-') + 1, path.lastIndexOf('.'))),
            songPath=path
        )
    }
}