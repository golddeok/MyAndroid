package com.vcapp.virtualcoordinator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemListviewSubAdd extends AppCompatActivity {

    private final LatLng SEOUL = new LatLng(37.56, 126.97);

    private Uri mImageCaptureUri;
    private int refPosition;
    private int samplePosition;
    private int addCount=0;
    private String imageString;  /// ItemListviewAdd에서 온 image
    private String titleString; // ItemListviewAdd에서 온 title;
    private  LatLng refAddress;

    private Bundle refBundle;

    private int btnCount=0;

    private GoogleMap ksAddMap;
    private Button ksAddMapButton;

    private Button ImageAddButton;
    private EditText ksAddIntro;
    private EditText ksAddTime;
    private EditText ksAddDelivery;
    private EditText ksAddPhoneNum;
    private EditText ksAddHomepageText;
    private EditText ksAddAddress;
    private EditText ksAddOrigin;
    private ImageView ksAddImage1;
    private ImageView ksAddImage2;
    private ImageView ksAddImage3;
    private ImageView ksAddImage4;
    private ImageView ksAddImage5;
    private ImageView ksAddImage6;
    private ImageView ksAddImage7;
    private ImageView ksAddImage8;
    private ArrayList<ImageView> ksAddImage =new ArrayList<ImageView>();



    SharedPreferences sp;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_listview_sub_add);

        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);
        Context mContext=getBaseContext();

        refBundle=getIntent().getExtras();
        refPosition=refBundle.getInt("adapterCount");
        samplePosition=refBundle.getInt("samplePosition");
        imageString=refBundle.getString("imageString");
        titleString=refBundle.getString("title");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.ksAddMap);



        ksAddMapButton= (Button) findViewById(R.id.ksAddMapButton);
        ImageAddButton= (Button) findViewById(R.id.ksAddImageButton);
        ksAddIntro= (EditText) findViewById(R.id.ksAddIntro);
        ksAddTime= (EditText) findViewById(R.id.ksAddTime);
        ksAddDelivery= (EditText) findViewById(R.id.ksAddDelivery);
        ksAddPhoneNum= (EditText) findViewById(R.id.ksAddPhoneNum);
        ksAddHomepageText= (EditText) findViewById(R.id.ksAddHomepageText);
        ksAddAddress= (EditText) findViewById(R.id.ksAddAddress);
        ksAddOrigin= (EditText) findViewById(R.id.ksAddOrigin);


        ksAddImage1 =(ImageView)findViewById(R.id.ksAddImage1);
        ksAddImage2 =(ImageView)findViewById(R.id.ksAddImage2);
        ksAddImage3 =(ImageView)findViewById(R.id.ksAddImage3);
        ksAddImage4 =(ImageView)findViewById(R.id.ksAddImage4);
        ksAddImage5 =(ImageView)findViewById(R.id.ksAddImage5);
        ksAddImage6 =(ImageView)findViewById(R.id.ksAddImage6);
        ksAddImage7 =(ImageView)findViewById(R.id.ksAddImage7);
        ksAddImage8 =(ImageView)findViewById(R.id.ksAddImage8);

