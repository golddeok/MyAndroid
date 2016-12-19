package com.vcapp.virtualcoordinator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewModifyActivity extends AppCompatActivity {

    private RatingBar mRt;
    private TextView modifyRatingText;
    private EditText reviewModiText;
    private int position;
    private int samplePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_modify);

        reviewModiText =(EditText)findViewById(R.id.reviewModiEditText1);
        modifyRatingText= (TextView) findViewById(R.id.reviewModiRatingText);
        mRt= (RatingBar) findViewById(R.id.reviewModiRating);
        Intent receiveB=getIntent();
        reviewModiText.setText(receiveB.getExtras().getString("savedText"));
        mRt.setRating(receiveB.getExtras().getFloat("savedRating"));
        position=receiveB.getExtras().getInt("position");
        samplePosition=receiveB.getExtras().getInt("samplePosition");
        mRt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final float mRating=rating;
                Handler mHandler =new Handler();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        modifyRatingText.setText(Float.toString(mRating));

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.review_modify_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.reviewModifyMenu1 :

                if(reviewModiText.getText().toString().trim().length()<=0){
                    Toast toast=Toast.makeText(ReviewModifyActivity.this,"내용을 입력해주세요.",Toast.LENGTH_LONG);
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



        if (mRt != null && reviewModiText.getText().toString().trim().length()>0) {
            Intent intent =new Intent();
            intent.putExtra("modifyRating",mRt.getRating());
            intent.putExtra("modifyReview", reviewModiText.getText().toString());
            intent.putExtra("modifyPosition",position);

            setResult(RESULT_OK,intent);
        }


        super.finish();

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
                        mRt.setRating(0);
                        reviewModiText.setText("");

                        finish();
                        //android.os.Process.killProcess(android.os.Process.myPid());

                    }
                });
        AlertDialog dialog = alt_bld.create();
        //dialog.setTitle("종료");
        dialog.show();
    }
}
