package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSetting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSetting extends Fragment {
    List<MoneyManagement> moneyManagementsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SharedPreferences preferences;
    private final String PREF_NAME = "your_preference_name";
    private final String BASE_UNIT = "BaseUnit";
    private final String STOP_LOSS = "StopLoss";
    private final String STOP_PROFIT = "StopProfit";
    private final String SHIELD_LOSE = "ShieldLoss";
    private final String SHIELD_WIN = "ShieldWin";

    public FragmentSetting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSetting.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSetting newInstance(String param1, String param2) {
        FragmentSetting fragment = new FragmentSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        preferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        EditText txtBaseBet = view.findViewById(R.id.txtBaseBet);
        EditText txtStopProfitUnit = view.findViewById(R.id.txtStopProfitUnit);
        EditText txtStopLossUnit = view.findViewById(R.id.txtStopLossUnit);
        EditText txtLossShield = view.findViewById(R.id.txtLossShield);
        EditText txtWinShield = view.findViewById(R.id.txtWinShield);


        String b = preferences.getString(BASE_UNIT, "0.1");
        int stopProfit = preferences.getInt(STOP_PROFIT, 0);
        int stopLoss = preferences.getInt(STOP_LOSS, 0);
        int shieldLoss = preferences.getInt(SHIELD_LOSE, 0);
        int shieldWin = preferences.getInt(SHIELD_WIN, 0);

        txtBaseBet.setText(b);
        txtStopProfitUnit.setText(String.valueOf(stopProfit));
        txtStopLossUnit.setText(String.valueOf(stopLoss));
        txtLossShield.setText(String.valueOf(shieldLoss));
        txtWinShield.setText(String.valueOf(shieldWin));

        AppCompatButton btnSaveSetting = view.findViewById(R.id.btnSaveSetting);

        btnSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences.edit().putString(BASE_UNIT, txtBaseBet.getText().toString()).apply();
                preferences.edit().putInt(STOP_PROFIT, Integer.parseInt(txtStopProfitUnit.getText().toString())).apply();
                preferences.edit().putInt(STOP_LOSS, Integer.parseInt(txtStopProfitUnit.getText().toString())).apply();
                preferences.edit().putInt(SHIELD_LOSE, Integer.parseInt(txtLossShield.getText().toString())).apply();
                preferences.edit().putInt(SHIELD_WIN, Integer.parseInt(txtWinShield.getText().toString())).apply();

                hideKeyboard();


                ViewPager pager = MainActivity.viewPager;
                pager.setCurrentItem(1);
            }
        });

        GetMoneyManagementList(view);

        return view;
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void GetMoneyManagementList(View view) {

        ListView listView = view.findViewById(R.id.listViewMoneyManagement);
        moneyManagementsList = MoneyManagement.getMoneyManagementList();

        try {
            updateMOneyManagementListStatus(MainActivity.getMoneyManagementName());
        } catch (Exception e) {

        }


        MoneyManagementAdapter adapter = new MoneyManagementAdapter(getContext(), moneyManagementsList);
        listView.setAdapter(adapter);
        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the item click
                MoneyManagement moneyManagement = moneyManagementsList.get(position);
//                boolean clickedItemState = !brain.getSelected();
                String mmName = moneyManagement.getFieldName();

                MainActivity mainActivity = new MainActivity();
                mainActivity.setMoneyManagementName(mmName);
                updateMOneyManagementListStatus(mmName);


                // For example, you can do something with the clicked item
                // (e.g., display a message, start a new activity, etc.)
                // Toast.makeText(MainActivity.this, "Clicked: " + clickedItem.getFieldName(), Toast.LENGTH_SHORT).show();
                adapter.setSelectedPosition(position);
            }
        });

    }

    private void updateMOneyManagementListStatus(String mm) {

        for (int i = 0; i < moneyManagementsList.size(); i++) {
            MoneyManagement item = moneyManagementsList.get(i);

            if (item.getFieldName().equals(mm)) {

                moneyManagementsList.set(i, new MoneyManagement(item.getFieldName(), item.getDescription(), 20, true));
            } else {

                moneyManagementsList.set(i, new MoneyManagement(item.getFieldName(), item.getDescription(), 20, false));
            }
        }
    }


}