package sana_.photo;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private void init(){

        ChooseBtn = (Button)findViewById(R.id.ChooseBtn);
        UploadBtn = (Button)findViewById(R.id.UploadBtn);
        ShowBtn = (Button)findViewById(R.id.ShowBtn);
        editDesc = (EditText)findViewById(R.id.editDesc);
        imageView = (ImageView)findViewById(R.id.imageView);

    }

    Button ChooseBtn, UploadBtn, ShowBtn;
    ImageView imageView;
    EditText editDesc;

    public static SQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        sqLiteHelper =  new SQLiteHelper(this, "ImagesDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS IMAGES (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, description VARCHAR, image BLOB)");

        ChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY
                );
            }
        });

        UploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sqLiteHelper.insertData(editDesc.getText().toString().trim(),
                            imageViewToByte(imageView)
                    );
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    editDesc.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "NOT Uploaded", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }

            else{
                Toast.makeText(getApplicationContext(), "You don't have permission", Toast.LENGTH_SHORT).show();

            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
