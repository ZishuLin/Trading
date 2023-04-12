package ca.dal.cs.csci3130.group16.courseproject;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

public class Dashboard extends AppCompatActivity {

    locationFinder locationFinder;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_dash);

        TextView locationText = findViewById(R.id.location);

        locationFinder = new locationFinder(this);
        locationText.setText("Loading...");
        setLocation(locationText);
    }

    private void setLocation(TextView locationText) {
        Handler handler = new Handler();
        locationFinder.requestLocationInfo(new locationFinder.LocationFinderCallback() {
            @Override
            public void onLocationFound(@Nullable LatLng location, @NonNull String cityState) {
                locationText.setText(cityState);
                DetectNewJobs alertNewJobs = new DetectNewJobs(getApplicationContext(), Dashboard.this, location);
                handler.postDelayed(alertNewJobs::listenForNewJobs, 2000);
            }
        });
    }

}
