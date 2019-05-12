package com.example.win.easy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.win.easy.R;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
            }
        });

        addtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"歌单 ",Toast.LENGTH_LONG).show();
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
    }
}
