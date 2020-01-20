package com.example.safero.reachout;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterFragment extends Fragment {

    EditText email; //email holder
    EditText password; //password holder
    EditText confirmPassword; //confirmpassword holder
    Button register;//btn login holder
    Context context;

    OnRegisterFormActivityListner registerFormActivityListner;

    public interface OnRegisterFormActivityListner{
        public void performRegister(String email, String password);
        //public void showProgressDialog();
        //public void hideProgressDialog();
    }


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        email = view.findViewById(R.id.emailRegister);
        password = view.findViewById(R.id.passwordRegister);
        confirmPassword = view.findViewById(R.id.passwordRegisterConf);
        register = view.findViewById(R.id.btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegister(email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString());
            }
        });


        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        registerFormActivityListner = (OnRegisterFormActivityListner) activity;
    }

    private void performRegister(String email, String pass, String confPass){
        //Toast.makeText(context, email, Toast.LENGTH_SHORT).show();
        if (!((email.equals("") || pass.equals("") || confPass.equals("")))){ //check whether the fields are not empty
            if (pass.equals(confPass)){
                Toast.makeText(context, "Registration Successfull", Toast.LENGTH_SHORT).show();
                registerFormActivityListner.performRegister(email, pass);
            }
            else {
                Toast.makeText(context, "Password & Conf Password does not match", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show();
        }
    }
}
