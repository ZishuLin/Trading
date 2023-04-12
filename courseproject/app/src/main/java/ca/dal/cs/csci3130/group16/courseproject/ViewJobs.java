package ca.dal.cs.csci3130.group16.courseproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewJobs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_viewjobs);

        JobFilter filter;
        Bundle args = getIntent().getExtras();
        if (args != null
                && args.containsKey("JobFilter")
                && args.getParcelable("JobFilter") != null) {
            filter = args.getParcelable("JobFilter");
        } else filter = new JobFilter.Builder().build();

        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        DatabaseReference ref = databaseInstance.getReference("Jobs/");

        List<Job> jobs = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // looping through all submitted jobs
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Job job = postSnapshot.getValue(Job.class);
                    jobs.add(job);
                }

                List<Job> filtered = filter.filter(jobs);

                // displaying the retrieved list of jobs in the recycler view
                RecyclerView recyclerView = findViewById(R.id.employerJobsView);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewJobs.this));
                recyclerView.setAdapter(new ViewAdapter(getApplicationContext(), filtered));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
