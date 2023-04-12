package ca.dal.cs.csci3130.group16.courseproject;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class FirebaseSnapper {
    private FirebaseSnapper(){}

    public static DataSnapshot get(@NonNull DatabaseReference reference) {
        Task<DataSnapshot> task = reference.get();
        while (true) {
            if (task.isComplete()) break;
        }
        return task.getResult();
    }
}
