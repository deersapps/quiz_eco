package com.jkdeers.activitygame;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Splash extends Activity {

    protected int _splashTime = 40000;

    private Thread splashTread;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        //Toast.makeText(this, "Checking Application License", Toast.LENGTH_SHORT).show();
        // Check the license
        /*checkLicense();*/
        /*TextView titleintrotv=(TextView) findViewById(R.id.introtv);
        TextView titleintroline=(TextView) findViewById(R.id.introline);
        TextView titlesign=(TextView) findViewById(R.id.sign);
        Typeface RS=Typeface.createFromAsset(this.getAssets(),"RS.ttf");
        Typeface sm=Typeface.createFromAsset(this.getAssets(),"sm.ttf");
        titleintrotv.setTypeface(RS);
        titleintroline.setTypeface(sm);
        titlesign.setTypeface(sm);*/


        final Splash sPlashScreen = this;

        // thread for displaying the SplashScreen
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){
                        wait(_splashTime);
                    }

                } catch(InterruptedException e) {}
                finally {
                    finish();

                    Intent i = new Intent();
                    i.setClass(sPlashScreen, MainActivity.class);
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

}
