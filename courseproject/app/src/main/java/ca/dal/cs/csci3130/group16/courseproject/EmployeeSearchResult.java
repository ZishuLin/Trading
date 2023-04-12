package ca.dal.cs.csci3130.group16.courseproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class EmployeeSearchResult extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_search_results);

        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        DatabaseReference ref = databaseInstance.getReference("Users/");

        List<User> users = new ArrayList<>();

        Button searchBtn = findViewById(R.id.employeeSearchBtn);

        searchBtn.setOnClickListener(view -> {
            String searchName = ((EditText) findViewById(R.id.editEmployeeName)).getText().toString();

            if (searchName.length() > 0) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // looping through all submitted jobs
                        users.clear();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);

                            // getting list of employers that match the search criteria
                            String userName = (user.firstName + " " + user.lastName).toLowerCase();
                            if (user.userRole == User.UserRole.EMPLOYEE && userName.contains(searchName.toLowerCase())) {
                                users.add(user);
                            }
                        }

                        // displaying the retrieved list of jobs in the recycler view
                        RecyclerView recyclerView = findViewById(R.id.employeeSearchResults);
                        recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeSearchResult.this));
                        recyclerView.setAdapter(new ExploreEmployeesViewAdapter(getApplicationContext(), users));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //not needed, required for listener
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Error: Search field is empty", Toast.LENGTH_LONG).show();
            }
        });
    }
}
