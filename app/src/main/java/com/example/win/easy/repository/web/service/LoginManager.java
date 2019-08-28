package com.example.win.easy.repository.web.service;

import com.example.win.easy.factory.CallbackFactory;
import com.example.win.easy.repository.web.request.BackendResourceWebService;
import com.example.win.easy.repository.deprecated.web.__CustomCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.AllArgsConstructor;
import lombok.Data;

@Singleton
public class LoginManager {

    private BackendResourceWebService backendResourceWebService;
    private CallbackFactory callbackFactory;

    @Inject
    public LoginManager(BackendResourceWebService backendResourceWebService,
                        CallbackFactory callbackFactory){
        setLogining(false);
        setStateHolder(null);
        this.backendResourceWebService=backendResourceWebService;
        this.callbackFactory=callbackFactory;
    }

    private boolean logining;
    private LoginStateHolder stateHolder;

    public void loginByPhone(String phone,String password){
        if (logining)
            return;
        logining =true;
//        backendResourceWebService.getUidByPhone(phone,password).enqueue(createCallback(LoginType.Phone));
    }

    public void loginByEmail(String email,String password){
        if (logining)
            return;
        logining =true;
//        backendResourceWebService.getUidByEmail(email,password).enqueue(createCallback(LoginType.Email));
    }

    public void fetchAllByUid(String uid) {
        //根据Uid抓取歌单，获得网络数据后设定同步歌单的异步IO任务
        //-----------------------------------------------------
        //tips:发起网络请求是在主线程，网络请求的处理是在子线程
        //     而网络请求完成后，回调函数的执行是在主线程，但是回调函数的任务只是
        //     发起一个异步的在子线程执行的歌单同步任务，不会造成主线程的阻塞
        //TODO 根据uid发起网络请求抓取歌曲
//        backendResourceWebService.getAllSongListsByUid(uid).enqueue(callbackFactory.create());
    }

    public void setLogining(boolean logining){ this.logining=logining; }
    public void setStateHolder(LoginStateHolder stateHolder){ this.stateHolder=stateHolder;}
    public boolean hasLogin(){
        return stateHolder!=null;
    }
    public String getCurrentUid(){
        return stateHolder==null?null:stateHolder.getUid();
    }
    public boolean isLogining(){
        return logining;
    }

    @AllArgsConstructor
    @Data
    public static class LoginStateHolder{
        private String uid;
        private LoginType loginType;
    }

    public enum LoginType{
        Phone,
        Email
    }

    @AllArgsConstructor
    public class LoginCallback extends __CustomCallback {

        private LoginManager.LoginType loginType;
        private LoginManager loginManager;

        @Override
        protected void process(Object data) {
            String uid=(String)data;

            loginManager.setLogining(false);
            loginManager.setStateHolder(new LoginManager.LoginStateHolder(uid,loginType));
            fetchAllByUid(uid);
        }
    }

    private LoginCallback createCallback(LoginType loginType){
        return new LoginCallback(loginType,this);
    }
}
