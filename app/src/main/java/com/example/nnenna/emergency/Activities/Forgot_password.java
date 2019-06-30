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
import android.widget.Toast;

import com.example.nnenna.emergency.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Forgot_password extends AppCompatActivity {

    private EditText etReset;
    private Button btnReset,btnBack;
    private FirebaseAuth auth;
    private ProgressDialog mDialog;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(Forgot_password.this);

        etReset = findViewById(R.id.etResetPassword);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forgot_password.this,SignIn.class));
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resetting();
            }
        });

    }

    public void Resetting(){

        email = etReset.getText().toString().trim();

        if (isNetworkConnectionAvailable()){
            if (!isValidEmaillId(email)) {
                etReset.setError("Please enter a correct email");
                return;
            }if (TextUtils.isEmpty(email)) {
                etReset.setError("Field cannot be empty");
                return;
            }

            mDialog.setMessage("Resetting password...");
            mDialog.show();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(Forgot_password.this,
                                        "Check your email for password reset,Thanks.",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Forgot_password.this, SignIn.class));
                                finish();
                            } else {
                                Toast.makeText(Forgot_password.this, "failed to send reset email.", Toast.LENGTH_LONG).show();
                        }
                    }
        });

        }else{
            Toast.makeText(this,
                    "Network Error!!!please check your network and try again.", Toast.LENGTH_SHORT).show();
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