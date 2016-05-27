package com.marshmallowdemo_androidhive;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    public static Context context;
    TextView textView;
    Button btnUser, btnOK, btnCancel;
    ImageView ivUser;
    Uri outPutfileUri;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ivUser = (ImageView) findViewById(R.id.iv_user);
        btnUser = (Button) findViewById(R.id.btn_user);


        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    Toast.makeText(Main2Activity.this, "Camera Allowed fro <=23 API..........", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 123);
                } else {
                    Toast.makeText(Main2Activity.this, "Camera Allowed for >23 API..........", Toast.LENGTH_SHORT).show();
                    dispatchTakePictureIntent();
                }
            }
        });

        //checking self permissions and providing the permission for camera and storage
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PLAYGROUND", "Permission is not granted, requesting");
            // FragmentCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 124);
            btnUser.setEnabled(false);
        } else {
            Log.d("PLAYGROUND", "Permission is granted");
        }

    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.CAMERA}, 124);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            Log.i("TakingPictureIntent", "Trying to take a photo");
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(Main2Activity.this.getPackageManager()) != null && checkHasCamera(Main2Activity.this.getApplicationContext())) {

                startActivityForResult(takePictureIntent, 124);
            }
        }
    }


    private File getTempFile(Context context) {
        final File path = new File(Environment.getExternalStorageDirectory(),
                context.getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, "myImage.png");
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(Main2Activity.this, "Camera Allowed..........", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(Main2Activity.this, "Camera Denied...........", Toast.LENGTH_SHORT).show();
                }
                break;
            case 124:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    btnUser.setEnabled(true);
                    Toast.makeText(Main2Activity.this, "Camera Allowed..........", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(Main2Activity.this, "Camera Denied...........", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Hi.............I am in onActivityResult");
        if (requestCode == 123) {

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");

                Log.i("ImageWidth = " + bmp.getWidth(), "ImageHeight = " + bmp.getHeight());
                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bmp);
                System.out.println("\n\n\n\nbitmap image size is ::::   " + bitmapByteCount);

                ivUser.setImageBitmap(bmp);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }


        // if (requestCode == 124) {

        if (requestCode == 124 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivUser.setImageBitmap(imageBitmap);
            // ContactsContract.Contacts.Photo photo = new ContactsContract.Contacts.Photo(imageBitmap);
            // vehicle.setPhoto(photo);
            //ivUser.setBackground(new BitmapDrawable(getResources(), imageBitmap));

            /*if (resultCode == DrawerActivity.RESULT_OK) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivUser.setImageBitmap(imageBitmap);*/
            System.out.println("Hi.............I am in onActivityResult 124" + imageBitmap);
        }
    }



    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(Main2Activity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public boolean checkHasCamera(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
}
