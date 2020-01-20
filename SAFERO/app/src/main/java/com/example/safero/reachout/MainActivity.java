package com.example.safero.reachout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFormActivityListner, RegisterFragment.OnRegisterFormActivityListner {

    FirebaseDatabase database;
    DatabaseReference myRef;
    User user;
    String DeviceToken;
    ArrayList<User> user_list;

    //polygen arrays
    double poly1[][] = {{7.219932, 80.994355}, {7.219565, 80.991906}, {7.218613, 80.991942}, {7.218581, 80.993872}};
    double pointsArray[][] = {{7.219469, 80.996586}, {7.219501, 80.995824}, {7.218954, 80.992950}, {7.219007, 80.990936}, {7.222540, 80.993379}, {7.220347, 80.990375}};
    double graph[][] = new double[][] { {0,1,0,0,0,0},
            {1,0,1,0,1,0},
            {0,1,0,1,0,0},
            {0,0,1,0,0,1},
            {0,1,0,0,0,1},
            {0,0,0,1,1,0} };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeviceToken = FirebaseInstanceId.getInstance().getToken();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users"); //reference to the database
        user = new User();

        //initializing the array list
        user_list = new ArrayList<User>();

        Intent myIntent = new Intent(this,   MapsActivity.class);
        startActivity(myIntent);


        if (findViewById(R.id.fragment_container)!=null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
            //navigationView.setCheckedItem(R.id.nav_home);
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User u = dataSnapshot1.getValue(User.class);
                    user_list.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RegUser(String email, String password) {
        user.setEmail(email);
        user.setFcm_token(DeviceToken);
        user.setLat("3.25"); //dummy data
        user.setLon("2.55"); //dummy data
        user.setPassword(password);
        user.setName("Dinindu");
        myRef.push().setValue(user); //pushing the data to the database
    }


    @Override
    public void performLogin(String email, String password) {
        for (User user: user_list){
            if (email.equals(user.getEmail())){
                if (password.equals(user.getPassword()))
                    Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(MainActivity.this, "Password Incorect", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        Intent myIntent = new Intent(this,   MapsActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void moveToRegisterFragment() {
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
        //
    }

    @Override
    public void performRegister(String email, String password) {
        RegUser(email, password); //register the user
    }
}
