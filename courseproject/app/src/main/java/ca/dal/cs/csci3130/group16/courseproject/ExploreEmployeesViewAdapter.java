package ca.dal.cs.csci3130.group16.courseproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ExploreEmployeesViewAdapter extends RecyclerView.Adapter<ExploreEmployeesHolder> {
    Context context;
    List<User> users;
    String employeeID = null;

    FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
    DatabaseReference firebaseRef = databaseInstance.getReference();

    public ExploreEmployeesViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ExploreEmployeesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExploreEmployeesHolder(LayoutInflater.from(context).inflate(R.layout.employee_details_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExploreEmployeesHolder holder, int position) {
        User user = users.get(position);
        TextView employeeName = holder.employeeName;
        TextView jobsTaken = holder.jobsTaken;
        TextView incomeEarned = holder.incomeEarned;
        TextView rating = holder.rating;

        employeeName.setText(user.firstName + " " + user.lastName);
        setEmployeeID(FirebaseSnapper.get(firebaseRef), user);
        List<Job> jobList = getJobsFromUser(FirebaseSnapper.get(firebaseRef));
        jobsTaken.setText("Jobs taken: " + jobList.size());
        incomeEarned.setText("Income earned: " + NumberFormat.getCurrencyInstance().format(tallyEarnings(jobList)));
        rating.setText(getRating(FirebaseSnapper.get(firebaseRef)));
    }

    private void setEmployeeID(@NonNull DataSnapshot snapshot, User target) {
        DataSnapshot usersSnapshot = snapshot.child("Users/");

        // retrieving target employee's id
        for (DataSnapshot currentUserSnap : usersSnapshot.getChildren()) {
            User user = currentUserSnap.getValue(User.class);
            if (user == null || user.email == null) continue;
            if (user.email.equals(target.email)) {
                employeeID = currentUserSnap.getKey();
                break;
            }
        }
    }

    @NonNull
    private List<Job> getJobsFromUser(@NonNull DataSnapshot snapshot) {
        DataSnapshot jobsSnapshot = snapshot.child("Jobs/");

        List<Job> jobList = new ArrayList<>();

        // retrieving the jobs taken by the target employee using the uid extracted above
        if (employeeID == null) return jobList;
        for (DataSnapshot currentJobSnap : jobsSnapshot.getChildren()) {
            Job job = currentJobSnap.getValue(Job.class);
            if (job == null) continue;
            if (job.uIDEmployee.equals(employeeID)) {
                jobList.add(job);
            }
        }

        return jobList;
    }

    @Contract(pure = true)
    private double tallyEarnings(@NonNull List<Job> jobList) {
        double earnings = 0.0;
        for (Job job : jobList) {
            earnings += job.salary;
        }

        return earnings;
    }

    String getRating(@NonNull DataSnapshot snapshot) {
        StringBuilder sb = new StringBuilder();
        DataSnapshot employeeSnapshot = snapshot.child("Users/" + employeeID + "/Average");

        if (employeeSnapshot.getValue(Double.class) != null) {
            RatingExtractor ratingExtractor = new RatingExtractor(employeeID);

            double rating = ratingExtractor.getCurrentAvg();

            //get from user's rating field
            int i;
            for (i = 0; i < Math.floor(rating); i++) {
                sb.append('★');
            }
            if (i < rating) {
                sb.append("½");
            }
        } else {
            sb.append("No rating");
        }

        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
