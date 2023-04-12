package ca.dal.cs.csci3130.group16.courseproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CCTemp extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctemp);

        //attach event handler to button
        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(this);

        //attach event handler to CC number field
        EditText ccNumberField = findViewById(R.id.CCNumber);
        ccNumberField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                //if focus is lost, set the CC type text to say the type of card
                String num = ccNumberField.getText().toString();
                setCardType(num);
            }
        });
    }

    //helper method to set the card type text view text
    protected void setCardType(String num) {
        TextView ccType = findViewById(R.id.CCTypeText);
        if (isVisa(num)) {
            ccType.setText(R.string.CC_VISA);
        } else if (isMaster(num)) {
            ccType.setText(R.string.CC_MASTER);
        } else if (isAmex(num)) {
            ccType.setText(R.string.CC_AMEX);
        } else {
            ccType.setText(R.string.EMPTY_STRING);
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

    @Override
    public void onClick(View view) {
        // find the status text
        TextView validText = findViewById(R.id.validText);

        // get the text from all fields as strings
        EditText ccNumberField = findViewById(R.id.CCNumber);
        String ccNumber = ccNumberField.getText().toString().trim();

        EditText cvvCodeEdit = findViewById(R.id.CVVCode);
        String cvvCode = cvvCodeEdit.getText().toString().trim();

        EditText ccExpiryMonthEdit = findViewById(R.id.CCExpiryMonth);
        String ccExpiryMonth = ccExpiryMonthEdit.getText().toString().trim();

        EditText ccExpiryYearEdit = findViewById(R.id.CCExpiryYear);
        String ccExpiryYear = ccExpiryYearEdit.getText().toString().trim();

        // validate all the fields, if all fields are valid change status text to VALID if not change to corresponding invalid msg
        if (isValidCCNumber(ccNumber) && isValidCVV(cvvCode) && isValidMonth(ccExpiryMonth) && isValidYear(ccExpiryYear)) {
            validText.setText("VALID".trim());
        } else {
            if (!isValidCCNumber(ccNumber)) {
                validText.setText(R.string.CCNUM_INVALID_ERR);
            } else if (!isValidCVV(cvvCode)) {
                validText.setText(R.string.CCNUM_INVALID_ERR);
            } else if (!isValidMonth(ccExpiryMonth) || !isValidYear(ccExpiryYear)) {
                validText.setText(R.string.CCNUM_INVALID_ERR);
            } else {
                validText.setText("umm".trim());
            }
        }

    }


}