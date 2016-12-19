package com.vcapp.virtualcoordinator;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    Button sbtn1;
    ImageView imv1;
    AnimationDrawable ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sbtn1 = (Button) findViewById(R.id.main_Login);
        imv1 = (ImageView)findViewById(R.id.imgview1);

        ani=(AnimationDrawable)imv1.getDrawable();



        Animation animation = AnimationUtils.loadAnimation(this, R.anim.start_anim);

        sbtn1.startAnimation(animation);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,2000);

        ani.setOneShot(true);

    }

    public void mOnClick(View v) {

        switch (v.getId()) {
            case R.id.main_Login:
                //'START' 버튼을 눌렀을 때 이전 Frame Animation이 진행중이면 정지
                //Frame Animation은 한번 start()해주면 계속 Running 상태임.
                //Frame Animation을 OneShot으로 하고 다시 시작하게 하고 싶다면
                //이전 Frame Animation을 'stop'시켜야 함.
                if (ani.isRunning()) ani.stop();

                //AnimationDrawable 객체에게
                //Frame 변경을 시작하도록 함.
                ani.start();

                break;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("onRestart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("onStop");
    }
    @Override
    protected void onDestroy( ){
        super.onDestroy();
        System.out.println("onDestroy");
    }
}
