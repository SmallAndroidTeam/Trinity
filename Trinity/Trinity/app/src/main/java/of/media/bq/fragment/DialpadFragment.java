package of.media.bq.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import of.media.bq.R;
import of.media.bq.BluetoothConstants;
import of.media.bq.saveData.BluetoothData;

public class DialpadFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "BT.DialpadFragment";

    private TextView tipsView;
    private View underline;
    private LinearLayout numberLayout;

    private TextView phoneNumber;
    private ImageButton deleteButton;
    private ImageButton dialButton;

    private ListView matchedList;

    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;
    private Button buttonFive;
    private Button buttonSix;
    private Button buttonSeven;
    private Button buttonEight;
    private Button buttonNine;
    private Button buttonZero;
    private Button buttonStar;
    private Button buttonPound;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothConstants.INTENT_TO_ACTIVITY)) {
                String arg1 = intent.getStringExtra(BluetoothConstants.ARG1);
                String arg2 = intent.getStringExtra(BluetoothConstants.ARG2);

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
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* register interested intent */
        IntentFilter intentFilter;
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothConstants.INTENT_TO_ACTIVITY);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.bluetooth_dialpad, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onDestroy()
    {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }

        super.onDestroy();
    }

    /**
     * Initializes the View components
     */
    private void initViews(View view) {
        initializeViews(view);
        addNumberFormatting();
        setClickListeners();

        if (BluetoothData.getHfpConnected()) {
            updateView(BluetoothConstants.HFP_CONNECTED);
        }
        else {
            if (BluetoothData.getBTEnabled()) {
                updateView(BluetoothConstants.BT_CONNECTED);
            }
            else {
                updateView(BluetoothConstants.BT_DISCONNECTED);
            }
        }
    }

    /**
     * Initializes the views from XML
     */
    private void initializeViews(View view) {

        numberLayout = view.findViewById(R.id.dialpad_number_layout);

        tipsView = view.findViewById(R.id.dialpad_tips);

        underline = view.findViewById(R.id.dialpad_underline);

        phoneNumber  = view.findViewById(R.id.dialpad_phone_number);
        phoneNumber.setInputType(android.text.InputType.TYPE_NULL);

        matchedList = view.findViewById(R.id.dialpad_matched_list);

        deleteButton = view.findViewById(R.id.dialpad_delete_button);
        dialButton   = view.findViewById(R.id.dialpad_dial_button);

        buttonOne    = view.findViewById(R.id.dialpad_number_one);
        buttonTwo    = view.findViewById(R.id.dialpad_number_two);
        buttonThree  = view.findViewById(R.id.dialpad_number_three);
        buttonFour   = view.findViewById(R.id.dialpad_number_four);
        buttonFive   = view.findViewById(R.id.dialpad_number_five);
        buttonSix    = view.findViewById(R.id.dialpad_number_six);
        buttonSeven  = view.findViewById(R.id.dialpad_number_seven);
        buttonEight  = view.findViewById(R.id.dialpad_number_eight);
        buttonNine   = view.findViewById(R.id.dialpad_number_nine);
        buttonZero   = view.findViewById(R.id.dialpad_number_zero);
        buttonStar   = view.findViewById(R.id.dialpad_number_star);
        buttonPound  = view.findViewById(R.id.dialpad_number_pound);
    }

    private void updateViewOnUiThread(int status) {
        getActivity().runOnUiThread(() -> updateView(status));
    }

    private void updateView(int status) {
        if ((status == BluetoothConstants.BT_CONNECTED) || status == BluetoothConstants.HFP_DISCONNECTED) {
            controlView(false, getString(R.string.hfp_disabled) + "\n" + getString(R.string.hfp_disabled_next));
        }
        else if (status == BluetoothConstants.BT_DISCONNECTED) {
            controlView(false, getString(R.string.bt_disabled) + "\n" + getString(R.string.bt_disabled_next));
        }
        else if (status == BluetoothConstants.HFP_CONNECTED) {
            controlView(true, null);
        }
    }

    private void controlView(boolean setEnable, String tipText) {
        deleteButton.setEnabled(setEnable);
        dialButton.setEnabled(setEnable);
        buttonOne.setEnabled(setEnable);
        buttonTwo.setEnabled(setEnable);
        buttonThree.setEnabled(setEnable);
        buttonFour.setEnabled(setEnable);
        buttonFive.setEnabled(setEnable);
        buttonSix.setEnabled(setEnable);
        buttonSeven.setEnabled(setEnable);
        buttonEight.setEnabled(setEnable);
        buttonNine.setEnabled(setEnable);
        buttonZero.setEnabled(setEnable);
        buttonStar.setEnabled(setEnable);
        buttonPound.setEnabled(setEnable);
        if (setEnable) {
            dialButton.setAlpha(1f);

            numberLayout.setVisibility(View.VISIBLE);
            underline.setVisibility(View.VISIBLE);
            matchedList.setVisibility(View.VISIBLE);

            tipsView.setVisibility(View.GONE);
            tipsView.setText("");
        }
        else {
            dialButton.setAlpha(0.5f);

            numberLayout.setVisibility(View.GONE);
            underline.setVisibility(View.INVISIBLE);
            matchedList.setVisibility(View.GONE);

            tipsView.setVisibility(View.VISIBLE);
            tipsView.setText(tipText);
        }
    }

    /**
     * Adds number formatting to the field
     */
    private void addNumberFormatting() {
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    /**
     * Sets click listeners for the views
     */
    private void setClickListeners() {
        deleteButton.setOnClickListener(this);
        deleteButton.setOnLongClickListener(this);

        dialButton.setOnClickListener(this);

        buttonZero.setOnClickListener(this);
        buttonZero.setOnLongClickListener(this);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSix.setOnClickListener(this);
        buttonSeven.setOnClickListener(this);
        buttonEight.setOnClickListener(this);
        buttonNine.setOnClickListener(this);
        buttonStar.setOnClickListener(this);
        buttonPound.setOnClickListener(this);
    }

    private void keyPressed(int keyCode) {
        Log.d(TAG, "keyPressed");
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        phoneNumber.onKeyDown(keyCode, event);
    }

    /**
     * Click handler for the views
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialpad_number_one: {
                keyPressed(KeyEvent.KEYCODE_1);
                break;
            }
            case R.id.dialpad_number_two: {
                keyPressed(KeyEvent.KEYCODE_2);
                break;
            }
            case R.id.dialpad_number_three: {
                keyPressed(KeyEvent.KEYCODE_3);
                break;
            }
            case R.id.dialpad_number_four: {
                keyPressed(KeyEvent.KEYCODE_4);
                break;
            }
            case R.id.dialpad_number_five: {
                keyPressed(KeyEvent.KEYCODE_5);
                break;
            }
            case R.id.dialpad_number_six: {
                keyPressed(KeyEvent.KEYCODE_6);
                break;
            }
            case R.id.dialpad_number_seven: {
                keyPressed(KeyEvent.KEYCODE_7);
                break;
            }
            case R.id.dialpad_number_eight: {
                keyPressed(KeyEvent.KEYCODE_8);
                break;
            }
            case R.id.dialpad_number_nine: {
                keyPressed(KeyEvent.KEYCODE_9);
                break;
            }
            case R.id.dialpad_number_zero: {
                keyPressed(KeyEvent.KEYCODE_0);
                break;
            }
            case R.id.dialpad_number_pound: {
                keyPressed(KeyEvent.KEYCODE_POUND);
                break;
            }
            case R.id.dialpad_number_star: {
                keyPressed(KeyEvent.KEYCODE_STAR);
                break;
            }
            case R.id.dialpad_delete_button: {
                keyPressed(KeyEvent.KEYCODE_DEL);
                break;
            }
            case R.id.dialpad_dial_button: {
                dialNumber();
                break;
            }
        }

    }

    /**
     * Long Click Listener
     */
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.dialpad_delete_button: {
                phoneNumber.setText("");
                return true;
            }
            case R.id.dialpad_number_zero: {
                keyPressed(KeyEvent.KEYCODE_PLUS);
                return true;
            }
        }
        return false;
    }

    /**
     * Starts the bt dial action
     */
    private void dialNumber() {
        String number = phoneNumber.getText().toString();
        if (number.length() > 0) {
            Log.d(TAG, "dialNumber " + number);
            sendToService(BluetoothConstants.HFP_DIAL_REQ, number);
        }
    }

    private void sendToService(String arg1, String arg2) {
        Intent intent = new Intent(BluetoothConstants.INTENT_TO_SERVICE);

        if (arg1 != null) {
            intent.putExtra(BluetoothConstants.ARG1, arg1);
            if (arg2 != null) {
                intent.putExtra(BluetoothConstants.ARG2, arg2);
            }
            getActivity().sendBroadcast(intent);
        }
    }

}
