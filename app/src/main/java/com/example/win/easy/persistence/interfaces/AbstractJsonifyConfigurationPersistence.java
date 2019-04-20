package com.example.win.easy.persistence.interfaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.example.win.easy.activity.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractJsonifyConfigurationPersistence<T> implements ConfigurationPersistence<T> {

    private static String fileDir;

    public AbstractJsonifyConfigurationPersistence(String fileDir){
        this.fileDir=fileDir;
    }

    public String getSDPath(){
        //更改权限的代码暂时放在这里
        if (ActivityCompat.checkSelfPermission(MainActivity.mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.mainActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            return null;
        }

        //存储路径
        File sdpath= Environment.getExternalStorageDirectory();
        return sdpath.getAbsolutePath();
    }

    protected abstract String toJsonString(T entity);
    protected abstract T fromJsonString(String json);
    protected abstract void writeEmptyObject();

    @Override
    public void save(T entity) {
        String jsonStr=toJsonString(entity);
        try{
            FileWriter fw=new FileWriter(getSDPath()+fileDir);
            PrintWriter out=new PrintWriter(fw);
            out.write(jsonStr);
            out.println();//通过写入行分隔符字符串终止当前行。
            fw.close();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public T load() {
        File file=new File(getSDPath()+fileDir);
        if(!file.exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeEmptyObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
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
        T entity= fromJsonString(content);
        return entity;
    }
}
