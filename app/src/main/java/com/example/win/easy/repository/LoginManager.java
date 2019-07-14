package com.example.win.easy.repository;

import com.example.win.easy.repository.web.BackendResourceWebService;
import com.example.win.easy.repository.web.callback.LoginCallBack;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.AllArgsConstructor;
import lombok.Data;

@Singleton
public class LoginManager {

    private BackendResourceWebService backendResourceWebService;

    @Inject
    public LoginManager(BackendResourceWebService backendResourceWebService){
        setLogining(false);
        setStateHolder(null);
        this.backendResourceWebService=backendResourceWebService;
    }

    private boolean logining;
    private LoginStateHolder stateHolder;

    public void loginByPhone(String phone,String password){
        if (logining)
            return;
        logining =true;
        backendResourceWebService.getUidByPhone(phone,password).enqueue(new LoginCallBack(LoginType.Phone));
    }

    public void loginByEmail(String email,String password){
        if (logining)
            return;
        logining =true;
        backendResourceWebService.getUidByEmail(email,password).enqueue(new LoginCallBack(LoginType.Email));
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

}
