package com.example.win.easy.repository.db;

import androidx.room.TypeConverter;

import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.song.DataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 持久化类型转换器<br/>
 * 部分类型Room并不知道该以何种形式存储，提供一个类型转换器供{@link OurDatabase} 使用
 * @see OurDatabase
 */
public class CustomTypeConverters {

    //List<Character>  <==>  String
    @TypeConverter
    public static List<Character> string2characterList(String string){
        List<Character> characterList=new ArrayList<>();
        if(string!=null){
            int length=string.length();
            for (int index=0;index<length;index++)
                characterList.add(string.charAt(index));
        }
        return characterList;
    }

    @TypeConverter
    public static String characterList2string(List<Character> characterList){
        StringBuilder string= new StringBuilder();
        if(characterList!=null)
            for (Character character:characterList)
                string.append(character);
        return string.toString();
    }

    //DataSource  <==>  String
    @TypeConverter
    public static DataSource string2dataSource(String string){
        return string==null?null:DataSource.valueOf(string);
    }

    @TypeConverter
    public static String dataSource2string(DataSource dataSource){
        return dataSource==null?null:dataSource.toString();
    }

    //Date  <==>  Long
    @TypeConverter
    public static Date long2date(Long _long){
        return _long==null?null:new Date(_long);
    }

    @TypeConverter
    public static Long date2long(Date date){
        return date==null?null:date.getTime();
    }
}
