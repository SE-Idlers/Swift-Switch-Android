package com.example.win.easy.persistence;

import com.example.win.easy.song.Song;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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

        /*
        *         //拆解
        Map<Integer,File> mapKey=new HashMap<>();
        Map<Integer,File> mapVal=new HashMap<>();
        int count=1;
        for(Map.Entry<File,Song> m: entity.entrySet()){
            mapKey.put(count,m.getKey());
            mapVal.put(count,m.getValue());
        }
        //转换
        JSONObject jsonKey=new JSONObject(mapKey);
        JSONObject jsonVal=new JSONObject(mapVal);

        //组合
        JSONArray jsonArr=new JSONArray();
        jsonArr.add(jsonKey);
        jsonArr.add(jsonVal);*/

    }

    @Override
    public void save(Map<File, Song> entity)  {
        String jsonStr=toJsonString(entity);
        FileWriter fw=new FileWriter(getSDPath()+fileDir);
        PrintWriter out=new PrintWriter(fw);
        out.write(entity);
        out.println();
        fw.close();
        out.close();
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
