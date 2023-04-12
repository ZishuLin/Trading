package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecommendEmployee {

    private FirebaseDatabase firebaseDB;
    //        = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com/");
    private DatabaseReference firebaseDBRef;

    //private DatabaseReference userRef = firebaseDB.getReference("Users");
    DataSnapshot snap;

    User[] best = new User[3];
    float[] avgs = new float[3];

    View view;
    Context context;

    public RecommendEmployee(View view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void openPopup() {
        setupBest();
    }

    public void prepPopup(DataSnapshot snap) {
        int n = 0;
        for (DataSnapshot adSnapshot : snap.getChildren()) {
            User user = adSnapshot.getValue(User.class);
            if (adSnapshot.hasChild("Average")) {
                double av = adSnapshot.child("Average").getValue(double.class);
                float temp = (float) av;
                avgs[n] = temp;
            } else {
                avgs[n] = -1;
            }
            best[n] = user;
            n = n + 1;
        }
        PopUpWindowRecommend popup = new PopUpWindowRecommend();
        popup.setPopup(view, context, best, avgs);
    }

    private void setupBest() {

        String FIREBASE_URL = "https://group-16-62934-default-rtdb.firebaseio.com";

        firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL);
        firebaseDBRef = firebaseDB.getReference("Users");
        firebaseDBRef.orderByChild("Average").limitToLast(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prepPopup(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //not used, required for listener
            }
        });

    }

}
