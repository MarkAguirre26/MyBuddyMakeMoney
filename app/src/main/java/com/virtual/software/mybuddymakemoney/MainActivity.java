package com.virtual.software.mybuddymakemoney;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    private static SharedPreferences preferences;
    private static final String PREF_NAME = "your_preference_name";
    private static final String BRAIN_NAME = "BrainName";
    private static final String MONEY_MANAGEMENT_NAME = "MoneyManagementName";
    private static final String CARDS_ELEMENT = "CardsElement";

    TextView txtStrategyName, txtMoneyManagementName;


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
        // Assuming you have a List<String> myList
        List<String> cards = new ArrayList<>();

        String jsonList = preferences.getString(CARDS_ELEMENT, "");
        System.out.println("jsonList:"+jsonList);

        if (!jsonList.equals("")) {

            Type listType = new TypeToken<List<String>>() {
            }.getType();
            Gson gson = new Gson();
            cards = gson.fromJson(jsonList, listType);
        }


        return cards;
    }

    public static void setCards(String card) {
        List<String> cards = getCards();
        cards.add(card);


        Gson gson = new Gson();


        String jsonList = gson.toJson(cards);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CARDS_ELEMENT, jsonList);
        editor.apply();
    }

    public static void clearCards() {
        preferences.edit().putString(CARDS_ELEMENT, "").apply();
    }

    public void setBrainName(String t) {
        preferences.edit().putString(BRAIN_NAME, t).apply();
    }

    public void setMoneyManagementName(String t) {
        preferences.edit().putString(MONEY_MANAGEMENT_NAME, t).apply();
    }

    public String getMoneyManagementName() {
        String mm = preferences.getString(MONEY_MANAGEMENT_NAME, MoneyManagement.OSCAR);
        return mm;
    }


    public String getBrainName() {
        String brain = preferences.getString(BRAIN_NAME, Brains.CHOP_STREAK);
        return brain;
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
                    txtMoneyManagementName.setText("Select you strategy");
                } else if (selectedFragmentName.equals("FragmentMain")) {
                    txtStrategyName.setText(getBrainName());
                    txtMoneyManagementName.setText(getMoneyManagementName());
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

    private void updateSelectedFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        if (fragment instanceof FragmentMain) {
            ((FragmentMain) fragment).updateSelectedFragment(pagerAdapter.getSelectedFragmentName());
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.viewPager, fragment)
                .commit();
    }


//    private class MyPagerAdapter extends FragmentPagerAdapter {
//        MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position) {
//                case 0:
//                    return new FragmentBrains();
//                case 1:
//                    FragmentMain fragmentMain = new FragmentMain();
//                    return fragmentMain;
//                case 2:
//                    return new FragmentSetting();
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }


}