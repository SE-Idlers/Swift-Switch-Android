package com.example.win.easy.view.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.win.easy.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 具有一个TopBar，一个右上角按钮和一个GroupListView的模板
 */
public abstract class ListFragment extends Fragment {

    public static final int CONTENT_ALL_SONGS=0;
    public static final int CONTENT_ALL_SONG_LISTS=1;
    @BindView(R.id.fragment_list_top_bar) QMUITopBar topBar;
    @BindView(R.id.fragment_list_group_list) QMUIGroupListView groupListView;
    QMUIAlphaImageButton imageButton;

    /**
     * 创建视图时的行为包括：<br/>
     * <span>
     *     <li>从xml文件中抽取视图</li>
     *     <li>将id绑定到本地的变量</li>
     *     <li>为topBar右边添加一个button</li>
     * </span>
     * @param inflater 系统提供
     * @param container 包含整个Fragment的Activity
     * @param savedInstanceState 传递下来的属性
     * @return 这个视图本身
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View thisView=inflater.inflate(R.layout.fragment_list_view,container,false);
        ButterKnife.bind(this,thisView);
        imageButton=topBar.addRightImageButton(R.drawable.ic_action_name,new ImageButton(getContext()).getId());
        return thisView;
    }

    /**
     * 供子类设置标题
     * @param title topBar的标题
     */
    public void setTopBarTitle(String title){
        topBar.setTitle(title);
    }

    /**
     * 供子类设置右上角按钮的监听
     * @param listener 按钮监听
     */
    public void setRightImageButtonOnClickListener(View.OnClickListener listener){
        imageButton.setOnClickListener(listener);
    }

    /**
     * 一个异步的解码图片的任务<br/>
     * 因为解码需要涉及磁盘IO及图片处理，可能会阻塞主线程，故在此设置一个异步Task专门处理图片的读取与解码,在结束后会自动更新传入的itemView
     */
    public static class DecodeImageAsyncTask extends AsyncTask<String,Void, Bitmap>{

        private WeakReference<QMUICommonListItemView> itemViewWeakReference;
        private Resources resources;

        public DecodeImageAsyncTask(QMUICommonListItemView itemView,Resources resources){
            this.itemViewWeakReference=new WeakReference<>(itemView);
            this.resources=resources;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return BitmapFactory.decodeFile(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            itemViewWeakReference.get().setImageDrawable(new BitmapDrawable(resources,bitmap));
        }


    }

}
