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


public class LoginFragment extends Fragment {

    EditText email; //email holder
    EditText password; //password holder
    Button login;//btn login holder
    Button regmov;

    OnLoginFormActivityListner loginFormActivityListner;
    public interface OnLoginFormActivityListner{
        public void performLogin(String email, String password);
        public void moveToRegisterFragment();
        //public void hideProgressDialog();
    }


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.emailLogin); //taking the data from the layout
        password = view.findViewById(R.id.passwordLogin); //taking the data from the layout
        login = view.findViewById(R.id.btnLogin); //taking the data from the layout

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFormActivityListner.performLogin(email.getText().toString(), password.getText().toString());
            }
        });

        regmov = view.findViewById(R.id.regmovbtn);

        regmov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFormActivityListner.moveToRegisterFragment();
            }
        });

        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListner = (OnLoginFormActivityListner) activity;
    }

}
