package org.dev4u.hv.my_diagnostic.FragmentsIntro;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import org.dev4u.hv.my_diagnostic.R;

/**
 * Created by Home on 1/12/2015.
 */


class MiAnimacion extends View implements Handler.Callback {
    View fondoApp;
    private Handler mHandler = new Handler(this);

    private int mDelay = 15000; // Delay in milliseconds.

    private Runnable mEvent = new Runnable() {
        public void run() {
            mHandler.removeCallbacks(mEvent);
            mHandler.postDelayed(mEvent, mDelay);
            Message message = mHandler.obtainMessage();
            mHandler.sendMessage(message);
        }
    };

    public MiAnimacion(Context context, View objeto) {
        super(context);
        fondoApp = objeto;
        fondoApp.setBackgroundResource(R.drawable.transicion);
    }

    public void start() {
        mHandler.post(mEvent);
    }

    public void stop() {
        //Toast.makeText(fondoApp.getContext(),"Fin ",Toast.LENGTH_SHORT).show();
        mHandler.removeCallbacks(mEvent);
    }

    @Override
    public boolean handleMessage(Message msg) {
        TransitionDrawable transition = (TransitionDrawable) fondoApp.getBackground();
        transition.reverseTransition(mDelay);

        return false;
    }
}
