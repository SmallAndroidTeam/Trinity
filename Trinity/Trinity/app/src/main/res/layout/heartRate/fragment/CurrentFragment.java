package of.meida.bq.heartRate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trinity.R;

import java.util.ArrayList;

import of.meida.bq.heartRate.chartview.ChartEntity;

/**
 * Create By rongxinglan IN 2018/10/24
 */
public class CurrentFragment extends Fragment {
    private of.meida.bq.heartRate.chartview.LineChartView mChartView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  View view = inflater.inflate(R.layout.chart, null);
        View view=inflater.inflate(R.layout.chart,container,false);

        initView(view);
        return view;
    }

    private  void initView(View view){
      mChartView = view.findViewById(R.id.lineView);
        ArrayList<ChartEntity> data = new ArrayList<>();
        of.meida.bq.heartRate.chartview.ChartEntity entity1 = new of.meida.bq.heartRate.chartview.ChartEntity();
        entity1.setValue(50);
        entity1.setText("10/22");
        of.meida.bq.heartRate.chartview.ChartEntity entity2 = new of.meida.bq.heartRate.chartview.ChartEntity();
        entity2.setValue(103);
        entity2.setText("10/23");
        of.meida.bq.heartRate.chartview.ChartEntity entity3 = new of.meida.bq.heartRate.chartview.ChartEntity();
        entity3.setValue(89);
        entity3.setText("10/24");

        data.add(entity1);
        data.add(entity2);
        data.add(entity3);

        mChartView.setShadow(true);
//        mChartView.setUnitText("g");
        mChartView.setDataChart(data);
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        final ChartActivity activity = (ChartActivity)getActivity();
//        mChartView = (LineChartView).findViewById(R.id.lineView);
//        ArrayList<ChartEntity> data = new ArrayList<>();
//        ChartEntity entity1 = new ChartEntity();
//        entity1.setValue(50);
//        entity1.setText("10/22");
//        ChartEntity entity2 = new ChartEntity();
//        entity2.setValue(103);
//        entity2.setText("10/23");
//        ChartEntity entity3 = new ChartEntity();
//        entity3.setValue(89);
//        entity3.setText("10/24");
//
//        data.add(entity1);
//        data.add(entity2);
//        data.add(entity3);
//
//        mChartView.setShadow(true);
////        mChartView.setUnitText("g");
//        mChartView.setDataChart(data);
//    }
}
