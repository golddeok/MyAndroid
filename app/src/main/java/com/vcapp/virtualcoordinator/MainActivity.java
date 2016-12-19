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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button sbtn;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    EditText searchText;
    TextView tv;

    SharedPreferences sp;

    Animation animation;
    Animation animation2;
    Animation animation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();

        editor.remove("CURRENT_STATUS_LOGIN");
        if(!sp.getBoolean("CURRENT_AUTO_LOGIN",false))
        {
            editor.remove("CURRENT_LOG_ID");
            editor.remove("CURRENT_LOG_ID_GRADE");
        }
        editor.commit();

        searchText=(EditText)findViewById(R.id.editText) ;
        tv=(TextView)findViewById(R.id.textView);
        sbtn = (Button)findViewById(R.id.sButton);

        btn1 = (Button)findViewById(R.id.gostore1);
        btn2 = (Button)findViewById(R.id.itemList);
        btn3 = (Button)findViewById(R.id.musicList);
        btn4 = (Button)findViewById(R.id.listviewsample);


        animation = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);
        animation2 = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);
        animation3 = AnimationUtils.loadAnimation(this,R.anim.start_anim);


        searchText=(EditText)findViewById(R.id.editText) ;

        btn1.startAnimation(animation);
        btn2.startAnimation(animation);
        btn3.startAnimation(animation2);
        btn4.startAnimation(animation2);

        searchText.startAnimation(animation3);
        tv.startAnimation(animation3);
        sbtn.startAnimation(animation3);


        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sBtnIntent2=new Intent(MainActivity.this,ItemListview.class);
                EditText searchText=(EditText)findViewById(R.id.editText) ;
                String massage=searchText.getText().toString();
                sBtnIntent2.putExtra("message",massage);
                sBtnIntent2.putExtra("samplePosition",0);
                sBtnIntent2.putExtra("label","My Store List");
                startActivity(sBtnIntent2);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sBtnIntent2=new Intent(MainActivity.this,ItemListview.class);
                EditText searchText=(EditText)findViewById(R.id.editText) ;
                String massage=searchText.getText().toString();
                sBtnIntent2.putExtra("message",massage);
                sBtnIntent2.putExtra("samplePosition",1);
                sBtnIntent2.putExtra("label","My Food List");
                startActivity(sBtnIntent2);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);

            }
        });

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ExtraActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sBtnIntent2=new Intent(MainActivity.this,ItemListview.class);
                EditText searchText=(EditText)findViewById(R.id.editText) ;
                String massage=searchText.getText().toString();
                sBtnIntent2.putExtra("message",massage);
                sBtnIntent2.putExtra("samplePosition",2);
                sBtnIntent2.putExtra("label","My Clothes List");
                startActivity(sBtnIntent2);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
            }
        });

        sbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sBtnIntent=new Intent(MainActivity.this,MapService.class);
                sBtnIntent.putExtra("searchText",searchText.getText().toString());
                startActivity(sBtnIntent);
            }
        });


    }



    @Override
    public void onBackPressed(){
        System.out.println("back key 입력");

        AlertDialog.Builder alt_bld=new AlertDialog.Builder(this);
        alt_bld.setMessage("종료하시겠습니까?");
        alt_bld.setCancelable(true);
        alt_bld.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alt_bld.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moveTaskToBack(true);
                        finish();


                    }
                });
        AlertDialog dialog=alt_bld.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        //dialog.setTitle("종료");
        dialog.show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        EditText textBox=(EditText)findViewById(R.id.editText);

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.mainMenu1 :
                if(sp.getBoolean("CURRENT_AUTO_LOGIN",false) || sp.getBoolean("CURRENT_STATUS_LOGIN",false)) {
                    Intent intent = new Intent(MainActivity.this, MyInform.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish(){
        SharedPreferences.Editor editor=sp.edit();
        editor.remove("CURRENT_STATUS_LOGIN");
        if(!sp.getBoolean("CURRENT_AUTO_LOGIN",false))
        {
            editor.remove("CURRENT_LOG_ID");
        }

        editor.commit();

        super.finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        btn1.startAnimation(animation);
        btn2.startAnimation(animation);
        btn3.startAnimation(animation2);
        btn4.startAnimation(animation2);
        searchText.startAnimation(animation3);
    }

    @Override
    protected void onDestroy(){

        SharedPreferences.Editor editor=sp.edit();
        editor.remove("CURRENT_STATUS_LOGIN");
        editor.commit();
        super.onDestroy();
    }
}