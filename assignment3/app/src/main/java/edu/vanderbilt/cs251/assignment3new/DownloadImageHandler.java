package edu.vanderbilt.cs251.assignment3new;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by alisonchen on 11/15/15.
 */

/*Clicking the “Download Thread” button will download the image using a
        background thread that has an associated MessageQueue and Handler. You should
        send your download request to the thread’s MessageQueue using a custom Handler
        object; you will also use this custom Handler to process the request. Communication
        from the background thread back to the UI thread should done using a Messenger
        (which is a wrapper around a Handler) that posts a Message to the MessageQueue of
        the main UI thread. This Messenger can be passed using the “replyTo” field of the
        request Message.
        */

public class DownloadImageHandler extends Handler {

    public static final String FILE_NAME= "FileName";

    private Context mContext;

    public DownloadImageHandler(Looper looper, Context context ){
        super(looper);
        mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {

        long mTotalTime;
        long beginTime = System.currentTimeMillis();

        Uri absolutePath = DownloadUtils.downloadImage(mContext, (Uri) msg.obj);

        long endTime = System.currentTimeMillis();
        mTotalTime = endTime - beginTime;
        Message replyMsg= Message.obtain();
        replyMsg.obj = absolutePath;

        String fileName= (String) msg.obj;
        fileName = fileName.substring(fileName.lastIndexOf("/")+1, fileName.length()-1);

        Bundle b = new Bundle();
        b.putString(FILE_NAME, fileName);
        replyMsg.setData(b); //store filename into data field of message

        replyMsg.arg1 = (int) mTotalTime; //store download time in arg1
        Messenger backgroundMessenger = msg.replyTo;

        try {
            backgroundMessenger.send(replyMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
