package com.virtual.software.mybuddymakemoney;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentMain.UpdateSetting {
    public static ViewPager viewPager;
    public static MyPagerAdapter pagerAdapter;

    private final String BASE_UNIT = "BaseUnit";
    private static SharedPreferences preferences;
    private static final String PREF_NAME = "your_preference_name";
    private static final String BRAIN_NAME = "BrainName";
    private static final String MONEY_MANAGEMENT_NAME = "MoneyManagementName";
    private static final String CARDS_ELEMENT = "CardsElement";
    private static final String TRACKER_ELEMENT = "TrackerElement";

    private static final String STOP_LOSS = "StopLoss";
    private static final String STOP_PROFIT = "StopProfit";

    private static final String SHIELD_LOSE = "ShieldLoss";
    private static final String SHIELD_WIN = "ShieldWin";
    TextView txtStrategyName, txtMoneyManagementName;

    double baseBetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);

        txtStrategyName = findViewById(R.id.txtStrategyName);
        txtMoneyManagementName = findViewById(R.id.txtMoneyManagementName);


        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        setupViewPager(viewPager);

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        preferences.edit().putString(BASE_UNIT, "0").apply();
//        preferences.edit().putInt(STOP_PROFIT, 0).apply();
//        preferences.edit().putInt(STOP_LOSS, 0).apply();


        // Set the default fragment to Fragment1
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for this example
            }

            @Override
            public void onPageSelected(int position) {
                // Handle page change, if needed
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for this example
            }
        });

    }

    public static List<String> getCards() {

        List<String> cards = new ArrayList<>();
        String jsonList = preferences.getString(CARDS_ELEMENT, "");

        if (!jsonList.equals("")) {
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            Gson gson = new Gson();
            cards = gson.fromJson(jsonList, listType);

        }

        return cards;
    }

    public static List<String> getTrackerList() {

        List<String> list = new ArrayList<>();
        String jsonList = preferences.getString(TRACKER_ELEMENT, "");

        if (!jsonList.equals("")) {
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            Gson gson = new Gson();
            list = gson.fromJson(jsonList, listType);

        }

        return list;
    }

//    public static void setTrackerList(String input) {
//        List<String> list = getTrackerList();
//        list.add(input);
//
//        Gson gson = new Gson();
//        String jsonList = gson.toJson(list);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(TRACKER_ELEMENT, jsonList);
//        editor.apply();
//    }


    public static void setCards(String card) {
        List<String> cards = getCards();
        cards.add(card);

        Gson gson = new Gson();
        String jsonList = gson.toJson(cards);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CARDS_ELEMENT, jsonList);
        editor.apply();
    }


    public static void removeLastCard() {

        List<String> cards = getCards();
        if (!cards.isEmpty()) {

            cards.remove(cards.size() - 1);


            Gson gson = new Gson();
            String jsonList = gson.toJson(cards);


            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(CARDS_ELEMENT, jsonList);
            editor.apply();

        }


    }

//    public static void removeLastItemFromTrackerList() {
//
//        List<String> list = getTrackerList();
//        if (!list.isEmpty()) {
//
//            list.remove(list.size() - 1);
//
//
//            Gson gson = new Gson();
//            String jsonList = gson.toJson(list);
//
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(TRACKER_ELEMENT, jsonList);
//            editor.apply();
//
//        }
//
//
//    }


    public static void clearCards() {
        preferences.edit().putString(CARDS_ELEMENT, "").apply();
        preferences.edit().putString(TRACKER_ELEMENT, "").apply();
    }


    public static void setBrainName(String t) {
        preferences.edit().putString(BRAIN_NAME, t).apply();
    }

    public static void setMoneyManagementName(String t) {
        preferences.edit().putString(MONEY_MANAGEMENT_NAME, t).apply();
    }

    public static String getMoneyManagementName() {
        String mm = preferences.getString(MONEY_MANAGEMENT_NAME, MoneyManagement.OSCAR);
        return mm;
    }


    public static String getBrainName() {
        String brain = preferences.getString(BRAIN_NAME, Brains.CHOP_STREAK);
        return brain;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
    }

    private void showAlertForChangingBrain(String b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You selected " + b + " as brain, you want to proceed?");

        // Set positive button and its click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes button
                // Add your code here for the action to be taken on Yes
                dialog.dismiss(); // Close the dialog

            }
        });

        // Set negative button and its click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No button
                // Add your code here for the action to be taken on No
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void setupViewPager(ViewPager viewPager) {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for your purpose
            }

            @Override
            public void onPageSelected(int position) {
                // The position variable holds the index of the currently selected fragment


                String selectedFragmentName = pagerAdapter.getPageTitle(position).toString();
                if (selectedFragmentName.equals("FragmentBrains")) {
                    txtStrategyName.setText("Brains");
                    txtMoneyManagementName.setText("Select your strategy");
                } else if (selectedFragmentName.equals("FragmentMain")) {
                    txtStrategyName.setText(getBrainName());
                    txtMoneyManagementName.setText(getMoneyManagementName());

                    String baseAmount = preferences.getString(BASE_UNIT, "0.1");
                    int stopProfit = preferences.getInt(STOP_PROFIT, 0);
                    int stopLoss = preferences.getInt(STOP_LOSS, 0);
                    int ShieldLoss = preferences.getInt(SHIELD_LOSE, 0);
                    int ShieldWin = preferences.getInt(SHIELD_WIN, 0);

                    onUpdateBaseBetAmount(baseAmount);
                    onUpdateShield(ShieldLoss, ShieldWin);
                    onUpdateStopProfit(stopProfit);
                    onUpdateStopLoss(stopLoss);

                } else if (selectedFragmentName.equals("FragmentSetting")) {
                    txtStrategyName.setText("Money Management");
                    txtMoneyManagementName.setText("Modify your preferred setup");


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for your purpose
            }
        });
    }


    @Override
    public void onUpdateBaseBetAmount(String newText) {

        TextView txtBaseBet = findViewById(R.id.txtBaseBet);
        if (txtBaseBet != null) {
            txtBaseBet.setText(newText);
        }
    }

    @Override
    public void onUpdateShield(int NoOfLoss, int win) {
        TextView txt = findViewById(R.id.txtShield);
        if (txt != null) {
            txt.setText(NoOfLoss + "-" + win);
        }
    }


    @Override
    public void onUpdateStopLoss(int unit) {
        TextView txt = findViewById(R.id.txtStopLoss);
        if (txt != null) {
            txt.setText(String.valueOf(unit));
        }
    }

    @Override
    public void onUpdateStopProfit(int unit) {
        TextView txt = findViewById(R.id.txtStopProfit);
        if (txt != null) {
            txt.setText(String.valueOf(unit));
        }
    }
}