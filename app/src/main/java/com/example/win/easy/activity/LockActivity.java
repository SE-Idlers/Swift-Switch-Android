package com.example.win.easy.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.win.easy.Constants;
import com.example.win.easy.DashBoard;
import com.example.win.easy.DialogTool;
import com.example.win.easy.R;
import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.gesture.GestureProxy;
import com.example.win.easy.persistence.component.FileSongMapConfigurationPersistence;
import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.TemporaryListGenerator;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.interfaces.SearchingView;
import com.example.win.easy.view.interfaces.SongListView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;
import java.util.List;

public class LockActivity extends AppCompatActivity implements SongListView, SearchingView {

    public static LockActivity lockActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lockActivity =this;
        SwiftSwitchClassLoader.init();
        RecognitionProxyWithFourGestures.getInstance().setAssetManager(getAssets());
        GestureProxy gestureProxy=GestureProxy.getInstance();
        getSupportFragmentManager();

        /////////////////////////
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        final ImageButton btnPrevious=LockActivity.lockActivity.findViewById(R.id.previous);
        final ImageButton btnNext=LockActivity.lockActivity.findViewById(R.id.next);

        //播放、暂停
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayManager.isPlaying()){
                    displayManager.pause();
                    btnPause.setImageResource(android.R.drawable.ic_media_play);
                }else {
                    displayManager.start();
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        //前一首
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!displayManager.isPlaying())
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                displayManager.previous();
            }
        });
        //后一首
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!displayManager.isPlaying())
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                displayManager.next();
            }
        });

        ///////////////////////////////////////////
        Button btnAddSong = LockActivity.lockActivity.findViewById(R.id.AddSong);
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });
        Button buttonSeeAllSongs=LockActivity.lockActivity.findViewById(R.id.SeeAllSongs);
        buttonSeeAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockActivity.lockActivity.createDialogSeeAllSongs();
            }
        });

        ///////////////////////////////////////////
        Button btnAddSongList = LockActivity.lockActivity.findViewById(R.id.AddSongList);
        Button btnSeeSongList = LockActivity.lockActivity.findViewById(R.id.SeeSongList);
        btnAddSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockActivity.lockActivity.createDialogAddSong();
            }
        });
        btnSeeSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognitionProxyWithFourGestures.getInstance().clear();
                LockActivity.lockActivity.createDialogSeeSongList();
            }
        });

      //  dashboardView.init();

       //File relative=new File("/ttt");
        //        File[] files=relative.listFiles();
        //
        //SongListMangerImpl songListManger=SongListMangerImpl.getInstance();
