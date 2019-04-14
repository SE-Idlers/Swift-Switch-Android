package com.example.win.easy.persistence;
import java.io.IOException;
import org.json.JSONException;

public interface ConfigurationPersistence<T>{

    void save(T entity)throws IOException;;

    T load()throws JOSNExcption;

}
