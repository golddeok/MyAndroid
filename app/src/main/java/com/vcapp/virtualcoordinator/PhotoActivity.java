package com.vcapp.virtualcoordinator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;  //카메라 촬영
    private static final int PICK_FROM_ALBUM = 1;	//앨범 선택
    private static final int CROP_FROM_CAMERA = 2;	//카메라 촬영후 크롭

    private static final String TYPE_IMAGE = "image/*";

    private static final int PROFILE_IMAGE_ASPECT_X = 1;
    private static final int PROFILE_IMAGE_ASPECT_Y = 1;
    private static final int PROFILE_IMAGE_OUTPUT_X = 600;
    private static final int PROFILE_IMAGE_OUTPUT_Y = 600;
    private static final String TEMP_FILE_NAME = "tempFile.jpg";

    private Uri mImageCaptureUri;



    private byte[] imgbyte;
    private EditText mEdityEntry;

    private Bitmap photo;
    private ImageView mPhotoImageView;
    private Uri mTempImageUri;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Button btn = (Button) findViewById(R.id.btn_send);
        btn.setOnClickListener(this);

        Button album_btn = (Button) findViewById(R.id.btn_album);
        album_btn.setOnClickListener(this);

        Button camera_btn = (Button) findViewById(R.id.btn_camera);
        camera_btn.setOnClickListener(this);

        mPhotoImageView = (ImageView) findViewById(R.id.img_imageview);

        mEdityEntry = (EditText)findViewById(R.id.dateEditText);

        btn1 = (Button)findViewById(R.id.btn_save);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {
            try {

                String mFilePath="";
                //DoFileUpload(mFilePath);
            } catch (Exception e) {
            }

        } else if (v.getId() == R.id.btn_album) {
            doAlbumCrop();
        } else if (v.getId() == R.id.btn_camera) {
            Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE, null);
            startActivityForResult( intent, PICK_FROM_CAMERA );
        }

    }

    private File getTempFile(){
        File file = new File( Environment.getExternalStorageDirectory(), TEMP_FILE_NAME );
        try{
            file.createNewFile();
        }
        catch( Exception e ){
            Log.e("cklee", "fileCreation fail" );
        }
        return file;
    }


    private Uri getJustTakenPictureUri(){
        Cursor cursor = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{ MediaStore.Images.ImageColumns.DATA }, null, null, null );
        if ( cursor == null ) return null;
        String fileName = null;
        if ( cursor.moveToLast())
            fileName = cursor.getString(0 );
        cursor.close();
        if ( TextUtils.isEmpty( fileName ) ) return null;
        return Uri.fromFile( new File( fileName ) );
    }

//앨범호출
    private void doAlbumCrop(){
        mTempImageUri = Uri.fromFile( getTempFile() );
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType( TYPE_IMAGE );
        intent.putExtra( "crop", "true" );
        intent.putExtra( "aspectX", PROFILE_IMAGE_ASPECT_X );
        intent.putExtra( "aspectY", PROFILE_IMAGE_ASPECT_Y );
        intent.putExtra( "outputX", PROFILE_IMAGE_OUTPUT_X );
        intent.putExtra( "outputY", PROFILE_IMAGE_OUTPUT_Y);
        intent.putExtra( "scale", true );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, mTempImageUri );
        intent.putExtra( "outputFormat", Bitmap.CompressFormat.JPEG.toString() );
        startActivityForResult( intent, PICK_FROM_ALBUM );
    }

    //카메라 호출
    private void doCameraCrop(){
        Uri justTakenPictureUri = getJustTakenPictureUri();

        mTempImageUri = Uri.fromFile( getTempFile() );
        Intent intent = new Intent( "com.android.camera.action.CROP" );

        intent.setDataAndType( justTakenPictureUri, TYPE_IMAGE );
        intent.putExtra( "scale", true );
        intent.putExtra( "aspectX", PROFILE_IMAGE_ASPECT_X );
        intent.putExtra( "aspectY", PROFILE_IMAGE_ASPECT_Y );
        intent.putExtra( "outputX", PROFILE_IMAGE_OUTPUT_X);
        intent.putExtra( "outputY", PROFILE_IMAGE_OUTPUT_Y);
        intent.putExtra( MediaStore.EXTRA_OUTPUT, mTempImageUri );
        startActivityForResult( intent,  CROP_FROM_CAMERA );
    }


    private void doImageView(){
        File tempFile = getTempFile();
        if ( tempFile.exists() )
            ((ImageView)findViewById( R.id.img_imageview )).setImageBitmap( BitmapFactory.decodeFile( tempFile.toString()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_CAMERA:{
                doCameraCrop();
                break;
            }
            case PICK_FROM_ALBUM: {
                doImageView();
            }

            case CROP_FROM_CAMERA: {
                doImageView();
            }
        }
    }

    public static String encodeTobase64(Bitmap image){
        Bitmap bitmap_image = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap_image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }



}
