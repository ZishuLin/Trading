package ca.dal.cs.csci3130.group16.courseproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewPushNotificationActivity extends AppCompatActivity {

    private TextView titleTV;
    private TextView bodyTV;
    private TextView jobIdTV;
    private TextView jobLocationTV;
    private FirebaseAuth auth;
    private DatabaseReference prefSearchRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_push_notification);
        initFirebaseRef();
        init();
        setData();

    }
    private void initFirebaseRef() {
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        prefSearchRef = databaseInstance.getReference("Users/" + auth.getUid() + "/preferredSearch");
    }

    private void init() {
        //binding the views with the variables
        titleTV = findViewById(R.id.titleTV);
        bodyTV = findViewById(R.id.bodyTV);
        jobIdTV = findViewById(R.id.jobIdTV);
        jobLocationTV = findViewById(R.id.jobLocationTV);
    }

    private void setData() {
        final Bundle extras = getIntent().getExtras();
        final String title = extras.getString("Name");
        final String body = extras.getString("description");
        titleTV.setText(title);
        bodyTV.setText(body);
    }
}