package com.example.win.easy.download;

import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<Void, Void, Void>{
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }


//    public boolean download(String urlStr,String filename){
//        int count;
//        byte[] dataBuffer = new byte[1024];
//        try {
//            URLConnection connection = new URL(urlStr).openConnection();
//            connection.connect();
//            File file=new File(filename);
//            if(!file.exists()){
//                file.getParentFile().mkdirs();
//                file.createNewFile();
//            }
//            InputStream networkIn = new BufferedInputStream(connection.getInputStream());
//            OutputStream localOut = new FileOutputStream(filename);
//            while ((count = networkIn.read(dataBuffer)) != -1)
//                localOut.write(dataBuffer, 0, count);
//            localOut.flush();
//            localOut.close();
//            networkIn.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
}
