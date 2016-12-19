package com.vcapp.virtualcoordinator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ReviewAddActivity extends AppCompatActivity {

    private RatingBar rt;
    private EditText addRatingText;
    private TextView ratingText;
    private int reviewItemCount;
    private int samplePosition;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        addRatingText =(EditText)findViewById(R.id.reviewAddEditText1);
        ratingText= (TextView) findViewById(R.id.reviewAddRatingText);

        rt= (RatingBar) findViewById(R.id.reviewAddRating);
        reviewItemCount=getIntent().getExtras().getInt("reviewCount");
        samplePosition=getIntent().getExtras().getInt("samplePosition");
        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);

        rt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final float aRating=rating;

                Handler aHandler =new Handler();
                aHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ratingText.setText(Float.toString(aRating));

                    }
                });
            }
        });




    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);


        CharSequence userText1= addRatingText.getText();


        outState.putCharSequence("savedText1", userText1);


    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence userText1=savedInstanceState.getCharSequence("savedText1");

        addRatingText.setText(userText1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.review_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.reviewAddMenu1 :
                if(addRatingText.getText().toString().trim().length()<=0){
                    Toast toast=Toast.makeText(ReviewAddActivity.this,"내용을 입력해주세요.",Toast.LENGTH_LONG);
                    toast.show();
                    return true;
                }

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish(){



        if (rt != null && addRatingText.getText().toString().trim().length()>0) {
            Calendar cal = Calendar.getInstance();
            StringBuffer sb= new StringBuffer();
            sb.append(cal.get(Calendar.YEAR)).append("-").append(0).append(cal.get(Calendar.MONTH)+1).append("-").append(cal.get(Calendar.DATE));
            String refDate=sb.toString();
            String refId=sp.getString("CURRENT_LOG_ID",null);


            Intent intent =new Intent();
            intent.putExtra("rating",rt.getRating());
            intent.putExtra("review", addRatingText.getText().toString());
            intent.putExtra("returnCount",reviewItemCount);
            intent.putExtra("reviewId",refId);
            intent.putExtra("reviewDate",refDate);



            setResult(RESULT_OK,intent);
        }


        super.finish();

    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alt_bld=new AlertDialog.Builder(this);
        alt_bld.setMessage("리뷰작성을 종료하시겠습니까?");
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
                        addRatingText.setText("");
                        finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());

                    }
                });
        AlertDialog dialog=alt_bld.create();
        //dialog.setTitle("종료");
        dialog.show();

    }
}
