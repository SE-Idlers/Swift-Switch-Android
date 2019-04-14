package com.example.win.easy.persistence;

//阿里巴巴的fastjson框架
//import java.lang.reflect.Field;
//import java.lang.NoSuchFieldExcption;

public class GlobalConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<GlobalConfiguration> {

    /*
    * 缺少单独一条配置更改的接口
    * */

    private static String fileDir="/SwiftSwitch/src/globalConfiguration.json";
    public GlobalConfigurationPersistence(){
        super(fileDir);
    }

    @Override
    Class<GlobalConfiguration> getClassInformation() {
        return GlobalConfiguration.class;
    }
}
