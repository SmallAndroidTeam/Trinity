package of.media.bq.fragment;
import of.media.bq.R;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import of.media.bq.adapter.IconAdapter;
import of.meida.bq.convertPXAndDP.DensityUtil;
import of.media.bq.localInformation.App;


public class MultiMediaFragment extends Fragment implements View.OnClickListener  {
    
    private TextView leftOnlinebutton;
    private TextView leftLocalbutton;
    private ImageView leftMenuLiner;
    private Fragment localFragment,onlineFragment;
    private  static  boolean exist=false;//判断此fragment是否存在
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.multi_media_fragment_item,container,false);
        initView(view);
        initEvents();
        selectTab(0);
        exist=true;
        return view;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        exist=false;
    }
    
    public static boolean isExist() {
        return exist;
    }
    
    /**
     *
     * @param i
     * i==0,选中本地
     * i==1,选中在线
     */
    private void selectTab(int i) {
        switch (i){
            case 0:
                leftLocalbutton.callOnClick();
                break;
            case 1:
                leftOnlinebutton.callOnClick();
                break;
            default:
                break;
        }
    }
    
    private void initEvents() {
        leftOnlinebutton.setOnClickListener(this);
        leftLocalbutton.setOnClickListener(this);
    }
    
    private void initView(View view) {
        leftOnlinebutton = view.findViewById(R.id.leftOnlinebutton);
        leftLocalbutton = view.findViewById(R.id.leftLocalbutton);
        leftMenuLiner = view.findViewById(R.id.leftMenuLiner);
    }
    
    @Override
    public void onClick(View v) {
        Fade fade=new Fade();
//        leftMenuLiner.setVisibility(View.INVISIBLE);
//        rightMenuLiner.setVisibility(View.INVISIBLE);
//        TransitionManager.beginDelayedTransition((ViewGroup) leftMenuLiner.getParent(),fade);
//        TransitionManager.beginDelayedTransition((ViewGroup) rightMenuLiner.getParent(),fade);
        
        TransitionManager.beginDelayedTransition((ViewGroup) leftOnlinebutton.getParent(),fade);
        
        final FragmentManager fragmentManage= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManage.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.card_flip_top_in,R.anim.card_flip_top_out);
        hideAllFragment(fragmentTransaction);
        switch (v.getId()){
            case R.id.leftOnlinebutton:
                leftOnlinebutton.getPaint().setFakeBoldText(true);//字体加粗
                leftOnlinebutton.setTextColor(0xffffffff);
                
                leftLocalbutton.getPaint().setFakeBoldText(false);
                leftLocalbutton.setTextColor(0x80ffffff);
                
                
                leftMenuLiner.setImageResource(R.drawable.bg_online);
                
                LinearLayout.LayoutParams leftMenuLinerLayoutParams= (LinearLayout.LayoutParams) leftMenuLiner.getLayoutParams();
                leftMenuLinerLayoutParams.bottomMargin=60;
                leftMenuLiner.setLayoutParams(leftMenuLinerLayoutParams);
                
          
                if(onlineFragment==null){
                    onlineFragment=new OnlineFragment();
                    fragmentTransaction.add(R.id.fl_multi,onlineFragment);
                }else{
                    fragmentTransaction.show(onlineFragment);
                }
                break;
            case R.id.leftLocalbutton:
                
                leftLocalbutton.getPaint().setFakeBoldText(true);//字体加粗
                leftLocalbutton.setTextColor(0xffffffff);//100%透明度
                
                leftOnlinebutton.getPaint().setFakeBoldText(false);
                leftOnlinebutton.setTextColor(0x80ffffff);//50%透明度
                
                
                leftMenuLiner.setImageResource(R.drawable.bg_local);
    
                LinearLayout.LayoutParams leftMenuLinerLayoutParams1= (LinearLayout.LayoutParams) leftMenuLiner.getLayoutParams();
                leftMenuLinerLayoutParams1.bottomMargin=0;
                leftMenuLiner.setLayoutParams(leftMenuLinerLayoutParams1);
                
                
                if(localFragment==null){
                    localFragment=new LocalFragment();
                    fragmentTransaction.add(R.id.fl_multi,localFragment);
                }else{
                    fragmentTransaction.show(localFragment);
                }
                break;
            default:
                break;
        }
//        leftMenuLiner.setVisibility(View.VISIBLE);
//        rightMenuLiner.setVisibility(View.VISIBLE);
        fragmentTransaction.commit();
    }
    
    private  void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(localFragment!=null){
            fragmentTransaction.hide(localFragment);
        }
        
        if(onlineFragment!=null){
            fragmentTransaction.hide(onlineFragment);
        }
    }
    
    
}
