package com.example.win.easy;

import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProxyList {
    AdministrateSongs administrateSongs=AdministrateSongs.getAdministrateSongs();



    private static ProxyList proxyList=new ProxyList();

    ListView listView = (ListView) MainActivity.mainActivity.findViewById(R.id.listView1);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.mainActivity,
            android.R.layout.simple_list_item_single_choice,
            administrateSongs.getSongNamelist());

    private ProxyList(){init();};

    public static ProxyList getProxyList(){
        return proxyList;
    }
    public  int getListLength(){
        return administrateSongs.getSonglistLongth();
    }

    private void init(){
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }
}
