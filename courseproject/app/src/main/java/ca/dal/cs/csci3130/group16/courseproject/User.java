package ca.dal.cs.csci3130.group16.courseproject;

public class User {

    public static enum UserRole {EMPLOYER, EMPLOYEE}

    public String firstName;
    public String lastName;
    public String ccNum;
    public String phoneNum;
    public String username;
    public String password;
    public String email;
    public UserRole userRole;
    public JobFilter preferredSearch;

    public User() {

    }

    public User(String firstName, String lastName, String ccNum, String email, String phoneNum, String username, String password, UserRole userRole, JobFilter preferredSearch) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccNum = ccNum;
        this.email = email;
        this.phoneNum = phoneNum;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.preferredSearch = preferredSearch;
    }
}
