package of.media.bq.activity;

import of.media.bq.R;
import of.media.bq.broadcastReceiver.VoiceReceiver;
import of.media.bq.fragment.BluetoothFragment;
import of.media.bq.fragment.CarWeiChatFragment;
import of.media.bq.fragment.InteriorViewFragment;
import of.media.bq.fragment.MultiMediaFragment;
import of.media.bq.fragment.OutsideViewFragment;
import of.media.bq.heartRate.fragment.heartFragment;
import of.media.bq.localInformation.App;

import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private TextView multiMediaButton;
    private TextView bluetoothTv;
    private TextView heartrateTv;
    private TextView interiorviewTv;
    private TextView outsideviewTv;
    private TextView carweichatTv;
    private TextView returnTv;
    private RelativeLayout multiMediaRelativeLayout;
    private RelativeLayout bluetoothRelativeLayout;
    private RelativeLayout heartRateRelativeLayout;
    private RelativeLayout interiorViewRelativeLayout;
    private RelativeLayout outsideViewRelativeLayout;
    private RelativeLayout carWeiChatRelativeLayout;
    private RelativeLayout returnRelativeLayout;
    public static Fragment multiMediaFragment;
    private Fragment bluetoothFragment;
    private Fragment carWeiChatFragment;
    private Fragment heartRateFragment;
    private Fragment interiorCarFragment;
    private Fragment outsideCarFragment;
    private ImageView bgmenu;
    public static   Fragment replaceFragment;
    public  static  boolean flag=false;
    private  static  boolean  start=false;//用于判断app是否启动
    public  static  int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
        initShowFragment();
        App.sContext=this;
        start=true;
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("music.play");
        intentFilter.addAction("music.prev");
        intentFilter.addAction("music.next");
        intentFilter.addAction("music.pause");
        intentFilter.addAction("music.start");
        intentFilter.addAction("music.open");
        intentFilter.addAction("music.close");
        intentFilter.addAction("music.continue");
        intentFilter.addAction("music.random");
        intentFilter.addAction("music.loop.all");
        intentFilter.addAction("music.loop.single");
        intentFilter.addAction("music.loop.random");
        intentFilter.addAction("music.list.open");
        intentFilter.addAction("music.list.close");
        intentFilter.addAction("music.favour");
        intentFilter.addAction("music.unfavour");
        intentFilter.addAction("music.favour.open");
        intentFilter.addAction("music.unfavour.close");
        intentFilter.addAction("txz.kws.set");
        intentFilter.addAction("txz.theme.set");
        intentFilter.addAction("custom.dialog.cancel");
        intentFilter.addAction("com.ofilm.gesture.send.music");
        registerReceiver(new VoiceReceiver(),intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        start=false;
    }

    public static boolean isStart() {//判断app是否启动
        return start;
    }

    private void initShowFragment() {
        multiMediaButton.callOnClick();
    }

    private void initEvents() {
        multiMediaButton.setOnClickListener(this);
        bluetoothTv.setOnClickListener(this);
        heartrateTv.setOnClickListener(this);
        interiorviewTv.setOnClickListener(this);
        outsideviewTv.setOnClickListener(this);
        returnTv.setOnClickListener(this);
        carweichatTv.setOnClickListener(this);

    }
    private void initView() {
        multiMediaButton = this.findViewById(R.id.tv_multimedia);
        bluetoothTv=this.findViewById(R.id.tv_bluetooth);
        heartrateTv=this.findViewById(R.id.tv_heartrate);
        interiorviewTv=this.findViewById(R.id.tv_interiorview);
        outsideviewTv=this.findViewById(R.id.tv_outsideview);
        returnTv=this.findViewById(R.id.tv_return);
        carweichatTv=this.findViewById(R.id.tv_carweichat);
        bluetoothRelativeLayout =this.findViewById(R.id.rl_bluetooth);
        heartRateRelativeLayout=this.findViewById(R.id.rl_heartrate);
        interiorViewRelativeLayout=this.findViewById(R.id.rl_interiorview);
        outsideViewRelativeLayout=this.findViewById(R.id.rl_outsideview);
        multiMediaRelativeLayout=this.findViewById(R.id.rl_multimedia);
        returnRelativeLayout=this.findViewById(R.id.rl_return);
        carWeiChatRelativeLayout=this.findViewById(R.id.rl_carweichat);
        bgmenu = this.findViewById(R.id.bgmenu);
    }

    //设置view透明度变化
    private void setTextViewAlphaChange(View view){
        Animation animation=new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        ID=0;
        Fade fade=new Fade();
        fade.setDuration(500);
        TransitionSet transitionSet=new TransitionSet().addTransition(fade);
        TransitionManager.beginDelayedTransition((ViewGroup) bgmenu.getParent(),transitionSet);
        final  FragmentManager fragmentManager=getSupportFragmentManager();
        final  FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        initTextViewColor();


        switch (v.getId()){
            case R.id.tv_multimedia:
                String text=multiMediaButton.getText().toString();
                int i=text.length();
                setTextViewAlphaChange(multiMediaButton);
                multiMediaButton.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.VISIBLE);
                bluetoothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(multiMediaFragment==null){
                    multiMediaFragment=new MultiMediaFragment();
                    fragmentTransaction.add(R.id.mainFragment,multiMediaFragment);
                }else{
                    fragmentTransaction.show(multiMediaFragment);
                }
                break;
            case R.id.tv_bluetooth:
                setTextViewAlphaChange(bluetoothTv);
                bluetoothTv.setTextColor(getResources().getColor(R.color.textSelect));
                bluetoothRelativeLayout.setVisibility(View.VISIBLE);
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(bluetoothFragment ==null){
                    bluetoothFragment =new BluetoothFragment();
                    fragmentTransaction.add(R.id.mainFragment, bluetoothFragment);
                }else{
                    fragmentTransaction.show(bluetoothFragment);
                }
                break;
            case R.id.tv_heartrate:
                ID=1;
                setTextViewAlphaChange(heartrateTv);
                heartrateTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                bluetoothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.VISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);

                if(replaceFragment==null){
                    replaceFragment=new heartFragment();

                    fragmentTransaction.
                            addToBackStack(null)
                            .add(R.id.mainFragment,replaceFragment);
                }else {

                    fragmentTransaction.show(replaceFragment);
                  }
                break;
            case R.id.tv_interiorview:
                setTextViewAlphaChange(interiorviewTv);
                interiorviewTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                bluetoothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.VISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(interiorCarFragment==null){
                    interiorCarFragment=new InteriorViewFragment();
                    fragmentTransaction.add(R.id.mainFragment,interiorCarFragment);
                }else{
                    fragmentTransaction.show(interiorCarFragment);
                }
                break;
            case R.id.tv_outsideview:
                setTextViewAlphaChange(outsideviewTv);
                outsideviewTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                bluetoothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.VISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(outsideCarFragment==null){
                    outsideCarFragment=new OutsideViewFragment();
                    fragmentTransaction.add(R.id.mainFragment,outsideCarFragment);
                }else{
                    fragmentTransaction.show(outsideCarFragment);
                }
                break;
            case R.id.tv_carweichat:
                setTextViewAlphaChange(carweichatTv);
                carweichatTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                bluetoothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.VISIBLE);
                returnRelativeLayout.setVisibility(View.INVISIBLE);
                if(carWeiChatFragment==null){
                    carWeiChatFragment=new CarWeiChatFragment();
                    fragmentTransaction.add(R.id.mainFragment,carWeiChatFragment);
                }else{
                    fragmentTransaction.show(carWeiChatFragment);
                }
                break;
            case R.id.tv_return:
                setTextViewAlphaChange(returnTv);
                returnTv.setTextColor(getResources().getColor(R.color.textSelect));
                multiMediaRelativeLayout.setVisibility(View.INVISIBLE);
                bluetoothRelativeLayout.setVisibility(View.INVISIBLE);
                heartRateRelativeLayout.setVisibility(View.INVISIBLE);
                interiorViewRelativeLayout.setVisibility(View.INVISIBLE);
                outsideViewRelativeLayout.setVisibility(View.INVISIBLE);
                carWeiChatRelativeLayout.setVisibility(View.INVISIBLE);
                returnRelativeLayout.setVisibility(View.VISIBLE);
                finish();
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    //隐藏所有的fragment
    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(multiMediaFragment!=null){
            fragmentTransaction.hide(multiMediaFragment);
        }
        if(bluetoothFragment !=null){
            fragmentTransaction.hide(bluetoothFragment);
        }
        if(carWeiChatFragment!=null){
            fragmentTransaction.hide(carWeiChatFragment);
        }
        if(heartRateFragment!=null){
            fragmentTransaction.hide(heartRateFragment);
        }
        if(interiorCarFragment!=null){
            fragmentTransaction.hide(interiorCarFragment);
        }
        if(outsideCarFragment!=null){
            fragmentTransaction.hide(outsideCarFragment);
        }
        if(replaceFragment!=null){
            fragmentTransaction.hide(replaceFragment);
        }
    }

    private void initTextViewColor(){
        multiMediaButton.setTextColor(getResources().getColor(R.color.textNoSelect));
        bluetoothTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        heartrateTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        interiorviewTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        outsideviewTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        carweichatTv.setTextColor(getResources().getColor(R.color.textNoSelect));
        returnTv.setTextColor(getResources().getColor(R.color.textNoSelect));
    }
//   public static SpannableString getHighLightKeyWord(int color,String text,int size){
//        SpannableString s=new SpannableString(text);
//        s.setSpan(color,0,size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return s;
//   }
}
