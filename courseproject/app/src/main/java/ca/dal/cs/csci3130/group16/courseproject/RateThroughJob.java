package ca.dal.cs.csci3130.group16.courseproject;

import static java.sql.DriverManager.println;

import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Score objects store the business logic of uploading the number rating to firebase for the user
public class RateThroughJob {

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com/");
    private DatabaseReference userRef = firebaseDB.getReference("Users");
    DatabaseReference jobsRef = firebaseDB.getReference("Jobs");
    private String jobID;
    private String userID;

    private boolean isEmployee;

    private String currentUserID;

    public RateThroughJob(String jobID) {
        this.jobID = jobID;
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserType();
        extractUserID();
    }

    private void setUserID(String id) {
        userID = id;
    }
    public String getUserID(){return userID;}
    private void extractUserID() {
        DatabaseReference jobIDRef = jobsRef.child(jobID);
        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    if (adSnapshot.getKey().equals(jobID)) {
                        setID(adSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private void getUserType() {
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentRef = userRef.child(currentUserID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    if (adSnapshot.getKey().equals(currentUserID)) {
                        setUserType(adSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setUserType(DataSnapshot snap) {
        User user = snap.getValue(User.class);
        User.UserRole type = user.userRole;
        if(user.userRole == User.UserRole.EMPLOYEE) {
            isEmployee = true;
        } else {
            isEmployee = false;
        }
    }
    private void setID(DataSnapshot snap) {
        Job job = snap.getValue(Job.class);
        if (isEmployee == true) {
            setUserID(job.uIDEmployer);
        } else {
            setUserID(job.uIDEmployee);
        }
    }
    public void rate(float rating) {
        DatabaseReference rateRef = userRef.child(userID).child("Rating");
        rateRef.push().setValue(rating);
    }


}

