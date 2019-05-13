package com.example.win.easy.persistence.interfaces;

import android.os.Environment;

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
//        if (ActivityCompat.checkSelfPermission(MainActivity.mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(LockActivity.lockActivity,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
//            return null;
//        }

        //存储路径
        File sdpath= Environment.getExternalStorageDirectory();
        return sdpath.getAbsolutePath();
    }

    protected abstract String toJsonString(T entity);
    protected abstract T fromJsonString(String json);//FastJson有此接口
    protected abstract void writeEmptyObject();
    protected abstract T getEmptyInstance();

    @Override
    public void save(T entity) {
        String jsonStr = toJsonString(entity);
        File file= new File(getSDPath() + fileDir);//把地址传给file但并不打开，因此不会引发IOException
        if (!file.exists()) {
            try{
                file.getParentFile().mkdirs();//创建多级目录，mkdir只创建一级
                file.createNewFile();//创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw=null;
        try {
            fw = new FileWriter(file);
            PrintWriter out = new PrintWriter(fw);
            out.write(jsonStr);
            out.println();//通过写入行分隔符字符串终止当前行。
            fw.close();
            out.close();
        } catch (IOException e) {
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
                writeEmptyObject(); //是否需要设置SerializerFeature呢？
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        BufferedReader breader=null;
        FileReader freader=null;
        String content="";
        try{
            freader=new FileReader(file);
            breader=new BufferedReader(freader);
            String tempString =null;
            while((tempString=breader.readLine())!=null){
                content=content+tempString;
            }
            freader.close();
            breader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (freader != null ){
                try {
                    freader.close();
                } catch (IOException e1) {
                }
            }
            if(breader!=null){
                try{
                    breader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        T entity= fromJsonString(content);

        if(entity==null)//如果文件内容为空，则创建一个空白文件，同时返回一个默认内容的实例
        {
            writeEmptyObject();//创建一个空白文件
            //返回一个默认内容的实例的过程比较繁琐（几乎是将正常情况下的加载行为重复了一遍），因为泛型T难以直接获得实例
            try{
                freader=new FileReader(file);
                breader=new BufferedReader(freader);
                String tempString =null;
                while((tempString=breader.readLine())!=null){
                    content=content+tempString;
                }
                freader.close();
                breader.close();
            }catch(IOException e){
                e.printStackTrace();
            }finally {
                if (freader != null ){
                    try {
                        freader.close();
                    } catch (IOException e1) {
                    }
                }
                if(breader!=null){
                    try{
                        breader.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            entity= fromJsonString(content);
        }
        return entity;
    }
}
