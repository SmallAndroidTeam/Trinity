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
    public final static String GESTURE_ACTION="com.ofilm.gesture.send.music";//手势控制音乐
    private  String KEY_TYPE="KEY_TYPE";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG, MultiMediaFragment.isExist()+"             ");
        Log.i("sendVoiceBroadCastReceiver",intent.getAction());
        if(intent!=null&& MultiMediaFragment.isExist()){
            String action=intent.getAction();
            if(action.contentEquals(PLAY_ACTION)||(action.contentEquals(GESTURE_ACTION)
                    &&intent.getStringExtra(KEY_TYPE).contentEquals("10011")
                    &&intent.getStringExtra("SOURCE_APP").contentEquals("ofilm")
                    &&intent.getIntExtra("EXTRA_status",0)==1)) {
                sendService(context,action);
                sendVoiceBroadCastReceiver(context,"为您播放音乐");
            }
            else if(action.contentEquals(PAUSE_ACTION)||(action.contentEquals(GESTURE_ACTION)
                    &&intent.getStringExtra(KEY_TYPE).contentEquals("10011")
                    &&intent.getStringExtra("SOURCE_APP").contentEquals("ofilm")
                    &&intent.getIntExtra("EXTRA_status",0)==0)){
                sendService(context,action);
                sendVoiceBroadCastReceiver(context,"暂停播放音乐");
            }
            else if(action.contentEquals(PREV_ACTION)||(action.contentEquals(GESTURE_ACTION)
                    &&intent.getStringExtra(KEY_TYPE).contentEquals("10009")
                    &&intent.getStringExtra("SOURCE_APP").contentEquals("ofilm")
                    &&intent.getIntExtra("EXTRA_status",0)==0)){
                sendService(context,PREV_ACTION);
                sendVoiceBroadCastReceiver(context,"上一首");
            }
            else if(action.contentEquals(NEXT_ACTION)||(action.contentEquals(GESTURE_ACTION)
                    &&intent.getStringExtra(KEY_TYPE).contentEquals("10010")
                    &&intent.getStringExtra("SOURCE_APP").contentEquals("ofilm")
                    &&intent.getIntExtra("EXTRA_status",0)==1)
                    ){
                sendService(context,NEXT_ACTION);
                sendVoiceBroadCastReceiver(context,"下一首");
            }
            else if(action.contentEquals(START_ACTION)){
                sendService(context,START_ACTION);
            }
            else if(action.contentEquals(STOP_ACTION)){
                sendService(context,STOP_ACTION);
            }
        } else {
            sendVoiceBroadCastReceiver(context,"您未打开音乐");
        }
    }
    //播放语音广播
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
        context.startService(intent);
    }
    
}
