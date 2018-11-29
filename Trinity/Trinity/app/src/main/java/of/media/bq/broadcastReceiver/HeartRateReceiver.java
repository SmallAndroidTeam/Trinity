package of.media.bq.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import of.media.bq.activity.MainActivity;
import of.media.bq.service.HeartrateService;

/**
 * Create By rongxinglan IN 2018/11/28
 */
public class HeartRateReceiver extends BroadcastReceiver {

    public  final  static String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
   public  final  static String    START_HEART="ofilm.intent.action.HEARTRATE_EVENT";//可以开始检测心率的广播
    public  final  static String    OVER_HEART="ofilm.intent.action.HEARTRATE_SUSPEND";//结束检测心率的广播
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null||intent.getAction()==null){
            return;
        }
         if(START_HEART.equals(intent.getAction())){//心率检测开始，则开启心率检测服务
             if(!MainActivity.isStart()){//此时app未启动，则启动app
            Intent startIntent=new Intent();
            startIntent.setAction(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(context.getPackageName(),"of.media.bq.activity.MainActivity"));
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(startIntent);
             }
             Intent service= new Intent(context, HeartrateService.class);
             service.setAction(START_HEART);
             context.startService(service);
         }else if(OVER_HEART.equals(intent.getAction())){//心率检测结束，则关闭心率检测服务
             Intent overHeartService= new Intent(context, HeartrateService.class);
             overHeartService.setAction(OVER_HEART);
             context.startService(overHeartService);
             Intent service= new Intent(context, HeartrateService.class);
             context.stopService(service);
         }
    }
}
