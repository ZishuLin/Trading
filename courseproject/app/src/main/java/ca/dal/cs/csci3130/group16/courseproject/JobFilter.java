package ca.dal.cs.csci3130.group16.courseproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobFilter implements Parcelable {

    protected JobFilter(Parcel in) {
        searchTokens = in.createStringArrayList();
        if (in.readByte() == 0) {
            salaryMin = null;
        } else {
            salaryMin = in.readFloat();
        }
        if (in.readByte() == 0) {
            salaryMax = null;
        } else {
            salaryMax = in.readFloat();
        }
        if (in.readByte() == 0) {
            durationMin = null;
        } else {
            durationMin = in.readInt();
        }
        if (in.readByte() == 0) {
            durationMax = null;
        } else {
            durationMax = in.readInt();
        }
        submitterUID = in.readString();
    }

    public static final Creator<JobFilter> CREATOR = new Creator<JobFilter>() {
        @Override
        public JobFilter createFromParcel(Parcel in) {
            return new JobFilter(in);
        }

        @Override
        public JobFilter[] newArray(int size) {
            return new JobFilter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringList(searchTokens);
        if (salaryMin == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(salaryMin);
        }
        if (salaryMax == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(salaryMax);
        }
        if (durationMin == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(durationMin);
        }
        if (durationMax == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(durationMax);
        }
        parcel.writeString(submitterUID);
    }

    public static class Builder {
        private List<String> searchTokens;
        private Float salaryMin;
        private Float salaryMax;
        private Integer durationMin;
        private Integer durationMax;
        private String submitterUID;

        public Builder withSalaryRange(Float min, Float max) {
            this.salaryMax = max;
            this.salaryMin = min;
            return this;
        }
        public Builder withDurationRange(Integer min, Integer max) {
            this.durationMax = max;
            this.durationMin = min;
            return this;
        }
        public Builder withStringSearch(String str) {
            List<String> tmp = Arrays.asList(str.trim().toLowerCase().split(" "));
            this.searchTokens = new ArrayList<>();
            for (String s: tmp) {
                if (!s.trim().isEmpty()) this.searchTokens.add(s);
            }
            return this;
        }

        public Builder withEmployer(String str) {
            this.submitterUID = str;
            return this;
        }

        public JobFilter build() {
            return new JobFilter(searchTokens, salaryMin, salaryMax, durationMin, durationMax, submitterUID);
        }
    }

    public List<String> searchTokens;
    public Float salaryMin, salaryMax;
    public Integer durationMin, durationMax;
    public String submitterUID;

    public JobFilter() {
        //for firebase
    }

    public JobFilter(List<String> searchTokens, Float salaryMin, Float salaryMax, Integer durationMin, Integer durationMax, String submitterUID) {
        this.searchTokens = searchTokens;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.durationMin = durationMin;
        this.durationMax = durationMax;
        this.submitterUID = submitterUID;
    }

    public List<Job> filter(List<Job> jobs) {
        List<Job> filtered = new ArrayList<>();
        for (Job job: jobs) {
            if (check(job)) filtered.add(job);
        }
        return filtered;
    }

    private boolean check(Job job) {
        return (checkText(job) && checkSalary(job) && checkDuration(job) && checkSubmitter(job));
    }

    private boolean checkText(Job job) {
        if (this.searchTokens == null || this.searchTokens.isEmpty()) return true;
        else if (job.jobType == null || job.description == null) return false;

        List<String> resultTokens = new ArrayList<>();
        resultTokens.addAll(Arrays.asList(job.description.trim().toLowerCase().split(" ")));
        resultTokens.addAll(Arrays.asList(job.jobType.trim().toLowerCase().split(" ")));

        return resultTokens.containsAll(searchTokens);
    }
    private boolean checkSalary(Job job) {
        boolean minOk, maxOk;
        if (this.salaryMin == null) minOk = true;
        else if (job.salary == null) minOk = false;
        else minOk = this.salaryMin <= job.salary;

        if (this.salaryMax == null) maxOk = true;
        else if (job.salary == null) maxOk = false;
        else maxOk = this.salaryMax >= job.salary;
        return minOk && maxOk;
    }
    private boolean checkDuration(Job job) {
        boolean minOk, maxOk;
        if (this.durationMin == null) minOk = true;
        else if (job.duration == null) minOk = false;
        else minOk = this.durationMin <= job.duration;

        if (this.durationMax == null) maxOk = true;
        else if (job.duration == null) maxOk = false;
        else maxOk = this.durationMax >= job.duration;
        return minOk && maxOk;
    }
    private boolean checkSubmitter(Job job) {
        if (submitterUID == null) return true;
        if (job.uIDEmployer == null) return false;
        return submitterUID.equals(job.uIDEmployer);
    }

}