//      SongManagerImpl.getInstance().addAll(new ArrayList<>(Arrays.asList(files)));
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SongListManager songListManager=SongListMangerImpl.getInstance();
//        SongManager songManager= SongManagerImpl.getInstance();
//        System.out.println("After switch all song lists: "+songListManager.getAllSongLists());
//        System.out.println("After switch all songs: "+songManager.getMap());
//        SongListConfigurationPersistence.getInstance().save(songListManager.getAllSongLists());
//        FileSongMapConfigurationPersistence.getInstance().save(songManager.getMap());
//        System.out.println(SongListConfigurationPersistence.getInstance().load());
//        System.out.println(FileSongMapConfigurationPersistence.getInstance().load());
    }

    /**
     * 添加歌曲文件且选取好要添加的歌曲文件后，会触发该函数，该函数用于跳转，让用户选择添加歌曲到哪一个歌单
     * @param requestCode 触发该函数的intent的请求码
     * @param resultCode 对该intent响应的响应码
     * @param resultData 如果成功操作，返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){

        //从查看文件夹Activity,即请求码为READ_REQUEST_CODE返回
        if (requestCode ==Constants.READ_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                &&resultData!=null) {
            Uri uri = resultData.getData();
            LockActivity.lockActivity.createDialogAddSongToSongList(uri);
        }
    }

    private TemporaryListGenerator tool = TemporaryListGenerator.getInstance();
    private SongListManager songListManager=SongListMangerImpl.getInstance();
    private DashBoard dashBoard = LockActivity.lockActivity.findViewById(R.id.dash_board);

    @Override
    public void update(Song song) {
        //获取歌曲出现过的所有歌单
        List<SongList> appearanceLists =songListManager.appearanceListsOf(song);
        //设置dashBoard
        dashBoard.setup(appearanceLists, DashBoard.DashBoardType.SwitchSongList);
    }

    @Override
    public void update(List<Integer> sortedIndices) {
        //获得按来源得到的结果列表
        List<SongList> candidates = tool.toSearchResult(sortedIndices);
        //设置dashBoard
        dashBoard.setup(candidates, DashBoard.DashBoardType.SelectingSong);
    }


    ///////////////////////////////////////////////
    private DisplayManager displayManager=DisplayManagerImpl.getInstance();

    public void updateBeginView(){
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        btnPause.setImageResource(android.R.drawable.ic_media_play);
    }
    public void updatePauseView(){
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        btnPause.setImageResource(android.R.drawable.ic_media_pause);
    }

    ///////////////////////////////////////////////
    ///////////////////////////////////////////////
    private SongManager songManager= SongManagerImpl.getInstance();

    public void createDialogSeeAllSongs(){
        DialogTool.createMenuDialog(
                LockActivity.lockActivity,
                "所有歌曲",
                songManager.getNamesOfAllSongs().toArray(new String[0]),
                null,
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    /**
     * 展示一个页面，让用户选择将音乐添加到哪一个歌单
     * @param uri 将添加的音乐文件的URI
     */
    public void createDialogAddSongToSongList(final Uri uri){
        final QMUIDialog.MultiCheckableDialogBuilder builder=new QMUIDialog.MultiCheckableDialogBuilder(LockActivity.lockActivity);
        DialogTool.createMultiCheckDialog(
                builder,
                "添加到歌单..",
                songListManager.getNameOfAllSelfDefinedSongLists().toArray(new String[0]),
                null,
                "确定",
                new AddSongToSongListListener(uri,builder),
                null,
                null,
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    class AddSongToSongListListener implements QMUIDialogAction.ActionListener {

        private Uri uri;
        private QMUIDialog.MultiCheckableDialogBuilder builder;
        private SongManager songManager=SongManagerImpl.getInstance();
        private SongListManager songListManager=SongListMangerImpl.getInstance();
        AddSongToSongListListener(Uri uri, QMUIDialog.MultiCheckableDialogBuilder builder){
            this.uri=uri;
            this.builder=builder;
        }
        @Override
        public void onClick(QMUIDialog dialog, int index) {
            File songFile=new File(getPathByUri4kitkat(builder.getBaseContext(),uri));
            songManager.add(songFile);
            Toast.makeText(builder.getBaseContext(),"添加成功", Toast.LENGTH_SHORT).show();
            int[] indices=builder.getCheckedItemIndexes();
            for (int checkedIndex:indices)
                songListManager.getAllSongLists().get(checkedIndex+1).add(songManager.toSong(songFile));
            FileSongMapConfigurationPersistence.getInstance()
                    .save(SongManagerImpl.getInstance().getMap());
            SongListConfigurationPersistence.getInstance()
                    .save(SongListMangerImpl.getInstance().getAllSongLists());
            dialog.dismiss();
        }
    }

    /**
     * 跳转到文件浏览页面，用于选择要添加的音乐文件
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        LockActivity.lockActivity.startActivityForResult(intent, Constants.READ_REQUEST_CODE);
    }

    //以下是我百度到的对URI的处理函数们，我对他的机制不是完全掌握，不要乱动。
    //但有挺多有用的信息。

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getPathByUri4kitkat(final Context context, final Uri uri) {
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
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
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
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    /**
     * 查看歌单的对话框
     */
    private void createDialogSeeSongList(){
        DialogTool.createMenuDialog(
                LockActivity.lockActivity,
                "所有歌单",
                songListManager.getNameOfAllSongLists().toArray(new String[0]),
                new CheckSongListListener(),
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    /**
     * 创建歌单的对话框
     */
    private void createDialogAddSong(){
        final EditText editText = new EditText(LockActivity.lockActivity);
        new AlertDialog.Builder(LockActivity.lockActivity)
                .setTitle("歌单名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        String songListName=editText.getText().toString();
                        if (songListManager.containsSongListWithName(songListName)){
                            Toast.makeText(LockActivity.lockActivity.getApplicationContext(),"歌单 "+ songListName+"已存在",Toast.LENGTH_LONG).show();
                            return;
                        }
                        songListManager.add(new SongList(songListName));
                        Toast.makeText(LockActivity.lockActivity.getApplicationContext(),"歌单 "+ songListName+"已创建",Toast.LENGTH_LONG).show();
                        SongListConfigurationPersistence.getInstance()
                                .save(SongListMangerImpl.getInstance().getAllSongLists());
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    class CheckSongListListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            SongList songList=SongListMangerImpl.getInstance().getAllSongLists().get(which);
            DialogTool.createMenuDialog(
                    LockActivity.lockActivity,
                    songList.getName(),
                    songList.getSongNames().toArray(new String[0]),null,
                    com.qmuiteam.qmui.R.style.QMUI_Dialog
            );
        }
    }
}
