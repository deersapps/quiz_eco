package com.jkdeers.activitygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;


public class StartActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start_login);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }

    protected int _splashTime = 7000;

    private Thread splashTread;

    MediaPlayer music;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_start_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final StartActivity sPlashScreen = this;

        TypeWriter tv = findViewById(R.id.appname);
        tv.setCharacterDelay(500);
        tv.setText("");
        tv.animateText("One Earth...");

        // thread for displaying the SplashScreen
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){
                        music = MediaPlayer.create(StartActivity.this, R.raw.introsound);
                        music.start();
                        music.setLooping(true); // Set looping
                        music.setVolume(100, 100);
                        wait(_splashTime);

                    }

                } catch(InterruptedException e) {}
                finally {
                    music.stop();
                    finish();

                    Intent i = new Intent();
                    i.setClass(sPlashScreen, registration.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                    //stop();
                }
            }
        };

        splashTread.start();
    }
    //for fading effect
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}*/
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }
}