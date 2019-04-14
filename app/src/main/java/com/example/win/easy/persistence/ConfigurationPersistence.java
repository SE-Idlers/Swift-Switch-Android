package com.example.win.easy.persistence;
/**
 * 将一个配置类对象持久化到本地文件，每次启动App时读取，关闭app时写入
 * @param <T> 需要持久化的对象类型
 */
public interface ConfigurationPersistence<T>{

    /**
     * 写入本地文件
     * @param entity 需要持久化的对象
     */
    void save(T entity);

    /**
     * 从文件中读取
     * @return 配置类对象
     */
    T load(Class<T> tClass);

}
