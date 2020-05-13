package com.example.win.easy.web.service

import com.example.win.easy.enumeration.LoginType
import com.example.win.easy.web.request.BackendRequestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * 在这个app中提供登陆的功能模块
 *
 * 主要功能包括登陆，获取登陆状态，获取用户id等
 *
 * 目前关于这个类还有一些地方不太确定：
 *
 *  1. 异步代码写得靠不靠谱。因为每次登陆前都会检测是不是正在登陆，防止有包正在路上，同时当每次登陆返回响应时，都把“正在登陆”设置为否。
 *  1. 要不要加锁同步。获取当前用户id是读取currentUid变量，判断当前是不是已经登陆也是读取currentUid，而当成功登陆时，会设置currentUid，也就是写currentUid。
 *
 * 目前来看似乎没有同步代码也没问题，主要是因为Callback文档中说，在安卓环境下OnResponse这个回调函数是在UI线程执行的，
 * 而目前似乎（注意是似乎）所有对login状态的访问也都是在UI线程，如果这样的话，讲道理就不会出现race condition
 *
 * 但是...
 *
 * 目前也只能先这样了
 * @see Callback
 */
class LoginService(private val backendRequestService: BackendRequestService) {

    /**
     *
     * 返回是否已经登陆（供其他诸如网络请求之类的使用 ）
     * @return 是否已经登陆
     */
    fun hasLogin() = currentUid != null

    /**
     *
     * 返回当前登陆用户的网易云用户id
     * @return 当前登陆用户的网易云id
     */
    var currentUid: String? = null
        private set

    private var isLogining = false

    /**
     * 通过手机登陆
     * @param account 手机号
     * @param password 密码
     */
    suspend fun login(account: String, password: String, loginType: LoginType) {
        if (isLogining)
            return
        isLogining = true
        try {
            currentUid=when(loginType) {
                LoginType.Phone->backendRequestService.getUidByPhone(account, password)
                LoginType.Email->backendRequestService.getUidByEmail(account, password)
            }
        }finally {
            isLogining = false
        }
    }
}