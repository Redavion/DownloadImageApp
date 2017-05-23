package vandy.cs251;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.EditText;
<<<<<<< HEAD
import java.io.File;
/**
 * Created by alisonchen on 11/15/15.
 */


public class ImageIntentService extends IntentService {


=======

public class ImageIntentService extends IntentService {
    // @@ Use vertical whitespace to increase readability:
    // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace
>>>>>>> e5314424c0a53994c311981985595fd1a9cfc93d

    //key for messenger replyMessenger placed in intent to start service
    public static final String MESSENGER_OBJECT= "messenger";
    //key for string fileName stored in bundle in return message's data field
    public static final String FILE_NAME= "FileName";
    public ImageIntentService() {
        super("ImageIntentService");
    }

    /*
    Factory method that stores address and messenger into an newly created intent
    */
    public static Intent makeIntent(Uri address, Handler mReplyHandler, Context context) {
        Intent serviceIntent = new Intent(context, ImageIntentService.class);
        serviceIntent.setData(address);
        Messenger replyMessenger = new Messenger(mReplyHandler);
        serviceIntent.putExtra(MESSENGER_OBJECT, replyMessenger);
        return serviceIntent;
    }
    /*
    Takes intent and downloads the url into a local server. Then returns time to download, file name,
    and absolute path in a message.
    */
    public void onHandleIntent (Intent data) {
        //create a messenger that can get the messenger object from your intent
        Messenger replyMessenger = data.getParcelableExtra(MESSENGER_OBJECT);
        long mTotalTime;
        long beginTime = System.currentTimeMillis();
        Uri absolutePath = DownloadUtils.downloadImage(getApplicationContext(), data.getData());
        long endTime = System.currentTimeMillis();
        mTotalTime = endTime - beginTime;

<<<<<<< HEAD

=======
        // @@ Move this to a separate method to reduce the length of onHandleIntent
>>>>>>> e5314424c0a53994c311981985595fd1a9cfc93d
        Message returnFilePath = Message.obtain();
        // @@ Why not use the setData method instead of the obj field?
        returnFilePath.obj = absolutePath; //store absolute path of downloaded file in object

<<<<<<< HEAD
         String filename = getFileName(data);
=======
        // @@ You need to handle the case where downloadImage returns null; if you don't,
        // @@ then getFileName could throw an exception
        String filename = getFileName(data);
>>>>>>> e5314424c0a53994c311981985595fd1a9cfc93d

        Bundle b = new Bundle();
        b.putCharSequence(FILE_NAME, filename);
        returnFilePath.setData(b); //store filename into data field of message

        returnFilePath.arg1 = (int) mTotalTime; //store download time in arg1

        try {
            replyMessenger.send(returnFilePath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will return the file name
     */
<<<<<<< HEAD
    protected String getFileName(Intent data) {
        if (data.getDataString() != null) {

            String filePath = data.getDataString();
            File f = new File(filePath);
            return f.getName();

        } else {
            return "null";
        }
=======
    protected String getFileName(Intent data){
        String filePath= data.getDataString();
        // @@ Simplify this. For instance, use the getName method of the File class
        return filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
>>>>>>> e5314424c0a53994c311981985595fd1a9cfc93d
    }

}


