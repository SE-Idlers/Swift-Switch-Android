package com.example.win.easy.activity.component;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.R;

import java.util.List;

/*数据域：
 * 歌曲名 播放次数* 序号* 歌手名 专辑名 已下载标识*
 * 可更新部分：播放次数；序号（未完成）；已下载标识；
 * */

public class SongAdapter extends BaseAdapter {
    List<SongPojo> mData;//数据源引用
    Context mContext;
    LayoutInflater inflater;

    public static class  ViewHolder {//界面刷新的缓存机制；保存可复用的View控件
        public ViewHolder(Context m){
            name=new TextView(m);
            times=new TextView(m);
            order=new TextView(m);
            downloaded=new ImageView(m);
        }
        public TextView name;
        public  TextView times;//播放次数，*可更改
        public TextView order;//序号，*可更改
        public ImageView downloaded;//下载状态，*可更改
    }


    public SongAdapter(List<SongPojo> data, Context context) {
        this.mData = data;
        this.mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount(){
        return mData.size();
    }
    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {//利用缓冲机制刷新某个item的视图
        SongPojo mdata=mData.get(index);
        ViewHolder mHolder;
        View view=null;
        if(convertView==null){
            view=LayoutInflater.from(mContext).inflate(R.layout.song_item,viewGroup,false);
            mHolder=new ViewHolder(mContext);
            mHolder.name=(TextView)view.findViewById(R.id.tv_song_name);
            mHolder.times=(TextView)view.findViewById(R.id.tv_song_replaytimes);
            mHolder.order=(TextView)view.findViewById(R.id.tv_song_order);
            mHolder.downloaded=(ImageView)view.findViewById(R.id.img_downloaded);

            view.setTag(mHolder);
        }else{
            view=convertView;
            mHolder=(ViewHolder)convertView.getTag();
        }

        //歌曲名
        mHolder.name.setText(mdata.getName());
        //播放次数
        mHolder.times.setText(0+"");
        //歌曲序号
        //是否显示 已下载的标记
        if(mdata.getSongPath()!=null)//已下载
            mHolder.downloaded.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ase16));
        else
            mHolder.downloaded.setImageDrawable(null);

        return view;
    }


    public void updateItemTimes(ListView listView,int index,int nm){//后台数据变化引发界面变化
        //功能:更新歌曲播放次数
        // 此代码段下，index指数据源中待更改数据的position
        int firstPos=listView.getFirstVisiblePosition();
        int lastPos=listView.getLastVisiblePosition();
        if(index>=firstPos&&index<=lastPos){//检查是否在视图内
            View view=listView.getChildAt(index-firstPos);//getChildAt获取视图内的view,其顺序以视图中的第一个为0;视图外的不计入
            ViewHolder vHolder=(ViewHolder)view.getTag();
            vHolder.times.setText(nm+"");//播放次数
        }

    }
    public void updateItemDownLoad(ListView listView,int index,boolean state) {//后台数据变化引发界面变化
        //功能:下载情况可视化
        //此代码段下，index指数据源中待更改数据的position
        int firstPos = listView.getFirstVisiblePosition();
        int lastPos = listView.getLastVisiblePosition();
        if (index >= firstPos && index <= lastPos) {//检查是否在视图内
            View view = listView.getChildAt(index - firstPos);//getChildAt获取视图内的view,其顺序以视图中的第一个为0;视图外的不计入
            ViewHolder vHolder = (ViewHolder) view.getTag();
            if (state) {
                vHolder.downloaded.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ase16));
                Log.d("SongList", "DownLoad");
            } else {
                vHolder.downloaded.setImageDrawable(null);
                Log.d("SongList", "N-DownLoad");
            }
        }
    }

}
