package ca.dal.cs.csci3130.group16.courseproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreEmployersHolder extends RecyclerView.ViewHolder {
    TextView jobsOffered;
    TextView rating;
    TextView employerName;

    public ExploreEmployersHolder(@NonNull View itemView) {
        super(itemView);
        //populate fields
        jobsOffered = itemView.findViewById(R.id.jobsPosted);
        rating = itemView.findViewById(R.id.employerRating);
        employerName = itemView.findViewById(R.id.employerName);

    }
}
