package ca.dal.cs.csci3130.group16.courseproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ViewAdapterEmployee extends RecyclerView.Adapter<JobViewEmployeeHolder> {
    Context context;
    List<Job> jobs;
    Geocoder geocoder;
    List<Address> addresses;

    public ViewAdapterEmployee(Context context, List<Job> jobs) {
        this.context = context;
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobViewEmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobViewEmployeeHolder(LayoutInflater.from(context).inflate(R.layout.job_view_employee, parent, false), context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JobViewEmployeeHolder holder, int position) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");

        holder.jobType.setText(jobs.get(position).jobType);

        String duration = jobs.get(position).duration.toString();
        Float salary = jobs.get(position).salary;
        holder.duration.setText("Duration: " + duration + "h");
        holder.salary.setText("Salary: " + NumberFormat.getCurrencyInstance().format(salary));
        holder.employerID = jobs.get(position).uIDEmployer;
        holder.descriptChecker = jobs.get(position).description;

        holder.description.setText(jobs.get(position).description);

        String showJob = jobs.get(position).isOpen ? "OPEN" : "CLOSED";

        if(showJob.equals("CLOSED")){
            holder.apply.setVisibility(View.INVISIBLE);
        }
        else{
            holder.apply.setVisibility(View.VISIBLE);
        }

        geocoder = new Geocoder(context, Locale.getDefault());

        Double latitude = jobs.get(position).latitude;
        Double longitude = jobs.get(position).longitude;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        holder.location.setText(city + ", " + state);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        holder.date.setText(simpleDateFormat.format(jobs.get(position).date));
        holder.urgency.setText(jobs.get(position).urgency.toString());
        holder.jobStatus.setText(jobs.get(position).isOpen ? "OPEN" : "CLOSED");
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}
