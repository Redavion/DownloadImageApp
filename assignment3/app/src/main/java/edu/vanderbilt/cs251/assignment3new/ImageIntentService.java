package edu.vanderbilt.cs251.assignment3new;

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

/**
 * Created by alisonchen on 11/15/15.
 */


//Clicking the “Download Service” button will download the image using a Service.
//        Graduate students must implement a bound service; undergraduates can implement
//        either a started service (e.g., with the IntentService class) or a bound service.
public class ImageIntentService extends IntentService {

    public static final String MESSENGER_OBJECT= "messenger";
    public static final String FILE_NAME= "FileName";

    public ImageIntentService() {
        super("ImageIntentService");
    }

    public static Intent makeIntent(Uri address, Handler mReplyHandler) {
        Intent serviceIntent = new Intent();
        serviceIntent.setData(address);
        Messenger replyMessenger = new Messenger(mReplyHandler);
        serviceIntent.putExtra(MESSENGER_OBJECT, replyMessenger);
        return serviceIntent;
    }

    public void onHandleIntent (Intent data) {
        //create a messenger that can get the messenger object from your intent
        Messenger replyMessenger = data.getParcelableExtra(MESSENGER_OBJECT);
        long mTotalTime;
        long beginTime = System.currentTimeMillis();
        Uri absolutePath = DownloadUtils.downloadImage(ImageIntentService.this, data.getData());
        long endTime = System.currentTimeMillis();
        mTotalTime = endTime - beginTime;

        //Message needs path to downloaded file, name, download time
        Message returnFilePath = Message.obtain();
        returnFilePath.obj = absolutePath; //store absolute path of downloaded file in object

        String filename = getFileName(data);

        Bundle b = new Bundle();
        b.putString(FILE_NAME, filename);
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
    protected String getFileName(Intent data){
        String filePath= data.toString();
        return filePath.substring(data.toString().lastIndexOf("/")+1, data.toString().length()-1);
    }

}


