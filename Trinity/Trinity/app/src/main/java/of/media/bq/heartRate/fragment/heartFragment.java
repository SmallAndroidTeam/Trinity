package of.media.bq.heartRate.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;


import of.media.bq.R;
import of.media.bq.fragment.HeartRateFragment;
/**
 * Create By rongxinglan IN 2018/11/8
 */
public class heartFragment extends Fragment implements  View.OnClickListener {

    private TextView mCurrent;
    private TextView mWeek;
    private TextView mMonth;
    private TextView testAgain;

    public Fragment currentFragment;
    public Fragment MonthFragment;
    private Fragment WeekFragment;
    public static Fragment HeartRateFragment;
    public static boolean isShowHeartRateFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_layout, container, false);
        initView(view);
        initListener();
        initShowFragment();
        return view;
    }

    private void initListener() {
        mCurrent.setOnClickListener(this);
        mWeek.setOnClickListener(this);
        mMonth.setOnClickListener(this);
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

    private void initView(View view) {
        mCurrent = view.findViewById(R.id.current);
        mWeek = view.findViewById(R.id.week);
        mMonth = view.findViewById(R.id.month);
        testAgain = view.findViewById(R.id.Test_again);
    }

    private void initShowFragment() {
        mCurrent.callOnClick();
    }

    //设置view透明度变化
    private void setTextViewAlphaChange(View view) {
        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(150);
        view.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        final FragmentManager fragmentManager = this.getChildFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removeAllFragment(fragmentTransaction);
        initTextViewColor();
        switch (v.getId()) {
            case R.id.current:
//                if(currentFragment==null){
                currentFragment = new CurrentFragment();
                fragmentTransaction.add(R.id.main_fragment, currentFragment);
//                }else{
//                    fragmentTransaction.show(currentFragment);
//                }
                setTextViewAlphaChange(mCurrent);
                mCurrent.setTextColor(getResources().getColor(R.color.rate));
                mCurrent.setBackgroundResource(R.drawable.chart_select);
                mWeek.setBackgroundResource(R.drawable.bg_week);
                mMonth.setBackgroundResource(R.drawable.bg_month);
                break;
            case R.id.week:
//                if(WeekFragment==null){
                WeekFragment = new WeekFragment();
                fragmentTransaction.add(R.id.main_fragment, WeekFragment);
//                }else{
//                    fragmentTransaction.show(WeekFragment);
//                }
                setTextViewAlphaChange(mWeek);
                mWeek.setTextColor(getResources().getColor(R.color.rate));
                mWeek.setBackgroundResource(R.drawable.chart_select);
                mCurrent.setBackgroundResource(R.drawable.bg_now);
                mMonth.setBackgroundResource(R.drawable.bg_month);
                break;
            case R.id.month:
                // if(MonthFragment==null){
                MonthFragment = new MonthFragment();
                fragmentTransaction.add(R.id.main_fragment, MonthFragment);
//                }else{
//                    fragmentTransaction.show(MonthFragment);
//                }
                setTextViewAlphaChange(mMonth);
                mMonth.setTextColor(getResources().getColor(R.color.rate));
                mMonth.setBackgroundResource(R.drawable.chart_select);
                mCurrent.setBackgroundResource(R.drawable.bg_now);
                mWeek.setBackgroundResource(R.drawable.bg_week);
                break;
//            case R.id.Test_again:
//                    HeartRateFragment=new HeartRateFragment();
//                    fragmentTransaction.replace(R.id.mainFragment, HeartRateFragment);
//                    isShowHeartRateFragment = true;
//                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    private void setTab(int index) {
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction fT = fm.beginTransaction();
        switch (index) {
            case 0:
                HeartRateFragment = new HeartRateFragment();
                fT.add(R.id.mainFragment, HeartRateFragment);
                isShowHeartRateFragment = true;
                break;
        }
        fT.commit();
    }

    private void removeAllFragment(FragmentTransaction fragmentTransaction) {
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

    private void initTextViewColor() {
        mCurrent.setTextColor(getResources().getColor(R.color.gray));
        mWeek.setTextColor(getResources().getColor(R.color.gray));
        mMonth.setTextColor(getResources().getColor(R.color.gray));
    }
}
