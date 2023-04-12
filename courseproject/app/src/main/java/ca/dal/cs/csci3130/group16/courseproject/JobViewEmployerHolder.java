package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobViewEmployerHolder extends RecyclerView.ViewHolder {
    TextView jobType;
    TextView description;
    TextView salary;
    TextView duration;
    TextView location;
    TextView jobStatus;
    TextView urgency;
    TextView date;
    Button pay;
    TextView respond;

    boolean hasEmployee;
    String descriptChecker;

    public JobViewEmployerHolder(@NonNull View itemView) {
        super(itemView);
        jobType = itemView.findViewById(R.id.jobType);
        description = itemView.findViewById(R.id.description);
        salary = itemView.findViewById(R.id.salary);
        duration = itemView.findViewById(R.id.duration);
        location = itemView.findViewById(R.id.location);
        jobStatus = itemView.findViewById(R.id.jobStatus);
        urgency = itemView.findViewById(R.id.urgency);
        date = itemView.findViewById(R.id.date);
        pay = itemView.findViewById(R.id.payButton);
        respond = itemView.findViewById(R.id.respondText);

        itemView.findViewById(R.id.payButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentPage = new Intent(view.getContext(), PayEmployee.class);
                paymentPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(paymentPage);
                PayEmployee.checkDecr = descriptChecker;
            }
        });

    }
}
