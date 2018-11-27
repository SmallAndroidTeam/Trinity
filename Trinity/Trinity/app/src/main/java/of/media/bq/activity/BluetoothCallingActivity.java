package of.media.bq.activity;

import android.bluetooth.BluetoothHeadsetClientCall;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import of.media.bq.R;
import of.media.bq.bean.Call;
import of.media.bq.localInformation.BluetoothConstants;
import of.media.bq.saveData.BluetoothData;

public class BluetoothCallingActivity extends AppCompatActivity {

    private static final String TAG = "BT.BluetoothCallingActivity";

    private ImageView callingAvatar;
    private TextView callingState;
    private TextView callingName;
    private TextView callingNumber;

    private ImageButton acceptButton;
    private ImageButton terminateButton;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothConstants.INTENT_TO_ACTIVITY)) {
                String arg1 = intent.getStringExtra(BluetoothConstants.ARG1);
                String arg2 = intent.getStringExtra(BluetoothConstants.ARG2);

                Log.d(TAG, "Message from service: " + arg1 + "/" + arg2);

                if (arg1.equals(BluetoothConstants.BT_CALLING_UPDATE)) {
                    if (arg2.equals(BluetoothConstants.CALLING_UI_UPDATE)) {
                        updateViewOnUiThread();
                    }
                    else if (arg2.equals(BluetoothConstants.CALLING_UI_FINISHED)) {
                        BluetoothCallingActivity.this.finish();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int screenWidth, screenHeight;

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        // Check the screen width and height to decide which layout to use
        if (screenWidth/screenHeight <= 2) {
            setContentView(R.layout.activity_bluetooth_calling);
        }
        else {
            setContentView(R.layout.activity_bluetooth_calling_wide_screen);
        }
        initViews();
        updateView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothConstants.INTENT_TO_ACTIVITY);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }
    
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    private void initViews() {
        callingAvatar = findViewById(R.id.calling_avatar);
        callingState = findViewById(R.id.calling_state);
        callingName = findViewById(R.id.calling_name);
        callingNumber = findViewById(R.id.calling_number);

        acceptButton = findViewById(R.id.calling_accept);
        terminateButton = findViewById(R.id.calling_terminate);
    }

    private void updateViewOnUiThread() {
        runOnUiThread(this::updateView);
    }

    private void updateView() {
        Call currentCall = BluetoothData.getCurrentCall();
        if (currentCall != null) {
            /* Calling info */
            callingAvatar.setImageBitmap(currentCall.getPhoto());
            callingName.setText(currentCall.getName());
            callingNumber.setText(currentCall.getNumber());

            String state = "";
            switch(currentCall.getState()){
                case BluetoothHeadsetClientCall.CALL_STATE_DIALING:
                    state = "拨号中";
                    break;
                case BluetoothHeadsetClientCall.CALL_STATE_ALERTING:
                    state = "拨号中";
                    break;
                case BluetoothHeadsetClientCall.CALL_STATE_INCOMING:
                    state = "来电中";
                    break;
                case BluetoothHeadsetClientCall.CALL_STATE_ACTIVE:
                    state = "通话中";
                    break;
                case BluetoothHeadsetClientCall.CALL_STATE_HELD:
                    state = "保留中";
                    break;
                case BluetoothHeadsetClientCall.CALL_STATE_WAITING:
                    state = "等待中";
                    break;
                default:
                    state = "未知";
                    break;
            }
            callingState.setText(state);

            if (currentCall.getState() == BluetoothHeadsetClientCall.CALL_STATE_INCOMING) {
                acceptButton.setVisibility(View.VISIBLE);

                /* Terminate handler */
                acceptButton.setOnClickListener(view1 -> {
                    sendToSerivce(BluetoothConstants.HFP_ACCEPT, null);
                });
            }
            else {
                acceptButton.setVisibility(View.GONE);
            }

            /* Terminate handler */
            terminateButton.setOnClickListener(view1 -> {
                sendToSerivce(BluetoothConstants.HFP_TERMINATE, null);
            });
        }
        else {
            /* Excuse me??? Something error? Quit current activity. */
            finish();
        }
    }

    private void sendToSerivce(String arg1, String arg2) {
        Intent intent = new Intent(BluetoothConstants.INTENT_TO_SERVICE);

        if (arg1 != null) {
            intent.putExtra(BluetoothConstants.ARG1, arg1);
            if (arg2 != null) {
                intent.putExtra(BluetoothConstants.ARG2, arg2);
            }
            sendBroadcast(intent);
        }
    }

}
