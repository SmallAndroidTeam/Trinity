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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import of.media.bq.R;
import of.media.bq.bean.Contact;

import of.media.bq.saveData.BluetoothData;
import of.media.bq.localInformation.BluetoothConstants;
import of.media.bq.adapter.BluetoothContactAdapter;

import java.util.List;

public class ContactsFragment extends Fragment {

    private static final String TAG = "BT.ContactsFragment";

    private ListView contactsListView;
    private FrameLayout fp_container;
    private BluetoothContactAdapter contactAdapter;
    private List<Contact> contactList;

    private Contact currentContact;

    private LinearLayout contactInfoLayout;

    private LinearLayout tipsLayout;
    private ImageView downloadingView;

    private TextView contactInfoTips;
    private TextView contactTips;

    private Button contactSync;
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
                    if (arg2.equals(BluetoothConstants.DOWNLOAD_PB_START)) {
                        updateViewOnUiThread(BluetoothConstants.PBAP_PB_START);
                    }
                    else if (arg2.equals(BluetoothConstants.DOWNLOAD_Pb_FINISHED)) {
                        updateViewOnUiThread(BluetoothConstants.PBAP_PB_DOWN);
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

        currentContact = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.bluetooth_contacts, container, false);
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
            if (!BluetoothData.getContactList().isEmpty()) {
                updateView(BluetoothConstants.PBAP_PB_DOWN);
            }
            else {
                updateView(BluetoothConstants.HFP_CONNECTED);
            }
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

    private void initViews(LayoutInflater flater, View view) {
        /* Left pad */
        fp_container = view.findViewById(R.id.contacts_left_pad);
        View inflateView = flater.inflate(R.layout.bluetooth_contact_info, fp_container, false);
        fp_container.addView(inflateView);

        contactInfoLayout = fp_container.findViewById(R.id.contact_info_pad);

        contactImg = fp_container.findViewById(R.id.contact_info_avatar);
        contactName = fp_container.findViewById(R.id.contact_info_name);
        contactNumber = fp_container.findViewById(R.id.contact_info_number);

        contactInfoTips = fp_container.findViewById(R.id.contact_info_tips);

        /* Sync button */
        contactSync = view.findViewById(R.id.contact_sync);
        contactSync.setOnClickListener(v -> sendToSerivce(BluetoothConstants.PBAP_DOWNLOAD_PHONEBOOK, null));

        /* Dial button */
        dialButton = view.findViewById(R.id.contact_dial_button);
        dialButton.setOnClickListener(view1 -> {
            if((currentContact != null) && (currentContact.getNumber().length() > 0)) {
                String number = currentContact.getNumber();
                sendToSerivce(BluetoothConstants.HFP_DIAL, number);
            }
        });

        tipsLayout = view.findViewById(R.id.contact_tips_layout);
        downloadingView = view.findViewById(R.id.contact_downloading);
        contactTips = view.findViewById(R.id.contacts_tips);

        /* Contact listview */
        contactsListView = view.findViewById(R.id.contacts_list_view);
    }

    private void initContactListView() {
        contactsListView.setOnItemClickListener((parent, view, position, id) -> {
            setContactSected(position);
        });

        contactList = BluetoothData.getContactList();
        contactAdapter = new BluetoothContactAdapter(getContext(), contactList);
        contactsListView.setAdapter(contactAdapter);

        // Set the first item selected when fist display
        if (!contactList.isEmpty()) {
            setContactSected(0);
        }
    }

    private void setContactSected(int position) {
        contactAdapter.setSelectItem(position);
        currentContact = (Contact) contactAdapter.getItem(position);
        contactImg.setImageBitmap(currentContact.getPhoto());
        contactName.setText(currentContact.getName());
        contactNumber.setText(currentContact.getNumber());
    }

    private void updateViewOnUiThread(int status) {
        getActivity().runOnUiThread(() -> updateView(status));
    }

