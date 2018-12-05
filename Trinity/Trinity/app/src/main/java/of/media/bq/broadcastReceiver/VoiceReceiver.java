package of.media.bq.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import of.media.bq.fragment.MultiMediaFragment;
import of.media.bq.service.MusicService;


public class VoiceReceiver extends BroadcastReceiver {
    public final static String TAG="bq111";
    public final  static String STOP_ACTION="music.stop";//停止播放
    public final  static String PLAY_ACTION="music.play";//播放
    public final  static String PREV_ACTION="music.prev";//上一首
    public final  static String NEXT_ACTION="music.next";//下一首
    public final  static String PAUSE_ACTION="music.pause";//暂停
    public final  static String START_ACTION="music.continue";//继续播放
    public final static String OPENMUSIC_ACTION="music.open";//打开音乐
    public final static String CLOSEMUSIC_ACTION="music.close";//关闭音乐
    public final static String RANDOM_ACTION="music.random";//随机
    public final static String LOOPALL_ACTION="music.loop.all";//顺序播放
    public final static String LOOPSINGLE_ACTION="music.loop.single";//单曲循环
    public final static String LOOPRANDOM_ACTION="music.loop.random";//随机播放
    public final static String LISTOPEN_ACTION="music.list.open";//打开音乐列表
    public final static String LISTCLOSE_ACTION="music.list.close";//关闭音乐列表
    public final static String FAVOUR_ACTION="music.favour";//收藏
    public final static String UNFAVOUR_ACTION="music.unfavour";//取消收藏
    public final static String FAVOUROPEN_ACTION="music.favour.open";//打开收藏列表
    public final static String UNFAVOURCLOSE_ACTION="music.unfavour.close";//关闭收藏列表
    public final static String GESTURE_ACTION="com.ofilm.gesture.send";//手势控制音乐
    public final static String VOICE_ACTION="com.txznet.adapter.send";
    private  String KEY_TYPE="key_type";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("sendVoiceBroadCastReceiver","收到广播");
        String action=intent.getAction();
        if(intent!=null&& action!=null&&MultiMediaFragment.isExist()){
            Log.i("sendVoiceBroadCastReceiver", MultiMediaFragment.isExist()+"             ");
            if(action.contentEquals(VOICE_ACTION)&&intent.getStringExtra("action")!=null&&intent.getIntExtra(KEY_TYPE,0)==1060){
                String value=intent.getStringExtra("action");
                if(value.contentEquals(PLAY_ACTION)){
                    Log.i("sendVoiceBroadCastReceiver",PLAY_ACTION);
                    sendVoiceBroadCastReceiver(context,"为您播放音乐");
                    sendService(context,PLAY_ACTION);
                }else if(value.contentEquals(PAUSE_ACTION)){
                    Log.i("sendVoiceBroadCastReceiver",PAUSE_ACTION);
                    sendVoiceBroadCastReceiver(context,"暂停播放音乐");
                    sendService(context,PAUSE_ACTION);
                }else if(value.contentEquals(PREV_ACTION)){
                    Log.i("sendVoiceBroadCastReceiver",PREV_ACTION);
                  
                    sendVoiceBroadCastReceiver(context,"上一首");
                    sendService(context,PREV_ACTION);
                }else if(value.contentEquals(NEXT_ACTION)){
                    Log.i("sendVoiceBroadCastReceiver",NEXT_ACTION);
                    sendVoiceBroadCastReceiver(context,"下一首");
                    sendService(context,NEXT_ACTION);
                }
            }else if(action.contentEquals(GESTURE_ACTION)&&intent.getStringExtra("SOURCE_APP")!=null&&intent.getStringExtra("SOURCE_APP").contentEquals("ofilm")){
                int keyType=intent.getIntExtra(KEY_TYPE,0);
                int extraStatus=intent.getIntExtra("EXTRA_status",0);
                if(keyType==10011&&extraStatus==1){
                    Log.i("sendVoiceBroadCastReceiver",PLAY_ACTION);
                    
                    sendVoiceBroadCastReceiver(context,"为您播放音乐");
                    sendService(context,PLAY_ACTION);
                }else   if(keyType==10011&&extraStatus==0){
                    Log.i("sendVoiceBroadCastReceiver",PAUSE_ACTION);
                    sendVoiceBroadCastReceiver(context,"暂停播放音乐");
                    sendService(context,PAUSE_ACTION);
                }else   if(keyType==10009&&extraStatus==0){
                    Log.i("sendVoiceBroadCastReceiver",PREV_ACTION);
                    
                    sendVoiceBroadCastReceiver(context,"上一首");
                    sendService(context,PREV_ACTION);
                }else   if(keyType==10010&&extraStatus==1){
                    Log.i("sendVoiceBroadCastReceiver",NEXT_ACTION);
                    sendVoiceBroadCastReceiver(context,"下一首");
                    sendService(context,NEXT_ACTION);
                }
            }
        } else {
            sendVoiceBroadCastReceiver(context,"您未打开音乐");
        }
    }
    //发送播放语音广播
    public   void sendVoiceBroadCastReceiver(Context context,String message){
        Intent intent=new Intent("com.txznet.adapter.recv");
        intent.putExtra("action", "txz.tts.speak");
        intent.putExtra("key_type",2400);
        intent.putExtra("tts",message);
        context.sendBroadcast(intent);
        Log.i("sendVoiceBroadCastReceiver",message);
    }
    public  void sendService(Context context,String action){
        Intent intent=new Intent(context, MusicService.class);
        intent.putExtra("flag",MusicService.FLAG);
        intent.setAction(action);
        Log.i("sendVoiceBroadCastReceiver",action);
        context.startService(intent);
    }
    
}
