package com.example.win.easy.web.callback;

/**
 * <p>专门用来当lambda函数用的接口</p>
 * <p>比如上层要获取所有网络歌单，但是“获取网络歌单”这个行为肯定没法做同步，只能异步获取，获取成功的时候触发回调函数</p>
 * <p>而在这个过程中，本接口就充当了回调函数的角色</p>
 * <p>我们可以比较一下本来同步调用跟现在异步调用的写法有什么区别，就拿获取所有歌单举例吧</p>
 * <p>同步版本：</p>
 * <p>&emsp;WebService的方法签名： List&lt;SongListDTO&gt; fetchAllSongLists();</p>
 * <p>&emsp;代码：</p>
 * <p>&emsp;&emsp;List&lt;SongListDTO&gt; allSongList=webService.fetchAllSongLists();</p>
 * <p>&emsp;&emsp;...</p>
 * <p>&emsp;&emsp;//logic</p>
 * <p>&emsp;&emsp;...</p>
 * <p>异步版本：</p>
 * <p>&emsp;WebService的方法签名： void fetchAllSongLists(OnReadyFunc&lt;List&lt;SongListDTO&gt;&gt; onReadyFunc);</p>
 * <p>&emsp;代码：</p>
 * <p>&emsp;&emsp;webService.fetchAllSongLists(allSongList->{</p>
 * <p>&emsp;&emsp;&emsp;...</p>
 * <p>&emsp;&emsp;&emsp;//logic</p>
 * <p>&emsp;&emsp;&emsp;...</p>
 * <p>&emsp;&emsp;&emsp;</p>
 * <p>&emsp;&emsp;})</p>
 * <p>总的来说，只是把业务逻辑打包到了lambda函数（也就是这个接口）里面交给下面，等到ok以后自动调用</p>
 * @param <T> 要异步获取的数据类型
 */
public interface OnReadyFunc<T> {

    void onReady(T t);
}
