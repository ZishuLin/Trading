package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubmitJobValidator {
    private Job.Builder builder;
    private Context context;

    public interface Status {
        boolean isOK();
        String getMessage();
    }

    public static class Error implements Status {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        @Override
        public boolean isOK() {
            return false;
        }
        @Override
        public String getMessage() {
            return message;
        }
    }

    public static class Success implements Status {
        @Override
        public boolean isOK() {
            return true;
        }
        @Override
        public String getMessage() {
            return null;
        }
    }

    public SubmitJobValidator(Context context, Job.Builder builder) {
        this.context = context;
        this.builder = builder;
    }
    public Status hasEmptyFields() {
        if (rawIsEmpty(builder)) return new Error(context.getResources().getString(R.string.JOB_EMPTY_FIELD_ERR));
        return new Success();


    }
    public Status hasDatePassed() {
        if (!hasEmptyFields().isOK()) return hasEmptyFields();
        if (rawIsBadDate(builder.getDate())) {
            return new Error(context.getResources().getString(R.string.JOB_DATE_PASSED_ERR));
        }
        return new Success();
    }
    public Status hasBadDuration() {
        if (!hasEmptyFields().isOK()) return hasEmptyFields();
        if (rawIsBadDuration(builder.getDuration())) {
            return new Error(context.getResources().getString(R.string.JOB_TOO_LONG_ERR));
        }
        return new Success();
    }
    public Status validate() {
        List<Status> statuses = new ArrayList<>();
        statuses.add(hasEmptyFields());
        statuses.add(hasDatePassed());
        statuses.add(hasBadDuration());

        for (Status status : statuses) {
            if (!status.isOK()) {
                return status;
            }
        }
        return new Success();
    }

    protected static boolean rawIsBadDate(Date date) {
        return date.before(Calendar.getInstance().getTime());
    }
    protected static boolean rawIsBadDuration(Integer duration) {
        return duration.compareTo(24) > 0;
    }
    protected static boolean rawIsEmpty(Job.Builder builder) {
        return (builder.getType() == null
                || builder.getType().isEmpty()
                || builder.getDescription() == null
                || builder.getDescription().isEmpty()
                || builder.getDate() == null
                || builder.getDuration() == null
                || builder.getLocation() == null
                || builder.getUrgency() == null
                || builder.getSalary() == null
                || builder.getEmployerID() == null
                || builder.getEmployerID().isEmpty()
        );
    }
}
