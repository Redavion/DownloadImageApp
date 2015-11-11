package edu.vanderbilt.cs251.assignment3new;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.content.pm.PackageManager;

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

    private class Download extends AsyncTask<Uri, Void, Uri> {
        // @@ Prefix this member variable with "m"
        long mtotalTime;
        private static final String TIME_DOWNLOAD= "timeDownload";
        // @@ Insert a brief comment explaining what this method does

        /**
         * This method downloads an Image to a local server and records the total time taken
         */
        @Override
        protected Uri doInBackground(Uri[] params) {

            long beginTime = System.currentTimeMillis();

            Uri absolutePath= DownloadUtils.downloadImage(DownloadActivity.this, params[0]);

            long endTime = System.currentTimeMillis();

            mtotalTime = endTime - beginTime;

            return absolutePath;
        }

        // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace

        // @@ Insert a brief comment explaining what this method does
        /**
         * This method stores download time into an Intent
         */
        @Override
        protected void onPostExecute(Uri o) {

            Log.w("DLActivity", o.toString());

            Intent intent = new Intent("", o);
            // @@ Use a named constant here instead of "timeDownload"
            intent.putExtra(TIME_DOWNLOAD,  String.valueOf(mtotalTime));

            // @@ Simplify this so that setResult is only called once
            if (o != null) {
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED, intent);
            }
            finish();

            // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace


        }


    }
}
