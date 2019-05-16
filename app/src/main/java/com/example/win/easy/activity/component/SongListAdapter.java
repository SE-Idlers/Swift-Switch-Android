package com.example.win.easy.activity.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.win.easy.R;
import com.example.win.easy.repository.db.pojo.SongListPojo;

import java.util.List;

/*数据域：
 * 歌单图片* 歌单名 播放次数* 来源
 * */

/*viewModel需要提供的函数
 * 歌单：
 * 所有歌单图片更新结束 ：SongListImgLoadDone()
 * 播放次数发生更新 : SongListReplayTimesUpdate()
 * */


public class SongListAdapter extends BaseAdapter {
    List<SongListPojo> mData;//数据源引用
    Context mContext;//上下文
    LayoutInflater inflater;//解析器

    public static class ViewHolder{//界面刷新的缓存机制；保存可复用的View控件
        public ViewHolder(Context m){
            tv_songlist_name=new TextView(m);
            tv_songlist_replaytimes=new TextView(m);
            tv_source=new TextView(m);
            img_songlist=new ImageView(m);
            bt_delete=new Button(m);
            position=0;
        }
        public TextView tv_songlist_name;
        public TextView tv_songlist_replaytimes;//播放次数，*可更改
        public TextView tv_source;
        public ImageView img_songlist;//歌单图片，*异步更新
        public Button bt_delete;
        public long position;
    }

    public SongListAdapter(List<com.example.win.easy.repository.db.pojo.SongListPojo> data, Context context){
        this.mData=data;
        this.mContext=context;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mData.size();
    }//item数据总数
    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }//获取某个item的数据
    @Override
    public long getItemId(int i) {
        return i;
    }//获取item的总id，暂时默认返回序号

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {//刷新某个item的视图，利用缓冲机制
        SongListPojo mdata=mData.get( index);
        ViewHolder mHolder;
        View view=null;
        if(convertView==null){
            view=LayoutInflater.from(mContext).inflate(R.layout.songlist_item,viewGroup,false);
            mHolder=new ViewHolder(mContext);
            mHolder.tv_songlist_name=(TextView)view.findViewById(R.id.tv_songlist_name);
            mHolder.tv_songlist_replaytimes=(TextView)view.findViewById(R.id.tv_songlist_replaytimes);
            mHolder.tv_source=(TextView)view.findViewById(R.id.tv_source);
            mHolder.img_songlist=(ImageView)view.findViewById(R.id.img_songlist);
            mHolder.bt_delete=(Button)view.findViewById(R.id.bt_delete);
            mHolder.bt_delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(true){
                        //反馈给数据库【未写】
                    }
                }
            });

            view.setTag(mHolder);
        }else{
            view=convertView;
            mHolder=(ViewHolder)view.getTag();
        }


        //歌单名
        mHolder.tv_songlist_name.setText(mdata.getName());
        //来源
        mHolder.tv_source.setText(mdata.getSource().toString());
        //图片初始化为null
//        mHolder.img_songlist.setImageDrawable(mdata.getPic());

        return view;
    }



    public void updateItemPhoto(ListView listView){//后台数据变化引发界面变化
        //功能：图片完成异步后，刷新显示视图内的图片
        int cnt=listView.getChildCount();
        for(int index=0;index<cnt;++index){
            View view=listView.getChildAt(index);
            ViewHolder vHolder=(ViewHolder)view.getTag();
            SongListPojo mdata=mData.get(index);
//            vHolder.img_songlist.setImageDrawable(mdata.getPic());
            //Log.d("SongList","SongListItenPhoto"+index);
        }
    }

    public void updateItemTimes(ListView listView,int index,int nm){//后台数据变化引发界面变化
        //功能:更新歌曲播放次数
        // 此代码段下，index指数据源中待更改数据的position
        int firstPos=listView.getFirstVisiblePosition();
        int lastPos=listView.getLastVisiblePosition();
        if(index>=firstPos&&index<=lastPos){
            View view=listView.getChildAt(index-firstPos);
            ViewHolder vHolder=(ViewHolder)view.getTag();
            vHolder.tv_songlist_replaytimes.setText(nm+"");//播放次数
            //Log.d("SongList","SongListItenTimes"+index);
        }

    }

}
