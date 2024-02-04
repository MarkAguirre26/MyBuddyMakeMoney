package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BrainsAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private Context context;
    private List<Brains> itemList;
    private String selectedFragmentName;  // Add this variable

    public BrainsAdapter(Context context, List<Brains> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Brains getItem(int position) {

        return itemList.get(position);
    }
    public String getSelectedFragmentName() {
        return selectedFragmentName;
    }

    public void setSelectedFragmentName(String name) {
        selectedFragmentName = name;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_brains, null);
        }

        TextView fieldNameTextView = convertView.findViewById(R.id.txtBrainName);
        RadioButton radioButton = convertView.findViewById(R.id.rbBrain);


        Brains currentItem = itemList.get(position);


        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getSelected()) {
                selectedPosition = i;
                break;
            }
        }

        radioButton.setChecked(position == selectedPosition);
        fieldNameTextView.setText(currentItem.getName());


        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
