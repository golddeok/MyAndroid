package com.vcapp.virtualcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements RegisterDialogFragment.registerLoginId {

    private Button logButton;
    private EditText idText;
    private EditText passText;
    private CheckBox autoLogCheck;
    private SharedPreferences sp;

    private TextView virtualStylist;

    private String id;
    private String password;
    private boolean checkAuto;

    TextView tv;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv = (TextView)findViewById(R.id.login_textView);
        animation = AnimationUtils.loadAnimation(this,R.anim.login_anim);
        tv.startAnimation(animation);

        logButton= (Button) findViewById(R.id.login_btn);
        idText= (EditText) findViewById(R.id.idText);
        passText= (EditText) findViewById(R.id.passwordText);
        autoLogCheck= (CheckBox) findViewById(R.id.autoCheck);
        virtualStylist= (TextView) findViewById(R.id.login_textView);


        sp=getSharedPreferences("virtualcoordinator",MODE_PRIVATE);

        final SharedPreferences.Editor editor=sp.edit();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLogin();
                for(int i=0;true;i++) {
                    String checkId=sp.getString("LOG_IN_ID_"+Integer.toString(i),null);
                    String checkPass=sp.getString("LOG_IN_PASS_"+Integer.toString(i),null);
                    String idGrade=sp.getString("LOG_IN_GRADE_"+Integer.toString(i),null);
                    boolean autoCheck=autoLogCheck.isChecked();
                    if(checkId!=null && checkPass!=null)
                    {
                        if(checkId.equals(id))
                        {
                            if(checkPass.equals(password))
                            {
                                Toast.makeText(LoginActivity.this, "로그인 완료!!.", Toast.LENGTH_SHORT).show();
                                editor.putString("CURRENT_LOG_ID",checkId);
                                editor.putString("CURRENT_LOG_ID_GRADE",idGrade);
                                editor.putBoolean("CURRENT_AUTO_LOGIN",autoCheck);
                                editor.putBoolean("CURRENT_STATUS_LOGIN",true);
                                editor.commit();
                                finish();
                                break;
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"등록된 아이디가 없습니다.",Toast.LENGTH_SHORT).show();
                        break;
                    }


                }


            }
        });


    }

    public void getLogin(){
        id=idText.getText().toString();
        password=passText.getText().toString();
        checkAuto=autoLogCheck.isChecked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.logMenu1 :
                showLoginDialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLoginDialog()
    {
        RegisterDialogFragment dialog = new RegisterDialogFragment();
        dialog.show(getFragmentManager(),"dialog");

    }

    @Override
    public void setLoginInform(String id, String password, String passwordCheck, boolean gChecked, boolean sChecked) {
        SharedPreferences.Editor editor=sp.edit();

        String refUserId = id;
        String refPassword=password;
        String refPassword1=passwordCheck;
        String grade=null;
        boolean refChecked=gChecked;
        boolean refCheked1=sChecked;
        int checkCount;
        int lastIndex;




        if(!refPassword.equals(refPassword1)){
            Toast.makeText(LoginActivity.this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
            showLoginDialog();
            return;
        }
        else {

            checkCount=0;
            for (int i = 0; true; i++) {
                String checkId = sp.getString("LOG_IN_ID_" + Integer.toString(i), null);

                if (checkId != null)
                {
                    if(checkId.equals(refUserId))
                    {
                        checkCount++;
                    }
                }
                else if(checkId==null)
                {
                    lastIndex=i;
                    break;
                }
            } //for문

            if(checkCount>=1)
            {
                Toast.makeText(LoginActivity.this,"중복된 아이디 입니다.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(refChecked)
                {
                    grade="normal";
                }
                else if(refCheked1)
                {
                    grade="special";
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"등급을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    showLoginDialog();
                    return;

                }

                if(refUserId.trim().length()<=0){
                    Toast.makeText(LoginActivity.this,"아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
                    showLoginDialog();
                    return;
                }
                else if(refPassword.trim().length()<=3){
                    Toast.makeText(LoginActivity.this,"비밀번호는 4자 이상 입력해주세요.",Toast.LENGTH_SHORT).show();
                    showLoginDialog();
                    return;
                }
                else {
                    editor.putString("LOG_IN_ID_" + Integer.toString(lastIndex), refUserId);
                    editor.putString("LOG_IN_PASS_" + Integer.toString(lastIndex), refPassword);
                    editor.putString("LOG_IN_GRADE_" + Integer.toString(lastIndex), grade);
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "회원가입 완료!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




}
