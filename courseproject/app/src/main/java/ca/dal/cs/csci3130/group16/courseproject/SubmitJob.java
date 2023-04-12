// Some code segments adapted from Google Android Documentation, at the following locations:
// https://developers.google.com/maps/documentation/places/android-sdk/autocomplete
// https://developer.android.com/develop/ui/views/components/dialogs
// https://developer.android.com/develop/ui/views/components/pickers

package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SubmitJob
        extends AppCompatActivity
        implements PopupMenu.OnMenuItemClickListener,
        ChooseStringFragment.ChooseStringListener,
        ChooseDateFragment.ChooseDateListener{

    private static final int LOCATION_REQ_CODE = 1;

    private Job.Builder builder;
    private SubmitJobValidator validator;

    private Button buttonJobType, buttonEditDate, buttonLocation, urgency;
    private EditText editDescription, editDuration, editSalary;
    private TextView statusLabel;

    private DialogFragment chooseType;
    private DialogFragment chooseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Group 16 - Quick Cash - New job");
        setContentView(R.layout.activity_submit_job);

        buttonJobType = findViewById(R.id.editJobType);
        editDescription = findViewById(R.id.editDescription);
        buttonEditDate = findViewById(R.id.editDate);
        editDuration = findViewById(R.id.editDuration);
        buttonLocation = findViewById(R.id.editPlace);
        urgency = findViewById(R.id.editUrgency);
        editSalary = findViewById(R.id.editSalary);
        statusLabel = findViewById(R.id.jobStatusLabel);

        // Initialize Helper Objects
        Places.initialize(getApplicationContext(), "AIzaSyAtPfHiI8DiIJCPnpUuz9I1HQzP2V-dq10");
        builder = new Job.Builder(this, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        validator = new SubmitJobValidator(this, builder);
        chooseDate = new ChooseDateFragment();
        initTypeChooser();
        addEditTextWatchers();
    }

    public void typeButtonListener(View view) {
        chooseType.show(getSupportFragmentManager(), "chooseType");
    }

    public void dateButtonListener(View view){
        chooseDate.show(getSupportFragmentManager(), "chooseDate");
    }

    public void initTypeChooser() {
        chooseType = new ChooseStringFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", getResources().getString(R.string.SELECT_JOB_TYPE_PROMPT));
        bundle.putCharSequenceArray("options", getResources().getStringArray(R.array.JOB_TYPES));
        chooseType.setArguments(bundle);
    }

    // KEYBOARD INPUT FIELDS
    private void addEditTextWatchers() {
        editDescription.addTextChangedListener(ContentWatcherFactory.make(String.class, content -> {
            builder.addDescription(content);
            updateStatus();
        }));
        editSalary.addTextChangedListener(ContentWatcherFactory.make(Float.class, content -> {
            builder.addSalary(content);
            updateStatus();
        }));
        editDuration.addTextChangedListener(ContentWatcherFactory.make(Integer.class, content -> {
            builder.addDuration(content);
            updateStatus();
        }));
    }

    // TYPE INPUT
    @Override
    public void onDialogClick(DialogFragment dialog, CharSequence item) {
        String type = item.toString();
        builder.addType(type);
        buttonJobType.setText(type);
        updateStatus();
    }

    // DATE INPUT
    @Override
    public void onDateChosen(DialogFragment dialog, Calendar calendar) {
        Date date = calendar.getTime();
        builder.addDate(date);
        buttonEditDate.setText(DateFormat.getDateInstance().format(date));
        updateStatus();
    }

    // URGENCY INPUT
    public void showUrgencyPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.urgency_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Job.Urgency u = Job.Urgency.fromString(this, item.getTitle().toString());
        builder.addUrgency(u);
        updateStatus();

        if (u == null) return false;
        urgency.setText(item.getTitle());
        return true;
    }

    // LOCATION INPUT
    public void locationButtonListener(View view) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.ID, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, LOCATION_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != LOCATION_REQ_CODE) super.onActivityResult(requestCode, resultCode, data);
        else if (resultCode == RESULT_OK && data != null) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            builder.addLocation(place);
            buttonLocation.setText(place.getAddress());
        }
        updateStatus();
    }

    // VALIDATION
    public void updateStatus() {
        SubmitJobValidator.Status status = validator.validate();
        if (status.isOK()) {
            statusLabel.setText(R.string.JOB_STATUS_OK);
        } else {
            statusLabel.setText(status.getMessage());
        }
    }

    // FORM SUBMISSION
    public void submitButtonListener(View view) {
        updateStatus();
        SubmitJobValidator.Status status = validator.validate();
        if (status.isOK()) {
            submitToDB(builder.build());
            Toast.makeText(SubmitJob.this, "Job submitted successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SubmitJob.this, EmployerDashboard.class);
            intent.putExtra("fromSubmitJob", true);
            startActivity(intent);

        }
    }

    private void submitToDB(Job job) {
        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference().child("Jobs");
        jobsRef.push().setValue(job);
    }

}