//        ksAddImage1.setAdjustViewBounds(true);
//        ksAddImage2.setAdjustViewBounds(true);
//        ksAddImage3.setAdjustViewBounds(true);
//        ksAddImage4.setAdjustViewBounds(true);
//        ksAddImage5.setAdjustViewBounds(true);
//        ksAddImage6.setAdjustViewBounds(true);
//        ksAddImage7.setAdjustViewBounds(true);
//        ksAddImage8.setAdjustViewBounds(true);

        ksAddImage.add(ksAddImage1);ksAddImage.add(ksAddImage2);ksAddImage.add(ksAddImage3);ksAddImage.add(ksAddImage4);ksAddImage.add(ksAddImage5);
        ksAddImage.add(ksAddImage6);ksAddImage.add(ksAddImage7);ksAddImage.add(ksAddImage8);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ksAddMap = googleMap;
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(ItemListviewSubAdd.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(ItemListviewSubAdd.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                ksAddMap.setMyLocationEnabled(true);
                ksAddMap.getUiSettings().setZoomControlsEnabled(true);

                ksAddMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));

                ksAddMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                ksAddMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                        ImageView makerImage= (ImageView) v.findViewById(R.id.customImage);
                        TextView markerText= (TextView) v.findViewById(R.id.customText);
                        if(imageString!=null) {
                            byte[] b = Base64.decode(imageString, Base64.DEFAULT);
                            InputStream is = new ByteArrayInputStream(b);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            makerImage.setImageBitmap(bitmap);
                            markerText.setText(titleString);
                        }
                        return v;
                    }
                });

                ksAddMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(final Marker marker) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                double lat=marker.getPosition().latitude;
                                double lng=marker.getPosition().longitude;
                                String refAddress=findAddress(lat,lng);
                                int index=refAddress.indexOf("#");

                                ksAddAddress.setText(refAddress.substring(5,index));

                            }
                        });



                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                        double lat=marker.getPosition().latitude;
                        double lng=marker.getPosition().longitude;
                        String refAddress=findAddress(lat,lng);
                        int index=refAddress.indexOf("#");

                        ksAddAddress.setText(refAddress.substring(5,index));



                    }
                });

            }
        });

        ksAddMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Barcode.GeoPoint reference = findGeoPoint(ksAddAddress.getText().toString());
                double refLat=reference.lat*1E-6;
                double refLng=reference.lng*1E-6;
                refAddress = new LatLng(refLat, refLng);


                if(btnCount==0) {
                    ksAddMap.moveCamera(CameraUpdateFactory.newLatLngZoom(refAddress, 15));
                    ksAddMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                    ksAddMap.addMarker(new MarkerOptions()
                            .position(refAddress).draggable(true)

                    );
                    btnCount++;
                }
                else {
                    ksAddMap.clear();
                    ksAddMap.moveCamera(CameraUpdateFactory.newLatLngZoom(refAddress, 15));
                    ksAddMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                    ksAddMap.addMarker(new MarkerOptions()
                            .position(refAddress).draggable(true)

                    );
                }

            }
        });






        ImageAddButton.setOnClickListener(new View.OnClickListener() {
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

                    new AlertDialog.Builder(ItemListviewSubAdd.this)
                            .setTitle("업로드할 이미지 선택")
                            //.setPositiveButton("사진촬영", cameraListener)
                            .setNeutralButton("앨범선택", albumListener)
                            .setNegativeButton("취소", cancelListener)
                            .show();
                }


            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.sub_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.ksAddMenu1 :
                SharedPreferences.Editor editor = sp.edit();
                String ref1=ksAddIntro.getText().toString();
                String ref2=ksAddTime.getText().toString();
                String ref3=ksAddDelivery.getText().toString();
                String ref4=ksAddPhoneNum.getText().toString();
                String ref5=ksAddHomepageText.getText().toString();
                String ref6=ksAddAddress.getText().toString();
                String ref7=ksAddOrigin.getText().toString();

                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_INTRO",ref1);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_TIME",ref2);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_DELIVERY",ref3);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_PHONENUM",ref4);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_HOMEPAGE",ref5);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_ADDRESS",ref6);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_ORIGIN",ref7);


                double latitude = refAddress.latitude;
                double longitude= refAddress.longitude;


                String latitudeString=Double.toString(latitude);
                String longitudeString=Double.toString(longitude);

                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_LATITUDE",latitudeString );
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_LONGITUDE",longitudeString );


                editor.putInt(latitudeString+"/"+longitudeString+"_1",samplePosition);
                editor.putInt(latitudeString+"/"+longitudeString+"_2",refPosition);
                editor.commit();


                for(int i=0;true;i++) {

                    ImageView refImageView=ksAddImage.get(i);

                    BitmapDrawable refImage = (BitmapDrawable) refImageView.getDrawable();

                    if(refImage!=null){
                        Bitmap bitmap = refImage.getBitmap();
                        editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_IMAGE_"+Integer.toString(i),encodeTobase64(bitmap));
                        editor.commit();

                        bitmap.recycle();

                    }
                    else
                    {
                        break;
                    }
                }





                finish();


            default:
                return super.onOptionsItemSelected(item);
        }
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
                Bitmap photo;

                if(extras != null)
                {
                    if(addCount==0) {
                        photo = extras.getParcelable("data");
                        ksAddImage1.setImageBitmap(photo);
                        addCount++;
                        return;
                    }
                    else if(addCount==1){
                        photo = extras.getParcelable("data");
                        ksAddImage2.setImageBitmap(photo);
                        addCount++;
                        return;

                    }
                    else if(addCount==2){
                        photo = extras.getParcelable("data");
                        ksAddImage3.setImageBitmap(photo);
                        addCount++;
                        return;

                    }
                    else if(addCount==3){
                        photo = extras.getParcelable("data");
                        ksAddImage4.setImageBitmap(photo);
                        addCount++;
                        return;

                    }
                    else if(addCount==4){
                        photo = extras.getParcelable("data");
                        ksAddImage5.setImageBitmap(photo);
                        addCount++;
                        return;

                    }
                    else if(addCount==5){
                        photo = extras.getParcelable("data");
                        ksAddImage6.setImageBitmap(photo);
                        addCount++;
                        return;

                    }
                    else if(addCount==6){
                        photo = extras.getParcelable("data");
                        ksAddImage7.setImageBitmap(photo);
                        addCount++;
                        return;

                    }
                    else if(addCount==7){
                        photo = extras.getParcelable("data");
                        ksAddImage8.setImageBitmap(photo);
                        addCount++;
                        return;

                    }


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

    public static String encodeTobase64(Bitmap image){
        Bitmap bitmap_image = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap_image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);
                }
            }

        } catch (IOException e) {

            Toast.makeText(ItemListviewSubAdd.this, "주소취득 실패"
                    , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return bf.toString();
    }

    private Barcode.GeoPoint findGeoPoint(String address) {
        Geocoder geocoder = new Geocoder(this);
        Address addr;
        Barcode.GeoPoint location = null;
        try {
            List<Address> listAddress = geocoder.getFromLocationName(address, 1);
            if (listAddress.size() > 0) { // 주소값이 존재 하면
                addr = listAddress.get(0); // Address형태로
                int lat = (int) (addr.getLatitude() * 1E6);
                int lng = (int) (addr.getLongitude() * 1E6);
                location = new Barcode.GeoPoint(0,lat,lng);

                Log.d("test", "주소로부터 취득한 위도 : " + lat + ", 경도 : " + lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

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


                        finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());

                    }
                });
        AlertDialog dialog = alt_bld.create();
        //dialog.setTitle("종료");
        dialog.show();
    }
}
