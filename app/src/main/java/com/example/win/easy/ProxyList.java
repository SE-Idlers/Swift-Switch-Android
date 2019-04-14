package com.example.win.easy;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.win.easy.display.component.DisplayManagerImpl;
import com.example.win.easy.display.component.SongListMangerImpl;
import com.example.win.easy.display.component.SongListViewImpl;
import com.example.win.easy.display.SongList;

public class ProxyList {

    private DisplayManagerImpl implementDisplayManager=DisplayManagerImpl.getInstance();
    private SongListViewImpl implementSongListView=SongListViewImpl.getInstance();
    private SongListMangerImpl implementSongListManger=SongListMangerImpl.getInstance();

    ProxyMediaPlayer proxyMediaPlayer=ProxyMediaPlayer.getProxyMediaPlayer();

    TabHost tabHost =(TabHost) MainActivity.mainActivity.findViewById(android.R.id.tabhost);
    TabHost.TabSpec[] spec=new TabHost.TabSpec[Constants.NumberOfList];


    private static ProxyList proxyList=new ProxyList();

    private boolean ifSearchView=true;

    private String[] tabName=new String[]{"tab1","tab2","tab3"};

    private String[] ListSource=new String[]{"本地","网易云","酷狗"};//测试tabhost控件用的
    private String[] ListName=new String[]{"歌单一","歌单二","歌单三"};//同上

    private SongList[] songLists=new SongList[Constants.NumberOfList];//具体实现,所要展示的SongList

    private ListView[] listView=new ListView[Constants.NumberOfList];

    private Integer[] listViewID=new Integer[]{R.id.listView1,R.id.listView2,R.id.listView3};
    private Integer[] tabs=new Integer[]{R.id.tab1,R.id.tab2,R.id.tab3};

    private ProxyList(){initlist();init();}

    public static ProxyList getProxyList(){
        return proxyList;
    }


    public void init(){
        tabHost.setup();
        for(int i=0;i<Constants.NumberOfList;i++){
            spec[i]=tabHost.newTabSpec(tabName[i]);
            spec[i].setIndicator(ListSource[i],null);
            spec[i].setContent((tabs[i]));
            tabHost.addTab(spec[i]);
        }
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "tab1":
                        //在选中的歌单中，找到当前播放歌曲的索引，再在LISTVIEW中选中，但这个方法会让歌曲重新播放
                        listView[0].setSelection(songLists[0].getSongList().indexOf(implementDisplayManager.getDisplayList().getSongAt(proxyMediaPlayer.getCurrentPosition())));
                        implementDisplayManager.setDisplayList(songLists[0]);
                        break;
                    case "tab2":
                        listView[1].setSelection(songLists[1].getSongList().indexOf(implementDisplayManager.getDisplayList().getSongAt(proxyMediaPlayer.getCurrentPosition())));
                        implementDisplayManager.setDisplayList(songLists[1]);
                        break;
                    case "tab3":
                        listView[2].setSelection(songLists[2].getSongList().indexOf(implementDisplayManager.getDisplayList().getSongAt(proxyMediaPlayer.getCurrentPosition())));
                        implementDisplayManager.setDisplayList(songLists[2]);
                        break;
                }
                ;
            }
        });
    }

    public void initlist() {
        for(int i=0;i<Constants.NumberOfList;i++) {

            listView[i] = MainActivity.mainActivity.findViewById(listViewID[i]);
            listView[i].setAdapter(implementDisplayManager.setDisplayList(songLists[i]));
            listView[i].setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            //选择列表中某个歌曲后，进行searchlist 到listview的转换，同时播放歌曲
            listView[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(ifSearchView)
                    {
                        //第一个参数，包含所选歌曲的所有列表，第二个参数，所选歌曲（在当前播放的歌曲列表中）
                        implementSongListView.update(implementSongListManger.appearanceListsOf(implementDisplayManager.getDisplayList().getSongAt(position)),implementDisplayManager.getDisplayList().getSongAt(position),tabHost,tabs);
                        ifSearchView=false;
                    }
                    implementDisplayManager.restartWith(implementDisplayManager.getDisplayList().getSongAt(position).getAbsolutePath().toString(),proxyMediaPlayer.getmediaPlayer());
                }
            });
        }
    }

    /**
     * 在ListView视图时，手写板有新的识别结果，或用户利用搜索框手动搜索某个歌曲时，转回searchListView视图，即按来源显示可匹配到的歌曲列表。
     *这个函数应该放在seatchview里，但我没太理解所留的函数 update(List<Integer> sortedIndices )是不是这个意思。
     */

    public void changeToSearchListView(SongList[] songLists){
        for(int i=0;i<Constants.NumberOfList;i++){
            spec[i]=tabHost.newTabSpec(tabName[i]);
            spec[i].setIndicator(ListName[i],null);
            spec[i].setContent((tabs[i]));
        }
        tabHost.clearAllTabs();
        for(int i=0;i<Constants.NumberOfList;i++)
            tabHost.addTab(spec[i]);
    };

}
