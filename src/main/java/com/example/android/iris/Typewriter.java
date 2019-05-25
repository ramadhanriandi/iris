package com.example.android.iris;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

public class Typewriter extends android.support.v7.widget.AppCompatTextView {
    private CharSequence text;
    private int index;
    private long delay = 500;

    public Typewriter(Context context){
        super(context);
    }

    public Typewriter(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    private Handler handler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, index++));
            if(index <= text.length()){
                handler.postDelayed(characterAdder, delay);
            }
        }
    };

    public void animateText(CharSequence text){
        this.text = text;
        index = 0;

        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder, delay);
    }

    public void setCharacterDelay(long millis){
        delay = millis;
    }
}
