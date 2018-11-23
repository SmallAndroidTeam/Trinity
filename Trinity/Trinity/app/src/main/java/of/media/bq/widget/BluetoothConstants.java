package of.media.bq.widget;

/**
 * Various strings used to identify operations or data in the Bluetooth service
 * mainly used in Intents passed between Activities and the Service.
 */
public interface BluetoothConstants {

    /* Identifies an Intent which send to Activity/Service */
    String INTENT_TO_ACTIVITY = "com.ofilm.bluetooth.IntentToActivity";
    String INTENT_TO_SERVICE  = "com.ofilm.bluetooth.IntentToService";

    /* Identifiers for extra data on Intents broadcast to the Activity */
    String ARG1 = "bluetooth.arg1";
    String ARG2 = "bluetooth.arg2";

    /*
     * Activity ==> Service
     */
    String HFP_DIAL_REQ             = "hfp_dial_req";
    String PBAP_DOWNLOAD_PHONEBOOK  = "pbap_download_phonebook";
    String PBAP_DOWNLOAD_CALLLOG    = "pbap_download_calllog";

    /*
     * Service ==> Activity
     */
    String BT_STATUS_CHANGE       = "bt_status_change";
    String HFP_STATUS_CHANGE      = "hfp_status_change";
    String PBAP_DOWNLOAD_UPDATE   = "pbap_download_update";


    String STATUS_ON   = "status_on";
    String STATUS_OFF  = "status_off";

    String DOWNLOAD_PB_START     = "download_pb_start";
    String DOWNLOAD_Pb_FINISHED  = "download_pb_finished";
    String DOWNLOAD_CL_START     = "download_cl_start";
    String DOWNLOAD_CL_FINISHED  = "download_cl_finished";


    /*
     *  Common used by Activity
     */
    int BT_CONNECTED     = 0;
    int BT_DISCONNECTED  = 1;
    int HFP_CONNECTED    = 2;
    int HFP_DISCONNECTED = 3;
    int PBAP_PB_START    = 4;
    int PBAP_CL_START    = 5;
    int PBAP_PB_DOWN     = 6;
    int PBAP_CL_DOWN     = 7;

}
