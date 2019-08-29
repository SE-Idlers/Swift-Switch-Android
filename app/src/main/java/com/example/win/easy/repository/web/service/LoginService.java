package com.example.win.easy.repository.web.service;

import com.example.win.easy.repository.web.callback.OnReadyFunc;
import com.example.win.easy.repository.web.request.BackendRequestService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p>在这个app中提供登陆的功能模块</p>
 * <p>主要功能包括登陆，获取登陆状态，获取用户id等</p>
 * <p>目前关于这个类还有一些地方不太确定：</p>
 * <ol>
 *     <li>异步代码写得靠不靠谱。因为每次登陆前都会检测是不是正在登陆，防止有包正在路上，同时当每次登陆返回响应时，都把“正在登陆”设置为否。</li>
 *     <li>要不要加锁同步。获取当前用户id是读取currentUid变量，判断当前是不是已经登陆也是读取currentUid，而当成功登陆时，会设置currentUid，也就是写currentUid。</li>
 * </ol>
 * 目前来看似乎没有同步代码也没问题，主要是因为Callback文档中说，在安卓环境下OnResponse这个回调函数是在UI线程执行的，
 * 而目前似乎（注意是似乎）所有对login状态的访问也都是在UI线程，如果这样的话，讲道理就不会出现race condition
 * <p>但是...</p>
 * <p>目前也只能先这样了</p>
 * @see Callback
 */
public class LoginService {

    private BackendRequestService backendRequestService;

    public LoginService(BackendRequestService backendRequestService){
        this.backendRequestService = backendRequestService;
    }


    /**
     * 通过手机登陆
     * @param phone 手机号
     * @param password 密码
     * @param onReadyFunc 登陆结果返回后要执行的行为（包括成功时要干啥，失败时要干啥）
     */
    public void loginByPhone(String phone, String password, OnReadyFunc<Boolean> onReadyFunc){
        if (isLogining())
            return;
        setLogining(true);
        backendRequestService.getUidByPhone(phone,password).enqueue(newLoginCallback(onReadyFunc));
    }

    /**
     * 通过email登陆
     * @param email 邮箱地址
     * @param password 密码
     * @param onReadyFunc 登陆结果返回后要执行的行为（包括成功时要干啥，失败时要干啥）
     */
    public void loginByEmail(String email,String password,OnReadyFunc<Boolean> onReadyFunc){
        if (isLogining())
            return;
        setLogining(true);
        backendRequestService.getUidByEmail(email,password).enqueue(newLoginCallback(onReadyFunc));
    }

    /**
     * <p>返回是否已经登陆（供其他诸如网络请求之类的使用 ）</p>
     * @return 是否已经登陆
     */
    public boolean hasLogin(){ return currentUid!=null; }

    /**
     * <p>返回当前登陆用户的网易云用户id</p>
     * @return 当前登陆用户的网易云id
     */
    public String getCurrentUid(){ return currentUid; }


    private void setLogining(boolean logining){ this.logining=logining; }
    private void setCurrentUid(String currentUid){this.currentUid=currentUid;}
    private boolean isLogining(){ return logining; }
    private LoginCallback newLoginCallback(OnReadyFunc<Boolean> onReadyFunc){ return new LoginCallback(onReadyFunc); }


    private String currentUid;
    private boolean logining=false;


    /**
     * 登陆结果返回时要调用的callback
     */
    private class LoginCallback implements Callback<String> {

        private OnReadyFunc<Boolean> onReadyFunc;
        LoginCallback(OnReadyFunc<Boolean> onReadyFunc){
            this.onReadyFunc=onReadyFunc;
        }

        /**
         * <p>成功登陆时：</p>
         * <ol>
         *     <li>把正在登陆取消</li>
         *     <li>设置好当前用户的uid</li>
         *     <li>调用上层给的回调函数（成功登陆时该干的事儿）</li>
         * </ol>
         */
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            setLogining(false);
            setCurrentUid(response.body());
            onReadyFunc.onReady(true);
        }

        /**
         * <p>登陆失败时：</p>
         * <ol>
         *     <li>把正在登陆取消</li>
         *     <li>调用上层给的回调函数（登陆失败时该干的事儿）</li>
         * </ol>
         */
        @Override
        public void onFailure(Call<String> call, Throwable t) {
            setLogining(false);
            onReadyFunc.onReady(false);
        }
    }

}
