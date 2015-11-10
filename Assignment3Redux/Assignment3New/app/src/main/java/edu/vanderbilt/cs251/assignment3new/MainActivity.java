package edu.vanderbilt.cs251.assignment3new;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
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
import cs251.vanderbilt.edu.assignment3.R;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int DOWNLOAD_IMAGE= 1;
    public static EditText meditText;
    private TableLayout mtableL;
    public static final String IMAGE_DOWNLOAD_TIME= "timeDownload";

    /**
     * This method starts DownloadActivity and sends it an intent upon receiving the click event
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        meditText = (EditText) findViewById(R.id.editText);

        int permissionCheck = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Button button = (Button) findViewById(R.id.button);
        mtableL = (TableLayout) findViewById(R.id.table);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {

                Intent downloadIntent = new Intent(MainActivity.this, DownloadActivity.class);
                downloadIntent.setData(Uri.parse(meditText.getText().toString().trim()));
                startActivityForResult(downloadIntent, DOWNLOAD_IMAGE);

            }
        });
    }

    /**
     * This method checks the result it receives from DownloadActivity and if it is valid
     * it will call createOneFullRow.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DOWNLOAD_IMAGE && resultCode == Activity.RESULT_OK){
            createOneFullRow(data);
        } else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Not working.", Toast.LENGTH_SHORT).show();

        }

    }
    /**
     * This method will create a row that displays the image download information and open the picture
     */
    protected void createOneFullRow(Intent data) {
        LayoutInflater i = getLayoutInflater();
        final Uri path = data.getData();
        TableRow tableRow = (TableRow) i.inflate(R.layout.table_row, null, false);

        mtableL.addView(tableRow);

        TextView timeView = (TextView) tableRow.findViewById(R.id.dl_time);


        String time = data.getStringExtra(IMAGE_DOWNLOAD_TIME);
        timeView.setText(time);
        TextView imageName = (TextView) tableRow.findViewById(R.id.image_name);

        String filename = getFileName(data);
        imageName.setText(filename);

        Button openGallery = (Button) findViewById(R.id.button2);
        final String imagePath = "file://" + path.toString();

        openGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("main", imagePath);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(imagePath), "image/*");
                startActivity(intent);

            }
        });
    }

    /**
     * This method will return the file name
     */
    protected String getFileName(Intent data){

        return data.toString().substring(data.toString().lastIndexOf("/")+1, data.toString().length()-1);
    }
}
