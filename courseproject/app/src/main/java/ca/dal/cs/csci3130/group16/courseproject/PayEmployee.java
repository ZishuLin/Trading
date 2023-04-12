package ca.dal.cs.csci3130.group16.courseproject;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;


public class PayEmployee extends AppCompatActivity {
    public static String checkDecr;
    String user;
    String amount;

    FirebaseDatabase databaseInstance = getInstance("https://group-16-62934-default-rtdb.firebaseio.com");
    private static final String TAG = MainActivity.class.getName();

    //launching a previously-prepared call to start the process of executing an ActivityResultContract.
    private ActivityResultLauncher<Intent> activityResultLauncher;

    //for using Paypal related methods
    private PayPalConfiguration payPalConfig;

    //UI Elements
    Button cancelButton;
    Button payButton;
    TextView paypalReturn;

    Button rateButton;

    String jobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        payButton = (Button) findViewById(R.id.confirm_button);
        rateButton = (Button) findViewById(R.id.rateButton);
        configPayPal();
        initActivityLauncher();

        FirebaseAuth auth = FirebaseAuth.getInstance();


        Log.d("TEST", checkDecr);
        DatabaseReference ref = databaseInstance.getReference().child("Jobs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                    //String jobEmployerID = objSnapshot.child("uIDEmployer").getValue(String.class);
                    if(String.valueOf(objSnapshot.child("uIDEmployer").getValue()).equals(auth.getUid()) && String.valueOf(objSnapshot.child("description").getValue()).equals(checkDecr)){
                        user = (String) objSnapshot.child("uIDEmployee").getValue();
                        getAmount(objSnapshot.getKey());
                        jobID = objSnapshot.getKey();
                        getUsername(user);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(PayEmployee.this, EmployerDashboard.class);
                startActivity(goBack);
            }
        });
    }

    private void initActivityLauncher() {
        // Registering a request to start an activity for result, designated by the given contract
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    //when operation succeeded
                    if (result.getResultCode() == RESULT_OK) {
                        //getting the resulting datas and checking an additional confirmation required by paypal
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        System.out.println(confirmation);
                        //if confirmed
                        if (confirmation != null) {
                            try {

                                // Get the payment details
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                Log.i(TAG, paymentDetails);

                                // Extract json response and display it in a text view.
                                JSONObject payObj = new JSONObject(paymentDetails);

                                String payID = payObj.getJSONObject("response").getString("id");
                                String state = payObj.getJSONObject("response").getString("state");

                                paypalReturn = (TextView) findViewById((R.id.paypalReturn));
                                paypalReturn.setText(String.format("Payment %s%n with payment id is %s", state, payID));
                                paypalReturn.setVisibility(View.VISIBLE);
                                cancelButton.setText("Go Back");
                                payButton.setClickable(false);
                                payButton.setVisibility(View.INVISIBLE);
                                rateButton.setVisibility(View.VISIBLE);

                                rateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PopUpWindowRating popup = new PopUpWindowRating();
                                        popup.setPopup(view, jobID, getApplicationContext());
                                        popup.start(view);
                                    }
                                });
                                //DISPLAY RATING
                            } catch (JSONException e) {
                                Log.e("Error", "an extremely unlikely failure occurred: ", e);
                            }
                        }

                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {

                        //returned invalid result - sometimes when the user decides to not go through with the payment
                        Log.d(TAG, "Launcher Result Invalid");

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                        //when transaction is cancelled
                        Log.d(TAG, "Launcher Result Cancelled");
                    }
                });
    }

    public void getUsername(String id){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        DatabaseReference userRef = databaseInstance.getReference("Users/" + id + "/username");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView displayUsername = (TextView) findViewById(R.id.User);
                displayUsername.setText((String) snapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }
        });
    }

    public void getAmount(String jobID){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        DatabaseReference userRef = databaseInstance.getReference("Jobs/" + jobID + "/salary");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView displayAmount = (TextView) findViewById(R.id.Amount);
                Integer check = snapshot.getValue(Integer.class);
                Log.d("TEST2", String.valueOf(check));
                amount = String.valueOf(check);

                displayAmount.setText("$" + String.valueOf(check));
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }
        });
    }

    private void configPayPal() {
        //configuring paypal i.e defining we're using SANDBOX Environment and setting the paypal client id
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Config.PAYPAL_CLIENT_ID);
    }


    private void processPayment() {
        final String paypalAmount = amount;

        Log.d("TESTTTTTT", paypalAmount);

        //setting the parameters for payment i.e the amount, the currency, intent of the sale
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(
                paypalAmount), "CAD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);


        // Create Paypal Payment activity intent
        final Intent intent = new Intent(this, PaymentActivity.class);

        // Adding paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);

        // Adding paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        // Starting Activity Request launcher
        activityResultLauncher.launch(intent);
    }
}
