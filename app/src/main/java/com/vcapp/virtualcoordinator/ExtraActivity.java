package com.vcapp.virtualcoordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ExtraActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        btn1 = (Button)findViewById(R.id.music_go);
//        btn2 = (Button)findViewById(R.id.stopwatch_go);
        btn3 = (Button)findViewById(R.id.exbtn3);



        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MusicPlayer.class);
                startActivity(intent);
            }
        });

//        btn2.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(getApplicationContext(), WatchService.class);
//                startActivity(intent);
//            }
//        });

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MusicPlayListview.class);
                startActivity(intent);
            }
        });
    }

    public void onClick_StopWatch(View v){
        Intent intent= new Intent(getApplicationContext(), WatchService.class);
        startActivity(intent);

        Toast.makeText(this,"STOP WATCH로 이동합니다.",Toast.LENGTH_SHORT).show();
    }


}
