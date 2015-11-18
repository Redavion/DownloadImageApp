package vandy.cs251;


import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Looper;

import java.io.File;

public class MainActivity extends Activity {

    //text field for user to input url
    public static EditText mEditText;
    //TableLayout
    private TableLayout mtableL;
    //Handler for main thread
    private CustomHandler mReplyHandler;
    //Messenger for messages sent to background thread
    private Messenger bgMessenger;
    //instance of DownloadImageHandler
    private DownloadImageHandler dih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //create a thread with looper and let instance of DownloadImageHandler handle its messages
        HandlerThread imageThread = new HandlerThread("Image");
        imageThread.start();
        //looper dispatches message to DownloadImageHandler
        dih = new DownloadImageHandler(imageThread.getLooper(), getApplicationContext());
        bgMessenger = new Messenger(dih);

        mEditText = (EditText) findViewById(R.id.editText);
        Button buttonService = (Button) findViewById(R.id.button);
        Button buttonThread = (Button) findViewById(R.id.button2);
        mtableL = (TableLayout) findViewById(R.id.table);

        mReplyHandler = new CustomHandler();

        //event that happens when you click Download Service

        buttonService.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clickService();
            }
        });

        //event that happens when you click Download Thread

        buttonThread.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                clickThread();
            }
        });
    }

    /*
    When main thread receives a message, it will pass it to the method createOneFullRow
    */
    public class CustomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //msg contains the path to the downloaded file
            createOneFullRow(msg);
        }
    }
    /*
    Method that retrieves the URI from user input mEditText field
    */
    private Uri getURI() {
        String text = mEditText.getText().toString();
        if (text.isEmpty()) {
            text = "http://balasub.com/attheshire.jpg";
        }

        return Uri.parse(text);
    }

    /**
     * This method will create a row that displays the image download information and open the picture
     */
    protected void createOneFullRow(Message data) {
        LayoutInflater i = getLayoutInflater();
        final String path =  data.obj.toString(); //string of downloadpath
        TableRow tableRow = (TableRow) i.inflate(R.layout.table_row, null, false);

        mtableL.addView(tableRow);

        TextView timeView = (TextView) tableRow.findViewById(R.id.dl_time);

        final String time = Integer.toString(data.arg1); //download time
        timeView.setText(time);
        TextView imageName = (TextView) tableRow.findViewById(R.id.image_name);


        final Bundle storeFileName = data.getData();
        String filename = (String) storeFileName.getCharSequence(ImageIntentService.FILE_NAME);
        imageName.setText(filename);

        Button openGallery = (Button) tableRow.findViewById(R.id.galleryButton);
        final Uri imagePath = Uri.parse("file://" + path);

        openGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickGallery(imagePath);
            }
        });

    }

    /*
    This method will create an intent to start a service and perform permission checks
    */
    protected void clickService() {
        Intent serviceIntent = ImageIntentService.makeIntent(getURI(), mReplyHandler, getApplicationContext());

        int permissionCheck = MainActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            MainActivity.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        startService(serviceIntent);
    }

    /*
    This method will create a message to send to the background thread and perform permission checks
    */
    protected void clickThread() {
        int permissionCheck = MainActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            MainActivity.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Message msgToBackground = Message.obtain();
        msgToBackground.obj = getURI();
        msgToBackground.replyTo = new Messenger(mReplyHandler);
        //send the message to the background messenger
        try {
            bgMessenger.send(msgToBackground);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    /*
    This method will take the path to the image and open the image in the gallery
    */
    protected void clickGallery(Uri imagePath){
        Log.w("main", imagePath.toString());

        Intent intent = new Intent();
        // @@ Should be able to use Intent.ACTION_VIEW instead of Intent.ACTION_GET_CONTENT
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(imagePath, "image/*");
        startActivity(intent);
    }

}


