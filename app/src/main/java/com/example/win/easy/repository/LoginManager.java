package com.example.win.easy.repository;

import com.example.win.easy.repository.web.BackendResourceWebService;
import com.example.win.easy.repository.web.callback.LoginCallBack;

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
        if (isLogining)
            return;
        isLogining=true;
        backendResourceWebService.getUidByEmail(phone,password).enqueue(new LoginCallBack(LoginType.Phone));
    }

    public static void loginByEmail(String email,String password){
        if (isLogining)
            return;
        isLogining=true;
        backendResourceWebService.getUidByEmail(email,password).enqueue(new LoginCallBack(LoginType.Email));
    }

    public static void success(String uid,LoginType loginType){
        isLogining=false;
        stateHolder=new LoginStateHolder(uid,loginType);
    }

    private LoginManager(){ }

    @AllArgsConstructor
    @Data
    private static class LoginStateHolder{
        private String uid;
        private LoginType loginType;
    }

    public enum LoginType{
        Phone,
        Email
    }

}
