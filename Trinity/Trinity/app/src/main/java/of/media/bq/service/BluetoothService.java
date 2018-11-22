package of.media.bq.service;

import android.accounts.Account;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothHeadsetClient;
import android.bluetooth.BluetoothHeadsetClientCall;
import android.bluetooth.BluetoothPbapClient;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContactsEntity;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;

import of.media.bq.R;

import of.media.bq.bean.CallLog;
import of.media.bq.bean.Contact;
import of.media.bq.BluetoothConstants;
import of.media.bq.saveData.BluetoothData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class BluetoothService extends Service {

    final private static String TAG = "BT.BluetoothService";

    final int HEADSET_CLIENT = 16;
    final int PBAP_CLIENT = 17;

    /* bluetooth stuff */
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothHeadsetClient bluetoothHeadsetClient = null;
    private BluetoothDevice bluetoothDevice = null;
    private BluetoothPbapClient bluetoothPbapClient = null;

    private int currentPbapType = -1;

    private BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.d(TAG, "onServiceConnected, profile is " + profile);

            if (profile == HEADSET_CLIENT) {
                bluetoothHeadsetClient = (BluetoothHeadsetClient) proxy;

                List<BluetoothDevice> list = bluetoothHeadsetClient.getConnectedDevices();
                if (!list.isEmpty()) {
                    bluetoothDevice = list.get(0);
                    int state = bluetoothHeadsetClient.getConnectionState(bluetoothDevice);
                    /* Should always enter */
                    if (state == BluetoothProfile.STATE_CONNECTED) {
                        BluetoothData.setHfpConnected(true);
                        for (BluetoothHeadsetClientCall call : bluetoothHeadsetClient.getCurrentCalls(bluetoothDevice)) {
                            // FIXME: Notify UI ongoing call
                        }

                        // FIXME: Download contact and calllog *ONLY* if no call is going on
                        doBackgroundWork(true, true);
                    }
                }
            } else if (profile == PBAP_CLIENT) {
                bluetoothPbapClient = (BluetoothPbapClient) proxy;
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Log.d(TAG, "onServiceDisconnected, profile is " + profile);

            if (profile == HEADSET_CLIENT) {
                bluetoothHeadsetClient = null;
                bluetoothDevice = null;
            } else if (profile == PBAP_CLIENT) {
                bluetoothPbapClient = null;
            }
        }
    };

    // Broadcast receiver for all changes to states of various profiles
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice currentDevice;
            int prevState, currState;

            if (intent.getAction() == null) {
                return;
            }

            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int newState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (newState == BluetoothAdapter.STATE_ON) {
                        sendToActivity(BluetoothConstants.BT_STATUS_CHANGE, BluetoothConstants.STATUS_ON);
                        BluetoothData.setBTEnabled(true);
                    }
                    else if (newState == BluetoothAdapter.STATE_OFF) {
                        sendToActivity(BluetoothConstants.BT_STATUS_CHANGE, BluetoothConstants.STATUS_OFF);
                    }
                    break;
                case BluetoothHeadsetClient.ACTION_CONNECTION_STATE_CHANGED:
                    prevState = intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, 0);
                    currState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0);
                    currentDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    Log.d(TAG, "HFP Status: " + currentDevice.getAddress() + " (" + String.valueOf(prevState) + " -> " + String.valueOf(currState) + ")");

                    switch (currState) {
                        case BluetoothProfile.STATE_CONNECTED:
                            BluetoothData.setHfpConnected(true);
                            bluetoothDevice = currentDevice;
                            sendToActivity(BluetoothConstants.HFP_STATUS_CHANGE, BluetoothConstants.STATUS_ON);

                            // Download contact and calllog
                            doBackgroundWork(true, true);
                            break;
                        case BluetoothProfile.STATE_DISCONNECTED:
                            BluetoothData.setHfpConnected(false);
                            bluetoothDevice = null;
                            sendToActivity(BluetoothConstants.HFP_STATUS_CHANGE, BluetoothConstants.STATUS_OFF);
                            break;
                    }
                    break;
                case BluetoothPbapClient.ACTION_CONNECTION_STATE_CHANGED:
                    prevState = intent.getIntExtra(BluetoothProfile.EXTRA_PREVIOUS_STATE, 0);
                    currState = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0);
                    currentDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    Log.d(TAG, "PBAP Status: " + currentDevice.getAddress() + " (" + String.valueOf(prevState) + " -> " + String.valueOf(currState) + ")");

                    switch (currState) {
                        case BluetoothProfile.STATE_CONNECTED:
                            if (!currentDevice.equals(bluetoothDevice)) {
                                // Never coming here
                                Log.d(TAG, "Oops, PBAP connected device not as expected, what now");
                                break;
                            }
                            currentPbapType = BluetoothPbapClient.CONTACT_SIM_PATH;
                            break;
                        case BluetoothProfile.STATE_DISCONNECTED:
                            currentPbapType = -1;
                            break;
                    }
                    break;
                case BluetoothPbapClient.ACTION_SYNC_STATE_CHANGED:
                    int pbap_type = intent.getIntExtra(BluetoothPbapClient.EXTRA_PBAP_SYNC_TYPE, 0);
                    int pbap_state = intent.getIntExtra(BluetoothPbapClient.EXTRA_PBAP_SYNC_STATE, 0);
                    Log.d(TAG, "Pbap sync: type = " + pbap_type + ", state = " + pbap_state);
                    if (pbap_type == BluetoothPbapClient.PBAP_CONTACT_SYNC) {
                        if ((currentPbapType == BluetoothPbapClient.CONTACT_SIM_PATH)
                        && (pbap_state == BluetoothPbapClient.PBAP_SYNC_START)) {
                            sendToActivity(BluetoothConstants.PBAP_DOWNLOAD_UPDATE, BluetoothConstants.DOWNLOAD_PB_START);
                        }
                        else if (pbap_state == BluetoothPbapClient.PBAP_SYNC_FINISHED) {
                            // Anything to do?
                        }
                        else if (pbap_state == BluetoothPbapClient.PBAP_WRITE_DATABASE_COMPLETE) {
                            if (currentPbapType == BluetoothPbapClient.CONTACT_SIM_PATH) {
                                currentPbapType = BluetoothPbapClient.CONTACT_PHONE_PATH;
                            }
                            else if (currentPbapType == BluetoothPbapClient.CONTACT_PHONE_PATH) {
                                doBackgroundWork(true, false);
                            }
                        }
                    }
                    else if (pbap_type == BluetoothPbapClient.PBAP_CALLLOG_SYNC) {
                        if (pbap_state == BluetoothPbapClient.PBAP_SYNC_START) {
                            sendToActivity(BluetoothConstants.PBAP_DOWNLOAD_UPDATE, BluetoothConstants.DOWNLOAD_CL_START);
                        }
                        else if (pbap_state == BluetoothPbapClient.PBAP_SYNC_FINISHED) {
                            // Anything to do?
                        }
                        else if (pbap_state == BluetoothPbapClient.PBAP_WRITE_DATABASE_COMPLETE) {
                            // FIXME: Expect BluetoothPbapClient.PBAP_CONTACT_SYNC/BluetoothPbapClient.PBAP_WRITE_DATABASE_COMPLETE not received
                            if (currentPbapType == BluetoothPbapClient.CONTACT_PHONE_PATH) {
                                doBackgroundWork(true, false);
                            }

                            doBackgroundWork(false, true);
                            // Ok, we should disconnect pbap now.
                            bluetoothPbapClient.disconnect(bluetoothDevice);
                        }
                    }
                    break;
                case BluetoothConstants.INTENT_TO_SERVICE:
                    String arg1 = intent.getStringExtra(BluetoothConstants.ARG1);
                    String arg2 = intent.getStringExtra(BluetoothConstants.ARG2);
                    Log.d(TAG, "Message from service: " + arg1 + "/" + arg2);

                    if (arg1.equals(BluetoothConstants.PBAP_DOWNLOAD_PHONEBOOK)) {
                        int pbapStatus = bluetoothPbapClient.getConnectionState(bluetoothDevice);
                        Log.d(TAG, "PBAP download required, current pbap status is " + pbapStatus);
                        if ((pbapStatus == BluetoothProfile.STATE_DISCONNECTED) || (pbapStatus == BluetoothProfile.STATE_DISCONNECTING)) {
                            // Auto download pbap PhoneBook and CallLog
                            bluetoothPbapClient.connect(bluetoothDevice, true);
                        }
                        else if (pbapStatus == BluetoothProfile.STATE_CONNECTED) {
                            // Anything to do?
                        }
                    }
                    break;
                default:
                    Log.e(TAG, "Received unexpected intent, action=" + intent.getAction());
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            /* in simulator, nothing to do */
            return;
        }

        /* The first time get the bluetooth status */
        BluetoothData.setBTEnabled(bluetoothAdapter.isEnabled());
        Log.d(TAG, "Bluetooth is enabled?" + bluetoothAdapter.isEnabled());
        bluetoothAdapter.getProfileProxy(getApplicationContext(), profileListener, HEADSET_CLIENT);
        bluetoothAdapter.getProfileProxy(getApplicationContext(), profileListener, PBAP_CLIENT);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothHeadsetClient.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothPbapClient.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothPbapClient.ACTION_SYNC_STATE_CHANGED);
        filter.addAction(BluetoothConstants.INTENT_TO_SERVICE);
        registerReceiver(receiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        doBackgroundWork(true, true);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        unregisterReceiver(receiver);
    }

    private void sendToActivity(String arg1, String arg2) {
        Intent intent = new Intent(BluetoothConstants.INTENT_TO_ACTIVITY);

        if (arg1 != null) {
            intent.putExtra(BluetoothConstants.ARG1, arg1);
            if (arg2 != null) {
                intent.putExtra(BluetoothConstants.ARG2, arg2);
            }
            sendBroadcast(intent);
        }
    }

    private void doBackgroundWork(boolean syncContact, boolean syncCallLog) {
        new Thread(() -> {
            if (syncContact) {
                getContactList();
            }
            if (syncCallLog) {
                getCallLogList();
            }
        }).start();
    }

    public void getContactList() {
        List<Contact> list = new ArrayList<Contact>();;

        if (bluetoothDevice == null)
            return;

        Log.d(TAG, "getContactList");

        //String address = "C0:EE:FB:F1:EA:C7";
        //Account account = new Account(address, getString(R.string.pbap_account_type));
        Account account = new Account(bluetoothDevice.getAddress(), getString(R.string.pbap_account_type));
        Uri uri = RawContacts.CONTENT_URI.buildUpon()
                .appendQueryParameter(RawContacts.ACCOUNT_NAME, account.name)
                .appendQueryParameter(RawContacts.ACCOUNT_TYPE, account.type)
                .build();
        Cursor cursor = getContentResolver().query(uri, new String[] { RawContacts._ID }, null, null, null);

        if ((cursor == null) || (cursor.getCount() <= 0)) {
            return;
        }
        Log.d(TAG, "ContactList Count: " + cursor.getCount());

        while (cursor.moveToNext()) {
            Cursor c;
            Bitmap photo;
            Contact contact;
            Map<Integer, String> phoneMap;

            contact = new Contact();
            phoneMap = new HashMap();

            c = getContentResolver().query(RawContactsEntity.CONTENT_URI,
                    new String[]{ RawContactsEntity.MIMETYPE, RawContactsEntity.DATA1, RawContactsEntity.DATA2},
                    RawContactsEntity._ID + " = ?",
                    new String[] { cursor.getString(0) },
                    null);
            try {
                while (c.moveToNext()) {
                    String mimeType = c.getString(0);
                    if (mimeType.equals(Phone.CONTENT_ITEM_TYPE)) {
                        int type = c.getInt(2);
                        String number = c.getString(1);
                        phoneMap.put(type, number);
                    }
                    else if (mimeType.equals(Photo.CONTENT_ITEM_TYPE)) {
                        byte[] bytes = c.getBlob(1);
                        if (bytes != null) {
                            photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        }
                        else {
                            photo = BitmapFactory.decodeResource(getResources(), R.drawable.bt_contact_default_avatar);
                        }
                        contact.setPhoto(photo);
                    }
                    else if (mimeType.equals(StructuredName.CONTENT_ITEM_TYPE)) {
                        contact.setName(c.getString(1));
                    }
                }
            } finally {
                    c.close();
            }
            if (contact.getName() != null || !phoneMap.isEmpty()) {
                contact.setNumberMap(phoneMap);
                //Log.d(TAG, contact.toString());
                list.add(contact);
            }
        }
        cursor.close();
        BluetoothData.setContactList(list);
        Log.d(TAG, "getContactList Done");
        if (!list.isEmpty()) {
            sendToActivity(BluetoothConstants.PBAP_DOWNLOAD_UPDATE, BluetoothConstants.DOWNLOAD_Pb_FINISHED);
        }
    }

    public void getCallLogList() {

        List<Contact> contactList;
        List<CallLog> list = new ArrayList<CallLog>();


        Cursor cursor = getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,
                null, null, null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);

        if ((cursor == null) || (cursor.getCount() <= 0)) {
            return;
        }
        Log.d(TAG, "CallLog Count: " + cursor.getCount());

        contactList = BluetoothData.getContactList();

        while (cursor.moveToNext()) {
            CallLog call = new CallLog();

            /* Reading Number */
            call.setNumber(cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER)));

            /* Reading Name */
            String name = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            if ((name == null) || (name.length() == 0)) {
                boolean found = false;
                for(Contact contact:contactList){
                    List<String> numberList = contact.getNumberList();
                    if ((numberList != null) && numberList.contains(call.getNumber())) {
                        call.setName(contact.getName());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    call.setName(call.getNumber());
                }
            }
            else {
                call.setName(name);
            }

            /* Reading Date */
            call.setTimestamp(formatDateTime(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE))));

            /* Reading duration, Duration is null when we get this from pbap */
            call.setDuration(formatDuration(cursor.getLong(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION))));

            /* Reading Type */
            call.setType(cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE)));

            call.setPhoto(BitmapFactory.decodeResource(getResources(), R.drawable.bt_contact_default_avatar));

            //Log.d(TAG, call.toString());
            list.add(call);
        }
        cursor.close();
        BluetoothData.setCallLogList(list);

        Log.d(TAG, "getCallLogList Done");

        if (!list.isEmpty()) {
            sendToActivity(BluetoothConstants.PBAP_DOWNLOAD_UPDATE, BluetoothConstants.DOWNLOAD_CL_FINISHED);
        }
    }

    public static String formatDateTime(long dateTime) {
        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(new Date(dateTime));
        return localTime;
    }

    private String formatDuration(long duration) {
        StringBuilder sb = new StringBuilder();

        if (duration == 0) {
            sb.append("00:00");
        } else if (duration > 0 && duration < 60) {
            sb.append("00:");
            if (duration < 10) {
                sb.append("0");
            }
            sb.append(duration);
        } else if (duration > 60 && duration < 3600) {
            long min = duration / 60;
            long sec = duration % 60;
            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        } else if (duration > 3600) {
            long hour = duration / 3600;
            long min = duration % 3600 / 60;
            long sec = duration % 3600 % 60;
            if (hour < 10) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");

            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        }

        return sb.toString();
    }
}
