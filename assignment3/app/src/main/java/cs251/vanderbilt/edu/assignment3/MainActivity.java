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
    // @@ Prefix these member variables with "m"
    public static EditText editText;
    private TableLayout tableL;

    // @@ Insert a brief comment explaining what this method does
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = (EditText) findViewById(R.id.editText);

        // @@ Move this permission check to right before you call downloadImage
        int permissionCheck = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // @@ Always use braces around if statements
        // @@ https://google.github.io/styleguide/javaguide.html#s4.1.1-braces-always-used
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            this.requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        Button button = (Button) findViewById(R.id.button);
        tableL = (TableLayout) findViewById(R.id.table);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                // @@ Consider making this a Factory method in DownloadActivity
                Intent downloadIntent = new Intent(MainActivity.this, DownloadActivity.class);
                // @@ Consider moving to functionality that grabs the text from editText to a separate method
                downloadIntent.setData(Uri.parse(editText.getText().toString().trim()));
                startActivityForResult(downloadIntent, DOWNLOAD_IMAGE);
            }
        });
    }
    // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace

    // @@ Insert a brief comment explaining what this method does
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DOWNLOAD_IMAGE && resultCode == Activity.RESULT_OK){
            createOneFullRow(data);
        } else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Not working.", Toast.LENGTH_SHORT).show();
        }
    }

    // @@ Insert a brief comment explaining what this method does
    protected void createOneFullRow(Intent data) {
        LayoutInflater i = getLayoutInflater();
        final Uri path = data.getData();
        TableRow tableRow = (TableRow) i.inflate(R.layout.table_row, null, false);

        tableL.addView(tableRow);

        TextView timeView = (TextView) tableRow.findViewById(R.id.dl_time);
        // @@ Use a named constant instead of "timeDownload"
        // @@ For example: public static final String "IMAGE_DOWNLOAD_TIME"
        String time = data.getStringExtra("timeDownload");
        timeView.setText(time);
        TextView imageName = (TextView) tableRow.findViewById(R.id.image_name);

        // @@ Consider moving this to a separate method; too much String processing in one line
        String filename = data.toString().substring(data.toString().lastIndexOf("/")+1, data.toString().length()-1);
        imageName.setText(filename);

        Button openGallery = (Button) findViewById(R.id.button2);
        final String imagePath = "file://" + path.toString();

        openGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("main", imagePath);

                // @@ Consider making this a separate method
                Intent intent = new Intent();
                // @@ Should be able to use Intent.ACTION_VIEW instead of Intent.ACTION_GET_CONTENT
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.parse(imagePath), "image/*");
                startActivity(intent);

            }
        });
    }
}
