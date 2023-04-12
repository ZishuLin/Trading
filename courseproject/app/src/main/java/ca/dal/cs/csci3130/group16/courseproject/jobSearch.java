package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class jobSearch extends AppCompatActivity {

    private EditText editSearchText, editMinHrs, editMaxHrs, editMinSalary, editMaxSalary;
    private Button buttonSearch, buttonSave;
    private FirebaseAuth auth;
    private DatabaseReference prefSearchRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobsearch);
        init();
        initFirebaseRef();
        setListeners();
    }

    private void initFirebaseRef() {
        auth = FirebaseAuth.getInstance();
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        prefSearchRef = databaseInstance.getReference("Users/" + auth.getUid() + "/preferredSearch");
    }

    private void init() {
        editSearchText = findViewById(R.id.findByDescription);
        editMinHrs = findViewById(R.id.editMinDuration);
        editMaxHrs = findViewById(R.id.editMaxDuration);
        editMinSalary = findViewById(R.id.editMinSalary);
        editMaxSalary = findViewById(R.id.editMaxSalary);
        buttonSearch = findViewById(R.id.searchBtn);
        buttonSave = findViewById(R.id.saveFilterButton);
    }

    private void setListeners() {
        buttonSearch.setOnClickListener(view -> {
            JobFilter filter = getFilter();
            Intent intent = new Intent(jobSearch.this, ViewJobsEmployee.class);
            intent.putExtra("JobFilter", filter);
            startActivity(intent);
        });
        buttonSave.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Preference set", Toast.LENGTH_SHORT).show();
            JobFilter filter = getFilter();
            prefSearchRef.setValue(filter);
        });
    }

    private JobFilter getFilter() {
        String searchTxt;
        Integer minHrs, maxHrs;
        Float minSal, maxSal;

        searchTxt = editSearchText.getText().toString();
        minHrs = tryParseInt(editMinHrs.getText().toString());
        maxHrs = tryParseInt(editMaxHrs.getText().toString());
        minSal = tryParseFloat(editMinSalary.getText().toString());
        maxSal = tryParseFloat(editMaxSalary.getText().toString());

        return new JobFilter.Builder()
                .withDurationRange(minHrs, maxHrs)
                .withSalaryRange(minSal, maxSal)
                .withStringSearch(searchTxt)
                .build();
    }

    private Float tryParseFloat(String s) {
        Float f;
        try {
            f = Float.parseFloat(s);
        } catch (Exception e) {
            return null;
        }
        return f;
    }
    private Integer tryParseInt(String s) {
        Integer i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
        return i;
    }

}
