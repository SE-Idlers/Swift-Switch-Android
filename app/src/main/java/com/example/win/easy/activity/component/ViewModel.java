package com.example.win.easy.activity.component;


/*正式代码中viewModel需要提供的接口
同AppListOne开头注释
 * 歌单：
 * 所有歌单图片更新结束 : boolean
 * 播放次数发生更新 : 歌单id，播放次数int
 * 播放顺序发生改变 : 未写
 *
 * 歌曲：
 * 某一首歌曲下载完成 : 歌曲id
 * 歌曲播放次数发生改变 : 歌曲id，播放次数int
 * 播放顺序发生改变 : 未写
 * */

/* 代码块
 * 1. 定义
 * 2. LiveData的Getter、Setter接口
 * 3. 模拟后台数据更改【最终须删除】
 * 4. 数据初始化【最终须删除】
 */

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.example.win.easy.R;

import java.util.ArrayList;
import java.util.List;

public class ViewModel extends AndroidViewModel {


    MutableLiveData<List<SongListPojo>> mSongListData;
    MutableLiveData<Boolean> mSongListPicDownLoaded;
    MutableLiveData<ArrayList<Integer>> mSongListReplayTimes;

    MutableLiveData<List<SongPojo>> mSongData;
    MutableLiveData<Integer> curDownloadedSong;
    MutableLiveData<ArrayList<Integer>> mSongReplayTimes;


    public ViewModel(Application app){
        super(app);init();
    }

    /*******************************LiveData的Getter、Setter接口*******************************/

    //mSongData: 歌曲资料【特别】*：获取的是数据而不是LiveData
    public  List<SongPojo> getSongData(){
        return mSongData.getValue();
    }
    void setSongData(List<SongPojo> l){mSongData.postValue(l);}

    //mSongReplayTimes：歌曲播放次数
    public LiveData<ArrayList<Integer>> getSongReplayTimes(){return mSongReplayTimes;}
    void setSongReplayTimes(int index,int times){ArrayList<Integer> temp=new ArrayList<Integer>();temp.add(index);temp.add(times);mSongReplayTimes.postValue(temp);}

    //curDownloadedSong：最新下载歌曲的id
    public LiveData<Integer> getCurDownloadedSong(){return curDownloadedSong;}
    void setCurDownloadedSong(int index){curDownloadedSong.postValue(index);}

    //mSongListData: 歌单资料【特别】*：获取的是数据而不是LiveData
    public  List<SongListPojo> getSongListData(){
        return mSongListData.getValue();
    }
     void setSongListData(List<SongListPojo> songListDatas){mSongListData.postValue(songListDatas);}

    //mSongListReplayTimes：歌单重播次数
    public LiveData<ArrayList<Integer>> getSongListReplayTimes(){return mSongListReplayTimes;}
    void setSongListReplayTimes(int index,int times){mSongListData.getValue().get(index).setTimes(times);
        ArrayList<Integer> temp=new ArrayList<Integer>();temp.add(index);temp.add(times);mSongListReplayTimes.postValue(temp);}

    //mSongListPicDownLoaded: 歌单图片加载是否结束
    public LiveData<Boolean> getSongListPicDownLoadState(){return mSongListPicDownLoaded;}
    void setSongListPicDownLoadState(boolean state){mSongListPicDownLoaded.postValue(state);}



    /********************************模拟后台数据更改********************************/

    //模拟后台更改数据：1 歌曲下载情况
    public void updateDownLoadedASong(ListView listView, int index){
/*//【正式代码】//此代码段下，index指数据源内的position，更新该数据
        //更新数据源
        List<SongPojo> mData =mSongData.getValue();
        SongPojo mdata=mData.get(index);
        mdata.setDownLoaded(true);
        setCurDownloadedSong(index);//更新LiveData for SongDownload，引发视图改变
 */

        //【测试代码】此代码段下，index指视图内的position，更新该数据
        //为了显示更多的变化
        int firstPos=listView.getFirstVisiblePosition();
        List<SongPojo> mData =mSongData.getValue();
        SongPojo mdata=mData.get(index+firstPos);
        mdata.setDownLoaded(true);
        setCurDownloadedSong(index+firstPos);//更新LiveData for SongDownload，引发视图改变
    }

    //模拟后台更改数据：2 歌曲播放次数
    public void updateSongTimes(ListView listView,int index,int times){
    /*    //【正式代码】//此代码段下，index指数据源内的position，更新该数据
        //更新数据源
        List<SongPojo> mData =mSongData.getValue();
        SongPojo mdata=mData.get(index);
        mdata.setTimes(times);
        setSongReplayTimes(index,times);
*/
        //【测试代码】此代码段下，index指视图内的position，更新该数据
        //为了显示更多的变化
        int firstPos=listView.getFirstVisiblePosition();
        List<SongPojo> mData =mSongData.getValue();
        SongPojo mdata=mData.get(index+firstPos);
        mdata.setTimes(times);
        setSongReplayTimes(index+firstPos,times);
    }

    //模拟后台更改数据：3 歌单图片异步加载情况
    public void updateImage(Context mContext) {/*歌单图片*/
        //模拟后台线程异步加载图片
        List<SongListPojo> mData=mSongListData.getValue();
        int cnt=mData.size();
        for(int i=0;i<cnt;++i){
            mData.get(i).setPic(mContext.getResources().getDrawable(R.drawable.ase64));
            Log.d("SongList","SongListPhotoLoad"+i);
        }

    }


