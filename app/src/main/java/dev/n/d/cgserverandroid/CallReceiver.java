package dev.n.d.cgserverandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

public class CallReceiver extends BroadcastReceiver {
    static CallStartEndDetector listener;
    static final int CALL_NUMBER_REQUEST = 1;
    Context savedContext;
    Class c;
    Method m;
    Object telephonyService;



    @Override
    public void onReceive(Context context, Intent intent) {

        savedContext = context;//not used currently
        if(listener == null){
            listener = new CallStartEndDetector();
        }
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        //Modifying the original telephony to end call here
        c = null;
        try {
            c = Class.forName(telephony.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        m = null;
        try {
            m = c != null ? c.getDeclaredMethod("getITelephony") : null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (m != null) {
            m.setAccessible(true);
        }
        telephonyService = null;
        try {
            telephonyService = m.invoke(telephony);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            c = Class.forName(telephonyService.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            m = c.getDeclaredMethod("endCall");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        m.setAccessible(true);
        //m.invoke(telephonyService);
    }



    public class CallStartEndDetector extends PhoneStateListener {
        int lastState = TelephonyManager.CALL_STATE_IDLE;
        boolean isIncoming;

        public CallStartEndDetector() {}
        //Incoming call-   IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when hung up
        //Outgoing call-  from IDLE to OFFHOOK when dialed out, to IDLE when hunged up

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Log.d("this",incomingNumber);

            if(lastState == state){
                //No change
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    isIncoming = true;

                    Log.d("state:","incoming call started");
                    try {
                        m.invoke(telephonyService);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //Transition of ringing->offhook are pickups of incoming calls.  Nothing down on them
                    if(lastState != TelephonyManager.CALL_STATE_RINGING){
                        isIncoming = false;
                        Log.d("state:","outgoing call started");
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //End of call(Idle).  The type depends on the previous state(s)
                    if(lastState == TelephonyManager.CALL_STATE_RINGING){
                        Log.d("this",incomingNumber);
                        Log.d("state:","Missed Call Hey");
                        Intent intent  = new Intent("CUSTOM_ON_MISS_CALL_CALLBACK_RECEIVER");
                        intent.putExtra("incomingNumber",incomingNumber);
                        savedContext.sendBroadcast(intent);

                    }
                    else if(isIncoming){
                        Log.d("state:","Incoming call ended");
                    }
                    else{
                        Log.d("state:","Outgoing call ended");
                    }
                    break;
            }
            lastState = state;
        }

    }
}
