package com.example.win.easy.repository.web.callback;

import java.util.List;

public abstract class BatchFetchCallBack<NetworkType> extends FetchCallBack<NetworkType> {

    public BatchFetchCallBack(){ super(); }

    @Override
    protected void process(Object data) {
        List<NetworkType> newData=(List<NetworkType>)data;
        update(newData);
    }

    protected abstract void update(List<NetworkType> newData);

}
