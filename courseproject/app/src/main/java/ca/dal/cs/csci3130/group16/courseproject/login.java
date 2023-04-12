package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class login extends AppCompatActivity {
    EditText editUsername;
    EditText editPassword;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Group 16 - Quick Cash - Login");
        editUsername = (EditText) findViewById(R.id.editEmailLogin);
        editPassword = (EditText) findViewById(R.id.editPasswordLogin);
        Button button = (Button) findViewById(R.id.loginButtonLogin);
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        button.setOnClickListener(new View.OnClickListener() {
            String errorMessage;
            public void onClick(View v) {
                if(v.getId() == R.id.loginButtonLogin){
                    // Take the value of two edit texts in Strings
                    String strEmail = editUsername.getText().toString();//input
                    String strPassword = editPassword.getText().toString();//input
                    // validations for input email and password
                    if (TextUtils.isEmpty(strEmail)) {
                        Toast.makeText(getApplicationContext(),
                                        "Please enter email!!",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                    if (TextUtils.isEmpty(strPassword)) {
                        Toast.makeText(getApplicationContext(),
                                        "Please enter password!!",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                    // signin existing user
                    else {
                        mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                    errorMessage = "Login successful!!";

                                    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference ref = databaseInstance.getReference("Users/" + uID);

                                    // Attach a listener to read the data at our posts reference
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);

                                            if (user.userRole == User.UserRole.EMPLOYEE) {
                                                Intent employeeDashboard = new Intent(login.this, EmployeeDashboard.class);
                                                startActivity(employeeDashboard);
                                            } else if (user.userRole == User.UserRole.EMPLOYER) {
                                                Intent employerDashboard = new Intent(login.this, EmployerDashboard.class);
                                                //TESTING DELETE
//                                                employerDashboard.putExtra("fromSubmitJob",true);
                                                startActivity(employerDashboard);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            System.out.println("The read failed: " + databaseError.getCode());
                                        }
                                    });
                                } else {
                                    // sign-in failed
                                    errorMessage = "Login not successful!!";
                                }
                                setStatusMessage(errorMessage);
                            }
                        });
                    }
                }
            }

        });

    }
    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.errorLabelLogin);
        statusLabel.setText(message.trim());
    }


}