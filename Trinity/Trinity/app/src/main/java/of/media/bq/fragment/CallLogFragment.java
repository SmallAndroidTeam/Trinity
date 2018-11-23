package of.media.bq.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import of.media.bq.R;

import of.media.bq.bean.CallLog;

import of.media.bq.saveData.BluetoothData;
import of.media.bq.widget.BluetoothConstants;
import of.media.bq.adapter.BluetoothCallLogAdapter;

import java.util.List;

public class CallLogFragment extends Fragment {
    private static final String TAG = "BT.CallLogFragment";

    private ListView callLogListView;
    private FrameLayout fp_container;
    private BluetoothCallLogAdapter callLogAdapter;
    private List<CallLog> callLogList;

    private LinearLayout callLogInfoLayout;

    private ImageView downloadingView;

    private TextView callLogInfoTips;
    private TextView callLogTips;

    private ImageButton dialButton;

    private ImageView contactImg;
    private TextView contactName;
    private TextView contactNumber;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothConstants.INTENT_TO_ACTIVITY)) {
                String arg1 = intent.getStringExtra(BluetoothConstants.ARG1);
                String arg2 = intent.getStringExtra(BluetoothConstants.ARG2);

                Log.d(TAG, "Message from service: " + arg1 + "/" + arg2);

                if (arg1.equals(BluetoothConstants.BT_STATUS_CHANGE)) {
                    if (arg2.equals(BluetoothConstants.STATUS_ON)) {
                        updateViewOnUiThread(BluetoothConstants.BT_CONNECTED);
                    }
                    else if (arg2.equals(BluetoothConstants.STATUS_OFF)) {
                        updateViewOnUiThread(BluetoothConstants.BT_DISCONNECTED);
                    }
                }
                else if (arg1.equals(BluetoothConstants.HFP_STATUS_CHANGE)) {
                    if (arg2.equals(BluetoothConstants.STATUS_ON)) {
                        updateViewOnUiThread(BluetoothConstants.HFP_CONNECTED);
                    }
                    else if (arg2.equals(BluetoothConstants.STATUS_OFF)) {
                        updateViewOnUiThread(BluetoothConstants.HFP_DISCONNECTED);
                    }
                }
                else if (arg1.equals(BluetoothConstants.PBAP_DOWNLOAD_UPDATE)) {
                    if (arg2.equals(BluetoothConstants.DOWNLOAD_CL_START)) {
                        updateViewOnUiThread(BluetoothConstants.PBAP_CL_START);
                    }
                    else if (arg2.equals(BluetoothConstants.DOWNLOAD_CL_FINISHED)) {
                        updateViewOnUiThread(BluetoothConstants.PBAP_CL_DOWN);
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothConstants.INTENT_TO_ACTIVITY);
        getContext().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.bluetooth_calllog, container, false);
        initViews(inflater, view);
        return view;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");

        showView();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
    }

    private void showView() {
        if (BluetoothData.getHfpConnected()) {
            if (!BluetoothData.getCallLogList().isEmpty()) {
                updateView(BluetoothConstants.PBAP_CL_DOWN);
            }
            else {
                updateView(BluetoothConstants.HFP_CONNECTED);
            }        }
        else {
            if (BluetoothData.getBTEnabled()) {
                updateView(BluetoothConstants.BT_CONNECTED);
            }
            else {
                updateView(BluetoothConstants.BT_DISCONNECTED);
            }
        }
    }

    private void initViews(LayoutInflater flater, View view) {
        /* Left pad */
        fp_container = view.findViewById(R.id.calllog_left_pad);
        View inflateView = flater.inflate(R.layout.bluetooth_contact_info, fp_container, false);
        fp_container.addView(inflateView);

        callLogInfoLayout = fp_container.findViewById(R.id.contact_info_pad);

        contactImg = fp_container.findViewById(R.id.contact_info_avatar);
        contactName = fp_container.findViewById(R.id.contact_info_name);
        contactNumber = fp_container.findViewById(R.id.contact_info_number);

        callLogInfoTips = fp_container.findViewById(R.id.contact_info_tips);

        /* Dial button */
        dialButton = view.findViewById(R.id.contact_dial_button);
        dialButton.setOnClickListener(view1 -> {
            String number = "10010";
            sendToSerivce(BluetoothConstants.HFP_DIAL_REQ, number);
        });

        downloadingView = view.findViewById(R.id.calllog_downloading);

        callLogTips = view.findViewById(R.id.calllog_tips);

        callLogListView = view.findViewById(R.id.calllog_list_view);
    }

    private void initCallListListView() {
        callLogListView.setOnItemClickListener((parent, view, position, id) -> {
            setContactSected(position);
        });

        callLogList = BluetoothData.getCallLogList();
        callLogAdapter = new BluetoothCallLogAdapter(getContext(), callLogList);
        callLogListView.setAdapter(callLogAdapter);

        // Set the first item selected when fist display
        if (!callLogList.isEmpty()) {
            setContactSected(0);
        }
    }

    private void setContactSected(int position) {
        callLogAdapter.setSelectItem(position);
        CallLog currentCallLog = (CallLog) callLogAdapter.getItem(position);
        contactImg.setImageBitmap(currentCallLog.getPhoto());
        contactName.setText(currentCallLog.getName());
        contactNumber.setText(currentCallLog.getNumber());
    }

    private void updateViewOnUiThread(int status) {
        getActivity().runOnUiThread(() -> updateView(status));
    }

    private void updateView(int status) {
        if (status == BluetoothConstants.BT_DISCONNECTED) {
            // Disable contact info and contact listview
            callLogListView.setVisibility(View.GONE);
            callLogInfoLayout.setVisibility(View.GONE);

            // Enable Status tips
            callLogInfoTips.setVisibility(View.VISIBLE);
            callLogInfoTips.setText(getText(R.string.bt_disabled));
            callLogTips.setVisibility(View.VISIBLE);
            callLogTips.setText(getText(R.string.bt_disabled) + " " + getText(R.string.bt_disabled_next));
            downloadingView.setVisibility(View.GONE);

            // Enable dial button
            dialButton.setEnabled(false);
            dialButton.setAlpha(0.5f);

        }
        else if ((status == BluetoothConstants.BT_CONNECTED)
              || (status == BluetoothConstants.HFP_DISCONNECTED)) {
            // Disable contact info and contact listview
            callLogListView.setVisibility(View.GONE);
            callLogInfoLayout.setVisibility(View.GONE);

            // Enable Status tips
            callLogInfoTips.setVisibility(View.VISIBLE);
            callLogInfoTips.setText(getText(R.string.hfp_disabled));
            callLogTips.setVisibility(View.VISIBLE);
            callLogTips.setText(getText(R.string.hfp_disabled) + " " + getText(R.string.hfp_disabled_next));
            downloadingView.setVisibility(View.GONE);

            // Enable dial button
            dialButton.setEnabled(false);
            dialButton.setAlpha(0.5f);
        }
        else if (status == BluetoothConstants.HFP_CONNECTED) {
            // Disable contact info and contact listview
            callLogListView.setVisibility(View.GONE);
            callLogInfoLayout.setVisibility(View.GONE);

            // Disable Status tips
            callLogInfoTips.setVisibility(View.VISIBLE);
            callLogInfoTips.setText(getText(R.string.pbap_cl_none));
            callLogTips.setVisibility(View.VISIBLE);
            callLogTips.setText(getText(R.string.pbap_cl_none) + " " + getText(R.string.pbap_pb_none_next));
            downloadingView.setVisibility(View.GONE);

            // Enable dial button
            dialButton.setEnabled(true);
            dialButton.setAlpha(1f);
        }
        else if (status == BluetoothConstants.PBAP_CL_START) {
            // Disable contact info and contact listview
            callLogListView.setVisibility(View.GONE);
            callLogInfoLayout.setVisibility(View.GONE);

            // Enable Status tips
            callLogInfoTips.setVisibility(View.VISIBLE);
            callLogInfoTips.setText(getText(R.string.pbap_cl_none));
            callLogTips.setVisibility(View.VISIBLE);
            callLogTips.setText(getText(R.string.pbap_cl_downloading));
            downloadingView.setVisibility(View.VISIBLE);

            // Enable dial button
            dialButton.setEnabled(true);
            dialButton.setAlpha(1f);
        }
        else if (status == BluetoothConstants.PBAP_CL_DOWN) {
            Log.d(TAG, "Display CallLog");

            // Enable contact info and contact listview
            callLogInfoLayout.setVisibility(View.VISIBLE);
            callLogListView.setVisibility(View.VISIBLE);
            initCallListListView();

            // Disable Status tips
            callLogInfoTips.setVisibility(View.GONE);
            callLogTips.setVisibility(View.GONE);
            callLogInfoTips.setText("");
            callLogTips.setText("");
            downloadingView.setVisibility(View.GONE);

            // Enable dial button
            dialButton.setEnabled(true);
            dialButton.setAlpha(1f);
        }
    }

    private void sendToSerivce(String arg1, String arg2) {
        Intent intent = new Intent(BluetoothConstants.INTENT_TO_SERVICE);

        if (arg1 != null) {
            intent.putExtra(BluetoothConstants.ARG1, arg1);
            if (arg2 != null) {
                intent.putExtra(BluetoothConstants.ARG2, arg2);
            }
            getContext().sendBroadcast(intent);
        }
    }

}
