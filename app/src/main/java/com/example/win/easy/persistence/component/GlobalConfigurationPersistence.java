package com.example.win.easy.persistence.component;

//阿里巴巴的fastjson框架
//import java.lang.reflect.Field;
//import java.lang.NoSuchFieldExcption;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.win.easy.persistence.GlobalConfiguration;
import com.example.win.easy.persistence.interfaces.AbstractJsonifyConfigurationPersistence;

public class GlobalConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<GlobalConfiguration> {

    /*
    * 缺少单独一条配置更改的接口
    * */

    private static String fileDir="/SwiftSwitch/src/globalConfiguration.json";
    private static GlobalConfigurationPersistence instance=new GlobalConfigurationPersistence();
    public GlobalConfigurationPersistence getInstance(){return instance;}
    private GlobalConfigurationPersistence(){
        super(fileDir);
    }

    @Override
    protected String toJsonString(GlobalConfiguration entity) {
        return JSON.toJSONString(entity);
    }

    @Override
    protected GlobalConfiguration fromJsonString(String json) {
        return JSON.parseObject(json,GlobalConfiguration.class);
    }

    @Override
    protected void writeEmptyObject() {
        save(new GlobalConfiguration());
    }
}
