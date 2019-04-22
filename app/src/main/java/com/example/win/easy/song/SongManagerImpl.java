package com.example.win.easy.song;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.persistence.component.FileSongMapConfigurationPersistence;
import com.example.win.easy.song.interfaces.File2SongConverter;
import com.example.win.easy.song.convert.File2SongConverterImpl;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.view.DashboardView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * i think this should just input file, and use file2song, then implement hashmap
 */
public class SongManagerImpl extends Activity implements SongManager  {

    private static FileSongMapConfigurationPersistence fileSongMapConfigurationPersistence = FileSongMapConfigurationPersistence.getInstance();
    private static File2SongConverter file2SongConverter = File2SongConverterImpl.getInstance();
    private static SongManagerImpl instance = new SongManagerImpl();

    private static Map<File, Song> fileToSong;
    private static Map<Song, File> songToFile;
    private static List<File> files;
    private static List<Song> songs;
    private static List<List<Character>> sequences;

    private SongListMangerImpl songListManger=SongListMangerImpl.getInstance();
    private DashboardView dashboardView=DashboardView.getInstance();

    private Button btnAddSong = (Button) MainActivity.mainActivity.findViewById(R.id.AddSong);

    private SongManagerImpl() {
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });
    }

    public static SongManagerImpl getInstance() {
        return instance;
    }

    static {
        fileToSong = fileSongMapConfigurationPersistence.load();
        if (fileToSong == null)
            fileToSong = new HashMap<>();
        files = new ArrayList<>(fileToSong.keySet());
        songs = new ArrayList<>(fileToSong.values());
        sequences = new ArrayList<>();
        songToFile = new HashMap<>();
        for (File file : files) {
            songToFile.put(fileToSong.get(file), file);
            //sequences.add(fileToSong.get(file).getSequence());
        }
    }

    @Override
    public File toFile(Song song) {
        return songToFile.get(song);
    }

    @Override
    public Song toSong(File file) {
        return fileToSong.get(file);
    }

    @Override
    public Boolean add(File file) {
        fileToSong.put(file, file2SongConverter.convert(file));
        update();
        return true;
    }

    @Override
    public Boolean remove(File file) {
        if (!fileToSong.containsKey(file))
            return false;
        fileToSong.remove(file);
        update();
        return true;
    }

    @Override
    public Boolean addAll(List<File> fileList) {
        for (File file : fileList) {
            fileToSong.put(file, file2SongConverter.convert(file));
        }
        update();
        return true;
    }

    @Override
    public Boolean removeAll(List<File> fileList) {
        boolean allPresent = true;
        boolean allAbsent = true;
        for (File file : fileList) {
            if (!fileToSong.containsKey(file)) {
                allPresent = false;
                continue;
            }
            fileToSong.remove(file);
            allAbsent = false;
        }
        if (!allAbsent)
            update();
        return allPresent;
    }

    @Override
    public List<Song> selectSongsByIndices(List<Integer> indices) {
        List<Song> songsToSelect = new ArrayList<>();
        for (Integer integer : indices) {
            songsToSelect.add(songs.get(integer));
        }
        return songsToSelect;
    }

    @Override
    public List<List<Character>> getAllSequences() {
        return sequences;
    }

    private void update() {
        files = new ArrayList<>(fileToSong.keySet());
        songs = new ArrayList<>(fileToSong.values());
        songToFile = new HashMap<>();
        for (File file : files) {
            songToFile.put(fileToSong.get(file), file);
            //sequences.add(fileToSong.get(file).getSequence());
        }
    }


    /**
     * 选取模拟器中的的音乐文件
     */
    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        MainActivity.mainActivity.startActivityForResult(intent, Constants.READ_REQUEST_CODE);
    }

    /**
     * 将歌曲加入选中歌单
     */

    Uri urithis;
    int ChoiceList;

    public void AddToSongListDialog(final Uri uri){
        ArrayList<String> items= songListManger.getAllNameOfList();
        final String[] itemArray = new String[items.size()];
        items.toArray(itemArray);
        ChoiceList = 0;//默认第一个歌单
        urithis=uri;
        //跳出加入歌单对话框
        final AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(MainActivity.mainActivity);

        singleChoiceDialog.setTitle("加入歌单");
        //选择歌单
        singleChoiceDialog.setSingleChoiceItems(itemArray, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChoiceList = which;
                    }
                });

        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
            String path=new String();
            String name=new String();
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获得歌曲名字和绝对路径，初始化一个歌，并放进歌单
                        path=getPathByUri4kitkat(MainActivity.mainActivity,uri);
                        name=getSongName(uri);
                       songListManger.getAllList().get(ChoiceList).add(new Song(path,name));
                       Toast.makeText(MainActivity.mainActivity,"添加成功", Toast.LENGTH_SHORT).show();
                       if(dashboardView.getMytab().getSelectedTabPosition()==ChoiceList){
                            dashboardView.addsong_update(songListManger.getSongListAtIndex(ChoiceList));
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    //以下是我百度到的对URI的处理函数们，我对他的机制不是完全掌握，不要乱动。
    //但有挺多有用的信息。

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null); } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore(and general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String getSongName(Uri uri) {
        Cursor cursor =MainActivity.mainActivity.getContentResolver()
                .query(uri, null, null, null, null, null);
        cursor.moveToFirst();
        String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        cursor.close();
        return displayName;
    }
}






