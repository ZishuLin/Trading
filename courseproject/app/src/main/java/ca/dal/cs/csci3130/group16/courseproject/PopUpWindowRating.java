
package ca.dal.cs.csci3130.group16.courseproject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.common.testing.accessibility.framework.replacements.LayoutParams;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopUpWindowRating {

    private RatingBar ratingBar;
    private RateThroughJob rater;
    String jobID;

    View currentView;
    Context con;

    public void setPopup(View view, String job, Context context) {
        this.currentView= view;
        this.jobID = job;
        con = context;
        start(currentView);
    }

    public void start(View view) {
        Button submit = (Button) currentView.findViewById(R.id.button);
        View window = currentView.findViewById(R.id.constraintLayout);

        openWindow(currentView, jobID);
        rater = new RateThroughJob(jobID);
    }


    private void openWindow(View view, String jobID) {
        LayoutInflater inflater = (LayoutInflater)
                con.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.rating_popup, null);
        //TextView textView = popupView.findViewById(R.id.textview);
        ratingBar= popupView.findViewById(R.id.ratingBar);
        Button submit = (Button) popupView.findViewById(R.id.button);

        int width = LayoutParams.WRAP_CONTENT;
        int height = LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                float rating = ratingBar.getRating();
                submitRating(rating);
                popupWindow.dismiss();
            }

        });

    }

    public void submitRating(float rating) {
        rater.rate(rating);
        RatingExtractor user = new RatingExtractor(rater.getUserID());
        user.getCurrentAvg();
    }
    
}
