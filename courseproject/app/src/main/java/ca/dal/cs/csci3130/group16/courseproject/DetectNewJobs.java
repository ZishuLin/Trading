package ca.dal.cs.csci3130.group16.courseproject;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetectNewJobs {
    //firebase stuff
    private FirebaseDatabase firebaseDB;
    private DatabaseReference firebaseDBRef;
    private String FIREBASE_URL;
    private AlertEmployee alert;
    private long jobCount;
    private int dataChanges;
    private LatLng location;
    private final double LOCAL_DISTANCE_DEGREES = 0.15;


    public DetectNewJobs(Context context, Activity activity, LatLng location) {
        alert = new AlertEmployee(context, activity);
        this.location = location;
        this.FIREBASE_URL = context.getResources().getString(R.string.FIREBASE_URL);

        firebaseDB = FirebaseDatabase.getInstance(FIREBASE_URL);
        firebaseDBRef = firebaseDB.getReference("Jobs");
        jobCount = 0;
        firebaseDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get the amount of jobs available
                jobCount = snapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //not used, required for listener
            }
        });
    }
     private void sawDataChange() {
        dataChanges++;
     }

     private int getDataChanges() {
        return dataChanges;
     }

     private boolean checkIfValidChild(DataSnapshot snapshot) {
         return snapshot.hasChild("jobType") && snapshot.hasChild("latitude") && snapshot.hasChild("longitude");
     }

    public void listenForNewJobs(){

        firebaseDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previous) {
                if(checkIfValidChild(snapshot)) {
                    if(getDataChanges() < jobCount) {
                        sawDataChange();
                    } else {
                        double latitude = snapshot.child("latitude").getValue(double.class);
                        double longitude = snapshot.child("longitude").getValue(double.class);
                        LatLng coordinates = new LatLng(latitude, longitude);
                        String jobType = snapshot.child("jobType").getValue(String.class);
                        if (isJobLocal(coordinates)) {
                            alert.sendNotification(jobType);
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previous) {
                //necessary override for addChildEventListener, this method is not actually used
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //necessary override for addChildEventListener, this method is not actually used
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previous) {
                //necessary override for addChildEventListener, this method is not actually used
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //necessary override for addChildEventListener, this method is not actually used
            }
        });
    }

    private boolean isJobLocal(LatLng jobLatLng) {
        double myLng = location.longitude;
        double myLat = location.latitude;

        double lng = jobLatLng.longitude;
        double lat = jobLatLng.latitude;

        return lng < (myLng + LOCAL_DISTANCE_DEGREES) && lng > (myLng - LOCAL_DISTANCE_DEGREES) &&
                lat < (myLat + LOCAL_DISTANCE_DEGREES) && lat > (myLat - LOCAL_DISTANCE_DEGREES);
    }
}
