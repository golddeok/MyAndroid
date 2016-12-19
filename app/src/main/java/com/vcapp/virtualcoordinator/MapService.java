package com.vcapp.virtualcoordinator;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;


public class MapService extends AppCompatActivity{

    private GoogleMap map;

    //    static final LatLng SEOUL = new LatLng(37.56, 126.97);
    static final LatLng HOME = new LatLng(37.288796, 126.852293);
    private static final String TAG = "FINDADRESS";
    private String searchText;
    private SharedPreferences sp;

    private EditText newSearch;
    private Button searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_service);
//
//        LatLng Origin = new LatLng(37.484585, 126.971228);
//        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map1)).getMap();
//        map.addMarker(new MarkerOptions().position(Origin).title("내위치").snippet(" "));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Origin,15));

        sp = getSharedPreferences("virtualcoodinator", MODE_PRIVATE);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        //latest sdk에서는 getMap 사용을 금함, 따라서 onmapready call back을 이용하여 map을 저장해서 사용해야함.
//       mapFragment.getMap();
        searchText = getIntent().getStringExtra("searchText");
        newSearch= (EditText) findViewById(R.id.search);
        searchBtn= (Button) findViewById(R.id.searchBtn);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(MapService.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MapService.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);

                Barcode.GeoPoint reference = findGeoPoint(searchText);
                double refLat = reference.lat * 1E-6;
                double refLng = reference.lng * 1E-6;
                LatLng refAddress = new LatLng(refLat, refLng);


                map.moveCamera(CameraUpdateFactory.newLatLngZoom(refAddress, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


                Marker marker = map.addMarker(new MarkerOptions()
                        .position(refAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1))
                );


                map.addCircle(new CircleOptions()
                        .center(refAddress)
                        .radius(500)
                        .strokeColor(Color.parseColor("#884169e1"))
                        .fillColor(Color.parseColor("#5587cefa")));

                addMarker(refAddress);

                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        map.clear();
                        Location ref = map.getMyLocation();
                        LatLng refLatLng = new LatLng(ref.getLatitude(), ref.getLongitude());

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(refLatLng, 15));
                        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        map.addCircle(new CircleOptions()
                                .center(refLatLng)
                                .radius(500)
                                .strokeColor(Color.parseColor("#884169e1"))
                                .fillColor(Color.parseColor("#5587cefa")));

                        addMarker(refLatLng);

                        return true;
                    }
                });

                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        double lat=marker.getPosition().latitude;
                        double lnt=marker.getPosition().longitude;


                        String latString=Double.toString(lat);
                        String lntString=Double.toString(lnt);
                        int samplePosition=sp.getInt(latString+"/"+lntString+"_1",1000);
                        int refPosition=sp.getInt(latString+"/"+lntString+"_2",1000);
                        if(samplePosition!=1000 && refPosition!=1000)
                        {
                            Intent intent=new Intent(MapService.this,ItemListview.class);
                            intent.putExtra("refPosition",refPosition);
                            intent.putExtra("samplePosition",samplePosition);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(MapService.this,"null",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }


        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAddress=newSearch.getText().toString();

                Barcode.GeoPoint reference = findGeoPoint(newAddress);
                double refLat = reference.lat * 1E-6;
                double refLng = reference.lng * 1E-6;
                LatLng refAddress = new LatLng(refLat, refLng);

                map.clear();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(refAddress, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


                Marker marker = map.addMarker(new MarkerOptions()
                        .position(refAddress).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1))
                );




                map.addCircle(new CircleOptions()
                        .center(refAddress)
                        .radius(500)
                        .strokeColor(Color.parseColor("#884169e1"))
                        .fillColor(Color.parseColor("#5587cefa")));

                addMarker(refAddress);

            }
        });


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

            Toast.makeText(MapService.this, "주소취득 실패"
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
                location = new Barcode.GeoPoint(0, lat, lng);

                Log.d("test", "주소로부터 취득한 위도 : " + lat + ", 경도 : " + lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public void addMarker(LatLng refAddress) {
        Location location = new Location("");
        location.setLatitude(refAddress.latitude);
        location.setLongitude(refAddress.longitude);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; true; j++) {
                double targetLat = Double.valueOf(sp.getString(Integer.toString(i) + "_SUB_POS_" + Integer.toString(j) + "_LATITUDE", "100"));
                double targetLng = Double.valueOf(sp.getString(Integer.toString(i) + "_SUB_POS_" + Integer.toString(j) + "_LONGITUDE", "100"));
                if (targetLat != 100 && targetLng != 100) {
                    Location targetLocation = new Location("");
                    targetLocation.setLatitude(targetLat);
                    targetLocation.setLongitude(targetLng);

                    if (location.distanceTo(targetLocation) < 500) {
                        int refI=i;
                        int refJ=j;
                        final String text = sp.getString(Integer.toString(refI) + "_TITLE_POS" + String.valueOf(refJ), null);
                        final String image = sp.getString(Integer.toString(refI) + "_IMAGE_POS" + String.valueOf(refJ), null);
                        switch (i) {

                            case 0 :
                            {
                                LatLng refLatLng = new LatLng(targetLat, targetLng);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(refLatLng).snippet(image).title(text).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                break;
                            }
                            case 1 :
                            {
                                LatLng refLatLng = new LatLng(targetLat, targetLng);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(refLatLng).snippet(image).title(text).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                break;
                            }
                            case 2 :
                            {
                                LatLng refLatLng = new LatLng(targetLat, targetLng);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(refLatLng).snippet(image).title(text).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                break;
                            }
                            case 3 :
                            {
                                LatLng refLatLng = new LatLng(targetLat, targetLng);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(refLatLng).snippet(image).title(text).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                break;
                            }
                            case 4 :
                            {
                                LatLng refLatLng = new LatLng(targetLat, targetLng);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(refLatLng).snippet(image).title(text).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                break;
                            }
                            case 5 :
                            {
                                LatLng refLatLng = new LatLng(targetLat, targetLng);
                                Marker marker = map.addMarker(new MarkerOptions()
                                        .position(refLatLng).snippet(image).title(text).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                                break;
                            }
                            default:
                            {
                                break;
                            }
                        }


//                        byte[] b = Base64.decode(image, Base64.DEFAULT);
//                        InputStream is = new ByteArrayInputStream(b);
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);



                        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                                ImageView imageView= (ImageView) v.findViewById(R.id.customImage);
                                TextView textView= (TextView) v.findViewById(R.id.customText);

                                String imageString =marker.getSnippet();
                                String titleString =marker.getTitle();
                                if(imageString!=null && titleString!=null) {
                                    byte[] b = Base64.decode(imageString, Base64.DEFAULT);
                                    InputStream is = new ByteArrayInputStream(b);
                                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                                    imageView.setImageBitmap(bitmap);
                                    textView.setText(titleString);
                                }
                                else
                                {
                                    imageView.setImageResource(R.drawable.pin1);
                                    textView.setText("내위치");
                                }

//                                String text=sp.getString(Integer.toString(refI)+"_TITLE_POS"+ String.valueOf(refJ), null);
//                                String image = sp.getString(Integer.toString(refI)+"_IMAGE_POS"+ String.valueOf(refJ), null);
//                                byte[] b = Base64.decode(image, Base64.DEFAULT);
//                                InputStream is = new ByteArrayInputStream(b);
//                                Bitmap bitmap = BitmapFactory.decodeStream(is);
//                                imageView.setImageBitmap(bitmap);
//                                textView.setText(text);

                                return v;
                            }
                        });

//                        LatLng refLatLng = new LatLng(targetLat, targetLng);
//                        Marker marker=map.addMarker(new MarkerOptions()
//                                .position(refLatLng)
//                        );


                    }
                } else {
                    break;
                }
            }

        }

    }

}
