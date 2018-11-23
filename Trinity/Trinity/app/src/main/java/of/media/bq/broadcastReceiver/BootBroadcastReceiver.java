package of.media.bq.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import of.media.bq.service.BluetoothService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    
    final private static String TAG = "BT.BootBroadcastReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
        
        if (ACTION_BOOT.equals(intent.getAction())) {
            Log.d(TAG, "Recv BOOT_COMPLETED, launch BluetoothService");
            
            /* Check https://stackoverflow.com/questions/46445265/android-8-0-java-lang-illegalstateexception-not-allowed-to-start-service-inten */
            Intent service = new Intent(context, BluetoothService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(service);
            } else {
                context.startService(service);
            }
        }
    }
    
}
