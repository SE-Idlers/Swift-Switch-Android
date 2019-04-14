package com.example.win.easy.persistence.component;

//阿里巴巴的fastjson框架
//import java.lang.reflect.Field;
//import java.lang.NoSuchFieldExcption;

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
    protected Class<GlobalConfiguration> getClassInformation() {
        return GlobalConfiguration.class;
    }
}
