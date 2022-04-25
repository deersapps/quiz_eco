package com.jkdeers.activitygame;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import org.jetbrains.annotations.NotNull;

import android.os.Handler;
import java.util.logging.LogRecord;

public class TypeWriter  extends androidx.appcompat.widget.AppCompatTextView {
    public CharSequence myText ;
    private int myIndex;
    private  long mydelay = 150;
    public TypeWriter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private  Handler myHandler = new Handler();

    private  Runnable characterAdder =  new Runnable() {
        @Override
        public void run() {
        setText(myText.subSequence(0,myIndex++));
        if(myIndex<=myText.length()){
            myHandler.postDelayed(characterAdder,mydelay);
        }
        }
    };
    public void animateText(CharSequence text) {
        myText = text;
        myIndex = 0;
        setText("");
        myHandler.removeCallbacks(characterAdder);
        myHandler.postDelayed(characterAdder,mydelay);
    }
    public void setCharacterDelay(long m) {
        mydelay = m;

    }
}
