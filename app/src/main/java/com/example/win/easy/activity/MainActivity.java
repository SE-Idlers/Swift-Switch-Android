package com.example.win.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mainActivity=this;
        SwiftSwitchClassLoader.init();
        setContentView(R.layout.main);
        QMUIGroupListView mGroupListContact=findViewById(R.id.listview);
        QMUITopBar qmuiTopBar=findViewById(R.id.topbar);

        QMUICommonListItemView listItemOne = mGroupListContact.createItemView("我的歌曲");
        QMUICommonListItemView listItemtwo = mGroupListContact.createItemView("我的歌单");

        listItemOne.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        listItemtwo.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);


        ImageButton add=new ImageButton(getApplicationContext());
        add.setImageResource(R.drawable.ic_action_name);

        ImageButton addtwo=new ImageButton(getApplicationContext());
        addtwo.setImageResource(R.drawable.ic_action_name);

        ImageButton cloud=new ImageButton(getApplicationContext());
        cloud.setImageResource(R.drawable.ic_action_cloud);

        ImageButton music=new ImageButton(getApplicationContext());
        music.setImageResource(R.drawable.ic_action_music);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"歌曲 ",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                startActivityForResult(intent, Constants.READ_REQUEST_CODE);
            }
        });

        addtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"歌单 ",Toast.LENGTH_LONG).show();
                 SongListManager songListManager=  SongListMangerImpl.getInstance();
                final EditText editText = new EditText(MainActivity.mainActivity);
                new AlertDialog.Builder(MainActivity.mainActivity)
                        .setTitle("歌单名称")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                String songListName=editText.getText().toString();
                                if (songListManager.containsSongListWithName(songListName)){
                                    Toast.makeText(MainActivity.mainActivity.getApplicationContext(),"歌单 "+ songListName+"已存在",Toast.LENGTH_LONG).show();
                                    return;
                                }
                                songListManager.add(new SongList(songListName));
                                Toast.makeText(MainActivity.mainActivity.getApplicationContext(),"歌单 "+ songListName+"已创建",Toast.LENGTH_LONG).show();
                                SongListConfigurationPersistence.getInstance()
                                        .save(SongListMangerImpl.getInstance().getAllSongLists());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        listItemOne.addAccessoryCustomView(add);

        listItemOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"点击歌单 ",Toast.LENGTH_LONG).show();
            }
        });

        listItemtwo.addAccessoryCustomView(addtwo);

        listItemtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"点击歌曲 ",Toast.LENGTH_LONG).show();
            }
        });

        QMUIGroupListView.newSection(this)
                .addItemView(listItemOne,null)
                .addItemView(listItemtwo,null)
                .addTo(mGroupListContact);

        qmuiTopBar.setTitle("我的");

        qmuiTopBar.addRightImageButton(R.drawable.ic_action_music,music.getId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"音乐 ",Toast.LENGTH_LONG).show();

            }
        });
        qmuiTopBar.addLeftImageButton(R.drawable.ic_action_cloud,cloud.getId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"云 ",Toast.LENGTH_LONG).show();
            }
        });
        startService(new Intent(this,MyService.class));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

    }
}
