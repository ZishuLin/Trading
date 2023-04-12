package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class EmployeeDashboard extends AppCompatActivity {
    locationFinder locationFinder;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
    DatabaseReference currentUserRef = databaseInstance.getReference("Users/"+mAuth.getUid());

    private RequestQueue requestQueue;

    private static final String PUSH_NOTIFICATION_ENDPOINT = "https://group-16-62934-default-rtdb.firebaseio.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employee_dashboard);
        Button buttonSearch = (Button) findViewById(R.id.search);
        Button buttonView = findViewById(R.id.buttonEmployeeViewJobs);
        Button buttonLogout = findViewById(R.id.employeeLogout);
        Button buttonExplore = findViewById(R.id.exploreEmployerBtn);
        Button buttonLoad = findViewById(R.id.loadSearch);
        requestQueue = Volley.newRequestQueue(this);

        FirebaseMessaging.getInstance().subscribeToTopic("jobs");
        TextView locationText = findViewById(R.id.currentLocation);

        locationFinder = new locationFinder(this);
        locationText.setText("Loading...");
        setLocation(locationText);

        buttonSearch.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDashboard.this, jobSearch.class);
            startActivity(intent);
        });

        buttonView.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDashboard.this, ViewJobs.class);
            startActivity(intent);
        });

        buttonExplore.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDashboard.this, EmployerSearchResult.class);
            startActivity(intent);});
        buttonView.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDashboard.this, ViewJobsEmployee.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(EmployeeDashboard.this, loginOrRegisterPage.class);
            startActivity(intent);
        });

        buttonLoad.setOnClickListener(view -> {
            DataSnapshot snapshot = FirebaseSnapper.get(currentUserRef);
            if (snapshot != null) {
                User user = snapshot.getValue(User.class);
                if (user != null && user.preferredSearch != null) {
                    JobFilter filter = user.preferredSearch;
                    Intent intent = new Intent(EmployeeDashboard.this, ViewJobsEmployee.class);
                    intent.putExtra("JobFilter", filter);
                    startActivity(intent);
                }
            }
            else Toast.makeText(this, "No Saved Search Found.", Toast.LENGTH_SHORT).show();
        });
    }

    private void setLocation(@NonNull TextView locationText) {
        Handler handler = new Handler();
        locationFinder.requestLocationInfo(new locationFinder.LocationFinderCallback() {
            @Override
            public void onLocationFound(@Nullable LatLng location, @NonNull String cityState) {
                locationText.setText(cityState);
                DetectNewJobs alertNewJobs = new DetectNewJobs(getApplicationContext(), EmployeeDashboard.this, location);
                handler.postDelayed(alertNewJobs::listenForNewJobs, 2000);
            }
        });
    }


}









