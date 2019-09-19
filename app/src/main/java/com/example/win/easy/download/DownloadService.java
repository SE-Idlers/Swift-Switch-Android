package com.example.win.easy.download;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.callback.OnReadyFunc;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

public class DownloadService {

    private ThreadPoolExecutor downloadPoolExecutor;
    private FileService fileService;

    public DownloadService(ThreadPoolExecutor downloadPoolExecutor,FileService fileService){
        this.downloadPoolExecutor =downloadPoolExecutor;
        this.fileService=fileService;
    }

    public void download(SongDO songDO, OnReadyFunc<SongDO> onReadyFunc){
        //TODO 下载一首歌（相关的封面（头像）是扔在这里面一块儿下载还是单开一个都行）
        try{
            //下载歌曲
            URL url = new URL(songDO.songUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.connect();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                long lengthOfFile = connection.getContentLength();
                long total = 0;
                int count = 0;
                Log.d("Prepare to download","MUSIC：Length of File: "+ lengthOfFile);
                InputStream networkIn = new BufferedInputStream(connection.getInputStream());
                File fileOut = fileService.file(songDO);
                songDO.songPath = fileOut.getAbsolutePath();
                OutputStream localOut = new FileOutputStream(fileOut);
                byte[] dataBuffer = new byte[1024];
                while((count = networkIn.read(dataBuffer))!=-1){
                    total += count;
                    //Log.d("Progress","Has Finished: "+(total * 100 / lengthOfFile)+"%");
                    localOut.write(dataBuffer,0,count);
                }
                localOut.flush();
                localOut.close();
                networkIn.close();

            }else{
                //无效URL
                Log.d("Fail to download","fail to connect to "+songDO.songUrl);
                Log.d("Error","Connection ResponseCode: "+connection.getResponseCode());
            }


            //下载图片

            url = new URL(songDO.avatarUrl);
            connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.connect();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                long lengthOfFile = connection.getContentLength();
                long total = 0;
                int count = 0;
                Log.d("Prepare to download","AVATAR：Length of File: "+ lengthOfFile);
                InputStream networkIn = new BufferedInputStream(connection.getInputStream());
                File fileOut = fileService.avatar(songDO);
                songDO.avatarPath = fileOut.getAbsolutePath();
                OutputStream localOut = new FileOutputStream(fileOut);
                byte[] dataBuffer = new byte[1024];
                while((count = networkIn.read(dataBuffer))!=-1){
                    total += count;
                    //Log.d("Progress","Has Finished: "+(total * 100 / lengthOfFile)+"%");
                    localOut.write(dataBuffer,0,count);
                }
                localOut.flush();
                localOut.close();
                networkIn.close();

            }else{
                Log.d("Fail to download","fail to connect to "+songDO.avatarUrl);
                Log.d("Error","Connection ResponseCode: "+connection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        onReadyFunc.onReady(songDO);
    }

    public void download(SongListDO songListDO,OnReadyFunc<SongListDO> onReadyFunc){
        //TODO 下载list封面（头像），现在是这样。虽然扔进来的参数是个完整的..

        try{
            //下载图片
            URL url = new URL(songListDO.avatarUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            connection.connect();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                long lengthOfFile = connection.getContentLength();
                long total = 0;
                int count = 0;
                Log.d("Prepare to download","AVATAR：Length of File: "+ lengthOfFile);
                InputStream networkIn = new BufferedInputStream(connection.getInputStream());
                File fileOut = fileService.avatar(songListDO);
                songListDO.avatarPath = fileOut.getAbsolutePath();
                OutputStream localOut = new FileOutputStream(fileOut);
                byte[] dataBuffer = new byte[1024];
                while((count = networkIn.read(dataBuffer))!=-1){
                    total += count;
                    //Log.d("Progress","Has Finished: "+(total * 100 / lengthOfFile)+"%");
                    localOut.write(dataBuffer,0,count);
                }
                localOut.flush();
                localOut.close();
                networkIn.close();

            }else{
                Log.d("Fail to download","fail to connect to "+songListDO.avatarUrl);
                Log.d("Error","Connection ResponseCode: "+connection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        onReadyFunc.onReady(songListDO);
    }

//    private final int CORE_POOL_SIZE=5;
//    private final int MAX_POOL_SIZE=5;
//    private final int KEEP_ALIVE_TIME=50;
//    public DownloadService(){
//        downloadPoolExecutor=new ThreadPoolExecutor(
//                CORE_POOL_SIZE,
//                MAX_POOL_SIZE,
//                KEEP_ALIVE_TIME,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<>());
//    }
}
