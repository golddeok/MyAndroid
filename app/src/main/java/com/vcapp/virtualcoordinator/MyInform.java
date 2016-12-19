package com.vcapp.virtualcoordinator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyInform extends AppCompatActivity {

    TextView myInformText1;
    TextView myInformText2;
    Button logoutBtn;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inform);
        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);

        myInformText1= (TextView) findViewById(R.id.myInformText1);
        myInformText2= (TextView) findViewById(R.id.myInformText2);
        logoutBtn= (Button) findViewById(R.id.logoutButton);
        myInformText1.setText(sp.getString("CURRENT_LOG_ID",null));
        myInformText2.setText(sp.getString("CURRENT_LOG_ID_GRADE",null));

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sp.edit();

                editor.putString("CURRENT_LOG_ID",null);
                editor.putString("CURRENT_LOG_ID_GRADE",null);
                editor.remove("CURRENT_AUTO_LOGIN");
                editor.remove("CURRENT_STATUS_LOGIN");
                editor.commit();
                Toast.makeText(MyInform.this,"로그아웃 완료!!",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
