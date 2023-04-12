package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.Arrays;
import java.util.Date;

public class Job {

    public static class Builder {
        private final String[] allowedTypes;

        private String type;
        private String description;
        private Date date;
        private Integer duration;
        private Place location;
        private Job.Urgency urgency;
        private Float salary;
        private final boolean isOpen;
        private final String uIDEmployer;
        private String uIDEmployee;

        public Builder(@NonNull Context context, @NonNull String uIDEmployer) {
            this.allowedTypes = context.getResources().getStringArray(R.array.JOB_TYPES);

            this.uIDEmployer = uIDEmployer;
            this.uIDEmployee = "";
            this.isOpen = true;
        }

        public void addType(String type) {
            if (!Arrays.asList(allowedTypes).contains(type)) throw new IllegalArgumentException();
            this.type = type;
        }


        public void addDescription(String description) {
            this.description = description;
        }

        public void addDate(Date date) {
            this.date = date;
        }

        public void addDuration(Integer duration) {
            this.duration = duration;
        }

        public void addLocation(Place location) {
            this.location = location;
        }

        public void addUrgency(Urgency urgency) {
            this.urgency = urgency;
        }

        public void addSalary(Float salary) {
            this.salary = salary;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }

        public Integer getDuration() {
            return duration;
        }

        public Place getLocation() {
            return location;
        }

        public Urgency getUrgency() {
            return urgency;
        }

        public Float getSalary() {
            return salary;
        }

        public String getEmployerID() { return uIDEmployer; }

        public Job build() {
            if (hasEmptyFields()) throw new NullPointerException();
            LatLng coordinates = this.location.getLatLng();
            Double latitude = coordinates.latitude;
            Double longitude = coordinates.longitude;
            return new Job(type, description, date, duration, latitude, longitude, urgency, salary, uIDEmployer, uIDEmployee, isOpen);
        }

        public boolean hasEmptyFields() {
            return (type == null
                    || type.isEmpty()
                    || description == null
                    || description.isEmpty()
                    || date == null
                    || duration == null
                    || location == null
                    || urgency == null
                    || salary == null);
        }
    }

    public enum Urgency {
        priority1{
            @Override
            String getString(Context context) {return context.getString(R.string.JOB_URGENCY_1);}
        },
        priority2{
            @Override
            String getString(Context context) {return context.getString(R.string.JOB_URGENCY_2);}
        },
        priority3{
            @Override
            String getString(Context context) {return context.getString(R.string.JOB_URGENCY_3);}
        };

        String getString(Context context) {return null;}
        static Urgency fromString(Context context, String input) {
            if (input == null) return null;
            for (Urgency u: Urgency.values()) {
                if (u.getString(context).equals(input)) return u;
            }
            return null;
        }
    }

    public String jobType;
    public String description;
    public Date date;
    public Integer duration;
    public Double latitude;
    public Double longitude;
    public Urgency urgency;
    public Float salary;
    public boolean isOpen;
    public String uIDEmployer;
    public String uIDEmployee;

    public Job() {
    }

    public Job(String jobType, String description, Date date, int duration, Double latitude, Double longitude,
                Urgency urgency, Float salary, String uIDEmployer, String uIDEmployee, boolean isOpen) {
        this.jobType = jobType;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urgency = urgency;
        this.salary = salary;
        this.uIDEmployer = uIDEmployer;
        this.uIDEmployee = uIDEmployee;
        this.isOpen = isOpen;
    }
}
