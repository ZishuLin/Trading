package ca.dal.cs.csci3130.group16.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Validation extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_validation_page);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateCredentials(username, password)) {
                // Credentials are valid, do show an successful message to the user.
                Toast.makeText(Validation.this, "Login Successful", Toast.LENGTH_SHORT).show();
            } else {
                // Credentials are not valid, show an error message to the user.
                Toast.makeText(Validation.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected static boolean validateCredentials(String username, String password) {
        // Replace this with your own validation logic
        return username.equals("Faisal195") && password.equals("12345678");
    }
}
