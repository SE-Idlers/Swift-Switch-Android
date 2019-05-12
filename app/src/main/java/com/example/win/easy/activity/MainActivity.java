package com.example.win.easy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class MainActivity extends AppCompatActivity {
    public static MainActivity mainActivity;
    private QMUIGroupListView mGroupListContact;
    private QMUITopBar qmuiTopBar;
    private QMUICommonListItemView allSongItem;
    private QMUICommonListItemView allSongListItem;
    private ImageButton addSongBtn;
    private ImageButton addSongListBtn;
    private ImageButton cloud;
    private ImageButton music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=this;
        //加载类
        SwiftSwitchClassLoader.init();
        //初始化界面
        setContentView(R.layout.main);
        bindView();
        initTopBar();
        initItems();
        initButtons();
        //开启锁屏后台服务
        startService(new Intent(this,MyService.class));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

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
        allSongItem.setOnClickListener(v -> Toast.makeText(getApplicationContext(),"点击歌单 ",Toast.LENGTH_LONG).show());
        allSongListItem.addAccessoryCustomView(addSongListBtn);
        allSongListItem.setOnClickListener(v -> Toast.makeText(getApplicationContext(),"点击歌曲 ",Toast.LENGTH_LONG).show());
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
            Toast.makeText(getApplicationContext(),"歌曲 ",Toast.LENGTH_LONG).show();
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
}
