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
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.example.win.easy.ActivityHolder;
import com.example.win.easy.Constants;
import com.example.win.easy.DialogTool;
import com.example.win.easy.R;
import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.viewmodel.SimpleViewModel;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QMUIGroupListView mGroupListContact;
    private QMUITopBar qmuiTopBar;
    private QMUICommonListItemView allSongItem;
    private QMUICommonListItemView allSongListItem;
    private ImageButton addSongBtn;
    private ImageButton addSongListBtn;
    private ImageButton cloud;
    private ImageButton music;
    private SimpleViewModel viewModel;
    private LiveData<Integer> songAmount;
    private LiveData<Integer> songListAmount;
    private LiveData<List<SongPojo>> allSongs;
    private LiveData<List<SongListPojo>> allSongLists;
    private List<LiveData<List<SongPojo>>> recordTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHolder.update(this);
        //加载类
        SwiftSwitchClassLoader.init();
        SwiftSwitchClassLoader.setOurDatabase(Room.databaseBuilder(getApplicationContext(), OurDatabase.class,"ourDatabase").build());
        //初始化界面
        initView();
        //开启锁屏后台服务
        startService(new Intent(this,MyService.class));
        //注册数据监听
        registerData();
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
            createDialogAddSongToSongList(uri);
        }
    }

    /**
     * 查看所有歌曲的对话框
     */
    public void createDialogSeeAllSongs(){
        List<String> songNames=new ArrayList<>();
        if (allSongs.getValue()!=null)
            for (SongPojo songPojo:allSongs.getValue())
                songNames.add(songPojo.getName());
        DialogTool.createMenuDialog(
                this,
                "所有歌曲",
                songNames.toArray(new String[0]),
                null,
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    /**
     * 添加歌曲到歌单的对话框
     * @param uri 将添加的音乐文件的URI
     */
    public void createDialogAddSongToSongList(final Uri uri){
        if (allSongLists.getValue()!=null){
            List<String> songListNames=new ArrayList<>();
            for (SongListPojo songListPojo:allSongLists.getValue())
                songListNames.add(songListPojo.getName());
            final QMUIDialog.MultiCheckableDialogBuilder builder=new QMUIDialog.MultiCheckableDialogBuilder(this);
            DialogTool.createMultiCheckDialog(
                    builder,
                    "添加到歌单..",
                    songListNames.toArray(new String[0]),
                    null,
                    "确定",
                    new AddSongToSongListListener(uri,builder),
                    null,
                    null,
                    com.qmuiteam.qmui.R.style.QMUI_Dialog
            );
        }else
            viewModel.insert(new SongPojo(new File(getPathByUri4kitkat(this,uri))));
    }

    /**
     * 查看歌单的对话框
     */
    private void createDialogSeeSongList(){
        List<String> songListNames=new ArrayList<>();
        if(allSongLists.getValue()!=null)
            for (SongListPojo songListPojo:allSongLists.getValue())
                songListNames.add(songListPojo.getName());
        DialogTool.createMenuDialog(
                this,
                "所有歌单",
                songListNames.toArray(new String[0]),
                null,
//                new CheckSongListListener(),
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    /**
     * 初始化界面及其相应监听
     */
    private void initView(){
        setContentView(R.layout.main);
        bindView();
        initTopBar();
        initItems();
        initButtons();
    }


    /**
     * 绑定视图
     */
    private void bindView(){
        mGroupListContact=findViewById(R.id.listview);
        qmuiTopBar=findViewById(R.id.topbar);
        allSongItem = mGroupListContact.createItemView("我的歌曲");
        allSongListItem = mGroupListContact.createItemView("我的歌单");
        addSongBtn =new ImageButton(getApplicationContext());
        addSongListBtn =new ImageButton(getApplicationContext());
        cloud=new ImageButton(getApplicationContext());
        music=new ImageButton(getApplicationContext());
    }
    /**
     * 初始化Item
     */
    private void initItems(){
        allSongItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        allSongListItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        allSongItem.addAccessoryCustomView(addSongBtn);
        allSongItem.setOnClickListener(v -> createDialogSeeAllSongs());
        allSongListItem.addAccessoryCustomView(addSongListBtn);
        allSongListItem.setOnClickListener(v -> createDialogSeeSongList());
        QMUIGroupListView.newSection(this)
                .addItemView(allSongItem,null)
                .addItemView(allSongListItem,null)
                .addTo(mGroupListContact);
    }

    /**
     * 初始化按钮
     */
    private void initButtons(){
        addSongBtn.setImageResource(R.drawable.ic_action_name);
        addSongListBtn.setImageResource(R.drawable.ic_action_name);
        addSongBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            startActivityForResult(intent, Constants.READ_REQUEST_CODE);
        });
        addSongListBtn.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"歌单 ",Toast.LENGTH_LONG).show();
            SongListManager songListManager=  SongListMangerImpl.getInstance();
//            final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getBaseContext());
//            builder.create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
//            builder.setTitle("新建歌单")
//                    .setPlaceholder("您的新歌单叫什么呢")
//                    .setInputType(InputType.TYPE_CLASS_TEXT)
//                    .addAction("取消", (dialog, index) -> dialog.dismiss())
//                    .addAction("确定", (dialog, index) -> {
//                        String songListName = builder.getEditText().getText().toString();
//                        if (songListName.length()> 0) {
//                            if (songListManager.containsSongListWithName(songListName)){
//                                Toast.makeText(getApplicationContext(),"歌单 "+ songListName+"已存在",Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                            songListManager.add(new SongList(songListName));
//                            Toast.makeText(getApplicationContext(),"您的新歌单 :"+ songListName+"已创建",Toast.LENGTH_LONG).show();
//                            SongListConfigurationPersistence.getInstance()
//                                    .save(SongListMangerImpl.getInstance().getAllSongLists());
//                            dialog.dismiss();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "歌单名字不能为空哦", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog)
//                    .show();
        });
    }

    /**
     * 初始化顶栏
     */
    private void initTopBar(){
        cloud.setImageResource(R.drawable.ic_action_cloud);
        music.setImageResource(R.drawable.ic_action_music);
        qmuiTopBar.setTitle("我的");
        qmuiTopBar.addRightImageButton(R.drawable.ic_action_music,music.getId()).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"音乐 ",Toast.LENGTH_LONG).show());
        qmuiTopBar.addLeftImageButton(R.drawable.ic_action_cloud,cloud.getId()).setOnClickListener(v -> Toast.makeText(getApplicationContext(),"云 ",Toast.LENGTH_LONG).show());
    }

    /**
     * 注册MVVM数据
     */
    private void registerData(){
        viewModel= ViewModelProviders.of(this).get(SimpleViewModel.class);
        songAmount=viewModel.getSongAmount();
        songListAmount=viewModel.getSongListAmount();
        allSongs=viewModel.getAllSongs();
        allSongLists=viewModel.getAllSongLists();
        songAmount.observe(this, integer -> {
            allSongItem.setDetailText(integer.toString());
        });
        songListAmount.observe(this,integer -> {
            allSongListItem.setDetailText(integer.toString());
        });
        allSongs.observe(this,songPojos -> {});
        allSongLists.observe(this, songListPojos -> { });
        recordTable=new ArrayList<>();
        if (allSongLists.getValue()!=null)
            for (SongListPojo songListPojo:allSongLists.getValue()){
                LiveData<List<SongPojo>> record=viewModel.getAllSongsForSongList(songListPojo);
                record.observe(this, songPojos -> { });
                recordTable.add(record);
            }
    }

    /**
     * 添加歌曲到歌单的监听器类
     */
    class AddSongToSongListListener implements QMUIDialogAction.ActionListener {

        private Uri uri;
        private QMUIDialog.MultiCheckableDialogBuilder builder;
        private SongManager songManager= SongManagerImpl.getInstance();
        private SongListManager songListManager=SongListMangerImpl.getInstance();
        AddSongToSongListListener(Uri uri, QMUIDialog.MultiCheckableDialogBuilder builder){
            this.uri=uri;
            this.builder=builder;
        }
        @Override
        public void onClick(QMUIDialog dialog, int index) {
            File songFile=new File(getPathByUri4kitkat(builder.getBaseContext(),uri));
            List<SongListPojo> songListPojos=new ArrayList<>();
            int[] indices=builder.getCheckedItemIndexes();
            for (int checkedIndex:indices)
                songListPojos.add(allSongLists.getValue().get(checkedIndex));
            viewModel.insertNewSongAndToSongLists(new SongPojo(songFile),songListPojos);
            Toast.makeText(builder.getBaseContext(),"添加成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    /**
     * 选择歌单的监听类
     */
    class CheckSongListListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            SongListPojo songListPojo=allSongLists.getValue().get(which);
            List<String> songNames=new ArrayList<>();
            for (SongPojo songPojo:recordTable.get(which).getValue())
                songNames.add(songPojo.getName());
            DialogTool.createMenuDialog(
                    getApplicationContext(),
                    songListPojo.getName(),
                    songNames.toArray(new String[0]),null,
                    com.qmuiteam.qmui.R.style.QMUI_Dialog
            );
        }
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

}
