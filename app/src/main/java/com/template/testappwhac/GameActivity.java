package com.template.testappwhac;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    int varRandMole;
    private TextView mTimeView;
    private TextView mScoreView;
    public int varScore = 0;
    final Handler handler = new Handler();
    public boolean varClose = false;

    private int maxTime = 30 * 1000;
    private long stepTime = 1 * 1000;

    public int timeInterval = 1000;
    public int moleUpTime = 350;

    public CountDownTimer mTimer = new myTimer(maxTime, stepTime);

    public MediaPlayer mPlayerWhack;
    public MediaPlayer mPlayerMiss;

    public String currentDiff;

    public ImageView molesClick[] = new ImageView[9];

    public int yValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTimeView = (TextView) findViewById(R.id.textTimeVal);
        mScoreView = (TextView) findViewById(R.id.textScoreVal);

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        currentDiff = sharedPref.getString("saved_difficulty", "Medium");

        // Start the game!
        mTimer.start();
        handler.post(moleLoop);

        varClose = false;

        mPlayerWhack = MediaPlayer.create(getApplicationContext(), R.raw.whack);
        mPlayerMiss = MediaPlayer.create(getApplicationContext(), R.raw.miss);

        molesClick[0] = (ImageView) findViewById(R.id.imageMole1);
        molesClick[1] = (ImageView) findViewById(R.id.imageMole2);
        molesClick[2] = (ImageView) findViewById(R.id.imageMole3);
        molesClick[3] = (ImageView) findViewById(R.id.imageMole4);
        molesClick[4] = (ImageView) findViewById(R.id.imageMole5);
        molesClick[5] = (ImageView) findViewById(R.id.imageMole6);
        molesClick[6] = (ImageView) findViewById(R.id.imageMole7);
        molesClick[7] = (ImageView) findViewById(R.id.imageMole8);
        molesClick[8] = (ImageView) findViewById(R.id.imageMole9);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int sHeight = metrics.heightPixels;
        yValue = (sHeight / 12) * -1;

    }


    @Override
    public void onPause() {
        super.onPause();

        varClose = true;
        mTimer.cancel();

        mPlayerWhack.stop();
        mPlayerMiss.stop();

    }

    @Override
    public void onStop() {
        super.onStop();

        varClose = true;
        mTimer.cancel();

        mPlayerWhack.stop();
        mPlayerMiss.stop();

    }

    @Override
    public void onResume() {
        super.onResume();

        varClose = false;

    }

    public class myTimer extends CountDownTimer {
        public myTimer(int maxTime, long stepTime) {
            super(maxTime, stepTime);

        }

        @Override
        public void onFinish() {

            this.cancel();
            String messageTime = getString(R.string.str_end_time);
            EndGame(varScore, messageTime);

            timeInterval = 1000;
            moleUpTime = 20;

        }

        public void onTick(long millisUntilFinished) {

            mTimeView.setText(String.valueOf(millisUntilFinished / 1000));

            if (((millisUntilFinished / 1000) % 5 == 0) && (millisUntilFinished / 1000) != 60) {
                increaseDifficulty();
            }

        }
    }

    public void increaseDifficulty() {

        String diff1 = getString(R.string.diff1);
        String diff3 = getString(R.string.diff3);

        if (currentDiff.equals(diff1)) {
            timeInterval *= 0.99;
            moleUpTime *= 0.99;
        } else if (currentDiff.equals(diff3)) {
            timeInterval *= 0.90;
            moleUpTime *= 0.90;
        } else {
            timeInterval *= 0.95;
            moleUpTime *= 0.95;
        }

    }

    public void EndGame(int EndScore, String Reason) {

        Intent intent = new Intent(getApplicationContext(), EndActivity.class);
        intent.putExtra("score", EndScore);
        intent.putExtra("reason", Reason);

        mTimer.cancel();
        startActivity(intent);
        this.finish();

    }

     public Runnable moleLoop = new Runnable() {

        int varPrevRandMole = 10;

        @Override
        public void run() {

            varRandMole = new Random().nextInt(8);

            if (varRandMole == varPrevRandMole) {
                do
                    varRandMole = new Random().nextInt(8);
                while (varRandMole == varPrevRandMole);
            }

            varPrevRandMole = varRandMole;

            molesClick[varRandMole].animate().translationY(yValue).setDuration(moleUpTime);

            new Timer().schedule(new TimerTask() {
                public void run() {

                    if (!varClose) {

                        for (int i = 0; i < 9; i++) {
                            if (molesClick[i].getTranslationY() == yValue) {

                                final int j = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        molesClick[j].animate().translationY(0).setDuration(5);
                                    }
                                });

                                if (mPlayerMiss.isPlaying() && mPlayerMiss != null) {
                                    mPlayerMiss.stop();
                                    mPlayerMiss.reset();
                                    mPlayerMiss.release();
                                }
                                mPlayerMiss.start();


                            }
                        }
                    }
                }
            }, timeInterval);

            if (!varClose) {
                handler.postDelayed(moleLoop, timeInterval);
            }
        }
    };

    public void updateScore(int Score) {
        mScoreView.setText(String.valueOf(Score));
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageMole1:
                if (molesClick[0].getTranslationY() < 0) {
                    molesClick[0].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole2:
                if (molesClick[1].getTranslationY() < 0) {
                    molesClick[1].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole3:
                if (molesClick[2].getTranslationY() < 0) {
                    molesClick[2].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole4:
                if (molesClick[3].getTranslationY() < 0) {
                    molesClick[3].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole5:
                if (molesClick[4].getTranslationY() < 0) {
                    molesClick[4].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole6:
                if (molesClick[5].getTranslationY() < 0) {
                    molesClick[5].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole7:
                if (molesClick[6].getTranslationY() < 0) {
                    molesClick[6].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole8:
                if (molesClick[7].getTranslationY() < 0) {
                    molesClick[7].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole9:
                if (molesClick[8].getTranslationY() < 0) {
                    molesClick[8].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
        }
    }

    public void directHit() {

        if (mPlayerWhack != null && mPlayerWhack.isPlaying()) {
            mPlayerWhack.stop();
            mPlayerWhack.reset();
            mPlayerWhack.release();
        }

        mPlayerWhack = MediaPlayer.create(getApplicationContext(), R.raw.whack);
        mPlayerWhack.start();

        varScore += 250;
        updateScore(varScore);
    }
}










