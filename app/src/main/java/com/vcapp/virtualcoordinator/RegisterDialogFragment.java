package com.vcapp.virtualcoordinator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by Deok on 2016-07-14.
 */
public class RegisterDialogFragment extends DialogFragment {

    private EditText userId;
    private EditText userPassword;
    private EditText userPasswordCheck;
    private CheckBox generalUser;
    private CheckBox ownerUser;


    public interface registerLoginId
    {
        void setLoginInform(String id, String password, String passwordCheck, boolean gChecked, boolean sChecked);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.activity_register, null);
        userId = (EditText) view.findViewById(R.id.userId);
        userPassword = (EditText) view.findViewById(R.id.userPassword);
        userPasswordCheck = (EditText) view.findViewById(R.id.userPassword1);
        generalUser= (CheckBox) view.findViewById(R.id.generalUserCheck);
        ownerUser= (CheckBox) view.findViewById(R.id.ownerUserCheck);

//        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.main_ani);
//        view.startAnimation(animation);



        generalUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ownerUser.setChecked(false);
                }
            }
        });
        ownerUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    generalUser.setChecked(false);
                }
            }
        });


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                registerLoginId listener = (registerLoginId) getActivity();

                                listener.setLoginInform(userId
                                        .getText().toString(), userPassword
                                        .getText().toString(),userPasswordCheck.getText().toString(),generalUser.isChecked(),ownerUser.isChecked());

                            }
                        }).setNegativeButton("취소", null);
        return builder.create();
    }
}
