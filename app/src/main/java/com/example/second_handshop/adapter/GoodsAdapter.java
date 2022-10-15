package com.example.second_handshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.second_handshop.GetGoodsDetail;

import java.util.List;

public class GoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<GetGoodsDetail> mGoodsList;

    public GoodsAdapter(Context mContext, List<GetGoodsDetail> mGoodsList) {
        this.mContext = mContext;
        this.mGoodsList = mGoodsList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
