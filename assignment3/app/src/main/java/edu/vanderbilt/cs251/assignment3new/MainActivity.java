package edu.vanderbilt.cs251.assignment3new;


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

    public static EditText mEditText;

    private TableLayout mtableL;

    private CustomHandler mReplyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //create a thread with looper and let instance of DownloadImageHandler handle its messages
        HandlerThread imageThread = new HandlerThread("Image");
        imageThread.start();
        //looper dispatches message to DownloadImageHandler
        final DownloadImageHandler dih = new DownloadImageHandler(imageThread.getLooper(), getApplicationContext());

        mEditText = (EditText) findViewById(R.id.editText);
        Button buttonService = (Button) findViewById(R.id.button);
        Button buttonThread = (Button) findViewById(R.id.button2);
        mtableL = (TableLayout) findViewById(R.id.table);

        mReplyHandler = (CustomHandler) new Handler(Looper.getMainLooper());

        //event that happens when you click Download Service

        buttonService.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent serviceIntent = ImageIntentService.makeIntent(getURI(), mReplyHandler);

                int permissionCheck = MainActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    MainActivity.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

                startService(serviceIntent);
            }
        });

        buttonThread.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int permissionCheck = MainActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    MainActivity.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

                Messenger bgMessenger = new Messenger(dih);
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
        });
    }

    public class CustomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //msg contains the path to the downloaded file
            createOneFullRow(msg);
        }
    }

    private Uri getURI() {
        return Uri.parse(mEditText.getText().toString().trim());
    }

    /**
     * This method will create a row that displays the image download information and open the picture
     */
    protected void createOneFullRow(Message data) {
        LayoutInflater i = getLayoutInflater();
        final String path = (String) data.obj; //string of downloadpath
        TableRow tableRow = (TableRow) i.inflate(R.layout.table_row, null, false);

        mtableL.addView(tableRow);

        TextView timeView = (TextView) tableRow.findViewById(R.id.dl_time);

        final String time = Integer.toString(data.arg1); //download time
        timeView.setText(time);
        TextView imageName = (TextView) tableRow.findViewById(R.id.image_name);


        final Bundle storeFileName = data.getData();
        String filename = storeFileName.toString();
        imageName.setText(filename);

        Button openGallery = (Button) findViewById(R.id.button2);
        final Uri imagePath = Uri.parse("file://" + path.toString());

        openGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("main", imagePath.toString());

                // @@ Consider making this a separate method
                Intent intent = new Intent();
                // @@ Should be able to use Intent.ACTION_VIEW instead of Intent.ACTION_GET_CONTENT
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(imagePath, "image/*");
                startActivity(intent);

            }
        });
    }
}
