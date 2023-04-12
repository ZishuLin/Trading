package ca.dal.cs.csci3130.group16.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class NewUser extends AppCompatActivity {

    String firstName;
    String lastName;
    String ccNum;
    String email;
    String phoneNum;
    String username;
    String password;
    String confirmPassword;
    String cvvCode;
    String creditExpiryMonth;
    String creditExpiryYear;

    EditText editFirstName;
    EditText editLastName;
    EditText editCreditCardNumber;
    EditText editEmail;
    EditText editPhoneNumber;
    EditText editUsername;
    EditText editPassword;
    EditText editConfirmPassword;
    EditText editCVVCode;
    EditText editCreditExpiryMonth;
    EditText editCreditExpiryYear;

    TextView errorLabel;

    StringChecker checkerFName;
    StringChecker checkerLName;
    StringChecker checkerCCNum;
    StringChecker checkerEmail;
    StringChecker checkerPhone;
    StringChecker checkerUsername;
    StringChecker checkerPassword;
    StringChecker checkerConfirmPassword;
    StringChecker checkerCVV;
    StringChecker checkerCCExpiryYear;
    StringChecker checkerCCExpiryMonth;

    Button buttonRegister;

    User.UserRole userRole;
    FirebaseDatabase databaseInstance;
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        boolean isEmployee = extras.getBoolean("IsEmployee");
        if (isEmployee) userRole = User.UserRole.EMPLOYEE;
        else userRole = User.UserRole.EMPLOYER;

        if (isEmployee) {
            setTitle("Group16 - Quick Cash - Employee");
        } else {
            setTitle("Group16 - Quick Cash - Employer");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        databaseInstance = FirebaseDatabase.getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
        mAuth = FirebaseAuth.getInstance();

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editCreditCardNumber = findViewById(R.id.CCNumber);
        editEmail = findViewById(R.id.editEmail);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        editCVVCode = findViewById(R.id.CVVCode);
        editCreditExpiryYear = findViewById(R.id.CCExpiryYear);
        editCreditExpiryMonth = findViewById(R.id.CCExpiryMonth);

        errorLabel = findViewById(R.id.errorLabel);

        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(view -> {
            if (view.getId() != buttonRegister.getId()) return;
            updateStrings();
            updateCheckers();
            updateLabel();
            if (!fieldsAreValid()) return;

            mAuth.createUserWithEmailAndPassword(NewUser.this.email, NewUser.this.password)
                    .addOnSuccessListener(authResult -> {
                        User user = new User(firstName, lastName, ccNum, email, phoneNum, username, password, userRole, null);
                        DatabaseReference currentUserRef = usersRef.child(mAuth.getCurrentUser().getUid());
                        currentUserRef.setValue(user)
                                .addOnSuccessListener(setUserValue -> {
                                Toast.makeText(NewUser.this, R.string.REGISTERED_MESSAGE, Toast.LENGTH_LONG).show();
                                if (user.userRole == User.UserRole.EMPLOYEE) {
                                    Intent employeeDashboard = new Intent(NewUser.this, EmployeeDashboard.class);
                                    startActivity(employeeDashboard);
                                } else if (user.userRole == User.UserRole.EMPLOYER) {
                                    Intent employerDashboard = new Intent(NewUser.this, EmployerDashboard.class);
                                    startActivity(employerDashboard);
                                }})
                                .addOnFailureListener(e -> Toast.makeText(NewUser.this, R.string.REGISTRATION_FAILED_ERR, Toast.LENGTH_LONG).show());
                    })
                    .addOnFailureListener(e -> Toast.makeText(NewUser.this, R.string.ACCOUNT_EXISTS_ERR, Toast.LENGTH_LONG).show());
        });

        setInputRules();

        //attach event handler to CC number field
        editCreditCardNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                //if focus is lost, set the CC type text to say the type of card
                String num = editCreditCardNumber.getText().toString();
                setCardType(num);
            }
        });

    }

    protected void setInputRules() {

        //the Predicate<String> must take a string, and output a boolean.
        //it represents a requirement.
        //add the requirement and the matching error message to the checker.
        checkerFName = buildFNameChecker();
        checkerLName = buildLNameChecker();
        checkerCCNum = buildCCNumChecker();
        checkerEmail = buildEmailChecker();
        checkerPhone = buildPhoneChecker();
        checkerUsername = buildUsernameChecker();
        checkerPassword = buildPasswordChecker();
        checkerConfirmPassword = buildConfirmPasswordChecker();
        checkerCVV = buildCVVChecker();
        checkerConfirmPassword = buildConfirmPasswordChecker();
        checkerCCExpiryMonth = buildCCExpiryMonthChecker();
        checkerCCExpiryYear = buildCCExpiryYearChecker();

    }

    protected StringChecker buildFNameChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .build();
    }

    protected StringChecker buildLNameChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .build();
    }

    protected StringChecker buildCCNumChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .withRule(NewUser::isValidCCNumber, getResources().getString(R.string.CCNUM_INVALID_ERR))
                .build();
    }

    protected StringChecker buildEmailChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .withRule(NewUser::hasEmailFormat, getResources().getString(R.string.EMAIL_ILLEGAL_FORMAT_ERR))
                .build();
    }

    protected StringChecker buildPhoneChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .withRule(NewUser::isValidPhoneLength, getResources().getString(R.string.PHONE_ILLEGAL_FORMAT_ERR))
                .withRule(NewUser::hasCanadianAreaCode, getResources().getString(R.string.PHONE_NOT_CANADIAN_ERR))
                .build();
    }

    protected StringChecker buildUsernameChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .withRule(NewUser::isUnameValidLength, getResources().getString(R.string.USERNAME_ILLEGAL_LENGTH_ERR))
                .withRule(NewUser::isUnameValidChars, getResources().getString(R.string.USERNAME_ILLEGAL_CHAR_ERR))
                .build();
    }

    protected StringChecker buildPasswordChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .withRule(NewUser::isValidPsswdLength, getResources().getString(R.string.PSSWD_ILLEGAL_FORMAT_ERR))
                .withRule(NewUser::hasSpecialChar, getString(R.string.PSSWD_NO_SPECIAL_CHAR_ERR))
                .build();
    }

    protected StringChecker buildConfirmPasswordChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withNotEmptyRule(getResources().getString(R.string.FIELD_EMPTY_ERR))
                .build();
    }

    protected StringChecker buildCVVChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withRule(NewUser::isValidCVV, getResources().getString(R.string.CCNUM_INVALID_ERR))
                .build();
    }

    protected StringChecker buildCCExpiryYearChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withRule(NewUser::isValidYear, getResources().getString(R.string.CCNUM_INVALID_ERR))
                .build();
    }

    protected StringChecker buildCCExpiryMonthChecker() {
        return new StringCheckerBuilder(getResources().getString(R.string.EMPTY_STRING))
                .withRule(NewUser::isValidMonth, getResources().getString(R.string.CCNUM_INVALID_ERR))
                .build();
    }

    protected boolean passwordsMatch() {
        return confirmPassword.equals(password);
    }

    /**
     * Methods for validating that the input fields have enough characters and are in-bound
     */
    // Check if the string has 16 characters (it is a valid CC number)
    protected static boolean isValidCCNumber(String num) {
        return num.length() == 16;
    }

    // Check if the string has 3 characters (it is a valid CVV code)
    protected static boolean isValidCVV(String num) {
        return num.length() == 3;
    }

    // Check if the string has 2 characters and is between 1 and 12 (inclusive) which means it is a valid month
    protected static boolean isValidMonth(String num) {
        return num.length() == 2 && Integer.parseInt(num) >= 1 && Integer.parseInt(num) <= 12;
    }

    // Check if the string has 2 characters and is between 23 and 30 (inclusive) which means it is a valid year
    protected static boolean isValidYear(String num) {
        return num.length() == 2 && Integer.parseInt(num) >= 23 && Integer.parseInt(num) <= 30;
    }

    protected static boolean hasEmailFormat(String input) {
        return Pattern.matches("^\\S+@\\S+\\.\\S+$", input);
    }

    protected static boolean hasCanadianAreaCode(String input) {
        int areaCode = Integer.parseInt(input.substring(0, 3));
        int[] canadianCodes = {
                204, 226, 236, 249, 250, 289, 306, 343, 365, 367, 368, 403, 416, 418, 431,
                437, 438, 450, 474, 506, 514, 519, 548, 579, 581, 587, 604, 613, 639, 647,
                672, 683, 705, 709, 742, 753, 778, 780, 782, 807, 819, 825, 867, 873, 902,
                905, 263, 354, 382, 428, 468, 584, 879
        };
        for (int i : canadianCodes) {
            if (areaCode == i) return true;
        }
        return false;
    }

    protected static boolean isValidPhoneLength(String s) {
        return s.length() == 10;
    }

    protected static boolean isUnameValidLength(String s) {
        return s.length() > 5 && s.length() < 31;
    }

    protected static boolean isUnameValidChars(String s) {
        return Pattern.matches("^[a-zA-Z0-9-]*$", s);
    }

    protected static boolean isValidPsswdLength(String s) {
        return s.length() >= 6;
    }

    protected static boolean hasSpecialChar(String s) {
        return Pattern.matches("^.*[~`!@#$%^&*()\\-_=+{}|\\[\\]\\\\:;\"'<>,.?/]+.*$", s);
    }

    //helper method to set the card type text view text
    protected void setCardType(String num) {
        TextView CCType = findViewById(R.id.CCTypeText);
        if (isVisa(num)) {
            CCType.setText(R.string.CC_VISA);
        } else if (isMaster(num)) {
            CCType.setText(R.string.CC_MASTER);
        } else if (isAmex(num)) {
            CCType.setText(R.string.CC_AMEX);
        } else {
            CCType.setText(R.string.EMPTY_STRING);
        }
    }

    /**
     * Methods for checking the CC number type
     */
    // Tests whether a string starts with 4 (it is a visa)
    protected static boolean isVisa(String num) {
        if (num.length() > 0) {
            return num.charAt(0) == '4';
        }
        return false;
    }

    // Tests whether a string starts with 5 (it is a MasterCard)
    protected static boolean isMaster(String num) {
        if (num.length() > 0) {
            return num.charAt(0) == '5';
        }
        return false;
    }

    // Tests whether a string starts with 3 (it is an Amex)
    protected static boolean isAmex(String num) {
        if (num.length() > 0) {
            return num.charAt(0) == '3';
        }
        return false;
    }

    protected String getStringFromEditText(EditText e) {
        return e.getText().toString().trim();
    }

    protected void updateStrings() {
        firstName = getStringFromEditText(editFirstName);
        lastName = getStringFromEditText(editLastName);
        ccNum = getStringFromEditText(editCreditCardNumber);
        email = getStringFromEditText(editEmail);
        phoneNum = getStringFromEditText(editPhoneNumber);
        username = getStringFromEditText(editUsername);
        password = getStringFromEditText(editPassword);
        confirmPassword = getStringFromEditText(editConfirmPassword);
        cvvCode = getStringFromEditText(editCVVCode);
        creditExpiryMonth = getStringFromEditText(editCreditExpiryMonth);
        creditExpiryYear = getStringFromEditText(editCreditExpiryYear);
    }

    //run StringCheckers on strings.
    //each checker holds the requirements for its respective field.
    //after this is run, the evaluation of the input and the correct message will be stored
    //in the Checker until it is run again.
    public void updateCheckers() {
        checkerFName.check(firstName);
        checkerLName.check(lastName);
        checkerCCNum.check(ccNum);
        checkerEmail.check(email);
        checkerPhone.check(phoneNum);
        checkerUsername.check(username);
        checkerPassword.check(password);
        checkerConfirmPassword.check(confirmPassword);
        checkerCVV.check(cvvCode);
        checkerCCExpiryYear.check(creditExpiryYear);
        checkerCCExpiryMonth.check(creditExpiryMonth);
    }

    //set status labels based on how the Checkers evaluated the input.
    public void updateLabel() {
        if (isAtleastOneFieldEmpty()) {

            errorLabel.setText(R.string.FIELD_EMPTY_ERR);
            return;
        }
        for (StringChecker checker :
                new StringChecker[]{checkerFName, checkerLName, checkerCCNum, checkerEmail,
                        checkerPhone, checkerUsername, checkerPassword, checkerConfirmPassword,
                        checkerCVV, checkerCCExpiryYear, checkerCCExpiryMonth}) {
            if (!checker.checkedWasValid()) {
                errorLabel.setText(checker.checkedStatusMessage());
                return;
            }
        }
        if (!passwordsMatch()) {
            errorLabel.setText(R.string.CPSSWD_DIFFERENT_ERR);
            return;
        }
        errorLabel.setText(R.string.EMPTY_STRING);
        return;
    }

    public boolean isAtleastOneFieldEmpty() {
        return (firstName.isEmpty()
                || lastName.isEmpty()
                || ccNum.isEmpty()
                || email.isEmpty()
                || phoneNum.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty())
                || cvvCode.isEmpty()
                || creditExpiryYear.isEmpty()
                || creditExpiryMonth.isEmpty();
    }

    //returns true if the strings checked all met the requirements.
    public boolean fieldsAreValid() {
        return checkerFName.checkedWasValid()
                && checkerLName.checkedWasValid()
                && checkerCCNum.checkedWasValid()
                && checkerEmail.checkedWasValid()
                && checkerPhone.checkedWasValid()
                && checkerUsername.checkedWasValid()
                && checkerPassword.checkedWasValid()
                && checkerConfirmPassword.checkedWasValid()
                && passwordsMatch()
                && checkerCVV.checkedWasValid()
                && checkerCCExpiryYear.checkedWasValid()
                && checkerCCExpiryMonth.checkedWasValid();
    }
}