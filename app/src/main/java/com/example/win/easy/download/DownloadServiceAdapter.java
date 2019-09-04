package com.example.win.easy.download;

import com.example.win.easy.repository.UpdateService;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.web.callback.OnReadyFunc;

/**
 * <p>作为DownloadService和Activity之间的适配器来工作</p>
 * <p>为什么需要这个层呢？又DownloadService还不够吗？它不是已经实现下载功能了吗？</p>
 * <p>老实说，DownloadService在下载方面提供的功能（或者服务）已经，很完善了（对现在这整个app来说），但是，还有一些问题要处理</p>
 * <p>首先，视图层使用的是VO对象，而不是DO对象，所以肯定要加一个中间层来解决转化问题。</p>
 * <p>但单单这一个理由，连我自己都会觉得傻逼，因为如果就因为这种吊原因平白无故地加一个中间层啥也不干，那这个分层设计的副作用可就太特么大了</p>
 * <p>其实主要是因为需要更新数据库。</p>
 * <p>从关注点分离的角度来说，搞下载的最好只知道下载和下载所必须的所有东西，别的啥都不知道。不然维护起来，不是下载部分的bug出现在了下载模块里，那就会增大调试难度，还污染下载模块的代码</p>
 * <p>同样的，负责事件监听的最好只负责事件监听，不是必须的逻辑全部扔给其他部分（没错这个负责监听的，我说的就是你Activity类，当然还有UI管理）</p>
 * <p>那么问题来了，实际上来说，“点击歌曲开始下载，下载完开始播放”这个过程应该包括：</p>
 * <ul>
 *     <li>下载歌曲</li>
 *     <li>播放歌曲（下载完毕后）</li>
 *     <li><h2>更新数据库</h2></li>
 * </ul>
 * <p>下载歌曲交给DownloadService了，播放歌曲（事件分发）交给Activity了，谁来更新数据库里SongDO关于文件的域啊？</p>
 * <p>按照前面关注点分离的分析，肯定不能分给DownloadService了，那能不能交给Activity呢？</p>
 * <p>也不能，因为Activity是使用VO数据的，它对DO的存在一无所知，况且“下载后要同时更新MVVM中的数据库数据”这个东西，对Activity来说太底层了</p>
 * <p>那么按照职责分配，负责更新数据库的“人“都应该知道些什么呢？</p>
 * <p>首先，它知道有DO的存在，而且它知晓在自己更新前下载必然已经成功完成，最后，它要在别人访问这个数据前更新好数据库</p>
 * <p>很明显，交给这个适配器再合适不过了</p>
 * <p>“适配”我想也不仅仅只是类型适配这么简单，而更多的是职责适配。假如只是要下载个东西或者只是要播放一首歌曲，
 * 它们之间毫无联系，那么也就不会有更新数据库这种奇怪的需要了，正是因为他们相互依赖，才间接产生了更新数据库的需求，适配器也应运而生，
 * 所以我想，把更新交给这个适配层，也没啥大问题。
 * </p>
 */
public class DownloadServiceAdapter {

    private VOUtil voUtil;
    private DownloadService downloadService;
    private UpdateService updateService;

    public DownloadServiceAdapter(VOUtil voUtil,DownloadService downloadService,UpdateService updateService){
        this.voUtil=voUtil;
        this.downloadService=downloadService;
        this.updateService=updateService;
    }


    /**
     * <p>概括地来说这个方法的功能就像类上的注释说的那样，更新数据库，类型转换，但是细节方面还是要描述一下</p>
     * <p>讲道理逻辑应该是：上调用Adapter后，歌曲会下载，然后下载完毕后，更新LiveData的歌曲Url（LiveData的数据会同时更新给所有的观察者），同时发起异步任务更新数据库，
     *  接着调用onReadyFunc这个上层给的回调函数
     *  </p>
     *  <p>而且需要特别specify出来的地方有：</p>
     *  <ul>
     *      <li>上层调用后，不会有任何同步结果返回（也就是不会有任何返回值，也不会被阻塞）</li>
     *      <li>第一次产生影响是在下载完毕后，更新数据库数据的时候发生的。这个所谓的“更新数据库数据”是朝两个方向，使用两种方式进行的。
     *      一方面通过LiveData，向上层所有观察者通知更新，这个过程是同步的。一方面是接着发起一个异步的物理数据库update任务，向下层更新物理数据库，这个过程是异步的。
     *      所以在这个过程中，调用者如果观察了这个数据，那它的某些和这个数据相关的“更新回调函数会被调用”（不是传进来的那个），比如把歌曲由灰色变回亮色
     *      </li>
     *      <li>第二次产生影响就是传进来的回调函数被执行的时候了，直到这时，传入的回调函数才开始执行，比如开始播放等等</li>
     *  </ul>
     * @param songVO 要下载的歌曲
     * @param onReadyFunc 下载完毕后要接着进行的任务的回调函数
     */
    public void download(SongVO songVO, OnReadyFunc<SongVO> onReadyFunc){

        //日常转换
        SongDO songDO=voUtil.toDO(songVO);


        //包装一下上层的回调函数，多加一层对数据库的更新
        downloadService.download(songDO,newSongDO->{
            if (newSongDO.getSongPath()!=null)
                updateService.update(newSongDO);
            onReadyFunc.onReady(voUtil.toVO(newSongDO));
        });
    }


}

