package com.example.win.easy.repository.task;

import com.example.win.easy.repository.web.domain.NetworkSong;

public class SongDownloadTask implements Runnable {

    private NetworkSong networkSong;
    private long songId;

    public SongDownloadTask(NetworkSong networkSong,long songId){
        this.networkSong=networkSong;
        this.songId=songId;
    }

    @Override
    public void run() {
        //TODO 下载及跟新数据库逻辑
//        String filename=networkSong.totalName+networkSong.extensionName;
//
//        try {
//            URL url=new URL(networkSong.songUrl);
//            URLConnection connection=url.openConnection();
//            connection.connect();
//            long lengthOfFile=connection.getContentLength();
//            long total=0;
//            int count=0;
//            Log.d("Prepare to download","Length of file: "+lengthOfFile);
//            InputStream networkIn=new BufferedInputStream(url.openStream());
//            OutputStream localOut=new FileOutputStream();
//            byte[] dataBuffer=new byte[1024];
//            while ((count=networkIn.read(dataBuffer))!=-1){
//                total+=count;
//                Log.d("Progress","Has Finished: "+(total*100/lengthOfFile)+"%");
//                localOut.write(dataBuffer,0,count);
//            }
//            localOut.flush();
//            localOut.close();
//            networkIn.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
