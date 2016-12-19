package com.vcapp.virtualcoordinator;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WatchService extends AppCompatActivity {

    TextView mEllapse;
    TextView mSplit;
    Button btnStart;
    Button btnSplit;

    final static int IDLE = 0;
    final static int RUNNING = 1;
    final static int PAUSE = 2;
    int mStatus = IDLE; //처음 상태는 IDLE
    long mBaseTime;
    long mPauseTIme;
    int mSplitCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_service);

        mEllapse = (TextView)findViewById(R.id.ellapse);
        mSplit = (TextView)findViewById(R.id.split);
        btnStart =(Button)findViewById(R.id.btnstart);
        btnSplit = (Button)findViewById(R.id.btnsplit);
    }

    //스톱워치 핸들러
    Handler mTimer = new Handler(){

        public void handleMessage(android.os.Message msg){

            //텍스트뷰 수정
            mEllapse.setText(getEllapse());

            //메세지를 다시보냄.
            mTimer.sendEmptyMessage(0);//0은 메세지를 구분하기 위한것.
        };

    };

    @Override
    protected void onDestroy(){

        mTimer.removeMessages(0); //메모리 릭 방지.
        super.onDestroy();
    }

    public void mOnclick(View v){
        switch(v.getId()){

            //시작버튼 눌리면
            case R.id.btnstart:
                switch (mStatus){
                    //IDLE 상태이면
                    case IDLE:
                        //현재값을 세팅
                        mBaseTime = SystemClock.elapsedRealtime();
                        //핸들러로 메시지를 보냄
                        mTimer.sendEmptyMessage(0);
                        //시작을 중지로 바꾸고
                        btnStart.setText("STOP");
                        //옆버튼의 Enable을 푼 다음
                        btnSplit.setEnabled(true);
                        //상태를 RUNNING으로 바꾼다.
                        mStatus = RUNNING;
                        break;

                    //버튼이 실행상태이면
                    case RUNNING:
                        //핸들러 메세지를 없애고
                        mTimer.removeMessages(0);

                        //멈춘 시간을 파악
                        mPauseTIme = SystemClock.elapsedRealtime();

                        //버튼 텍스트를 바꿔줌
                        btnStart.setText("START");
                        btnSplit.setText("RESET");
                        mStatus = PAUSE; //상태를 멈춤으로 표시
                        break;

                    //멈춤이면
                    case PAUSE:
                        //현재값 가져옴
                        long now = SystemClock.elapsedRealtime();
                        //베이스타임 = 베이스타임 + (now - mPauseTime)
                        //잠깐 스톱워치를 멈췄다가 다시 시작하면 기준점이 변하게 되므로..
                        mBaseTime += (now - mPauseTIme);
                        mTimer.sendEmptyMessage(0);

                        //텍스트수정
                        btnStart.setText("STOP");
                        btnSplit.setText("RECORD");
                        mStatus = RUNNING;
                        break;
                }
                break;

            case R.id.btnsplit:
                switch(mStatus){
                    //RUNNING상태일 때
                    case RUNNING:

                        //기존의 값을 가져온뒤 이어붙이기 위해서
                        String sSplit = mSplit.getText().toString();

                        //+연산자로 이어붙임
                        sSplit += String.format("%d => %s\n", mSplitCount,getEllapse());

                        //텍스트뷰의 값을 바꿔줌
                        mSplit.setText(sSplit);
                        mSplitCount++;
                        break;
                    case PAUSE: //여기서는 초기화 버튼이 됨
                        //핸들러를 없애고
                        mTimer.removeMessages(0);

                        //처음상태로 원상복귀시킴
                        btnStart.setText("START");
                        btnSplit.setText("RECORD");
                        mEllapse.setText("00:00:00");
                        mStatus = IDLE;
                        mSplit.setText("");
                        btnSplit.setEnabled(false);
                        break;
                }
                break;
        }
    }

    String getEllapse(){
        long now = SystemClock.elapsedRealtime();
        long ell = now - mBaseTime; //현재 시간과 지난 시간을 빼서 ell값을 구하고 아래에서 포맷을 바꾼다음 리턴해준다.
        String sEll = String.format("%02d:%02d:%02d", ell/1000/60, (ell/1000)%60, (ell %1000)/10);
        return sEll;
    }
}
