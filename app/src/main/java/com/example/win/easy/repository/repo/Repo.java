package com.example.win.easy.repository.repo;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.web.callback.OnReadyFunc;
import com.example.win.easy.web.service.SongListWebService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repo {
    // Dao,网络服务,executor
    protected SongDao songDao;
    protected SongListDao songListDao;
    protected SongXSongListDao songXSongListDao;
    protected SongListWebService songListWebService;
    protected ExecutorService executorService; // 处理多次fetch
    protected Executor executor;
    // LiveData
    protected MutableLiveData<List<SongDO>> AllSong;
    protected MutableLiveData<List<SongListDO>> AllSongList;
    protected MutableLiveData<List<SongXSongListDO>> AllRelation;
    // 缓存数据
    protected List<SongDO> allSong;
    protected List<SongListDO> allSongList;
    protected List<SongXSongListDO> allRelation;
    // 网络歌曲
    protected List<SongDO> songOnWeb;
    protected List<SongListDO> songListOnWeb;
    protected List<SongXSongListDO> songXSongListOnWeb;
    // 本地歌曲
    protected List<SongDO> songOnLocal;
    protected List<SongListDO> songListOnLocal;
    protected List<SongXSongListDO> songXSongListOnLocal;
    // 歌单内容
    protected List<SongDO> songOf;
    protected List<SongDO> songNotIn;
    // 包含某首歌的所有歌单
    protected List<SongListDO> songContain;

    public Repo(SongDao songDao,
                SongListDao songListDao,
                SongXSongListDao songXSongListDao,
                SongListWebService songListWebService) {
        this.songDao = songDao;
        this.songListDao = songListDao;
        this.songXSongListDao = songXSongListDao;
        this.songListWebService = songListWebService;

        executorService = Executors.newSingleThreadExecutor();// 使用单线程化线程池，按照指定顺序执行Repo_task
        // 初始化LiveData
        AllSong = new MutableLiveData<>();
        AllSongList = new MutableLiveData<>();
        AllRelation = new MutableLiveData<>();
        // 测试用
        executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        // TODO:可能删去下面两行
        songOf = new ArrayList<>();
        songNotIn = new ArrayList<>();
    }

    //-------------------------接口------------------------------
    /**
     * 获取所有歌曲
     * @param callBack
     */
    public void getAllSong(OnReadyFunc<LiveData<List<SongDO>>> callBack) {
        AllSong_AsyncTask task = new AllSong_AsyncTask(callBack);
        task.executeOnExecutor(executorService);
    }

    /**
     * 获取所有歌单
     * @param callBack
     */
    public void getAllSongList(OnReadyFunc<LiveData<List<SongListDO>>> callBack) {

        AllSongList_AsyncTask task = new AllSongList_AsyncTask(callBack);
        task.executeOnExecutor(executorService);
    }

    /**
     * 获取特定歌单的歌曲
     * @param callBack
     * @param songListDO
     */
    public void songsOf(OnReadyFunc<List<SongDO>> callBack, SongListDO songListDO) {
        SongOf_AsyncTask task = new SongOf_AsyncTask(callBack, songListDO);
        task.executeOnExecutor(executorService);
    }

    /**
     * 寻找所有包含此首歌的歌单
     * @param callBack
     * @param songDO
     */
    public void songListsContain(OnReadyFunc<List<SongListDO>> callBack, SongDO songDO){
        SongContain_AsyncTask task = new SongContain_AsyncTask(callBack, songDO);
        task.executeOnExecutor(executorService);
    }

    /**
     * 包含不在某歌单的其他歌曲
     * @param callBack
     * @param songListDO
     */
    public void getSongNotIn(OnReadyFunc<List<SongDO>> callBack, SongListDO songListDO) {
        SongNotIn_AsyncTask task = new SongNotIn_AsyncTask(callBack, songListDO);
        task.executeOnExecutor(executorService);
    }
    //-----------------------------------------------------------


    //-------------------------功能类----------------------------
    // 基类
    private class BasicTask extends AsyncTask<Object,Object,Long>{
        @Override
        protected Long doInBackground(Object... objects) {

            songListWebService.getAllSongLists((songMap) -> {
                AsyncTask task = new Repo_Task_AccessDB(songMap);// 子线程获取数据库数据
                task.execute();
            });
            return null;
        }
    }

    // 所有歌曲
    private class AllSong_AsyncTask extends BasicTask{
        OnReadyFunc<LiveData<List<SongDO>>> myCallBack;
        public AllSong_AsyncTask(OnReadyFunc<LiveData<List<SongDO>>> callBack) {
            myCallBack = callBack;
        }
        @Override
        protected void onPostExecute(Long aLong) {
            // callBack返回
            myCallBack.onReady(AllSong);
        }
    }

    // 所有歌单
    private class AllSongList_AsyncTask extends BasicTask{
        OnReadyFunc<LiveData<List<SongListDO>>> myCallBack;
        public AllSongList_AsyncTask(OnReadyFunc<LiveData<List<SongListDO>>> callBack) {
            myCallBack = callBack;
        }
        @Override
        protected void onPostExecute(Long aLong) {
            // callBack返回
            myCallBack.onReady(AllSongList);
        }
    }

    // 某歌单的歌曲
    private class SongOf_AsyncTask extends BasicTask{
        OnReadyFunc<List<SongDO>> myCallBack;
        SongListDO toFindSongOf;
        public SongOf_AsyncTask(OnReadyFunc<List<SongDO>> callBack, SongListDO songListDO) {
            myCallBack = callBack;
            toFindSongOf = songListDO;
        }
        @Override
        protected Long doInBackground(Object... objects) {
            songListWebService.getAllSongLists((songMap) -> {
                AsyncTask task = new Repo_Task_AccessDB(songMap, toFindSongOf);// 重写方法，加入SongList参数
                task.execute();
            });
            return null;
        }
        @Override
        protected void onPostExecute(Long aLong) {
            // callBack返回
            myCallBack.onReady(songOf);
        }
    }
    // 包含某首歌的歌单
    private class SongContain_AsyncTask extends BasicTask{
        OnReadyFunc<List<SongListDO>> myCallBack;
        SongDO toFindSongContain;
        public SongContain_AsyncTask(OnReadyFunc<List<SongListDO>> callBack, SongDO songDO) {
            myCallBack = callBack;
            toFindSongContain = songDO;
        }
        @Override
        protected Long doInBackground(Object... objects) {
            songListWebService.getAllSongLists((songMap) -> {
                AsyncTask task = new Repo_Task_AccessDB(songMap, toFindSongContain);// 重写方法，加入SongList参数
                task.execute();
            });
            return null;
        }
        @Override
        protected void onPostExecute(Long aLong) {
            // callBack返回
            myCallBack.onReady(songContain);
        }
    }
    // 不在某歌单里的其他歌曲
    private class SongNotIn_AsyncTask extends BasicTask{
        OnReadyFunc<List<SongDO>> myCallBack;
        SongListDO toFindSongNotIn;
        public SongNotIn_AsyncTask(OnReadyFunc<List<SongDO>> callBack, SongListDO songListDO) {
            myCallBack = callBack;
            toFindSongNotIn = songListDO;
        }
        @Override
        protected Long doInBackground(Object... objects) {
            songListWebService.getAllSongLists((songMap) -> {
                AsyncTask task = new Repo_Task_AccessDB(songMap, toFindSongNotIn, 0);// 重写方法，加入SongList参数
                task.execute();
            });
            return null;
        }
        @Override
        protected void onPostExecute(Long aLong) {
            // callBack返回
            myCallBack.onReady(songNotIn);
        }
    }

    //-----------------------------------------------------------


    // 请将此类缩略--具体实现
    private class Repo_Task_AccessDB extends AsyncTask<Object, Object, Long>{
        // 可能需要的SongListDO, SongDO
        SongListDO toFindSongOf;
        SongListDO toFindSongNotIn;
        SongDO toFindSongContain;
        // 需要获取songMap作为数据成员
        Map<SongListDO,List<SongDO>> songMap;
        public Repo_Task_AccessDB(Map<SongListDO,List<SongDO>> songMap){
            this.songMap = songMap;
            toFindSongOf = null; //默认方法，防止后续bug
            toFindSongContain = null;
            toFindSongNotIn = null;
        }
        public Repo_Task_AccessDB(Map<SongListDO,List<SongDO>> songMap, SongListDO songListDO){
            this.songMap = songMap;
            toFindSongOf = songListDO;
            toFindSongContain = null;
            toFindSongNotIn = null;
        }
        public Repo_Task_AccessDB(Map<SongListDO,List<SongDO>> songMap, SongDO songDO){
            this.songMap = songMap;
            toFindSongOf = null;
            toFindSongContain = songDO;
            toFindSongNotIn = null;
        }
        public Repo_Task_AccessDB(Map<SongListDO,List<SongDO>> songMap, SongListDO songListDO, int temp){
            this.songMap = songMap;
            toFindSongOf = null;
            toFindSongContain = null;
            toFindSongNotIn = songListDO;
        }

        @Override
        protected Long doInBackground(Object... params) {
            beforeUpdate();
            int i = 1;
            Iterator iterator = songMap.keySet().iterator();
            SongListDO songListDO;
            while (iterator.hasNext()) {
                songListDO = (SongListDO) iterator.next();
                boolean end_one = songMap.keySet().size() == i;
                List<SongDO> allSongDO = songMap.get(songListDO);//要在id更新语句之前取出来
                Long songListId = updateSongList(songListDO, end_one == true);
                int j = 1;
                for (SongDO songDO : allSongDO) {
                    boolean end_two = allSongDO.size() == j;
                    Long songId = updateSong(songDO, end_one && end_two);
                    updateRelation(
                            SongXSongListDO.builder().songId(songId).songListId(songListId).build(),
                            end_one && end_two);
                    j++;
                }
                i++;
            }
            allSong = songDao.getAllSong() == null ? new ArrayList<>() : songDao.getAllSong();
            allSongList = songListDao.getAllSongList() == null ? new ArrayList<>() : songListDao.getAllSongList();
            allRelation = songXSongListDao.getAllRelation() == null
                    ? new ArrayList<>()
                    : songXSongListDao.getAllRelation();

            if(toFindSongOf != null)
                songOf = songXSongListDao.getSongsOf(toFindSongOf.id);
            if(toFindSongNotIn != null)
                songNotIn = songXSongListDao.getSongsNotIn(toFindSongOf.id);
            if(toFindSongContain != null)
                songContain = songXSongListDao.findAllSongListsForSongById(toFindSongContain.id);
            return null;
        }


        private void beforeUpdate() {
            //fetch之前从新获取缓存数据
            allSong = songDao.getAllSong()==null?new ArrayList<>():songDao.getAllSong();
            allSongList = songListDao.getAllSongList()==null?new ArrayList<>():songListDao.getAllSongList();
            allRelation = songXSongListDao.getAllRelation()==null?new ArrayList<>():songXSongListDao.getAllRelation();
            songOnWeb = songDao.findAllDataOnWeb()==null?new ArrayList<>():songDao.findAllDataOnWeb();
            songListOnWeb = songListDao.findAllDataOnWeb()==null?new ArrayList<>():songListDao.findAllDataOnWeb();
            songXSongListOnWeb = songXSongListDao.findAllDataOnWeb()==null?new ArrayList<>():songXSongListDao.findAllDataOnWeb();
            songOnLocal = songDao.findAllDataOnLocal()==null?new ArrayList<>():songDao.findAllDataOnLocal();
            songListOnLocal = songListDao.findAllDataOnLocal()==null?new ArrayList<>():songListDao.findAllDataOnLocal();
            songXSongListOnLocal = songXSongListDao.findAllDataOnLocal()==null?new ArrayList<>():songXSongListDao.findAllDataOnLocal();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            // 更新LiveData
            AllSong.postValue(allSong);
            AllSongList.postValue(allSongList);
            AllRelation.postValue(allRelation);
        }


        private Long updateSong(SongDO data, boolean isEnd) {
            Long songId = insertSong(data);
            //手动迭代
            int index = songOnWeb.size() - 1;
            for (SongDO hasIt; index >= 0; index--) {
                hasIt = songOnWeb.get(index);
                if (hasIt.remoteId.longValue() == data.remoteId.longValue()) {
                    songOnWeb.remove(index);//remove可行性存疑
                    break;
                }
            }
            if (isEnd == true && songOnWeb.isEmpty() == false) {
                for (SongDO item : songOnWeb) {
                    deleteSong(item);
                }
            }
            return songId;
        }

        private Long updateSongList(SongListDO data, boolean isEnd) {
            Long songListId = insertSongList(data);
            //手动迭代
            int index = songListOnWeb.size() - 1;
            for (SongListDO hasIt; index >= 0; index--) {
                hasIt = songListOnWeb.get(index);
                if (hasIt.remoteId.longValue() == data.remoteId.longValue()) {
                    songListOnWeb.remove(index);
                    break;
                }
            }
            if (isEnd == true && songListOnWeb.isEmpty() == false) {
                for (SongListDO item : songListOnWeb) {
                    deleteSongList(item);
                }
            }
            return songListId;
        }

        private void updateRelation(SongXSongListDO data, boolean isEnd) {
            insertSongXSongList(data);
            int index = songXSongListOnWeb.size() - 1;
            for (SongXSongListDO hasIt; index >= 0; index--) {
                hasIt = songXSongListOnWeb.get(index);
                if (hasIt.songId.longValue() == data.songId.longValue()
                        && hasIt.songListId.longValue() == data.songListId.longValue()) {
                    songXSongListOnWeb.remove(index);
                    break;
                }
            }
            if (isEnd == true && songXSongListOnWeb.isEmpty() == false) {
                for (SongXSongListDO item : songXSongListOnWeb) {
                    deleteSongXSongList(item);
                }
            }
        }

        private Long insertSong(SongDO data) {
            SongDO result = songDao.findByRemoteId(data.remoteId);
            if (result == null)
                return songDao.insert(data);
            else
                return result.id;
        }

        private void deleteSong(SongDO data) {
            SongDO result = songDao.findByRemoteId(data.remoteId);
            if (result != null)
                songDao.delete(data);
        }

        private Long insertSongList(SongListDO data) {
            SongListDO result = songListDao.findByRemoteId(data.remoteId);
            if (result == null)
                return songListDao.insert(data);
            else
                return result.id;
        }

        private void deleteSongList(SongListDO data) {
            SongListDO result = songListDao.findByRemoteId(data.remoteId);
            if (result != null)
                songListDao.delete(data);
        }

        private void insertSongXSongList(SongXSongListDO data) {
            SongXSongListDO result = songXSongListDao.findById(data.songId, data.songListId);
            if (result == null)
                songXSongListDao.insert(data);
        }

        private void deleteSongXSongList(SongXSongListDO data) {
            SongXSongListDO result = songXSongListDao.findById(data.songId, data.songListId);
            if (result != null)
                songXSongListDao.delete(data);
        }
    }

}
