package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
Author: Alex Kotzeff
Iteration: 1
This class controls the functionality for the login_or_register_page, where users select to either login or register as a new user.
 */
public class loginOrRegisterPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_register_page);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(loginOrRegisterPage.this, login.class);
                startActivity(login);
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerPage = new Intent(loginOrRegisterPage.this, LandingPage.class);
                startActivity(registerPage);
            }
        });
    }
}
