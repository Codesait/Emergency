package com.example.nnenna.emergency.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nnenna.emergency.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText et_email, et_password, et_Phone, et_firstName, et_secondName;
    private Button  btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private KProgressHUD hud;
    ProgressBar bar;
    ProgressDialog progressDialog;
    String uId;
    private String firstName, lastName, email, phone, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //getting Firiebase Auth instance
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignUp.this);
        bar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);

        btnSignUp = findViewById(R.id.btSign_up);
        et_email = findViewById(R.id.etEmail);
        et_password =findViewById(R.id.etPassword);
        et_Phone = findViewById(R.id.etPhone);
        et_firstName = findViewById(R.id.etFirstName);
        et_secondName = findViewById(R.id.etSecondName);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


}

    // validating users
    private void validate(){

        firstName = et_firstName.getText().toString().trim();
        lastName = et_secondName.getText().toString().trim();
        email = et_email.getText().toString().trim();
        phone = et_Phone.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (isNetworkConnectionAvailable()){
            if (TextUtils.isEmpty(firstName)) {
                et_firstName.setError("First name cannot be empty");
                return;
            }if (TextUtils.isEmpty(lastName)) {
                et_secondName.setError("Field cannot be empty");
                return;
            } if (!isValidEmaillId(email)) {
                et_email.setError("Please enter a correct email");
                return;
            }if (TextUtils.isEmpty(email)) {
                et_email.setError("Field cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                et_password.setError("Field cannot be empty");
                return;
            }if (password.length() < 6) {
                et_password.setError("Password cannot be less than 6");
            }else {
//                hud.create(SignUp.this)
//                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                        .setLabel("Please wait")
//                        .setDetailsLabel("Registering..." +" " + firstName + " " + lastName)
//                        .setCancellable(true)
//                        .setAnimationSpeed(2)
//                        .setDimAmount(0.5f)
//                        .show();

                progressDialog.setMessage("Signing up user...");
                progressDialog.show();

                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_LONG).show();
                                } else {

                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    uId = user.getUid();

                                    startActivity(new Intent(SignUp.this, SignIn.class));
                                    finish();
                                }
                            }
                        });
            }


        }else {
            Toast.makeText(SignUp.this, "please check your network...", Toast.LENGTH_LONG).show();
        }


    }


    boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    boolean isValidEmaillId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
