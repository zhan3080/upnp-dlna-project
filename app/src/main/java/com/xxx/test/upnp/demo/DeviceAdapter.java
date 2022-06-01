package com.xxx.test.upnp.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxx.test.upnp.demo.dlna.Device.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private static final String TAG = "DeviceAdapter";
    private Context mContext;
    private List<DeviceBean> mDeviceList = new ArrayList<>();
    private DeviceBean mSelectInfo;

    public DeviceAdapter(Context context, List<DeviceBean> list) {
        super();
        Log.i(TAG,"DeviceAdapter size:" + list.size());
        mContext = context;
        mDeviceList = list;
    }

    public void setSelectInfo(DeviceBean bean){
        Log.i(TAG,"setSelectInfo bean:" + bean);
        mSelectInfo = bean;
    }

    @Override
    public int getCount() {
        Log.i(TAG,"getCount count:" + mDeviceList.size());
        return mDeviceList == null ? 0 : mDeviceList.size();
    }

    @Override
    public Object getItem(int i) {
        Log.i(TAG,"getItem i:" + i);
        return mDeviceList == null ? 0 : mDeviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i(TAG,"getView i:" + i + ", view:" + view + ",viewGroup:" + viewGroup);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder(mContext);
            view = holder.mRootView;
            view.setTag(holder);
            Log.i(TAG,"getView setTag:" + holder);
        } else {
            holder = (ViewHolder) view.getTag();
            Log.i(TAG,"getView getTag:" + holder);
            if (holder == null) {
                holder = new ViewHolder(mContext);
                view = holder.mRootView;
                view.setTag(holder);
            }
        }
        DeviceBean info = null;
        try {
            info = mDeviceList.get(i);
        } catch (Exception e) {
            Log.w(TAG, e);
            return view;
        }
        holder.mDeviceName.setText(info.getName());
        if(mSelectInfo != null && mSelectInfo.getName().equals(info.getName())){
            holder.mRootView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.mRootView.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }


    static class ViewHolder {
        public RelativeLayout mRootView;
        public LinearLayout mDeviceView;
        public TextView mDeviceName;
        public ViewHolder(Context context){
            mRootView = new RelativeLayout(context);
            mRootView.setLayoutParams(new ListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mDeviceView = new LinearLayout(context);
            mDeviceView.setOrientation(LinearLayout.HORIZONTAL);
            mDeviceView.setLayoutParams(new ListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            mRootView.addView(mDeviceView);

            mDeviceName = new TextView(context);
            mDeviceName.setPadding(20,20,20,20);
            mDeviceName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mDeviceName.setTextColor(Color.WHITE);
            mDeviceName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams namePara = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDeviceView.addView(mDeviceName, namePara);
        }
    }
}
