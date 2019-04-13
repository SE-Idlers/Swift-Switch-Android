package com.example.win.easy.persistence;

import com.example.win.easy.persistence.GlobalConfiguration;

import org.json.JSCNArray;import org.json.JSCNArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Field;
import java.lang.NoSuchFieldExcption;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;

public class GlobalConfiguraionPersistence implements ConfigurationPersistence<GlobalConfiguraion> {

    /*
    * 缺少单独一条配置更改的接口
    * */


    /
    /*
    GlobalConfiguration globalConfiguration;//配置
    public void init(GlobalConfiguration configurationInst){
        //初始化GlobalConfiguration
        globalConfiguration=configurationInst;
    }
     */
    static String fileDir="/SwiftSwitch/src/globalConfiguration.json";
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


    public JSONObject toJson(Object toJson_obj)throws JSONException,NoSuchFieldExcption{

        JSONObject jsonObject=new JSONObject(toJson_obj);
        return jsonObject;

        /*
        //Object globalConfig_obj=(Object)globalConfiguration;//转为Object
        Field[] fields=toJson_obj.getClass().getDeclaredFields();//获取类的全部属性
        JSONObject jsonArray=new JSONArray();
        for(int i =0,len=fields.length;i<len;i++){
            try{//修改属性的访问权限
                boolean accessFlag=fields[i].isAccessible();//保存属性的访问权限
                fields[i].setAccessible(true);//设置为可访问
                try{//获取属性，并存入json
                    String varName=fields[i].getSimpleName();//获取不带包名的简单类名
                    String varValue=fields[i].get(globalConfig_obj).toString();//属性
                    try{
                        JSONObject jsonObject=jsonObject.put(varName,varValue);//全部转为String存储，可能会有问题
                        jsonArray.put(jsonObject);
                    }catch(JSONException e){
                        throw new RuntimeException("getJson(): JSONException: "+e);
                    }
                }catch (NoSuchFieldExcption e){
                    throw new RuntimeException("getJson(): NoSuchFieldExcption: "+e);
                }
                fields[i].setAccessible(accessFlag);//恢复访问权限
            }catch (RuntimeException e){
               e.printStackTrace();
            }
        }
        return jsonObject;
         */
    }



    @Override
    public void save(GlobalConfiguration entity)throws IOException {
        JSONObject json=toJson(entity);

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
    public GlobalConfiguration load()throws JOSNExcption {
        String sdpath=getSDPath();
        File file=new File(sdpath+fileDir);
        String content=FileUtils.readFileToString(file);
        GlobalConfiguration globalConfig=JSON.parseObject(content,GlobalConfiguration.class);
        return globalConfig;

       /*
        JSONArray json=new JSONOArray(content);

        GlobalConfiguration toJson_obj=new GlobalConfiguration();
        Field[] fields=toJson_obj.getClass().getDeclaredFields();//获取类的全部属性
        for(int i =0,len=fields.length;i<len;i++){
            try{//修改属性的访问权限
                boolean accessFlag=fields[i].isAccessible();//保存属性的访问权限
                fields[i].setAccessible(true);//设置为可访问
                try{//获取属性，并存入json
                    String varName=fields[i].getSimpleName();//获取属性名的String
                    JSONOBject json=JSONArray(i);
                    Class c=fields[i].getType();//获得属性类型
                    fields[i].set(toJson_obj,JSON.parseObject(json,c));
                    }catch (JOSNExcption e){
                    throw new RuntimeException("getJson(): NoSuchFieldExcption: "+e); }
            }
/*
*                 String c_str=c.toString();
                boolean stored=false;
                switch(c_str){
                    case "String":

                        stored=true;
                        break;
                    case "Integer":
                        fields[i].set(toJson_obj,json.getInt(varName));
                        stored=true;
                        break;
                    case "Double":
                        stored=true;
                        break;
                    case"Character":
                        stored=true;
                        break;
                    case"Boolean":
                        stored=true;
                        break;
                    case "Long":
                        stored=true;
                        break;
                    case"Float":
                        stored=true;
                        break;
                    case"Short":
                        stored=true;
                        break;
                    default:
                        break;
                    fields[i].set(toJson_obj,varValString);//取决于具体字段是什么
                    // 有问题啊，又不会仅仅是String
*

        fields[i].setAccessible(accessFlag);//恢复访问权限
    }catch (RuntimeException e){
        e.printStackTrace();
    }
}
        return toJson_obj;
        */

    }
}
