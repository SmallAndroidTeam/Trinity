package of.media.bq.heartRate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import of.media.bq.R;
import of.media.bq.activity.MainActivity;
import of.media.bq.fragment.HeartRateFragment;
/**
 * Create By rongxinglan IN 2018/11/8
 */
public class heartFragment extends Fragment implements  View.OnClickListener{

    private TextView mCurrent;



    private TextView mRecent;
    private TextView mMore;
    private TextView testAgain;

    public Fragment currentFragment;
    public Fragment MonthFragment;
    private Fragment WeekFragment;
    public static Fragment HeartRateFragment;
    private ImageView heartCurrentUndeerLine;
    private ImageView heartRecentUndeerLine;
    private ImageView heartMoreUndeerLine;

    public static boolean isShowHeartRateFragment;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.heart_rate_chart, null);
        View view=inflater.inflate(R.layout.heart_test_result,container,false);
        initView(view);
        initListener();
        initShowFragment();
        return view;
    }

    private void initListener() {
        mCurrent.setOnClickListener(this);
        mRecent.setOnClickListener(this);
        mMore.setOnClickListener(this);
        testAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Test_again:
                        setTab(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView(View view){
        mCurrent=view.findViewById(R.id.current);
        mRecent=view.findViewById(R.id.recent);
        mMore=view.findViewById(R.id.more);
        heartCurrentUndeerLine=view.findViewById(R.id.heartCurrentUnderline);
        heartRecentUndeerLine=view.findViewById(R.id.heartRecentUnderline);
        heartMoreUndeerLine=view.findViewById(R.id.heartMoreUnderline);
        testAgain =view.findViewById(R.id.Test_again);
    }
    private void initShowFragment() {
        mCurrent.callOnClick();
    }

    @Override
    public void onClick(View v){
        final FragmentManager fragmentManager = this.getFragmentManager();
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        removeAllFragment(fragmentTransaction);
        setTextViewColorAndShowUnderLineByIndex(v);
        switch (v.getId()){
            case R.id.current:

                currentFragment=new CurrentFragment();
                fragmentTransaction.add(R.id.main_fragment,currentFragment);
                break;
            case R.id.recent:

                WeekFragment=new WeekFragment();
                fragmentTransaction.add(R.id.main_fragment,WeekFragment);

                break;
            case R.id.more:

                MonthFragment=new MonthFragment();
                fragmentTransaction.add(R.id.main_fragment,MonthFragment);

                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void setTab(int index) {
        //   final FragmentManager fm = getActivity().getSupportFragmentManager();
        //  final FragmentTransaction fT = fm.beginTransaction();
        switch (index) {
            case 0:
//                HeartRateFragment = new HeartRateFragment();
//                fT.add(R.id.mainFragment, HeartRateFragment);
                // isShowHeartRateFragment = true;
                getFragmentManager().popBackStack();
                MainActivity.replaceFragment=new HeartRateFragment();
                getFragmentManager().beginTransaction().hide(MainActivity.multiMediaFragment).addToBackStack(null)
                        .add(R.id.mainFragment,MainActivity.replaceFragment).commit();
                break;
        }
        //   fT.commit();
    }
    public void setTextViewColorAndShowUnderLineByIndex(View v){
//        musicTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
//        videoTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
//        galleryTextView.setTextColor(getResources().getColor(R.color.textNoSelect));
//        final Fade fade=new Fade();
//        TransitionManager.beginDelayedTransition((ViewGroup) localMusicUnderline.getParent(),fade);
//        TransitionManager.beginDelayedTransition((ViewGroup) localVideoUnderline.getParent(),fade);
//        TransitionManager.beginDelayedTransition((ViewGroup) localGalleryUnderline.getParent(),fade);
        heartMoreUndeerLine.setVisibility(View.INVISIBLE);
        heartRecentUndeerLine.setVisibility(View.INVISIBLE);
        heartCurrentUndeerLine.setVisibility(View.INVISIBLE);
        switch (v.getId()){
            case R.id.current:
//                musicTextView.setTextColor(0xffffffff);//100%透明度，颜色为#ffffff
//                videoTextView.setTextColor(0x80ffffff);//50%透明度，颜色为#ffffff
//                galleryTextView.setTextColor(0x80ffffff);
                heartCurrentUndeerLine.setVisibility(View.VISIBLE);
                break;
            case R.id.recent:
//                musicTextView.setTextColor(0x80ffffff);
//                videoTextView.setTextColor(0xffffffff);
//                galleryTextView.setTextColor(0x80ffffff);
                heartRecentUndeerLine.setVisibility(View.VISIBLE);
                break;
            case R.id.more:
//                musicTextView.setTextColor(0x80ffffff);
//                videoTextView.setTextColor(0x80ffffff);
//                galleryTextView.setTextColor(0xffffffff);
                heartMoreUndeerLine.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private  void removeAllFragment(FragmentTransaction fragmentTransaction){
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }
        if (MonthFragment != null) {
            fragmentTransaction.remove(MonthFragment);
        }
        if (WeekFragment != null) {
            fragmentTransaction.remove(WeekFragment);
        }
        if (HeartRateFragment != null) {
            fragmentTransaction.remove(HeartRateFragment);
        }
    }
}
