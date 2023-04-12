package ca.dal.cs.csci3130.group16.courseproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreEmployeesHolder extends RecyclerView.ViewHolder {
    TextView jobsTaken;
    TextView incomeEarned;
    TextView rating;
    TextView employeeName;

    public ExploreEmployeesHolder(@NonNull View itemView) {
        super(itemView);
        //populate fields
        employeeName = itemView.findViewById(R.id.employeeName);
        jobsTaken = itemView.findViewById(R.id.jobsTaken);
        incomeEarned = itemView.findViewById(R.id.incomeEarned);
        rating = itemView.findViewById(R.id.employeeRating);
    }
}
