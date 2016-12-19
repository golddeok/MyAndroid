package com.vcapp.virtualcoordinator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ItemListviewAdd extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;
    private Button mButton;

    private EditText title;
    private EditText desc;
    private int refCount;
    private int samplePosition;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_listview_add);

        mButton = (Button) findViewById(R.id.button);
        mPhotoImageView = (ImageView) findViewById(R.id.image);
        title=(EditText)findViewById(R.id.addEditText1);
        desc=(EditText)findViewById(R.id.addEditText2);

        refCount=getIntent().getExtras().getInt("adapterCount");
        samplePosition=getIntent().getExtras().getInt("samplePosition");
        ///////////////////////////////////////
//        sharedPreferences=getSharedPreferences("TatstyLoad",MODE_PRIVATE);

        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);


        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                {
                    /*DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            doTakePhotoAction();
                        }
                    };*/

                    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            doTakeAlbumAction();
                        }
                    };

                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    };

                    new AlertDialog.Builder(ItemListviewAdd.this)
                            .setTitle("업로드할 이미지 선택")
                            //.setPositiveButton("사진촬영", cameraListener)
                            .setNeutralButton("앨범선택", albumListener)
                            .setNegativeButton("취소", cancelListener)
                            .show();
                }


            }
        });


    }

    private void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.


                mImageCaptureUri = data.getData();

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 150);
                intent.putExtra("outputY", 100);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.kaMenu1 :
                if(mPhotoImageView.getDrawable()==null){
                    Toast toast=Toast.makeText(ItemListviewAdd.this,"이미지를 넣어주세요.",Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                else if(title.getText().toString().trim().length()<=0){
                    Toast toast=Toast.makeText(ItemListviewAdd.this,"제목을 입력하세요..",Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                else if(desc.getText().toString().trim().length()<=0){
                    Toast toast=Toast.makeText(ItemListviewAdd.this,"설명을 입력하세요..",Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                else{

                    Intent intent = new Intent(ItemListviewAdd.this,ItemListviewSubAdd.class);
                    BitmapDrawable refImage = (BitmapDrawable)  mPhotoImageView.getDrawable();
                    Bitmap bitmap = refImage.getBitmap();
                    String imageString=encodeTobase64(bitmap);

                    intent.putExtra("adapterCount",refCount);
                    intent.putExtra("samplePosition",samplePosition);
                    intent.putExtra("imageString",imageString);
                    intent.putExtra("titleString",title.getText().toString());
                    startActivity(intent);

                    finish();


                    return true;

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish(){




        if(mPhotoImageView.getDrawable()!=null && title.getText().toString().trim().length()>0 && desc.getText().toString().trim().length()>0)
        {
            Intent intent=new Intent();
            BitmapDrawable refImage = (BitmapDrawable) mPhotoImageView.getDrawable();
            Bitmap bitmap = refImage.getBitmap();
            intent.putExtra("image",bitmap);
            intent.putExtra("title", title.getText().toString());
            intent.putExtra("desc", desc.getText().toString());
            intent.putExtra("returnCount",refCount);
            setResult(RESULT_OK,intent);

        }

        super.finish();



    }

    /*protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        EditText textBox1=(EditText)findViewById(R.id.addEditText1);
        EditText textBox2=(EditText)findViewById(R.id.addEditText2);
        ImageView image=(ImageView)findViewById(R.id.image);
        CharSequence userText1=textBox1.getText();
        CharSequence userText2=textBox2.getText();
        BitmapDrawable refImage = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = refImage.getBitmap();

        outState.putCharSequence("savedText1", userText1);
        outState.putCharSequence("savedText2", userText2);
        Intent i=new Intent();   //bitmap을 전달하기 위한 인텐트.bundle에 저장할수없기때문에
        i.putExtra("sendImage",bitmap); //intent에 bitmap 넣어서 전달
        Bundle subBundle = i.getExtras(); //intent 자체 전달 불가하여 번들에 정보저장
        outState.putBundle("bundle",subBundle); //메인 번들에 번들을 저장하여 보냄




    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        EditText textBox1=(EditText)findViewById(R.id.addEditText1);
        EditText textBox2=(EditText)findViewById(R.id.addEditText2);
        ImageView image=(ImageView)findViewById(R.id.image);
        CharSequence userText1=savedInstanceState.getCharSequence("savedText1");
        CharSequence userText2=savedInstanceState.getCharSequence("savedText2");
        Bundle receiveBundle=savedInstanceState.getBundle("bundle");  //보내온 번들을 꺼내서 저장
        Bitmap bitmap= (Bitmap) receiveBundle.get("sendImage");   //꺼낸 번들에서 bitmap 꺼냄

        image.setImageBitmap(bitmap);
        textBox1.setText(userText1);
        textBox2.setText(userText2);


    } */

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("작성을 종료하시겠습니까?");
        alt_bld.setCancelable(true);
        alt_bld.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title.setText("");
                        desc.setText("");
                        mPhotoImageView.setImageDrawable(null);

                        finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());

                    }
                });
        AlertDialog dialog = alt_bld.create();
        //dialog.setTitle("종료");
        dialog.show();
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
