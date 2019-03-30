package com.example.win.easy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    final MediaPlayer mp = new MediaPlayer();
    String song_path = "";
    private GestureOverlayView gesture;
    private GestureOverlayView gesture2;
    private GestureOverlayView gesture3;
    private GestureOverlayView gesture4;
    private int currentposition;//当前音乐播放的进度
    private ArrayList<String> list;
    private File[] songFiles;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMedia();
        initGesture();
    }

    private  void initMedia(){
        //访问权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            return;
        }
        //判断是否是AndroidN以及更高的版本 N=24
        //这个我不是很懂，但不加就不行
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        list = new ArrayList<String>();   //音乐列表
        File sdpath = Environment.getExternalStorageDirectory(); //获得手机SD卡路径
        File path = new File(sdpath +"//Music//");      //获得SD卡的Music文件夹
        songFiles = path.listFiles(new MyFilter(".mp3"));//返回以.mp3结尾的文件 (自定义文件过滤)
        for (File file : songFiles) {
            list.add(file.getAbsolutePath());   //获取文件的绝对路径,存入音乐列表中
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_single_choice,
                list);
        ListView li = (ListView) findViewById(R.id.listView1);
        li.setAdapter(adapter);
        li.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final ImageButton btnpause = (ImageButton) findViewById(R.id.start);
        final Button btnenter = (Button) findViewById(R.id.enter);

        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mp.isPlaying()) btnpause.setImageResource(android.R.drawable.ic_media_pause);
                song_path = ((TextView) view).getText().toString();
                currentposition = position;
                changeMusic(currentposition);
                try {
                    mp.reset();    //重置
                    mp.setDataSource(song_path);
                    mp.prepare();     //准备
                    mp.start(); //播放
                } catch (Exception e) {
                }
            }
        });

        btnenter.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(MainActivity.this,AppListOne.class);
                                            startActivity(intent);
                                        }
                                    }
        );

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song_path.isEmpty())
                    Toast.makeText(getApplicationContext(), "先选收歌曲先听听", Toast.LENGTH_SHORT).show();
                if (mp.isPlaying()) {
                    mp.pause();  //暂停
                    //isStop = true;
                    btnpause.setImageResource(android.R.drawable.ic_media_play);
                } else if (!song_path.isEmpty()) {
                    mp.start();   //继续播放
                    //isStop = false;
                    btnpause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        //上一曲和下一曲
        final ImageButton previous = (ImageButton) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic(--currentposition);
            }
        });
        final ImageButton next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic(++currentposition);
            }
        });
    }

    private void initGesture(){
        gesture = (GestureOverlayView) findViewById(R.id.gesture);
        gesture2 = (GestureOverlayView) findViewById(R.id.gesture2);
        gesture3 = (GestureOverlayView) findViewById(R.id.gesture3);
        gesture4 = (GestureOverlayView) findViewById(R.id.gesture4);
        gesture.setGestureColor(Color.GREEN);
        gesture.setBackgroundColor(Color.GRAY);
        gesture.setGestureStrokeWidth(5);
        gesture2.setGestureColor(Color.GREEN);
        gesture2.setBackgroundColor(Color.GRAY);
        gesture2.setGestureStrokeWidth(5);
        gesture3.setGestureColor(Color.GREEN);
        gesture3.setBackgroundColor(Color.GRAY);
        gesture3.setGestureStrokeWidth(5);
        gesture4.setGestureColor(Color.GREEN);
        gesture4.setBackgroundColor(Color.GRAY);
        gesture4.setGestureStrokeWidth(5);

        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {

                //检查写入权限
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    return;
                }

                String sdpath= Environment.getExternalStorageDirectory()+"/Pictures/";
                File path = new File(sdpath);
                File file =new File(sdpath,System.currentTimeMillis()+".jpg");

                try {
                    FileOutputStream outputStream=new FileOutputStream(file);
                    Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                    Toast.makeText(getApplicationContext(), "已存", Toast.LENGTH_SHORT).show();
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        gesture2.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {

                //检查写入权限
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    return;
                }

                String sdpath= Environment.getExternalStorageDirectory()+"/Pictures/";
                File path = new File(sdpath);
                File file =new File(sdpath,System.currentTimeMillis()+".jpg");

                try {
                    FileOutputStream outputStream=new FileOutputStream(file);
                    Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                    Toast.makeText(getApplicationContext(), "已存", Toast.LENGTH_SHORT).show();
                    outputStream.flush();
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    gesture3.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
        @Override
        public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {

            //检查写入权限
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                return;
            }

            String sdpath= Environment.getExternalStorageDirectory()+"/Pictures/";
            File path = new File(sdpath);
            File file =new File(sdpath,System.currentTimeMillis()+".jpg");

            try {
                FileOutputStream outputStream=new FileOutputStream(file);
                Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                Toast.makeText(getApplicationContext(), "已存", Toast.LENGTH_SHORT).show();
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    gesture4.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
@Override
public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {

        //检查写入权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(MainActivity.this,
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        return;
        }

        String sdpath= Environment.getExternalStorageDirectory()+"/Pictures/";
        File path = new File(sdpath);
        File file =new File(sdpath,System.currentTimeMillis()+".jpg");

        try {
        FileOutputStream outputStream=new FileOutputStream(file);
        Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
        Toast.makeText(getApplicationContext(), "已存", Toast.LENGTH_SHORT).show();
        outputStream.flush();
        outputStream.close();
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
        });

    }

    private void changeMusic(int position) {
        if (position < 0) {
            //比第一首在前一首
            currentposition = position = list.size() - 1;
        } else if (position > list.size() - 1) {
            //最后一首的下一首
            currentposition = position = 0;
        }
        song_path = songFiles[position].getAbsolutePath();

        try {
            // 切歌之前先重置，释放掉之前的资源
            mp.reset();
            // 设置播放源
            mp.setDataSource(song_path);
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mp.prepare();
            // 开始播放
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        Toast.makeText(getApplicationContext(), "退出啦", Toast.LENGTH_SHORT).show();
    }
}

