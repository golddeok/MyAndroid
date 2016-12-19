package com.vcapp.virtualcoordinator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ReviewListActivity extends AppCompatActivity {

    private ReviewItem reviewItem=null;
    final ReviewAdapter reviewAdapter=new ReviewAdapter();
    private Button reviewAddBtn= null;
    private static final int REQUEST_ITEM_ADD=1;
    private static final int REQUEST_ITEM_MODIFY=2;
    private SharedPreferences sp;
    private int reviewPosition;  //korea activity의 postion
    private int samplePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);
        Bundle refBundle=getIntent().getExtras();
        reviewPosition=refBundle.getInt("refPosition");
        samplePosition=refBundle.getInt("samplePosition");


        ListView reviewListView= (ListView) findViewById(R.id.reviewListView);
        assert reviewListView != null;
        reviewListView.setAdapter(reviewAdapter);

        if(reviewAdapter.getCount()==0) {

            init(reviewPosition); // SharedPreference에 있는 값 불러오기
            reviewAdapter.notifyDataSetChanged();

        }

        reviewAddBtn= (Button) findViewById(R.id.reviewListButton1);
        reviewAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkId=sp.getString("CURRENT_LOG_ID",null);
                String checkGrade=sp.getString("CURRENT_LOG_ID_GRADE",null);
                if(checkId!=null && checkGrade!=null) {
                    if(checkGrade.equals("normal")) {
                        Intent reviewIntent = new Intent(ReviewListActivity.this, ReviewAddActivity.class);
                        reviewIntent.putExtra("reviewCount", reviewAdapter.getCount());
                        reviewIntent.putExtra("samplePosition",samplePosition);
                        startActivityForResult(reviewIntent, REQUEST_ITEM_ADD);
                    }
                    else if(checkGrade.equals("special")){
                        Toast.makeText(ReviewListActivity.this,"권한이 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ReviewListActivity.this,"회원만 이용가능합니다. 로그인 하세요.",Toast.LENGTH_SHORT).show();
//                    Intent reviewIntent = new Intent(ReviewListActivity.this, ReviewAddActivity.class);
//                    reviewIntent.putExtra("reviewCount", reviewAdapter.getCount());
//                    reviewIntent.putExtra("samplePosition",samplePosition);
//                    startActivityForResult(reviewIntent, REQUEST_ITEM_ADD);
                }
            }
        });


        reviewListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int p=position;
                AlertDialog.Builder alt_bld=new AlertDialog.Builder(ReviewListActivity.this);
                alt_bld.setMessage("선택");
                alt_bld.setCancelable(true);
                alt_bld.setNegativeButton("수정",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ReviewItem refItem= (ReviewItem) reviewAdapter.getItem(p);
                                String currentId=sp.getString("CURRENT_LOG_ID",null);

                                if(refItem.getidText().equals(currentId))
                                {
                                    Intent i = new Intent(ReviewListActivity.this, ReviewModifyActivity.class);
                                    ReviewItem saveItem = (ReviewItem) reviewAdapter.getItem(p);
                                    i.putExtra("savedText", saveItem.getReview());
                                    i.putExtra("position", p);
                                    i.putExtra("savedRating", saveItem.getRating());
                                    i.putExtra("samplePosition",samplePosition);

                                    startActivityForResult(i, REQUEST_ITEM_MODIFY);
                                }
                                else
                                {
                                    Toast.makeText(ReviewListActivity.this,"권한이 없습니다.",Toast.LENGTH_SHORT).show();
                                }


                            }
                        }).setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ReviewItem refItem= (ReviewItem) reviewAdapter.getItem(p);
                                String currentId=sp.getString("CURRENT_LOG_ID",null);

                                if(refItem.getidText().equals(currentId))
                                {
                                    removeReview(p);
                                }
                                else
                                {
                                    Toast.makeText(ReviewListActivity.this,"권한이 없습니다.",Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                AlertDialog dialog=alt_bld.create();

                dialog.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==REQUEST_ITEM_ADD && resultCode==RESULT_OK && data!=null) {
            Bundle extras =data.getExtras();
            if (extras != null) {
                addReviewList(extras);

                Toast toast=Toast.makeText(this, "저장되었습니다..",Toast.LENGTH_LONG);
                toast.show();
                reviewAdapter.notifyDataSetChanged();
                extras.clear();
            }
        }
        if(requestCode==REQUEST_ITEM_MODIFY && resultCode==RESULT_OK && data!=null) {
            Bundle extras =data.getExtras();
            if (extras != null) {
                int position=extras.getInt("modifyPosition"); //review List의 postion;
                String modifyString=extras.getString("modifyReview");
                float modifyFloat=extras.getFloat("modifyRating");

                SharedPreferences.Editor editor=sp.edit();
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(position),modifyString);
                editor.putFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(position),modifyFloat);
                editor.commit();


                ReviewItem ri= (ReviewItem) reviewAdapter.getItem(position);
                ri.setRating(modifyFloat);
                ri.setReview(modifyString);
            }
        }
    }


    public void addReviewList(Bundle extras) {

        if (extras != null) {
            float rating = extras.getFloat("rating");
            String review = extras.getString("review");
            String refId= extras.getString("reviewId");
            String refDate=extras.getString("reviewDate");
            int returnCount=extras.getInt("returnCount"); //reviewAdapter.getCount()

            SharedPreferences.Editor editor=sp.edit();

            if(reviewAdapter.getCount()==0){
//                Toast.makeText(ReviewListActivity.this,"00000000000000000",Toast.LENGTH_SHORT).show();
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(0),review);
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(0),refId);
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(0),refDate);
                editor.putFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(0),rating);
                editor.commit();
            }
            else
            {
                for(int i=(reviewAdapter.getCount())-1; i >= 0 ;i--)
                {
//                    Toast.makeText(ReviewListActivity.this,"111111111111",Toast.LENGTH_SHORT).show();
                    String delayReview=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(i),null);
                    String delayReviewId=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(i),null);
                    String delayReviewDate=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(i),null);
                    float delayReviewRating=sp.getFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(i),100);

                    editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(i));
                    editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(i));
                    editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(i));
                    editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(i));
                    editor.commit();

                    editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(i+1),delayReview);
                    editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(i+1),delayReviewId);
                    editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(i+1),delayReviewDate);
                    editor.putFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(i+1),delayReviewRating);
                    editor.commit();


                }
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(0),review);
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(0),refId);
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(0),refDate);
                editor.putFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(0),rating);

            }


            editor.commit();


            reviewAdapter.addReviewItem(rating, review, refId, refDate);
        }
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.review_list_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.reviewMenu1 :

                return true;

            case R.id.reviewMenu2 :
