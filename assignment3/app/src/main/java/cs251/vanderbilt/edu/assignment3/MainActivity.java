package cs251.vanderbilt.edu.assignment3;

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

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends Activity {

    private static final int DOWNLOAD_IMAGE= 1;
    public static EditText editText;
    private TableLayout tableL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) findViewById(R.id.editText);
        int permissionCheck = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            this.requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);



        Button button = (Button) findViewById(R.id.button);
        tableL = (TableLayout) findViewById(R.id.table);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {

                Intent downloadIntent = new Intent(MainActivity.this, DownloadActivity.class);
                downloadIntent.setData(Uri.parse(editText.getText().toString().trim()));
                startActivityForResult(downloadIntent, DOWNLOAD_IMAGE);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DOWNLOAD_IMAGE && resultCode == Activity.RESULT_OK){
            createOneFullRow(data);
        } else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Not working.", Toast.LENGTH_SHORT).show();

        }

    }

    protected void createOneFullRow(Intent data) {
        LayoutInflater i = getLayoutInflater();
        final Uri path = data.getData();
        TableRow tableRow = (TableRow) i.inflate(R.layout.table_row, null, false);

        tableL.addView(tableRow);

        TextView timeView = (TextView) tableRow.findViewById(R.id.dl_time);
        String time = data.getStringExtra("timeDownload");
        timeView.setText(time);
        TextView imageName = (TextView) tableRow.findViewById(R.id.image_name);

        String filename = data.toString().substring(data.toString().lastIndexOf("/")+1, data.toString().length()-1);
        imageName.setText(filename);

        Button openGallery = (Button) findViewById(R.id.button2);
        final String imagePath = "file://" + path.toString();

        openGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("main", imagePath);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.parse(imagePath), "image/*");
                startActivity(intent);

            }
        });
    }
}
