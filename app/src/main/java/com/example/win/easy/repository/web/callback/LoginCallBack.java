package com.example.win.easy.repository.web.callback;

import com.example.win.easy.application.SwiftSwitchApplication;
import com.example.win.easy.repository.LoginManager;
import com.example.win.easy.repository.repo.SongListRepository;

public class LoginCallBack extends CustomCallBack {

    private LoginManager.LoginType loginType;
    private LoginManager loginManager= SwiftSwitchApplication.application.getAppComponent().getLoginManager();
    private SongListRepository songListRepository=SwiftSwitchApplication.application.getAppComponent().getSongListRepository();
    public LoginCallBack(LoginManager.LoginType loginType){
        this.loginType=loginType;
    }
    @Override
    protected void process(Object data) {
        String uid=(String)data;

        loginManager.setLogining(false);
        loginManager.setStateHolder(new LoginManager.LoginStateHolder(uid,loginType));
        songListRepository.fetchAllByUid(uid);
    }
}
