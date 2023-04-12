package ca.dal.cs.csci3130.group16.courseproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Locale;

public class locationFinder {

    public interface LocationFinderCallback {
        void onLocationFound(@Nullable LatLng location, @NonNull String cityState);
    }

    //Location request code.
    private static final int REQUEST_CODE = 111;
    private FusedLocationProviderClient client;

    private final Context context;
    private final Activity activity;

    public locationFinder(@NonNull AppCompatActivity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        client = LocationServices.getFusedLocationProviderClient(context);
    }

    private LatLng getLatLng(Location location) {
        if (location == null) return null;
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private String getCityState(Location location) {
        if (location == null) return null;
        Address address;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
        } catch (IOException | IllegalArgumentException | IndexOutOfBoundsException e) {
            return "Oops! Location Services are not available.";
        }
        String city = address.getLocality();
        String state = address.getAdminArea();
        return city + ", " + state;
    }

    public void requestLocationInfo(LocationFinderCallback listener) {
        //Make sure we have permission first.
        if (missingPermissions()) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            requestLocationInfo(listener);
            return;
        }

        @SuppressLint("MissingPermission")
        Task<Location> task = client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null);

        //Listen for success.
        task.addOnSuccessListener(location -> {
            listener.onLocationFound(getLatLng(location), getCityState(location));
        });
    }

    private boolean missingPermissions() {
        boolean missingCoarse = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean missingFine = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        return missingCoarse || missingFine;
    }

}