//                Toast toast1=Toast.makeText(ReviewListActivity.this,"menu2",Toast.LENGTH_LONG);
//                toast1.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void init(int position){
        final int p=position;

        if(sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_"+Integer.toString(0),null)!=null && sp.getFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_RATING_" + Integer.toString(0), 100)!=100
                && sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_ID_"+Integer.toString(0),null)!=null && sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_DATE_"+Integer.toString(0),null)!=null) {

            for (int i = 0; true; i++)
            {

                float initFloat = sp.getFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_RATING_" + Integer.toString(i), 100);
                String initReview = sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_"+Integer.toString(i), null);
                String initId=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_ID_"+Integer.toString(i),null);
                String initDate=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(p)+"_REVIEW_DATE_"+Integer.toString(i),null);
//               Toast.makeText(ReviewListActivity.this,"init 실행 sample:"+samplePosition+"p:"+p+"i:"+i,Toast.LENGTH_SHORT).show();

                if (initReview != null && initFloat != 100 && initId!=null && initDate!=null) {
//                    Toast.makeText(ReviewListActivity.this,"if문!!",Toast.LENGTH_SHORT).show();
                    reviewAdapter.initAddReviewItem(initFloat, initReview, initId, initDate);

                }
                else
                {
                    break;
                }
            }
        }

    }

    public void removeReview(int position){

        reviewAdapter.removeReviewItem(position);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_" + Integer.toString(position));
        editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_" + Integer.toString(position));
        editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(position));
        editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(position));
        editor.commit();


        for(int i=position+1; true; i++){
            String changeString=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(i),null);
            float changeFloat=sp.getFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(i),100);
            String changeId=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(i),null);
            String changeDate=sp.getString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(i),null);

            if(changeString!=null && changeFloat!=100 && changeId!=null && changeDate!=null)
            {
                editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(i));
                editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(i));
                editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(i));
                editor.remove(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(i));
                editor.commit();
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_"+Integer.toString(i-1),changeString);
                editor.putFloat(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_RATING_"+Integer.toString(i-1),changeFloat);
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_ID_"+Integer.toString(i-1),changeId);
                editor.putString(Integer.toString(samplePosition)+"_POS_"+Integer.toString(reviewPosition)+"_REVIEW_DATE_"+Integer.toString(i-1),changeDate);
                editor.commit();
            }
            else
            {
                break;
            }
        }

    }
}
