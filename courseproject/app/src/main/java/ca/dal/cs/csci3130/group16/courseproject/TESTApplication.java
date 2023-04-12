package ca.dal.cs.csci3130.group16.courseproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TESTApplication extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_view_employee);

        Button apply = (Button) findViewById(R.id.applyButton);
        TextView applied = (TextView) findViewById(R.id.appliedText);

        apply.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                apply.setVisibility(View.INVISIBLE);
                apply.setClickable(false);
                applied.setVisibility(View.VISIBLE);
            }
        });
    }
}
