package com.example.win.easy.download

import com.example.win.easy.exception.FailToDownloadException
import com.example.win.easy.repository.db.data_object.SongDO
import com.example.win.easy.repository.db.data_object.SongListDO
import com.example.win.easy.viewmodel.SongViewModel
import com.example.win.easy.web.callback.OnReadyFunc
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.URLConnection

class DownloadService(
        private val fileService: FileService,
        private val songViewModel: SongViewModel,
        private val downloadDispatcher: ExecutorCoroutineDispatcher
):CoroutineScope by CoroutineScope(downloadDispatcher) {

    /**
     * 下载songDO对应的歌曲，应当在背景线程中进行
     */
    @Throws(FailToDownloadException::class)
    fun download(songDO: SongDO){
        var file: File?=null
        try {
            file=fileService.file(songDO)
            download(songDO.songUrl,file)
        }catch (t: Throwable){
            file?.delete()
            throw FailToDownloadException("Fail to download song: ${songDO.name}")
        }
        songDO.songPath=file.path

        // 更新数据库
        songViewModel.update(songDO)
    }

    fun download(songListDO: SongListDO?, onReadyFunc: OnReadyFunc<SongDO?>?) {
        TODO()
    }

    /**
     * 将指定的url下载到执行的文件中
     * @param urlStr web地址
     * @param file 下载到的文件
     */
    @Throws(Throwable::class)
    private fun download(urlStr: String,file: File){
        var connection: URLConnection?=null
        var networkIn: BufferedInputStream?=null
        var localOut: FileOutputStream?=null

        try {
            connection=URL(urlStr).openConnection()
            connection.connect()

            networkIn=BufferedInputStream(connection.getInputStream())
            localOut=FileOutputStream(file)

            val dataBuffer=ByteArray(4096)
            var count: Int

            count=networkIn.read(dataBuffer)
            while (count!=-1){
                localOut.write(dataBuffer, 0, count);
                count=networkIn.read(dataBuffer)
            }

            localOut.flush()
        }finally {
            localOut?.close()
            networkIn?.close()
        }
    }
}