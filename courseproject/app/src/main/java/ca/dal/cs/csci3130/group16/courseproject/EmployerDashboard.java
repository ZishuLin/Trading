package ca.dal.cs.csci3130.group16.courseproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EmployerDashboard extends AppCompatActivity {
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_dashboard);

        Button logoutButton = findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(EmployerDashboard.this, loginOrRegisterPage.class);
            startActivity(intent);
        });

        Button submitJobBtn = (Button) findViewById(R.id.employerSubmitJobButton);
        submitJobBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerDashboard.this, SubmitJob.class);
            startActivity(intent);
        });

        Button buttonExplore = findViewById(R.id.exploreEmployeeBtn);
        buttonExplore.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerDashboard.this, EmployeeSearchResult.class);
            startActivity(intent);
        });

        Button viewJobsButton = (Button) findViewById(R.id.employerViewJobsButton);
        viewJobsButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerDashboard.this, ViewJobs.class);
            JobFilter filter = new JobFilter.Builder().withEmployer(FirebaseAuth.getInstance().getUid()).build();
            intent.putExtra("JobFilter", filter);
            startActivity(intent);
        });

        //Will show popup window if coming from SubmitJob
        Bundle args = getIntent().getExtras();
        if (args != null && args.containsKey("fromSubmitJob")
                && args.getBoolean("fromSubmitJob")) {
            RecommendEmployee rec = new RecommendEmployee(findViewById(android.R.id.content), getApplicationContext());
            rec.openPopup();
        }
//            //POPUP WINDOW
//            ArrayList<String> employeeList = (ArrayList<String>) uidList();
//            ArrayList<Pair<String, Float>> ratingList = new ArrayList<Pair<String,Float>>();
//            RatingExtractor rating;
//            for (int i = 0; i < employeeList.size(); i++) {
//                rating = new RatingExtractor(employeeList.get(i));
//                ratingList.add(new Pair<>(employeeList.get(i), rating.getCurrentAvg()));
//            }
//            Dialog employeePopup = new Dialog(this);
//            employeePopup.setContentView(R.layout.employee_popup);
//
//
//            TextView firstEmployee = (TextView) employeePopup.findViewById(R.id.firstEmployee);
//            TextView secondEmployee = (TextView) employeePopup.findViewById(R.id.secondEmployee);
//            TextView thirdEmployee = (TextView) employeePopup.findViewById(R.id.thirdEmployee);
//            firstEmployee.setText(employeeList.get(0));
//            secondEmployee.setText(employeeList.get(1));
//            thirdEmployee.setText(employeeList.get(2));
//            employeePopup.show();
//            }
        }



    public static List<String> uidList() {
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        DatabaseReference usersRef = databaseInstance.getReference("Users/");
        List<String> list = new ArrayList<>();

        Task<DataSnapshot> task = usersRef.get();
        while (true) {
            if (task.isComplete()) break;
        }
        DataSnapshot usersSnapshot = task.getResult();

        for (DataSnapshot currentUserSnap : usersSnapshot.getChildren()) {
            String uid = currentUserSnap.getKey();
            list.add(uid);
        }
        return list;
    }


}
