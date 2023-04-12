package ca.dal.cs.csci3130.group16.courseproject;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class JobViewEmployeeHolder extends RecyclerView.ViewHolder {
    TextView jobType;
    TextView description;
    TextView salary;
    TextView duration;
    TextView location;
    TextView jobStatus;
    TextView urgency;
    TextView date;
    Button apply;
    TextView applied;

    String jobID;

    String employerID;
    String descriptChecker;
    Context contex;
    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com/");
    DatabaseReference jobsRef = firebaseDB.getReference("Jobs");

    public JobViewEmployeeHolder(@NonNull View itemView, Context context) {
        super(itemView);
        jobType = itemView.findViewById(R.id.jobType);
        description = itemView.findViewById(R.id.description);
        salary = itemView.findViewById(R.id.salary);
        duration = itemView.findViewById(R.id.duration);
        location = itemView.findViewById(R.id.location);
        jobStatus = itemView.findViewById(R.id.jobStatus);
        urgency = itemView.findViewById(R.id.urgency);
        date = itemView.findViewById(R.id.date);
        apply = itemView.findViewById(R.id.applyButton);
        applied = itemView.findViewById(R.id.appliedText);
        this.contex = context;

        itemView.findViewById(R.id.applyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PopUpWindowRating popup = new PopUpWindowRating();
//
//                DataSnapshot snap = FirebaseSnapper.get(jobsRef);
//
//                for (DataSnapshot adSnapshot : snap.getChildren()) {
//                    Job job = adSnapshot.getValue(Job.class);
//                    if (job.uIDEmployer.equals(employerID)) {
//                        jobID = adSnapshot.getKey();
//                    }
//                }
//                popup.setPopup(view, jobID, context);
//                popup.start(view);

                apply.setVisibility(View.INVISIBLE);
                apply.setClickable(false);
                applied.setVisibility(View.VISIBLE);

                Toast.makeText(view.getContext(), "You have applied to the job!", Toast.LENGTH_SHORT).show();


                //Call AttachEmployeeWithJob so that the appropriate work can be done while following SRP.
                AttachEmployeeWithJob Attach = new AttachEmployeeWithJob();
                Attach.makeAttachment(employerID, descriptChecker);
                Intent refresh = new Intent(view.getContext(), ViewJobsEmployee.class);
                refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        view.getContext().startActivity(refresh);
                    }
                }, 5000);
                PopUpWindowRating popup = new PopUpWindowRating();

                DataSnapshot snap = FirebaseSnapper.get(jobsRef);

                for (DataSnapshot adSnapshot : snap.getChildren()) {
                    Job job = adSnapshot.getValue(Job.class);
                    if (job.uIDEmployer.equals(employerID)) {
                        jobID = adSnapshot.getKey();
                    }
                }
                popup.setPopup(view, jobID, context);


            }
        });
    }
}
