package com.example.nnenna.emergency.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nnenna.emergency.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {

    private TextInputEditText et_email,et_password;
    private Button btnSignIn, btnForgotPass,btnNewUser;
    ProgressBar bar;
    private FirebaseAuth auth;
    private String firstName,lastName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignIn.this);

        if (auth.getCurrentUser() != null) {

            startActivity(new Intent(SignIn.this, Dashboard.class));
            finish();
        }

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btnForgotPass = findViewById(R.id.btnForgotPass);
        btnNewUser = findViewById(R.id.btnRegPage);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = et_email.getText().toString().trim();
                final String password = et_password.getText().toString().trim();

                if (isNetworkConnectionAvailable()){
                     if (!isValidEmaillId(email)) {
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
                        progressDialog.setMessage("Signing user in...");
                        progressDialog.show();

                        auth.signInWithEmailAndPassword(email,password)
                                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();
                                        if (!task.isSuccessful()){
                                            Toast.makeText(SignIn.this, "An error occured", Toast.LENGTH_LONG).show();
                                        } else
                                        {
                                            Toast.makeText(SignIn.this, " sign in sucessful.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignIn.this, Dashboard.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                    }
                                });
                    }
                }
            }
        });




        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,SignUp.class));
                finish();
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,Forgot_password.class));
                finish();
            }
        });
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
