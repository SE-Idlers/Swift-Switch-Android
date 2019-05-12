package com.example.win.easy.repository.web.callback;

import com.example.win.easy.repository.LoginManager;

public class LoginCallBack extends CustomCallBack {

    private LoginManager.LoginType loginType;
    public LoginCallBack(LoginManager.LoginType loginType){
        this.loginType=loginType;
    }
    @Override
    protected void process(Object data) {
        LoginManager.success((String) data,loginType);
    }
}
