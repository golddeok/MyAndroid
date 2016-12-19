package com.vcapp.virtualcoordinator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ItemListview extends AppCompatActivity {

    private ItemListview myItem=null;
    private ItemAdapter adapter=new ItemAdapter();
    private static final int REQUEST_ADD = 0;


    //    private static ArrayList<String> titleList;
//    private static ArrayList<String> descList;
//    private static ArrayList<String> imageList;
    private SharedPreferences sp;
    private int samplePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_listview);

        Intent kIntent=getIntent();
        TextView kText=(TextView)findViewById(R.id.itemText1);
        String message=kIntent.getStringExtra("message");
        samplePosition=kIntent.getExtras().getInt("samplePosition");
        String label=kIntent.getExtras().getString("label");
        kText.setText(label);

        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);


        if(adapter.getCount()==0) {
            init(); // SharedPreference에 있는 값 불러오기
            adapter.notifyDataSetChanged();
        }



        ListView listView=(ListView)findViewById(R.id.itemListView);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){


                Intent i = new Intent(ItemListview.this, ItemListviewSub.class);
                i.putExtra("refPosition",position);
                i.putExtra("samplePosition",samplePosition);
                startActivity(i);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int p=position;
                AlertDialog.Builder alt_bld=new AlertDialog.Builder(ItemListview.this);
                alt_bld.setMessage("선택");
                alt_bld.setCancelable(true);
                alt_bld.setNegativeButton
                                ("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setPositiveButton
                                ("삭제", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      Handler handler=new Handler();
                                      handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            removeKoreaItem(p);
                                            }
                                        });
                                    }
                                });

                AlertDialog dialog=alt_bld.create();

                dialog.show();
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu1 :
                String refGrade=sp.getString("CURRENT_LOG_ID_GRADE",null);
                if (refGrade != null) {
                    if(refGrade.equals("normal")) {
                        Intent intent = new Intent(ItemListview.this, ItemListviewAdd.class);
                        intent.putExtra("adapterCount", adapter.getCount());
                        intent.putExtra("samplePosition",samplePosition);
                        startActivityForResult(intent, REQUEST_ADD);
                        return true;
                    }
                    else{
                        Toast.makeText(ItemListview.this,"권한이 없습니다.",Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                else
                {
                    Toast.makeText(ItemListview.this,"회원만 이용가능합니다. 로그인 하세요.",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ItemListview.this, ItemListviewAdd.class);
//                    intent.putExtra("adapterCount", adapter.getCount());
//                    intent.putExtra("samplePosition",samplePosition);
//                    startActivityForResult(intent, REQUEST_ADD);
                }

            case R.id.menu2 :



                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();

            addKoreaList(extras);
            Toast toast = Toast.makeText(this, "저장되었습니다..", Toast.LENGTH_LONG);
            toast.show();
            adapter.notifyDataSetChanged();
            extras.clear();
            System.out.println("activity result 실행...");
//            }
        }
    }



    public void addKoreaList(Bundle extras){

        if(extras!=null){
            String title=extras.getString("title");
            String desc=extras.getString("desc");
            Bitmap image = (Bitmap) extras.get("image");
            int returnCount=extras.getInt("returnCount");

            SharedPreferences.Editor editor=sp.edit();
            editor.putString(Integer.toString(samplePosition)+"_IMAGE_POS"+Integer.toString(returnCount),encodeTobase64(image));
            editor.putString(Integer.toString(samplePosition)+"_TITLE_POS"+Integer.toString(returnCount),title);
            editor.putString(Integer.toString(samplePosition)+"_DESC_POS"+Integer.toString(returnCount),desc);



            editor.apply();
            adapter.addItem(image,title,desc);
            extras.clear();
        }
    }
    public void init() {
        for (int i = 0; true; i++) {
            if (sp.getString(Integer.toString(samplePosition)+"_IMAGE_POS"+ String.valueOf(i), null) != null) {
                String title = sp.getString(Integer.toString(samplePosition)+"_TITLE_POS"+ String.valueOf(i), null);
                String desc = sp.getString(Integer.toString(samplePosition)+"_DESC_POS"+ String.valueOf(i), null);
                String image = sp.getString(Integer.toString(samplePosition)+"_IMAGE_POS"+ String.valueOf(i), null);
                byte[] b = Base64.decode(image, Base64.DEFAULT);
                InputStream is = new ByteArrayInputStream(b);
                Bitmap bitmap = BitmapFactory.decodeStream(is);


                adapter.addItem(bitmap, title, desc,i);
                System.out.println("init method 실행...");

            } else {
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


    public void removeKoreaItem(int position){
        SharedPreferences.Editor editor=sp.edit();
        int p= position;

        editor.remove(Integer.toString(samplePosition)+"_IMAGE_POS"+Integer.toString(p));
        editor.remove(Integer.toString(samplePosition)+"_TITLE_POS"+Integer.toString(p));
        editor.remove(Integer.toString(samplePosition)+"_DESC_POS"+Integer.toString(p));
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_INTRO");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_TIME");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_DELIVERY");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_HOMEPAGE");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_ADDRESS");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_ORIGIN");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_LATITUDE");
        editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_LONGITUDE");


        for(int j=0;true;j++){
            if(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_IMAGE_"+Integer.toString(j),null)!=null){
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(p)+"_IMAGE_"+Integer.toString(j));
            }
            else{
                break;
            }

        }
        adapter.removeItem(p);
        editor.commit();

        for(int i=p+1;true;i++){
            String refImage=sp.getString(Integer.toString(samplePosition)+"_IMAGE_POS"+Integer.toString(i),null);
            String refTitle=sp.getString(Integer.toString(samplePosition)+"_TITLE_POS"+Integer.toString(i),null);
            String refDesc=sp.getString(Integer.toString(samplePosition)+"_DESC_POS"+Integer.toString(i),null);

            String refIntro=sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_INTRO",null);
            String refTime=sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_TIME",null);
            String refDelivery=sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_DELIVERY",null);
            String refHomepage=sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_HOMEPAGE",null);
            String refAddress=sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_ADDRESS",null);
            String refOrigin=sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_ORIGIN",null);

            String refLatitude= sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_LATITUDE",null);
            String refLongitude= sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_LONGITUDE",null );

            if(refImage!=null && refTitle!=null && refDesc!= null){
                editor.remove(Integer.toString(samplePosition)+"_IMAGE_POS"+Integer.toString(i));
                editor.remove(Integer.toString(samplePosition)+"_TITLE_POS"+Integer.toString(i));
                editor.remove(Integer.toString(samplePosition)+"_DESC_POS"+Integer.toString(i));
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_INTRO");
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_TIME");
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_DELIVERY");
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_HOMEPAGE");
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_ADDRESS");
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_ORIGIN");
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_LATITUDE" );
                editor.remove(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_LONGITUDE" );

                editor.putString(Integer.toString(samplePosition)+"_IMAGE_POS"+Integer.toString(i-1),refImage);
                editor.putString(Integer.toString(samplePosition)+"_TITLE_POS"+Integer.toString(i-1),refTitle);
                editor.putString(Integer.toString(samplePosition)+"_DESC_POS"+Integer.toString(i-1),refDesc);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_INTRO",refIntro);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_TIME",refTime);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_DELIVERY",refDelivery);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_HOMEPAGE",refHomepage);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_ADDRESS",refAddress);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_ORIGIN",refOrigin);
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_LATITUDE",refLatitude );
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i-1)+"_LONGITUDE",refLongitude );

                editor.commit();
            }
            else{
                break;
            }


            ArrayList<String> refList= new ArrayList<>();
            for(int n=0;true;n++){
                if(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_IMAGE_"+Integer.toString(n),null)!=null){
                    refList.add(sp.getString(Integer.toString(samplePosition)+"_SUB_POS_"+Integer.toString(i)+"_IMAGE_"+Integer.toString(n),null));
                }
                else{
                    break;
                }
            }
            for(int m=0;m<refList.size();m++) {
                editor.putString(Integer.toString(samplePosition)+"_SUB_POS_" + Integer.toString(i - 1) + "_IMAGE_" + Integer.toString(m),refList.get(m));
                editor.commit();
            }



        } //for문

        for(int r=0;true;r++)
        {
            if(sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_"+Integer.toString(r),null)!=null && sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_ID_"+Integer.toString(r),null)!=null
                    &&sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_DATE_"+Integer.toString(r),null)!=null && sp.getFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_RATING_"+Integer.toString(r),100)!=100)
            {
                editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(p) + "_REVIEW_" + Integer.toString(r));
                editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(p) + "_REVIEW_ID_" + Integer.toString(r));
                editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(p) + "_REVIEW_DATE_" + Integer.toString(r));
                editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(p) + "_RATING_" + Integer.toString(r));

            }
            else
            {
                break;
            }
        }
        editor.commit();

        for(int l=p+1; true; l++){

            if(sp.getString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_" + Integer.toString(0),null)!=null) {
                for (int n = 0; true; n++) {
                    String delayReview = sp.getString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_" + Integer.toString(n), null);
                    String delayReviewId = sp.getString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_ID_" + Integer.toString(n), null);
                    String delayReviewDate = sp.getString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_DATE_" + Integer.toString(n), null);
                    float delayRating = sp.getFloat(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_RATING_" + Integer.toString(n), 100);



                    if (delayReview != null && delayReviewId != null && delayReviewDate != null & delayRating != 100) {
                        editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_" + Integer.toString(n));
                        editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_ID_" + Integer.toString(n));
                        editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_REVIEW_DATE_" + Integer.toString(n));
                        editor.remove(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l) + "_RATING_" + Integer.toString(n));

                        editor.putString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l - 1) + "_REVIEW_" + Integer.toString(n), delayReview);
                        editor.putString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l - 1) + "_REVIEW_ID_" + Integer.toString(n), delayReviewId);
                        editor.putString(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l - 1) + "_REVIEW_DATE_" + Integer.toString(n), delayReviewDate);
                        editor.putFloat(Integer.toString(samplePosition)+"_POS_" + Integer.toString(l - 1) + "_RATING_" + Integer.toString(n), delayRating);
                        editor.commit();
                    } else {
                        break;
                    }
                }
            }
            else
            {
                break;
            }

        }



    }
}
