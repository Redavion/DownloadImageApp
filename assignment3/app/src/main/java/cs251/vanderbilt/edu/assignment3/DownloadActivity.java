package cs251.vanderbilt.edu.assignment3;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;


/**
 * Created by alisonchen on 10/27/15.
 */
public class DownloadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri myData = getIntent().getData();
        new Download().execute(myData);
    }

    private class Download extends AsyncTask<Uri,Void,Uri>{
        long totalTime;

        @Override
        protected Uri doInBackground(Uri[] params) {

            long beginTime = System.currentTimeMillis();

            Uri absolutePath= DownloadUtils.downloadImage(DownloadActivity.this, params[0]);

            long endTime = System.currentTimeMillis();

            totalTime= endTime-beginTime;

            return absolutePath;
        }



        @Override
        protected void onPostExecute(Uri o) {

            Log.w("DLActivity", o.toString());

            Intent intent = new Intent("",o);
            intent.putExtra("timeDownload",  String.valueOf(totalTime));
            if (o !=null) {
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED, intent);
            }
            finish();




        }


    }
}
