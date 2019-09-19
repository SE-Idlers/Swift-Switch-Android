package com.example.win.easy.download;

import android.os.Environment;
import android.util.Log;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;

import java.io.File;
import java.io.IOException;

/**
 * <p>当需要下载歌曲、图片等等的东西的时候，设置下载文件的文件名，创建要下载的文件（当然包括已经存在文件检测等等）</p>
 * <p>对每一个创建文件的方法，都可以保证：</p>
 * <ol>
 *     <li>根据传入的对象，可以设置要创建的文件名</li>
 *     <li>返回的文件是确保已经创建好的</li>
 *     <li>假如这个service在创建文件的时候发现文件已经存在（比如之前下载了一半等等），会删掉重建，因此调用者不用担心文件存在这种问题</li>
 * </ol>
 */
public class FileService {

    private final String rootDir= Environment.getExternalStorageDirectory().getAbsolutePath();
    private String songDir="/songs";
    private String songListDir="/songLists";
    private String pictureDir="/pictures";
    private String d="/";

    /**
     * <p>根据songDO创建要下载的歌曲文件</p>
     * <p>文件名会变成：</p>
     * <p>{rootDir}/{songDir}/{uid}/{songFilename}{songExtension}</p>
     * @param songDO 要创建歌曲文件的歌曲对象
     */
    public File file(SongDO songDO) throws IOException {
        String songFileAbsolutePath=
                rootDir+
                        songDir+d+
                        songDO.getUid();
        //Log.d("Print","File rootDir: "+rootDir);
        //Log.d("Print","File absolutePath: "+songFileAbsolutePath);
        return create(songFileAbsolutePath,songDO.getName()+extension(songDO.getSongUrl()));
    }

    /**
     * <p>根据songDO创建要下载的歌曲头像文件</p>
     * <p>文件名会变成：</p>
     * <p>{rootDir}/{pictureDir}/{uid}/{songDir}/{pictureWebUri}</p>
     * @param songDO 要创建头像文件的歌曲对象
     */
    public File avatar(SongDO songDO) throws IOException {
        String songAvatarAbsolutePath=
                rootDir+
                        pictureDir+d+
                        songDO.getUid()+
                        songDir;
        return create(songAvatarAbsolutePath,
                webName(songDO.getAvatarUrl()));
    }

    /**
     * <p>根据songListDO创建要下载的歌单头像文件</p>
     * <p>文件名会变成：</p>
     * <p>{rootDir}/{pictureDir}/{uid}/{songListDir}/{dataSource}/{pictureWebUri}</p>
     * @param songListDO 依据的歌单对象
     */
    public File avatar(SongListDO songListDO) throws IOException {
        String songListAvatarAbsolutePath=
                rootDir+
                        pictureDir+d+
                        songListDO.getUid()+
                        songListDir+d+
                        songListDO.getSource().toString();
        return create(songListAvatarAbsolutePath,
                webName(songListDO.getAvatarUrl()));
    }

    /**
     * 根据文件名创建文件
     * @param dirPath 绝对路径
     * @return 创建完毕的文件
     * @throws IOException 文件创建失败
     */
    private File create(String dirPath,String filename) throws IOException {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(dirPath);
            //Log.d("Flag", "dir ?:"+dirPath);
            if(!dir.exists()){
                dir.mkdirs();
                Log.d("Flag", "dir mkdirs :"+dir.toPath());
            }
            File file = new File(dirPath,filename);

            if(file.exists()){
                //若文件已存在，删除
                Log.d("Exception", "File existed");
                file.delete();
                Log.d("Exception", "delete old File");
            }

            //Log.d("Flag", "newFile ?:"+file.toPath());
            file.createNewFile();
            Log.d("Flag", "newFile done");

            Log.d("Flag", "successfulCreation ?:"+file.exists());

            return file;
        }
        throw new IOException();
    }


    //从url中拿出文件的扩展名
    private String extension(String url){
        return url.substring(url.lastIndexOf("."));
    }

    //直接获取url里面最后的那个乱七八糟的文件名
    //需要这个很乱的文件名主要是因为，像图片这种东西，没啥好命名的..乱，还能保证大概率不冲突
    private String webName(String url){
        return url.substring(url.lastIndexOf("/")+1);
    }
}
