package of.media.bq.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class musicReceiver extends BroadcastReceiver {
    public final static String TAG="bq111";
    public final  static String PLAY_ACTION="music.play";//
    public final  static String PREV_ACTION="music.prev";
    public final  static String NEXT_ACTION="music.next";
    public final  static String PAUSE_ACTION="music.pause";
    public final  static String START_ACTION="music.start";
    public final static String OPENMUSIC_ACTION="music.open";
    public final static String CLOSEMUSIC_ACTION="music.close";
    public final static String CONTINUE_ACTION="music.continue";
    public final static String RANDOM_ACTION="music.random";
    public final static String LOOPALL_ACTION="music.loop.all";
    public final static String LOOPSINGLE_ACTION="music.loop.single";
    public final static String LOOPRANDOM_ACTION="music.loop.random";
    public final static String LISTOPEN_ACTION="music.list.open";
    public final static String LISTCLOSE_ACTION="music.list.close";
    public final static String FAVOUR_ACTION="music.favour";
    public final static String UNFAVOUR_ACTION="music.unfavour";
    public final static String FAVOUROPEN_ACTION="music.favour.open";
    public final static String UNFAVOURCLOSE_ACTION="music.unfavour.close";
    public static String action;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent!=null){
           action=intent.getAction();
         if(action.equals(PLAY_ACTION)) {
             Log.i(TAG, "onReceive: " + action);
             }
             else if(action.equals(PAUSE_ACTION)){
             Log.i(TAG, "onReceive: " + action);
              }
              else if(action.equals(PREV_ACTION)){
             Log.i(TAG, "onReceive: " + action);
              }
              else if(action.equals(NEXT_ACTION)){
             Log.i(TAG, "onReceive: " + action);
                  }
             else {
             Log.i(TAG, "onReceive: " + action);
             }
        }
    }
}
