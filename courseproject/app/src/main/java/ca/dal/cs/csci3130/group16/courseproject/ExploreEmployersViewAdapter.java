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

import java.util.ArrayList;
import java.util.List;

public class ExploreEmployersViewAdapter extends RecyclerView.Adapter<ExploreEmployersHolder> {
    Context context;
    List<User> users;
    String employerID = null;

    FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
    DatabaseReference firebaseRef = databaseInstance.getReference();

    public ExploreEmployersViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ExploreEmployersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExploreEmployersHolder(LayoutInflater.from(context).inflate(R.layout.employer_details_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExploreEmployersHolder holder, int position) {
        User user = users.get(position);
        TextView employerName = holder.employerName;
        TextView jobsOffered = holder.jobsOffered;
        TextView rating = holder.rating;

        employerName.setText(user.firstName + " " + user.lastName);
        setEmployeeID(FirebaseSnapper.get(firebaseRef), user);
        List<Job> jobList = getJobsFromUser(FirebaseSnapper.get(firebaseRef));
        jobsOffered.setText("Jobs offered: " + jobList.size());
        rating.setText(getRating(FirebaseSnapper.get(firebaseRef)));
    }

    private void setEmployeeID(@NonNull DataSnapshot snapshot, User target) {
        DataSnapshot usersSnapshot = snapshot.child("Users/");

        // retrieving target employee's id
        for (DataSnapshot currentUserSnap : usersSnapshot.getChildren()) {
            User user = currentUserSnap.getValue(User.class);
            if (user == null || user.email == null) continue;
            if (user.email.equals(target.email)) {
                employerID = currentUserSnap.getKey();
                break;
            }
        }
    }

    @NonNull
    private List<Job> getJobsFromUser(@NonNull DataSnapshot snapshot) {
        DataSnapshot jobsSnapshot = snapshot.child("Jobs/");

        List<Job> jobList = new ArrayList<>();

        if (employerID == null) return jobList;

        // retrieving the jobs submitted by the target employer using the uid extracted above
        for (DataSnapshot currentJobSnap: jobsSnapshot.getChildren()) {
            Job job = currentJobSnap.getValue(Job.class);

            if (job == null) continue;
            if (job.uIDEmployer.equals(employerID)) {
                jobList.add(job);
            }
        }
        return jobList;
    }

    String getRating(@NonNull DataSnapshot snapshot) {
        StringBuilder sb = new StringBuilder();

        DataSnapshot employeeSnapshot = snapshot.child("Users/" + employerID + "/Average");
        if (employeeSnapshot.getValue(Double.class) != null) {
            RatingExtractor ratingExtractor = new RatingExtractor(employerID);

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
