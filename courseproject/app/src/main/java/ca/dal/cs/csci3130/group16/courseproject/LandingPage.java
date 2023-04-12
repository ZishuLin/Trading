package ca.dal.cs.csci3130.group16.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Group 16 - Quick Cash - Register");
        setContentView(R.layout.landing_page);

        Button employeeButton = findViewById(R.id.EmployeeButton);
        employeeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent employeeChange = new Intent(LandingPage.this, NewUser.class);
                employeeChange.putExtra("IsEmployee",true);
                startActivity(employeeChange);
            }
        });

        Button employerButton = findViewById(R.id.EmployerButton);
        employerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent employerChange = new Intent(LandingPage.this, NewUser.class);
                employerChange.putExtra("IsEmployee",false);
                startActivity(employerChange);
            }
        });

    }
}
