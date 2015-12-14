package vandy.cs251;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;

/**
 * Created by alisonchen on 11/15/15.
 */



public class DownloadImageHandler extends Handler {


    //key for string fileName stored in bundle in return message's data field
    public static final String FILE_NAME= "FileName";
    //context to pass into constructor
    private Context mContext;
    /*
    Constructor for DIH
    */
    public DownloadImageHandler(Looper looper, Context context ){
        super(looper);
        mContext = context;
    }

    /*
    Method that takes a message with a url in it and returns another message back to main thread
    containing time to download, file name, and absolute path
    */
    @Override
    public void handleMessage(Message msg) {

        Log.w("main", "Inside DIH handleMessage");
        long mTotalTime;
        long beginTime = System.currentTimeMillis();

        Uri absolutePath = DownloadUtils.downloadImage(mContext, (Uri) msg.obj);

        long endTime = System.currentTimeMillis();
        mTotalTime = endTime - beginTime;
        Message replyMsg = Message.obtain();
        replyMsg.obj = absolutePath;
        String fileName= "null";

        if (msg.obj != null) {
            fileName = msg.obj.toString();
            fileName = getFileName(fileName);
        }

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


    protected String getFileName(String fileName){

        File f = new File(fileName);
        return f.getName();

    }
}
