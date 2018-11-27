package of.media.bq.saveData;

import java.util.ArrayList;
import java.util.List;

import of.media.bq.bean.Call;
import of.media.bq.bean.CallLog;
import of.media.bq.bean.Contact;

public class BluetoothData {

    private static boolean btEnabled = false;
    private static boolean hfpConnected = false;

    private static Call currentCall = null;

    private static List<Contact> contactList = new ArrayList<>();
    private static List<CallLog> callLogList = new ArrayList<>();

    public static boolean getBTEnabled(){
        return btEnabled;
    }

    public static void setBTEnabled(boolean enabled){
        btEnabled = enabled;
    }

    public static boolean getHfpConnected(){
        return hfpConnected;
    }

    public static void setHfpConnected(boolean enabled){
        hfpConnected = enabled;
    }

    public static Call getCurrentCall() {
        return currentCall;
    }

    public static void setCurrentCall(Call call) {
        currentCall = call;
    }

    public static List<Contact> getContactList() {
        return contactList;
    }

    public static void setContactList(List<Contact> contacts) {
        contactList = contacts;
    }

    public static List<CallLog> getCallLogList() {
        return callLogList;
    }

    public static List<CallLog> getMissedCallList() {
        List<CallLog> missedCallList = new ArrayList<>();
        for(CallLog callLog:callLogList){
            if (callLog.getType() == android.provider.CallLog.Calls.MISSED_TYPE) {
                missedCallList.add(callLog);
            }
        }
        return missedCallList;
    }

    public static void setCallLogList(List<CallLog> callLogs) {
        callLogList = callLogs;
    }

}
