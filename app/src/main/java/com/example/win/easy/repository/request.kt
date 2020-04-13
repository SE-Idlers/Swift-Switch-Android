package com.example.win.easy.repository


suspend fun <T: Any> request(before: ()->Unit, fail: ()->Unit,requestFunc: suspend ()->T){
    try {
        before()
        requestFunc()
    }catch (t: Throwable){
        
    }
    var t=Result.success(0)

}