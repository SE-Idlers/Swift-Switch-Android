package com.example.win.easy.persistence;

import com.example.win.easy.song.Song;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

//阿里巴巴的fastjson框架
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FileSongMapConfigurationPersistence implements ConfigurationPersistence<Map<File, Song>>{

    static String fileDir="/SwiftSwitch/src/MapOfFileAndSong.json";
    public String getSDPath(){
        //更改权限的代码暂时放在这里
        if (ActivityCompat.checkSelfPermission(MainActivity.mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.mainActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            return;
        }

        //存储路径
        String sdpath= Environment.getExternalStorageDirectory();
        return sdpath;
    }

    public String toJsonString(Map<File, Song> entity){
        String jsonStr=JSON.toJSONString(entity);
        return jsonStr;
    }

    @Override
    public void save(Map<File, Song> entity)  {
        String jsonStr=toJsonString(entity);
        try{
            FileWriter fw=new FileWriter(getSDPath()+fileDir);
            PrintWriter out=new PrintWriter(fw);
            out.write(jsonStr);
            out.println();//通过写入行分隔符字符串终止当前行。
            fw.close();
            out.close();
        }catch (IOException e){
            e.printTrackTrace();
        }
    }

    @Override
    public Map<File, Song> load() {
        File file=new File(getSDPath()+fileDir);
        BufferedReader reader=null;
        String content="";
        try{
            reader=new BufferedReader(new FileReader(file));
            String tempString =null;
            while((tempString=reader.readLine())!=null){
                content=content+tempString;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        Map<File,Song> map=JSONObject.parseObject(content,Map.class);
        return map;
    }
}