    private void updateView(int status) {
        if (status == BluetoothConstants.BT_DISCONNECTED) {
            // Disable contact info and contact listview
            contactsListView.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);

            // Enable Status tips
            tipsLayout.setVisibility(View.VISIBLE);
            contactInfoTips.setVisibility(View.VISIBLE);
            contactInfoTips.setText(getText(R.string.bt_disabled));
            contactTips.setVisibility(View.VISIBLE);
            contactTips.setText(getText(R.string.bt_disabled) + " " + getText(R.string.bt_disabled_next));
            downloadingView.setVisibility(View.GONE);

            // Disable sync and dial button
            contactSync.setVisibility(View.GONE);
            contactSync.setEnabled(false);
            dialButton.setEnabled(false);
            dialButton.setAlpha(0.5f);
        }
        else if ((status == BluetoothConstants.BT_CONNECTED)
              || (status == BluetoothConstants.HFP_DISCONNECTED)) {
            // Disable contact info and contact listview
            contactsListView.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);

            // Enable Status tips
            tipsLayout.setVisibility(View.VISIBLE);
            contactInfoTips.setVisibility(View.VISIBLE);
            contactInfoTips.setText(getText(R.string.hfp_disabled));
            contactTips.setVisibility(View.VISIBLE);
            contactTips.setText(getText(R.string.hfp_disabled) + " " + getText(R.string.hfp_disabled_next));
            downloadingView.setVisibility(View.GONE);

            // Disable sync and dial button
            contactSync.setVisibility(View.GONE);
            contactSync.setEnabled(false);
            dialButton.setEnabled(false);
            dialButton.setAlpha(0.5f);
        }
        else if (status == BluetoothConstants.HFP_CONNECTED) {
            // Disable contact info and contact listview
            contactsListView.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);

            // Disable Status tips
            tipsLayout.setVisibility(View.VISIBLE);
            contactInfoTips.setVisibility(View.VISIBLE);
            contactInfoTips.setText(getText(R.string.pbap_pb_none));
            contactTips.setVisibility(View.VISIBLE);
            contactTips.setText(getText(R.string.pbap_pb_none) + " " + getText(R.string.pbap_pb_none_next));
            downloadingView.setVisibility(View.GONE);

            // Enable sync and dial button
            contactSync.setVisibility(View.VISIBLE);
            contactSync.setEnabled(true);
            contactSync.setText(R.string.pbap_sync);
            dialButton.setEnabled(true);
            dialButton.setAlpha(1f);
        }
        else if (status == BluetoothConstants.PBAP_PB_START) {
            // Disable contact info and contact listview
            contactsListView.setVisibility(View.GONE);
            contactInfoLayout.setVisibility(View.GONE);

            // Enable Status tips
            tipsLayout.setVisibility(View.VISIBLE);
            contactInfoTips.setVisibility(View.VISIBLE);
            contactInfoTips.setText(getText(R.string.pbap_pb_none));
            contactTips.setVisibility(View.VISIBLE);
            contactTips.setText(getText(R.string.pbap_pb_downloading));
            downloadingView.setVisibility(View.VISIBLE);

            // Enable sync and dial button
            contactSync.setVisibility(View.GONE);
            contactSync.setEnabled(false);
            dialButton.setEnabled(true);
            dialButton.setAlpha(1f);
        }
        else if (status == BluetoothConstants.PBAP_PB_DOWN) {
            // Enable contact info and contact listview
            contactInfoLayout.setVisibility(View.VISIBLE);
            contactsListView.setVisibility(View.VISIBLE);
            initContactListView();

            // Disable Status tips
            tipsLayout.setVisibility(View.GONE);
            contactInfoTips.setVisibility(View.GONE);
            contactTips.setVisibility(View.GONE);
            contactInfoTips.setText("");
            contactTips.setText("");
            downloadingView.setVisibility(View.GONE);

            // Enable sync and dial button
            contactSync.setVisibility(View.VISIBLE);
            contactSync.setEnabled(true);
            contactSync.setText(R.string.pbap_sync);
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
