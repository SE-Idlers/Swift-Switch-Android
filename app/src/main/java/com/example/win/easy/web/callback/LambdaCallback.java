package com.example.win.easy.web.callback;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * <p>这个类相当于封装了一下原始的Callback函数</p>
 * <p>为什么要封装呢？</p>
 * <p>默认的Callback使用是自己实现onResponse方法，当有响应（没有fail）的时候这个对象会调用这个onResponse方法</p>
 * <p>但是其实我们更希望是能够扔一个回调函数给它，而不是自己实现onResponse。为什么呢？</p>
 * <p>如果用原生的Callback，那么当我们需要一个Callback的时候，要怎么使用呢？</p>
 * <p>Callback&lt;DTOType&gt; newCallback= new Callback&lt;&gt;{</p>
 * <p>&emsp;@Override</p>
 * <p>&emsp;public void onResponse(call,response){</p>
 * <p>&emsp;&emsp;</p>
 * <p>&emsp;&emsp;//logic</p>
 * <p>&emsp;&emsp;</p>
 * <p>&emsp;&emsp;....</p>
 * <p>&emsp;&emsp;</p>
 * <p>&emsp;}</p>
 * <p>}</p>
 * <p>但是其实下面一种方式更让人用起来更方便：</p>
 * <p>LambdaCallback&lt;DTOType&gt; newCallback=new LambdaCallback&lt;&gt;(dtoData->{</p>
 * <p>&emsp;</p>
 * <p>&emsp;//logic</p>
 * <p>&emsp;</p>
 * <p>&emsp;....</p>
 * <p>&emsp;</p>
 * <p>})</p>
 * <p>后一种用法显然更便利，一方面是写法，更重要的是，需要的数据类型已经在lambda函数中给出了</p>
 * <p>而原生的Callback必须自己手动处理Response，核对状态码等等</p>
 * <p>当然，使用原生的Callback无疑控制粒度更细一些，比如处理状态码等等，但是大多数情况下这并不是必要的，这也是做这个封装的主要原因之一</p>
 * @param <DTOType> 请求的DTO类型
 */
public class LambdaCallback<DTOType> implements Callback<DTOType> {

    private OnReadyFunc<DTOType> onReadyFunc;

    public LambdaCallback(OnReadyFunc<DTOType> onReadyFunc){
        this.onReadyFunc=onReadyFunc;
    }

    @Override
    public void onResponse(@NonNull Call<DTOType> call, @NonNull retrofit2.Response<DTOType> response) {
        if(response.code()==200){
            onReadyFunc.onReady(response.body());
        }
    }
    @Override
    public void onFailure(@NonNull Call<DTOType> call, @NonNull Throwable t) { }
}
