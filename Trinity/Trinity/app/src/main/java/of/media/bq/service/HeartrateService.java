package of.media.bq.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import of.media.bq.broadcastReceiver.HeartRateReceiver;

/**
 * Create By rongxinglan IN 2018/11/28
 */
public class HeartrateService extends Service {
    public  final  static String    GET_HERET_STATUS="ofilm.intent.action.HEARTRATE_START";//开始获取检测心率的数据
    private boolean canCheckHeart=false;//判断此时能否进行心率检测
    private static TransmissionData mTransmissionData;

    public HeartrateService() {
        super();
    }

    public static TransmissionData getmTransmissionData() {
        return mTransmissionData;
    }

    public static void setmTransmissionData(TransmissionData mTransmissionData) {
        HeartrateService.mTransmissionData = mTransmissionData;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public void onDestroy() {//关闭服务执行的方法，保存数据后才关闭

        Log.i("bq11", "onStartCommand: 收到心率关闭广播");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null||intent.getAction()==null){
            return super.onStartCommand(intent, flags, startId);
        }
        if(intent.getAction().equals(HeartRateReceiver.START_HEART)){
            canCheckHeart=true;
            Log.i("bq11", "onStartCommand: 收到心率检测广播");
        }else  if(intent.getAction().equals(HeartRateReceiver.OVER_HEART)){
            canCheckHeart=false;
        }  else if(intent.getAction().equals(GET_HERET_STATUS)){

            if(canCheckHeart){//判断此时能否进行心率检测
               if(mTransmissionData!=null) {
                   Log.i("bq11", "onStartCommand: 开始获取心率检测数据");
                   //调用aidl获取数据
                   mTransmissionData.getData(0);
               }
            }else{//
                if(mTransmissionData!=null) {

                }
            }
        }
        else{
            return super.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface  TransmissionData{
        void  getData(int data);//获取数据
    }

}
