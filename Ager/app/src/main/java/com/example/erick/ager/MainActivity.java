package com.example.erick.ager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final int IMAGE_GALLERY = 10;
    private static int RESULT_LOAD_IMG = 1;
    
    String imgDecodableString;

    public static final int CAMERA_REQUEST = 2;
    private ImageView temporalPhoto;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get access to the image view
        temporalPhoto = (ImageView) findViewById(R.id.photo_area);

    }

    /*public void btnTakePhotoClicked(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                temporalPhoto.setImageBitmap(cameraImage);
            }
        }

    }*/

    //Directory picker-------------------------------------------------------------------------------
    public void loadImageFromGallery(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    /*
    public void loadImageFromGallery(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();

        Uri picturesDirectory = Uri.parse(path);

        photoPickerIntent.setDataAndType(picturesDirectory, "images/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                ImageView imgView = (ImageView) findViewById(R.id.photo_area);
                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
        } else if(requestCode == IMAGE_GALLERY && resultCode == RESULT_OK){

            Uri photoLocation = data.getData();
            try {
                InputStream openInputStream = getContentResolver().openInputStream(photoLocation);

                selectedImage = BitmapFactory.decodeStream(openInputStream);

                temporalPhoto.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG).show();
        }
    }


}
