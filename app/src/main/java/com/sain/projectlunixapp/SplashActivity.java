package com.sain.projectlunixapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar3);

        // Mengatur maksimum nilai ProgressBar
        progressBar.setMax(100);

        // Membuat Thread untuk mengupdate ProgressBar
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;

                    // Mengirim pesan ke handler untuk memperbarui ProgressBar di UI thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });

                    try {
                        // Menunda eksekusi thread selama 50 milidetik
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Menjalankan intent setelah ProgressBar mencapai 100%
                Intent intent = new Intent(SplashActivity.this,  MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        thread.start();
    }
}

