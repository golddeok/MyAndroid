package com.vcapp.virtualcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ItemListviewSub extends AppCompatActivity {

    private static final int REQUEST_RECEIVE_RATING=2;
    private static RatingBar ksRatingBar;
    private SharedPreferences sp;
    private int refPosition;
    private int samplePosition;

    private final LatLng SEOUL = new LatLng(37.56, 126.97);
    private GoogleMap ksMap;
    private MapFragment mapFragment;

    Button ksButton1;
    Button ksButton2;
    Button ksButton3;
    ImageView ksImage[]=new ImageView[8];

    TextView ksRatingText;

    TextView ksIntro;
    TextView ksTime;
    TextView ksDelivery;
    TextView ksPhoneNum;
    TextView ksHomepageText;
    TextView ksAddress;
    TextView ksOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_listview_sub);

        Bundle bundle=getIntent().getExtras();
        refPosition=bundle.getInt("refPosition");
        samplePosition=bundle.getInt("samplePosition");

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.ksMap);

        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);


        ksImage[0] =(ImageView)findViewById(R.id.ksImage1);
        ksImage[1] =(ImageView)findViewById(R.id.ksImage2);
        ksImage[2] =(ImageView)findViewById(R.id.ksImage3);
        ksImage[3] =(ImageView)findViewById(R.id.ksImage4);
        ksImage[4] =(ImageView)findViewById(R.id.ksImage5);
        ksImage[5] =(ImageView)findViewById(R.id.ksImage6);
        ksImage[6] =(ImageView)findViewById(R.id.ksImage7);
        ksImage[7] =(ImageView)findViewById(R.id.ksImage8);
        ksImage[0].setAdjustViewBounds(true);
        //ksImage1.setMaxWidth(700);
        ksImage[1].setAdjustViewBounds(true);
        // ksImage2.setMaxWidth(700);
        ksImage[2].setAdjustViewBounds(true);
        //ksImage3.setMaxWidth(700);
        ksImage[3].setAdjustViewBounds(true);
        // ksImage4.setMaxWidth(700);
        ksImage[4].setAdjustViewBounds(true);
        //ksImage5.setMaxWidth(700);
        ksImage[5].setAdjustViewBounds(true);
        // ksImage6.setMaxWidth(700);
        ksImage[6].setAdjustViewBounds(true);
        ksImage[7].setAdjustViewBounds(true);

        ksButton1 =(Button)findViewById(R.id.ksButton1);
        ksButton2 =(Button)findViewById(R.id.ksButton2);
        ksButton3 =(Button)findViewById(R.id.ksButton3);
        ksRatingBar =(RatingBar)findViewById(R.id.ksRating);
        ksRatingText= (TextView) findViewById(R.id.ksRatingText);

        ksIntro= (TextView) findViewById(R.id.ksTextView2);
        ksTime= (TextView) findViewById(R.id.ksTextView5);
        ksDelivery= (TextView) findViewById(R.id.ksTextView7);
        ksPhoneNum =(TextView)findViewById(R.id.ksPhoneNum);
        ksHomepageText =(TextView)findViewById(R.id.ksHompageText);
        ksAddress= (TextView) findViewById(R.id.ksTextView13);
        ksOrigin= (TextView) findViewById(R.id.ksTextView15);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ksMap = googleMap;
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(ItemListviewSub.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(ItemListviewSub.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                ksMap.setMyLocationEnabled(true);
                ksMap.getUiSettings().setZoomControlsEnabled(true);

                ksMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));

                ksMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


            }
        });

        init(refPosition);




        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                setAverageRating();
            }
        });

        ksButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=ksPhoneNum.getText().toString();
                number.replaceAll("-","");
                Intent callIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+number));
                startActivity(callIntent);
            }
        });
        ksButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homepageName= ksHomepageText.getText().toString();
                Intent acessIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(homepageName));
                startActivity(acessIntent);
            }
        });

        ksButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent=new Intent(ItemListviewSub.this,ReviewListActivity.class);
                reviewIntent.putExtra("refPosition",refPosition);
                reviewIntent.putExtra("samplePosition",samplePosition);
                startActivity(reviewIntent);
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();

        init(refPosition);

        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                setAverageRating();
            }
        });

    }



    public void setAverageRating(){

        SharedPreferences.Editor editor= sp.edit();
        float totalSum = 0;
        float totalIndex = 0;
        //   totalSum/totalIndex의 result

        for(int i=0; true; i++)
        {
            float sampleFloat=sp.getFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(refPosition)+"_RATING_"+Integer.toString(i),100);
            if(sampleFloat!=100){
                totalSum+=sampleFloat;
                totalIndex+=1;
            }
            else{
                break;
            }
        }
        float aveRating=totalSum/totalIndex;

        float refRat= (float) (aveRating-(Math.floor(aveRating)));
        float setRat=0;

        if(refRat>=0.7){
            setRat=Math.round(aveRating);
//            Toast.makeText(this,"0.7이상:"+ setRat,Toast.LENGTH_SHORT).show();
        }
        else if(refRat<0.7 && refRat>=0.3){
            setRat= (float) (Math.floor(aveRating)+(float)0.5);
//            Toast.makeText(this,"0.7이하 0.5이상 :" + setRat,Toast.LENGTH_SHORT).show();
        }
        else if(refRat<0.3){
            setRat= (float) Math.floor(aveRating);
//            Toast.makeText(this," 0.5이하:"+ setRat,Toast.LENGTH_SHORT).show();
        }

        String stringAveRat = String.format("%.1f" , aveRating);

        ksRatingBar.setRating((float)setRat);
        ksRatingText.setText(stringAveRat);
    }

    public void init(int p){
        SharedPreferences.Editor editor=sp.edit();

        ksIntro.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_INTRO",null));
        ksTime.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_TIME",null));
        ksDelivery.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_DELIVERY",null));
        ksPhoneNum.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_PHONENUM",null));
        ksHomepageText.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_HOMEPAGE",null));
        ksAddress.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_ADDRESS",null));
        ksOrigin.setText(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+ String.valueOf(p)+"_ORIGIN",null));

        String latitudeString = sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+String.valueOf(p)+"_LATITUDE",null);
        String longitudeString = sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+String.valueOf(p)+"_LONGITUDE",null);
        final String imageString = sp.getString(Integer.toString(samplePosition)+"_IMAGE_POS_"+String.valueOf(p),null);
        final String titleString = sp.getString(Integer.toString(samplePosition)+"_TITLE_POS_"+String.valueOf(p),null);

        if(latitudeString!=null && longitudeString!=null) {
            double latitude = Double.valueOf(latitudeString);
            double longitude = Double.valueOf(longitudeString);
            final LatLng refAddress = new LatLng(latitude, longitude);


            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    ksMap.moveCamera(CameraUpdateFactory.newLatLngZoom(refAddress, 15));
                    ksMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    ksMap.addMarker(new MarkerOptions().position(refAddress).draggable(false));

                    ksMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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
                }
            });

        }





        for(int i=0; true; i++){

            String image = sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(refPosition)+"_IMAGE_"+Integer.toString(i), null);
            if(image!=null) {
                byte[] b = Base64.decode(image, Base64.DEFAULT);
                InputStream is = new ByteArrayInputStream(b);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ksImage[i].setImageBitmap(bitmap);
            }
            else
            {
                break;
            }

        }



    }
}
