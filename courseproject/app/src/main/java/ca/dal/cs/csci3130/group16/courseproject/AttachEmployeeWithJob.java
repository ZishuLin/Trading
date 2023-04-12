package ca.dal.cs.csci3130.group16.courseproject;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttachEmployeeWithJob {
    private FirebaseAuth auth;

    //This is a busy class. It needs to get the employee uID, along with finding the
    //job that the user applied to.
    public void makeAttachment(String employer, String description){
        auth = FirebaseAuth.getInstance();

        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");

        DatabaseReference ref = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com").getReference().child("Jobs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    if(String.valueOf(objSnapshot.child("uIDEmployer").getValue()).equals(employer) && String.valueOf(objSnapshot.child("description").getValue()).equals(description)){
                        databaseInstance.getReference("Jobs/" + objSnapshot.getKey() + "/uIDEmployee").setValue(auth.getUid());
                        databaseInstance.getReference("Jobs/" + objSnapshot.getKey() + "/isOpen").setValue(false);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }
        });
    }
}
