package com.example.win.easy.repository;

import com.example.win.easy.repository.web.BackendResourceWebService;

import lombok.AllArgsConstructor;
import lombok.Data;

public class LoginManager {

    private static boolean isLogining;
    private static LoginStateHolder stateHolder;
    private static BackendResourceWebService backendResourceWebService;

    public static boolean hasLogin(){
        return stateHolder!=null;
    }

    public static String getCurrentUid(){
        return stateHolder==null?null:stateHolder.getUid();
    }
    public static boolean isLogining(){
        return isLogining;
    }

    public static void init(BackendResourceWebService _backendResourceWebService){
        isLogining=false;
        stateHolder=null;
        backendResourceWebService=_backendResourceWebService;
    }

    public static void loginByPhone(String phone,String password){
        /*
          TODO 发起异步网络请求，获取uid
         */
        String uid="";
        stateHolder=new LoginStateHolder(phone,password,uid,LoginType.Phone);
    }

    public static void loginByEmail(String email,String password){
        /*
          TODO 发起异步网络请求，获取uid
         */
        String uid="";
        stateHolder=new LoginStateHolder(email,password,uid,LoginType.Email);
    }

    private LoginManager(){ }

    @AllArgsConstructor
    @Data
    private static class LoginStateHolder{
        private String account;
        private String password;
        private String uid;
        private LoginType loginType;
    }

    public enum LoginType{
        Phone,
        Email
    }

}