    //模拟后台更改数据：4 歌单图片异步加载完成后，提交反馈使对应LiveData变化
    public void updateImageDone() {
        setSongListPicDownLoadState(true);
    }


    //模拟后台更改数据：5 歌单播放次数
    public void updateSongListTimes(ListView listView,int index,int times){
     /*      //【正式代码】//此代码段下，index指数据源内的position，更新该数据
        //更新数据源
        List<SongListPojo> mData=mSongListData.getValue();
        SongListPojo mdata=mData.get(index);
        mdata.setTimes(times);
        setSongListReplayTimes(index,times);
*/

        //【测试代码】此代码段下，index指视图内的position，更新该数据
        //为了显示更多的变化
        int firstPos=listView.getFirstVisiblePosition();
        List<SongListPojo> mData=mSongListData.getValue();
        SongListPojo mdata=mData.get(index+firstPos);
        mdata.setTimes(times);
        setSongListReplayTimes(index+firstPos,times);
    }


    /********************************数据初始化**********************************/

    void init(){
        mSongListData=new MutableLiveData<>();
        mSongData=new MutableLiveData<>();
        mSongListPicDownLoaded=new MutableLiveData<>();
        curDownloadedSong=new MutableLiveData<>();
        mSongReplayTimes=new MutableLiveData<>();
        mSongListReplayTimes=new MutableLiveData<>();
        init_data();
    }

    void init_data(){
        //歌单
        List<SongListPojo> mData1 =init_songlist_data();
        mSongListData.postValue(mData1);

        //歌曲
        List<SongPojo> mData2=test_Songs_init();
        mSongData.postValue(mData2);
    }

    List<SongListPojo> init_songlist_data(){
        List<SongListPojo> mData = new ArrayList<>();
        mData.add(new SongListPojo("歌单一",1,"来源一","未知"));
        mData.add(new SongListPojo("歌单二",2,"来源二","未知"));
        mData.add(new SongListPojo("歌单三",3,"来源三","未知"));
        mData.add(new SongListPojo("歌单四",4,"来源四","未知"));
        mData.add(new SongListPojo("歌单五",1,"来源五","未知"));
        mData.add(new SongListPojo("歌单六",2,"来源六","未知"));
        mData.add(new SongListPojo("歌单七",3,"来源七","未知"));
        mData.add(new SongListPojo("歌单八",4,"来源八","未知"));
        mData.add(new SongListPojo("歌单九",1,"来源九","未知"));
        mData.add(new SongListPojo("歌单十",2,"来源十","未知"));
        mData.add(new SongListPojo("歌单一",1,"来源一","未知"));
        mData.add(new SongListPojo("歌单二",2,"来源二","未知"));
        mData.add(new SongListPojo("歌单三",3,"来源三","未知"));
        mData.add(new SongListPojo("歌单四",4,"来源四","未知"));
        mData.add(new SongListPojo("歌单五",1,"来源五","未知"));
        mData.add(new SongListPojo("歌单六",2,"来源六","未知"));
        mData.add(new SongListPojo("歌单七",3,"来源七","未知"));
        mData.add(new SongListPojo("歌单八",4,"来源八","未知"));
        mData.add(new SongListPojo("歌单九",1,"来源九","未知"));
        mData.add(new SongListPojo("歌单十",2,"来源十","未知"));
        return mData;
    }

    List<SongPojo> test_Songs_init(){
        List<SongPojo>mData=new ArrayList<>();
        mData.add(new SongPojo("歌曲一",1,1,"歌手一","专辑一",false));
        mData.add(new SongPojo("歌曲二",2,2,"歌手二","专辑二",false));
        mData.add(new SongPojo("歌曲三",3,3,"歌手三","专辑三",false));
        mData.add(new SongPojo("歌曲四",1,1,"歌手四","专辑四",false));
        mData.add(new SongPojo("歌曲五",2,2,"歌手五","专辑五",false));
        mData.add(new SongPojo("歌曲六",3,3,"歌手六","专辑六",false));
        mData.add(new SongPojo("歌曲七",1,1,"歌手七","专辑七",false));
        mData.add(new SongPojo("歌曲八",2,2,"歌手八","专辑八",false));
        mData.add(new SongPojo("歌曲九",3,3,"歌手九","专辑九",false));
        mData.add(new SongPojo("歌曲十",1,1,"歌手十","专辑十",false));
        mData.add(new SongPojo("歌曲一",1,1,"歌手一","专辑一",false));
        mData.add(new SongPojo("歌曲二",2,2,"歌手二","专辑二",false));
        mData.add(new SongPojo("歌曲三",3,3,"歌手三","专辑三",false));
        mData.add(new SongPojo("歌曲四",1,1,"歌手四","专辑四",false));
        mData.add(new SongPojo("歌曲五",2,2,"歌手五","专辑五",false));
        mData.add(new SongPojo("歌曲六",3,3,"歌手六","专辑六",false));
        mData.add(new SongPojo("歌曲七",1,1,"歌手七","专辑七",false));
        mData.add(new SongPojo("歌曲八",2,2,"歌手八","专辑八",false));
        mData.add(new SongPojo("歌曲九",3,3,"歌手九","专辑九",false));
        mData.add(new SongPojo("歌曲十",1,1,"歌手十","专辑十",false));
        return mData;
    }

}
