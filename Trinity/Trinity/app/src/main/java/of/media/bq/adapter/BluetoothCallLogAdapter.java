package of.media.bq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import of.media.bq.R;
import of.media.bq.bean.CallLog;

import java.util.List;

public class BluetoothCallLogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CallLog> callLog;
    private int selectItem = -1;
    
    public BluetoothCallLogAdapter(Context context, List<CallLog> callLog) {
        
        this.mInflater = LayoutInflater.from(context);
        this.callLog = callLog;
    }
    
    @Override
    public int getCount() {
        return callLog.size();
    }
    
    @Override
    public Object getItem(int position) {
        return callLog.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void setSelectItem(int item) {
        selectItem = item;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bluetooth_calllog_item, parent, false);
            holder = new ViewHolder();
            
            holder.name = convertView.findViewById(R.id.calllog_name);
            //holder.duration = convertView.findViewById(R.id.calllog_duration);
            holder.number = convertView.findViewById(R.id.calllog_number);
            holder.timestamp = convertView.findViewById(R.id.calllog_timestamp);
            
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        CallLog callLog = this.callLog.get(position);
        holder.name.setText(callLog.getName());
        //holder.duration.setText(callLog.getDuration());
        holder.number.setText(callLog.getNumber());
        holder.timestamp.setText(callLog.getTimestamp());
        
        if (callLog.getType() == android.provider.CallLog.Calls.MISSED_TYPE) {
            holder.name.setTextColor(android.graphics.Color.RED);
        }
        
        return convertView;
    }
    
    private class ViewHolder {
        TextView name;
        //TextView duration;
        TextView number;
        TextView timestamp;
    }
    
}