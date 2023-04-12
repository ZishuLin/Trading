package ca.dal.cs.csci3130.group16.courseproject;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingExtractor {
    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com/");
    private DatabaseReference userRef = firebaseDB.getReference("Users");
    private String userID;

    private float currentAvg;

    public RatingExtractor(String userID) {
        this.userID = userID;
    }

    private void updateAvg() {
        DatabaseReference rateRef = userRef.child(userID).child("Rating");
        avgExtract(get(rateRef));
        DatabaseReference avgRef = userRef.child(userID).child("Average");
        avgRef.setValue(currentAvg);
    }

    private void avgExtract(DataSnapshot snap) {
        float total = 0;
        int n = 0;
        for (DataSnapshot adSnapshot : snap.getChildren()) {
            double rating = adSnapshot.getValue(double.class);

            total = total + (float) rating;
            n = n + 1;
        }
        currentAvg = total/n;
    }

    public float getCurrentAvg() {
        updateAvg();
        return currentAvg;
    }

    public static DataSnapshot get(@NonNull DatabaseReference reference) {
        Task<DataSnapshot> task = reference.get();
        while (true) {
            if (task.isComplete()) break;
        }
        return task.getResult();
    }
}
