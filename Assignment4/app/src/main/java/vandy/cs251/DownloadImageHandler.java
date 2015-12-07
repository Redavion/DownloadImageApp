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

public class DownloadImageHandler extends Handler {
    // @@ Same thing here: use vertical whitespace for readability:
    // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace

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
        // @@ Use horizontal whitespace on the line below
        // @@ https://google.github.io/styleguide/javaguide.html#s4.6.2-horizontal-whitespace
        Message replyMsg= Message.obtain();
        replyMsg.obj = absolutePath;

        // @@ You need to handle the case where downloadImage returns null; if you don't,
        // @@ then the line below could throw an exception if obj is null
        String fileName= msg.obj.toString();
        // @@ Simplify this; something like the getName method of the File class or
        fileName = fileName.substring(fileName.lastIndexOf("/")+1, fileName.length());

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
