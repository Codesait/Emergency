package com.example.nnenna.emergency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(MainActivity.this, SignIn.class));
                }
            }
        };timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
