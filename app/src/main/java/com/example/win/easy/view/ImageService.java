package com.example.win.easy.view;

import android.graphics.Bitmap;

import com.example.win.easy.web.callback.OnReadyFunc;

public class ImageService {


    public void decode(String imagePath, OnReadyFunc<Bitmap> onReadyFunc){
        //TODO 解码图片的服务
    }
/////**
//// * 一个异步的解码图片的任务<br/>
//// * 因为解码需要涉及磁盘IO及图片处理，可能会阻塞主线程，故在此设置一个异步Task专门处理图片的读取与解码,在结束后会自动更新传入的itemView
// */
//    class DecodeImageAsyncTask extends AsyncTask<String,Void, Bitmap> {
//
//        private WeakReference<QMUICommonListItemView> itemViewWeakReference;
//        private Resources resources;
//
//        public DecodeImageAsyncTask(QMUICommonListItemView itemView,Resources resources){
//            this.itemViewWeakReference=new WeakReference<>(itemView);
//            this.resources=resources;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            return BitmapFactory.decodeFile(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap){
//            itemViewWeakReference.get().setImageDrawable(new BitmapDrawable(resources,bitmap));
//        }
//
//
//    }
}
