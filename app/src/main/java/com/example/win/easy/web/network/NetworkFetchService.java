package com.example.win.easy.web.network;

import com.example.win.easy.web.callback.OnReadyFunc;

public interface NetworkFetchService<DTOType> {

    /**
     * <p>抓取某种DTO类型的网络服务</p>
     * <p>（拉取后递交给上层的是DTO类型，关于DTO到DO的转化交给上层）</p>
     * @param onReadyFunc 网络数据拉取成功时调用的回调函数
     */
    void fetch(OnReadyFunc<DTOType> onReadyFunc);

}
