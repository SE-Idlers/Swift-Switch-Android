package com.example.win.easy.view.lock;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.OnClickFunc;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.ArrayList;
import java.util.List;

public class PageGroup extends PagerAdapter {

    private List<QMUIGroupListView> pages=new ArrayList<>();
    private Context context;

    public PageGroup(Context context){
        this.context=context;
    }

    public void clear(){
        pages.clear();
    }

    public void addPage(List<SongVO> songsInPage, OnClickFunc<SongVO> songOnClickFunc){
        QMUIGroupListView page=toPage(songsInPage,songOnClickFunc);
        pages.add(page);
    }

    public void flush(){
        notifyDataSetChanged();
    }

    private QMUIGroupListView toPage(List<SongVO> songsInPage,OnClickFunc<SongVO> songOnClickFunc){
        //用于返回的GroupListView
        QMUIGroupListView page=new QMUIGroupListView(context);
        setItemsIn(page,songsInPage,songOnClickFunc);
        return page;
    }

    private void setItemsIn(QMUIGroupListView page,List<SongVO> songsInPage,OnClickFunc<SongVO> songOnClickFunc){
        QMUIGroupListView.Section section=QMUIGroupListView.newSection(context);

        //为这个歌单每一首歌构建一个Item，并把它们放入同一个Section中
        for(SongVO song : songsInPage){
            //构建Item
            QMUICommonListItemView songItem=item(song,page);
            View.OnClickListener onClickListener= v-> songOnClickFunc.onclick(song,songItem);
            //将Item放入Section中
            section.addItemView(songItem,onClickListener);
        }

        //将这个Section放入GroupListView中（每个GroupListView只设置一个Section，虽然实际上允许放多个Section）
        section.addTo(page);
    }

    private QMUICommonListItemView item(SongVO song,QMUIGroupListView page){
        return page.createItemView(
                null,
                song.getName(),
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE
        );
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //获取相应视图
        View view=pages.get(position);

        //构造视图参数
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //向容器中添加视图
        container.addView(view,params);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //从容器中移除视图
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //强制每次刷新视图
        return POSITION_NONE;
    }

}
