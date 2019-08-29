package com.example.win.easy.repository.web.network;

import com.example.win.easy.factory.CallbackFactory;
import com.example.win.easy.repository.web.service.LoginService;
import com.example.win.easy.repository.web.callback.LambdaCallback;
import com.example.win.easy.repository.web.callback.OnReadyFunc;
import com.example.win.easy.repository.web.dto.SongListDTO;
import com.example.win.easy.repository.web.request.BackendRequestService;

import java.util.List;

import retrofit2.Call;

/**
 * <p>这个类的主要功能是根据不同的需要fetch的类型调用相应的网络请求方法</p>
 * <p>虽然看起来什么都没干，但其实是一个“胶水”功能的类，因为</p>
 * <p>url以及具体的参数要求是在{@link BackendRequestService}中定义的；Response到本地DTO类型的转化是在{@link CallbackFactory}中定义的</p>
 * <p>而这个类的功能就是，当上层说：“我要A类型的网络数据”时，调用这个类的fetch方法，这个类根据A，调用相应的网络方法（请求url的方法），设置相应的</p>
 * <p>CustomCallback用来将Response的数据转化为DTO数据。所以说这个类是一个“胶水”类，把一些功能联合起来</p>
 */
public class AllSongListNetworkFetchService implements NetworkFetchService<List<SongListDTO>> {

    private BackendRequestService webService;
    private LoginService loginService;
    public AllSongListNetworkFetchService(BackendRequestService webService,
                                          LoginService loginService){
        this.webService=webService;
        this.loginService = loginService;
    }

    /**
     * <p>对获取所有歌单这一行为发起具体的网络请求</p>
     * <p>同时，在这一层会判断登陆状态。</p>
     * <p>其实在之前的实现中，判断登陆状态这个工作在这个方法的调用者那一层就执行了，然后就发现，如果在这一层再判断一次，</p>
     * <p>一方面是重复，一方面两个层都依赖登陆这一服务。</p>
     * <p>最后选择放在这一层，是考虑到在上一层中判断完登陆其实login的服务就没啥用了，但在这一层判断登陆后立马就要获取uid</p>
     * <p>按照职责分配的原则，真正对登陆这个服务有全面依赖的是这一层，上一层知不知道其实无所谓。综上，登陆判断放在了这一层</p>
     * @param onReadyFunc 上层提供的，网络数据拉取成功时调用的回调函数
     */
    @Override
    public void fetch(OnReadyFunc<List<SongListDTO>> onReadyFunc) {

        //只有登陆后才发起网路请求，否则直接啥都不干
        if (loginService.hasLogin()){

            //构造一个网络请求
            Call<List<SongListDTO>> webCall=webService.getAllSongListsByUid(loginService.getCurrentUid());


            //构造一个请求成功时要调用的callback，这里callback直接执行上层给的onReadyFunc，不再自己构造onReadyFunc
            LambdaCallback<List<SongListDTO>> callbackWhenResponseIsOK=new LambdaCallback<>(onReadyFunc);


            //发起网络请求（有响应时自动执行上层的onReadyFunc）
            webCall.enqueue(callbackWhenResponseIsOK);
        }
    }

}
