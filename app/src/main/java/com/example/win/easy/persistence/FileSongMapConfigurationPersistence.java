package com.example.win.easy.persistence;

import com.example.win.easy.song.Song;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSCNArray;

import org.apache.commons.io.FileUtils;

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

    public JSONObject toJson(Map<File, Song> entity){

        JSONObject jsonObject=new JSONObject(entity);
        return jsonObject;
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
    public void save(Map<File, Song> entity)throws IOException  {
        JSONArray jsonArray=toJson(entity);

        String sdpath=getSDPath();
        File file=new File(sdpath+fileDir);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw=new FileWriter(file.getAbsoluteFIle());
        BufferedWriter bw=new BufferedWriter(fw);
        try {
            json.write(bw);
            bw.close();
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<File, Song> load() {
        String sdpath=getSDPath();
        File file=new File(sdpath+fileDir);
        String content=FileUtils.readFileToString(file);
        Map<File,Song> map=JSONObject.parseObject(content,Map.class);
        return map;
    }
}
