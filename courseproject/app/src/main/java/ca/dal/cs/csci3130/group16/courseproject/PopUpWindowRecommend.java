package ca.dal.cs.csci3130.group16.courseproject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.apps.common.testing.accessibility.framework.replacements.LayoutParams;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopUpWindowRecommend {
    View currentView;
    Context con;

    User[] bes;
    float[] avg;

    public void setPopup(View view, Context context, User[] bes, float[] avg) {
        this.currentView= view;
        con = context;
        this.bes = bes;
        this.avg = avg;
        openWindow(currentView);
    }

    private void openWindow(View view) {
        String[] strings = new String[3];

        for (int i = 0; i < 3; i++) {
            if (avg[i] == -1) {
                strings[i] = i+1 + ": " + bes[i].firstName + " " + bes[i].lastName + "\n Average Rating: N/A";
            } else {
                strings[i] = i+1 + ": " + bes[i].firstName + " " + bes[i].lastName + "\n Average Rating: " + avg[i];
            }
        }

        LayoutInflater inflater = (LayoutInflater)
                con.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.employee_popup, null);

        TextView emp1 = popupView.findViewById(R.id.firstEmployee);
        TextView emp2 = popupView.findViewById(R.id.secondEmployee);
        TextView emp3 = popupView.findViewById(R.id.thirdEmployee);

        emp1.setText(strings[0]);
        emp2.setText(strings[1]);
        emp3.setText(strings[2]);

        Button submit = (Button) popupView.findViewById(R.id.subBut);

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
                popupWindow.dismiss();
            }

        });

    }
}
