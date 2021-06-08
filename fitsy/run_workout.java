package com.example.fitsy;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

public class run_workout extends AppCompatActivity {
    ProgressBar progressBar;
    Button mButtonStartPause, mButtonReset;
    TextView mTextViewCountDown, phase;
    private ObjectAnimator animation;
    private static final long START_TIME_IN_MILLIS = 10000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    int phaser = 0;
    private int duration = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_workout);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        progressBar = findViewById(R.id.progress_bar);
        mButtonStartPause = findViewById(R.id.pause_id);
        mButtonReset = findViewById(R.id.end_run_id);
        phase = findViewById(R.id.phase_id);
        animation = ObjectAnimator.ofInt(progressBar, "progress", 100);


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.setDuration(duration);
                if (mTimerRunning) {
                    pauseTimer();
                    animation.pause();
                } else {
                    startTimer();
                    animation.start();
                }


            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                progressBar.setProgress(0);
                dashboard(v);
            }
        });

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                phaser++;
                progressBar.setProgress(0);
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.VISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                resetTimer();
                if(phaser%2==1) {
                    phase.setText("Walk");
                    mTimeLeftInMillis=5000;
                    duration = 5000;
                }
                else {
                    phase.setText("Run");
                    mTimeLeftInMillis = 10000;
                    duration = 10000;
                }

            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("Pause");
        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.VISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    public void dashboard(View view){
        Intent intent = new Intent(this,dashboard.class);
        startActivity(intent);
    }




}
