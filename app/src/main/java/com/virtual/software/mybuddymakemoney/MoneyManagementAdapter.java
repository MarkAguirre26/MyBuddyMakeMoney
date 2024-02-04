package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class MoneyManagementAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private Context context;
    private List<MoneyManagement> itemList;

    public MoneyManagementAdapter(Context context, List<MoneyManagement> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public MoneyManagement getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_setting, null);
        }

        TextView fieldNameTextView = convertView.findViewById(R.id.fieldNameTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        RadioButton radioButton  = convertView.findViewById(R.id.radioButton);


        MoneyManagement currentItem = itemList.get(position);



        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                selectedPosition = i;
                break;
            }
        }

        radioButton.setChecked(position == selectedPosition);

        fieldNameTextView.setText(currentItem.getFieldName());
        descriptionTextView.setText(currentItem.getDescription());


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
