package com.virtual.software.mybuddymakemoney;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    List<String> winLossListForMoneyManagement = new ArrayList<>();
    int selectBrainFromBrainPage = 0;
    public Boolean isLoaded = false;
    public Boolean isViewPagerSLided = false;
    private static final String TAG = "MainActivityBackgroundChecker";
    public BackgroundChecker backgroundChecker;
    MoneyManagementDatabaseHelper moneyManagementDatabaseHelper;
    BrainsDatabaseHelper brainsDatabaseHelper;
    public CustomViewPager viewPager;
    public static MyPagerAdapter pagerAdapter;
    RadioGroup radioGroup, radioGroupBrains;
    private int selectedPageIndex = 0;
    List<MoneyManagementModel> moneyManagementList;
    List<Brains> brainsList;

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
    private static final String MM_ID = "MM_ID";
    private int currentMoneyManagementID;
    TextView txtBrain, txtMoneyManagementName;

    EditText txtBaseBetAmount, txtStopProfitUnit, txtStopLossUnit, txtLossShield, txtWinShield;
    double baseBetAmount;

    TextView txtPrediction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TableLayout tableLayout;
    double currentBetAmount = 0;
    List<Double> betsListm;
    List<String> winLoseList;
    boolean isUndo = false;
    LinearLayout NavBarLinearLayout;
    TextView txtPos1, txtBase, txtStep2, txtStep3, txtStep4, txtStep5, txtStep6;

    TextView txtStep1_koi, txtStep2_koi, txtStep3_koi, txtStep4_koi, txtStep5_koi, txtStep6_koi,
            txtStep7_koi, txtStep8_koi, txtStep9_koi, txtStep10_koi, txtStep11_koi, txtStep12_koi,
            txtStep13_koi, txtStep14_koi, txtStep15_koi, txtStep16_koi, txtStep17_koi, txtStep18_koi,
            txtStep19_koi, txtStep20_koi, txtStep21_koi, txtStep22_koi, txtStep23_koi, txtStep24_koi;
    TextView txtHand, txtPlayerHandCount, txtbankerHandCount, txtBetAmount, txtMessage, txtProfit, txtProfitByUnit, txtStopProfit, txtStopLoss, txtShield;
    LinearLayout profitPanelLayout;
    private final String BET_AMOUNT = "BetAmount";
    LinearLayout trackerPanel;
    HorizontalScrollView trackerPanelHorizontalScroller;
    TextView txtSkip;
    CardDataSource cardDatabaseHelper;


    private int win = 0;
    private int lose = 0;
    List<Double> betsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moneyManagementDatabaseHelper = new MoneyManagementDatabaseHelper(this);
        brainsDatabaseHelper = new BrainsDatabaseHelper(this);

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        NavBarLinearLayout = findViewById(R.id.NavBarLinearLayout);
//        NavBarLinearLayout.setVisibility(View.GONE);

        txtBrain = findViewById(R.id.txtStrategyName);
        txtMoneyManagementName = findViewById(R.id.txtMoneyManagementName);


        int[] layouts = {R.layout.brain_layout, R.layout.main_layout, R.layout.setting_layout}; // Add layout IDs for each page
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new MyPagerAdapter(getApplicationContext(), layouts);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, true);
        setupViewPager(viewPager);

        if (moneyManagementDatabaseHelper.getFirstItem() == null) {
            addAllMoneyManagementItemsToDaTabase();
        }

        if (brainsDatabaseHelper.getFirstItem() == null) {
            addAllBrainsItemsToDaTabase();
        }

        setTheViewOfBrainAndMoneyManagementHeaderText();


//        ---------this is to initialize the components under main layout------------------------------------------------------
        // Create an instance of BackgroundChecker
        backgroundChecker = new BackgroundChecker(this);
        // Start background checking
        backgroundChecker.startChecking();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start background checking if it's not already started
        if (backgroundChecker != null) {
            backgroundChecker.startChecking();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop background checking to conserve resources
        if (backgroundChecker != null) {
            backgroundChecker.stopChecking();
        }
    }


    private void setTheViewOfBrainAndMoneyManagementHeaderText() {

        MoneyManagementModel moneyManagementModel = moneyManagementDatabaseHelper.getSelectedItem();
        if (moneyManagementModel != null) {
            String currentMoneyManagement = moneyManagementModel.getName();
            txtMoneyManagementName.setText(currentMoneyManagement);
        } else {
            moneyManagementModel = moneyManagementDatabaseHelper.getFirstItem();
            String currentMoneyManagement = moneyManagementModel.getName();
            txtMoneyManagementName.setText(currentMoneyManagement);
        }
//--------------------------------
        Brains brain = brainsDatabaseHelper.getSelectedItem();
        if (brain != null) {
            String currentBrain = brain.getName();
            txtBrain.setText(currentBrain);
            txtBrain.setTag(brain.getId());
        } else {

            brain = brainsDatabaseHelper.getFirstItem();
            String currentBrain = brain.getName();
            txtBrain.setText(currentBrain);
            txtBrain.setTag(brain.getId());
        }


    }

    public void initializeComponents() {
//        try {


            isLoaded = true;
            cardDatabaseHelper = new CardDataSource(this);
            betsList = new ArrayList<>();
            winLoseList = new ArrayList<>();
            trackerPanel = findViewById(R.id.trackerPanel);
            trackerPanelHorizontalScroller = findViewById(R.id.trackerPanelHorizontalScroller);
            tableLayout = findViewById(R.id.tableLayoutBeadRoad);
            txtPrediction = findViewById(R.id.txtPrediction);
            txtSkip = findViewById(R.id.txtSkip);
            txtPlayerHandCount = findViewById(R.id.txtPlayerHandCount);
            txtbankerHandCount = findViewById(R.id.txtbankerHandCount);
            txtHand = findViewById(R.id.txtHand);
            txtBetAmount = findViewById(R.id.txtBetAmount);
            txtMessage = findViewById(R.id.txtMessage);
            txtProfit = findViewById(R.id.txtProfit);
            txtProfitByUnit = findViewById(R.id.txtProfitByUnit);
            profitPanelLayout = findViewById(R.id.winAndLossLinearLayout);
            txtStopLoss = findViewById(R.id.txtStopLoss);
            txtStopProfit = findViewById(R.id.txtStopProfit);
            txtShield = findViewById(R.id.txtShield);
            preferences = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


            populateFieldsInMainPage();


            AppCompatButton btnPlayer = findViewById(R.id.btnPlayer);
            AppCompatButton btnReset = findViewById(R.id.btnReset);
            AppCompatButton btnBanker = findViewById(R.id.btnBanker);
            AppCompatButton btnSkip = findViewById(R.id.btnSkip);
            AppCompatButton btnUndo = findViewById(R.id.btnUndo);

            //get the previous data
            //  setBeadRoadView(6, 20, 50);


            ResetAll();


            btnPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playClickedSound();
                    isUndo = false;

                    saveCardItem("P");


                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playClickedSound();
                    isUndo = false;

                    ResetAll();

                }
            });


            btnBanker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    playClickedSound();
                    isUndo = false;
                    saveCardItem("B");


                }
            });


            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                setSkip(true);
                    isUndo = false;
                    playClickedSound();

                    if (!txtSkip.getText().toString().equalsIgnoreCase("Yes")) {
                        setSkip(true);
                    } else {
                        setSkip(false);
                    }
                }
            });

            btnUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isUndo = true;

                    playClickedSound();
                    List<Card> allCards = cardDatabaseHelper.getAllCards();


                    if (!allCards.isEmpty()) {
                        Card lastCard = allCards.get(allCards.size() - 1);
                        cardDatabaseHelper.deleteCard(lastCard.getId());
// Get the index of the last child view
                        int lastIndex = trackerPanel.getChildCount() - 1;

                        if (lastIndex >= 0) {
                            // Remove the last child view
                            trackerPanel.removeViewAt(lastIndex);

                        }

                        removelastItemFromOrcLevel();


                    } else {
                        ResetAll();
                    }

                    try {
                        setBeadRoadView(6, 20, 20);
                    } catch (Exception exception) {

                    }

                }
            });
//        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }

    }

    public void populateFieldsInMainPage() {

        int stopProfit = preferences.getInt(STOP_PROFIT, 0);
        int stopLoss = preferences.getInt(STOP_LOSS, 0);
        int ShieldLoss = preferences.getInt(SHIELD_LOSE, 0);
        int ShieldWin = preferences.getInt(SHIELD_WIN, 0);
        String b = preferences.getString(BET_AMOUNT, "0.1");

        txtBetAmount.setText(b);
        currentBetAmount = Double.parseDouble(b);
        baseBetAmount = currentBetAmount;
        txtStopProfit.setText(String.valueOf(stopProfit));
        txtStopLoss.setText(String.valueOf(stopLoss));
        txtShield.setText(ShieldLoss + "-" + ShieldWin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (backgroundChecker != null) {
            backgroundChecker.stopChecking();
            backgroundChecker = null;
        }
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


//    public static void setCards(String card) {
//        List<String> cards = getCards();
//        cards.add(card);
//
//        Gson gson = new Gson();
//        String jsonList = gson.toJson(cards);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(CARDS_ELEMENT, jsonList);
//        editor.apply();
//    }


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


    @Override
    public void onBackPressed() {


        if (viewPager.getCurrentItem() != 1) {
            viewPager.setCurrentItem(1, true);
        } else {
            super.onBackPressed();
        }

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

    int previousScrollPosition = 0;

    private void setupViewPager(ViewPager viewPager) {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                System.out.println("position:"+position);

//                // Not needed for your purpose
//                // This method will be invoked when the ViewPager is scrolled.
//                // You can determine the scroll direction here.
//                int currentScrollPosition = positionOffsetPixels;
//                if (currentScrollPosition > previousScrollPosition) {
//                    // Scroll down
//                } else if (currentScrollPosition < previousScrollPosition) {
//                    // Scroll up
//                    viewPager.setCurrentItem(1);
//                }
//                previousScrollPosition = currentScrollPosition;

            }

            @Override
            public void onPageSelected(int position) {
                // The position variable holds the index of the currently selected fragment
                isViewPagerSLided = true;
                selectedPageIndex = position;


                switch (selectedPageIndex) {
                    case 0:
                        txtBrain.setText("Brains");
                        txtMoneyManagementName.setText("Select your strategy");
                        GetBrainList();

                        break;
                    case 1:

                        if (!isLoaded) {
                            initializeComponents();
                        }

                        List<Card> cards = cardDatabaseHelper.getAllCards();
                        int currentId = Integer.parseInt(txtBrain.getTag().toString());

                        if (!cards.isEmpty() && selectBrainFromBrainPage != currentId) {
                            confirmDialog(currentId);
                        } else {
                            setMoneyManagementView();
                            setTheViewOfBrainAndMoneyManagementHeaderText();
                        }


                        break;
                    case 2:
                        NavBarLinearLayout.setVisibility(View.VISIBLE);
                        txtBrain.setText("Money Management");
                        txtMoneyManagementName.setText("Modify your preferred setup");
                        initializeSettingComponents();
                        break;
                    case 3:
                        viewPager.setCurrentItem(3);
                        break;

                    default:
                        // Handle any other cases here
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for your purpose

            }
        });


    }


    private void initializeSettingComponents() {

        txtBaseBetAmount = findViewById(R.id.txtBaseBet);
        txtStopProfitUnit = findViewById(R.id.txtStopProfitUnit);
        txtStopLossUnit = findViewById(R.id.txtStopLossUnit);
        txtStopProfitUnit = findViewById(R.id.txtStopProfitUnit);
        txtWinShield = findViewById(R.id.txtWinShield);
        txtLossShield = findViewById(R.id.txtLossShield);


        String b = preferences.getString(BET_AMOUNT, "0.1");
        int stopProfit = preferences.getInt(STOP_PROFIT, 0);
        int stopLoss = preferences.getInt(STOP_LOSS, 0);
        int shieldLoss = preferences.getInt(SHIELD_LOSE, 0);
        int shieldWin = preferences.getInt(SHIELD_WIN, 0);

        txtBaseBetAmount.setText(b);
        baseBetAmount = Double.parseDouble(b);

        txtStopProfitUnit.setText(String.valueOf(stopProfit));
        txtStopLossUnit.setText(String.valueOf(stopLoss));
        txtLossShield.setText(String.valueOf(shieldLoss));
        txtWinShield.setText(String.valueOf(shieldWin));

        GetMoneyManagementList();

        txtBaseBetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!txtBaseBetAmount.getText().toString().isEmpty()) {
                    preferences.edit().putString(BET_AMOUNT, txtBaseBetAmount.getText().toString()).apply();
                }

            }
        });
        txtStopProfitUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!txtStopProfitUnit.getText().toString().isEmpty()) {
                    preferences.edit().putInt(STOP_PROFIT, Integer.parseInt(txtStopProfitUnit.getText().toString())).apply();
                }

            }
        });
        txtStopLossUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!txtStopLossUnit.getText().toString().isEmpty()) {
                    preferences.edit().putInt(STOP_LOSS, Integer.parseInt(txtStopLossUnit.getText().toString())).apply();
                }

            }
        });
        txtLossShield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!txtLossShield.getText().toString().isEmpty()) {
                    preferences.edit().putInt(SHIELD_LOSE, Integer.parseInt(txtLossShield.getText().toString())).apply();
                }

            }
        });
        txtWinShield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!txtWinShield.getText().toString().isEmpty()) {
                    preferences.edit().putInt(SHIELD_WIN, Integer.parseInt(txtWinShield.getText().toString())).apply();
                }

            }
        });


    }

    private void confirmDialog(int brainId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Seems you want to change the strategy, This will reset to default. Do you want to proceed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

//                        updateBrainsListStatus(id);
                        setTheViewOfBrainAndMoneyManagementHeaderText();
                        ResetAll();
                        // Handle Yes action
                        dialog.dismiss(); // Dismiss the dialog
//                        viewPager.setCurrentItem(1, true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        updateBrainsListStatus(brainId);
                        setTheViewOfBrainAndMoneyManagementHeaderText();

                        // Handle No action
                        dialog.dismiss(); // Dismiss the dialog

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setMoneyManagementView() {


        MoneyManagementModel moneyManagementModel = moneyManagementDatabaseHelper.getSelectedItem();
        String currentMoneyManagement = moneyManagementModel.getName();


        if (!currentMoneyManagement.isEmpty()) {
            txtMoneyManagementName.setText(currentMoneyManagement);
        }


        profitPanelLayout.removeAllViews();

        switch (currentMoneyManagement) {
            case MoneyManagement.ORC:

                LinearLayout layout_orc = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.orc_layout, null);
                profitPanelLayout.addView(layout_orc);

                txtPos1 = findViewById(R.id.txtPos1);
                txtBase = findViewById(R.id.txtBase);
                txtStep2 = findViewById(R.id.txtStep2);
                txtStep3 = findViewById(R.id.txtStep3);
                txtStep4 = findViewById(R.id.txtStep4);
                txtStep5 = findViewById(R.id.txtStep5);
                txtStep6 = findViewById(R.id.txtStep6);

                OrcMoneyManagement.setBetAmount(baseBetAmount);

                List<BetAmountModel> orcbetAmounts = OrcMoneyManagement.getBetAmountList();
                txtPos1.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.POS1).amount));
                txtBase.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.BASE).amount));
                txtStep2.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.STEP1).amount));
                txtStep3.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.STEP2).amount));
                txtStep4.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.STEP3).amount));
                txtStep5.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.STEP4).amount));
                txtStep6.setText(String.valueOf(orcbetAmounts.get(OrcMoneyManagement.STEP5).amount));

                break;
            case MoneyManagement.KOI:
                LinearLayout layout_koi = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.koi_layout, null);
                profitPanelLayout.addView(layout_koi);

                txtPos1 = findViewById(R.id.txtPos1);
                txtBase = findViewById(R.id.txtBase);
                txtStep2 = findViewById(R.id.txtStep2);
                txtStep3 = findViewById(R.id.txtStep3);
                txtStep4 = findViewById(R.id.txtStep4);
                txtStep5 = findViewById(R.id.txtStep5);
                txtStep6 = findViewById(R.id.txtStep6);


                txtStep1_koi = findViewById(R.id.txtStep1_koi);
                txtStep2_koi = findViewById(R.id.txtStep2_koi);
                txtStep3_koi = findViewById(R.id.txtStep3_koi);
                txtStep4_koi = findViewById(R.id.txtStep4_koi);
                txtStep5_koi = findViewById(R.id.txtStep5_koi);
                txtStep6_koi = findViewById(R.id.txtStep6_koi);
                txtStep7_koi = findViewById(R.id.txtStep7_koi);
                txtStep8_koi = findViewById(R.id.txtStep8_koi);
                txtStep9_koi = findViewById(R.id.txtStep9_koi);
                txtStep10_koi = findViewById(R.id.txtStep10_koi);
                txtStep11_koi = findViewById(R.id.txtStep11_koi);
                txtStep12_koi = findViewById(R.id.txtStep12_koi);
                txtStep13_koi = findViewById(R.id.txtStep13_koi);
                txtStep14_koi = findViewById(R.id.txtStep14_koi);
                txtStep15_koi = findViewById(R.id.txtStep15_koi);
                txtStep16_koi = findViewById(R.id.txtStep16_koi);
                txtStep17_koi = findViewById(R.id.txtStep17_koi);
                txtStep18_koi = findViewById(R.id.txtStep18_koi);
                txtStep19_koi = findViewById(R.id.txtStep19_koi);
                txtStep20_koi = findViewById(R.id.txtStep20_koi);
                txtStep21_koi = findViewById(R.id.txtStep21_koi);
                txtStep22_koi = findViewById(R.id.txtStep22_koi);
                txtStep23_koi = findViewById(R.id.txtStep23_koi);
                txtStep24_koi = findViewById(R.id.txtStep24_koi);


                break;
            default:
                break;
        }

    }

    public void ResetAll() {
        try {


            betsList = new ArrayList<>();
            winLoseList = new ArrayList<>();
            winLossListForMoneyManagement = new ArrayList<>();
            orcCurrentLevelList = new ArrayList<>();
            currentPosition = "Base";

            cardDatabaseHelper.deleteAllCards();


            betsList.clear();
            trackerPanel.removeAllViews();
            profitPanelLayout.removeAllViews();
            txtMessage.setText("");
            txtBetAmount.setText(String.valueOf(baseBetAmount));
            txtProfitByUnit.setText("0");
            txtProfit.setText("0");


            txtStopProfit.setBackgroundColor(getColor(R.color.white));
            txtStopLoss.setBackgroundColor(getColor(R.color.white));
            txtProfit.setBackgroundColor(getColor(R.color.white));


            SetPredictionView("Wait");
            setMoneyManagementView();
            setTheViewOfBrainAndMoneyManagementHeaderText();

            setBeadRoadView(6, 20, 50);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void saveCardItem(String cardItem) {

        int count = cardDatabaseHelper.getAllCardNames().size();
//-------------------------------------------------------------------


        String selectedBrain = txtBrain.getText().toString();
        if (selectedBrain.isEmpty()) {
            System.out.println("selectedBrain is null or empty. No brain applied.");
        } else {

            String isSkip = txtSkip.getText().toString();
            String isWait = txtPrediction.getText().toString();

            isSkip = isSkip.equalsIgnoreCase("Yes") ? "Yes" : "No";
            isWait = isWait.equalsIgnoreCase("Wait") ? "Yes" : "No";


            switch (selectedBrain) {
                case Brains.ZIGZAG_STREAK:
                case Brains.TIAMAT:

                    if (count == 0) {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), selectedBrain, "Yes", "Yes", "Yes"));
                    } else {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), selectedBrain, "No", isSkip, isWait));
                    }


                    break;
                case Brains.CHOP_STREAK:
                case Brains.STAR_BLAZE:


                    if (count < 3) {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), selectedBrain, "Yes", "Yes", "Yes"));
                    } else {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), selectedBrain, "No", isSkip, isWait));
                    }


                    break;
                case Brains.BODY_GUARD:
                    if (count < 2) {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), selectedBrain, "Yes", "Yes", "Yes"));
                    } else {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), selectedBrain, "No", isSkip, isWait));
                    }


                    break;

                default:
                    System.out.println("Unknown Brain type '" + selectedBrain + "'. No Brain applied.");
                    break;
            }
        }


//-------------------------------------------------------------------
        setBeadRoadView(6, 20, 50);
    }


    private void setSkip(boolean b) {
        if (b) {
            txtSkip.setText("Yes");
            txtSkip.setTextColor(getColor(R.color.white));
            txtSkip.setBackgroundColor(getResources().getColor(R.color.blue_dark));
        } else {
            txtSkip.setText("No");
            txtSkip.setTextColor(getColor(R.color.light_black));
            txtSkip.setBackgroundColor(getResources().getColor(R.color.white));


        }

    }


    private String getTrackerView(Card card) {

        String result = "ala";
        String cardName = card.getName();
        String prediction = card.getPrediction();
        String isInitialize = card.getInitialize();
        String isSkip = card.getSkip();
        String isWait = card.getWait();


        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                50, 50);
        layoutParams.setMargins(1, 1, 1, 1);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource


        if (!isInitialize.equalsIgnoreCase("Yes")) {
            if (!isWait.equalsIgnoreCase("Yes")) {
                String resultText = "";
                int backgroundResource;

                if (cardName.equalsIgnoreCase(prediction)) {
                    resultText = "W";
                    backgroundResource = isSkip.equalsIgnoreCase("Yes") ? R.drawable.button_skip : R.drawable.win;

                    if (!isSkip.equalsIgnoreCase("Yes")) {
                        winLossListForMoneyManagement.add(resultText);
                        processMoneyMangement(resultText);
                    }

                } else {
                    resultText = "L";
                    backgroundResource = isSkip.equalsIgnoreCase("Yes") ? R.drawable.button_skip : R.drawable.button_banker;
                    if (!isSkip.equalsIgnoreCase("Yes")) {
                        winLossListForMoneyManagement.add(resultText);
                        processMoneyMangement(resultText);
                    }
                }

                textView.setText(resultText);
                textView.setBackground(getResources().getDrawable(backgroundResource));
                result = resultText;
                setSkip(false);
            } else {
                textView.setText("*");
                textView.setBackground(getResources().getDrawable(R.drawable.button_player));

            }
        } else {
            textView.setText("*");
            textView.setBackground(getResources().getDrawable(R.drawable.button_player));
        }


        trackerPanel.addView(textView);

        scrollToEnd();
        return result;
    }

    private void scrollToEnd() {
        // Scroll to the end
        trackerPanelHorizontalScroller.post(new Runnable() {
            @Override
            public void run() {
                trackerPanelHorizontalScroller.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }


    private void setBetsView() {


//        double total = sumOfDoubles(betsList);
//
//        if (total > 0) {
//
//            String p = txtProfit.getText().toString();
//            double newProfit = (Double.parseDouble(p) + total);
//            txtProfit.setText(String.valueOf(roundToOneDecimalPlace(newProfit)));
//            txtProfit.setBackgroundColor(getContext().getColor(R.color.win));
//
//
//        } else if (total < 0) {
//
//            txtProfit.setText(String.valueOf(total));
//            txtProfit.setBackgroundColor(getContext().getColor(R.color.banker));
//
//        } else {
//
//            txtProfit.setBackgroundColor(getContext().getColor(R.color.white));
//        }
//
//        double winPercentage = calculateProfitByUnit();
//        txtProfitByUnit.setText(String.valueOf(roundToOneDecimalPlace(winPercentage)));
//        ---------------------------------------------------------------------------------

        profitPanelLayout.removeAllViews();
        for (int i = 0; i < betsList.size(); i++) {

            System.out.println(betsList.get(i).toString());

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // width
                    60);
            layoutParams.setMargins(1, 1, 1, 1);
            textView.setLayoutParams(layoutParams);


            if (betsList.get(i) > 0) {

                textView.setBackgroundColor(getColor(R.color.win));
            } else {
                textView.setBackgroundColor(getColor(R.color.banker));
            }

            textView.setPadding(5, 0, 5, 0);
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setText(String.valueOf(roundToOneDecimalPlace(betsList.get(i))));
            textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource
            textView.setTypeface(null, android.graphics.Typeface.BOLD);

            profitPanelLayout.addView(textView);
        }
        scrollToEnd();


    }


    private void fillTable(String[][] table, List<String> listOfItemFromRoad) {
        int numRows = table.length;
        int numColumns = table[0].length;
        int value = 0;

        for (int col = 0; col < numColumns; col++) {
            for (int row = 0; row < numRows; row++) {

                if (value < listOfItemFromRoad.size()) {
                    table[row][col] = listOfItemFromRoad.get(value++);

                } else {

                    // If the list is exhausted, fill the remaining cells with null
                    table[row][col] = null;

                }
            }
        }
    }

    private void playClickedSound() {

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clicked_sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Release the MediaPlayer object after the sound has finished playing
            }
        });

    }

    public void setBeadRoadView(int numRows, int numColumns, int textViewSize) {


//        cardDatabaseHelper.open();
        List<String> list = cardDatabaseHelper.getAllCardNames();
//        cardDatabaseHelper.close();

        String[][] table = new String[numRows][numColumns];
        fillTable(table, list);

        // Check if the number of child views matches the expected count
        if (tableLayout.getChildCount() != numRows) {
            tableLayout.removeAllViews(); // Only remove views if needed
        }


        for (int i = 0; i < numRows; i++) {
            TableRow tableRow;

            // Reuse existing TableRow if it exists
            if (i < tableLayout.getChildCount()) {
                tableRow = (TableRow) tableLayout.getChildAt(i);
                tableRow.removeAllViews(); // Remove child views if needed
            } else {
                tableRow = new TableRow(this);
            }

            for (int j = 0; j < numColumns; j++) {
                TextView textView;

                // Reuse existing TextView if it exists
                if (j < tableRow.getChildCount()) {
                    textView = (TextView) tableRow.getChildAt(j);
                } else {
                    textView = createTextView(textViewSize);
                    tableRow.addView(textView);
                }

                String value = table[i][j];
                if (value != null) {
                    textView.setText(value + "\t");
                    textView.setBackgroundResource(value.equals("P") ? R.drawable.bead_road_item_color_blue : R.drawable.bead_road_item_color_red);


                }


            }
            tableRow.setGravity(Gravity.CENTER);

            // Add the TableRow to the tableLayout only if it's a new row
            if (i >= tableLayout.getChildCount()) {
                tableLayout.addView(tableRow);
            }

        }
        validateResultBaseOnBrain();
        countThePlayerBanker(list);

    }

    private static int countWordsStartingWith(List<String> stringList, String input) {
        // Initialize a counter
        int count = 0;

        // Iterate through each string in the list
        for (String word : stringList) {
            // Check if the word starts with 'P' (ignoring case)
            if (word.toUpperCase().startsWith(input)) {
                count++;
            }
        }

        // Return the final count
        return count;
    }

    private void countThePlayerBanker(List<String> listOfItemFromRoad) {
        txtHand.setText(String.valueOf(listOfItemFromRoad.size()));
        txtPlayerHandCount.setText(String.valueOf(countWordsStartingWith(listOfItemFromRoad, "P")));
        txtbankerHandCount.setText(String.valueOf(countWordsStartingWith(listOfItemFromRoad, "B")));
    }

    private void validateResultBaseOnBrain() {


        List<Card> list = cardDatabaseHelper.getAllCards();


        String selectedBrain = txtBrain.getText().toString();
        if (selectedBrain == null || selectedBrain.isEmpty()) {
            System.out.println("selectedBrain is null or empty. No brain applied.");
        } else {
            switch (selectedBrain) {
                case Brains.ZIGZAG_STREAK:
                    ZigZagBrain(list);
                    break;
                case Brains.CHOP_STREAK:
                    ChopStreak(list);
                    break;
                case Brains.STAR_BLAZE:
                    StarBlaze(list);
                    break;
                case Brains.TIAMAT:
                    Tiamat(list);
                    break;
                case Brains.BODY_GUARD:
                    BodyGuard(list);
                    break;
                default:
                    // Handle the case where selectedBrain doesn't match any known type
                    // Add appropriate error handling or default behavior
                    // For now, let's assume you want to log an error message
                    System.out.println("Unknown Brain type '" + selectedBrain + "'. No Brain applied.");
                    break;
            }
        }


    }

    private void SetPredictionView(String input) {

        String result;
        if (input.equals("P")) {
            result = "Player";
            txtPrediction.setText(result);
            txtPrediction.setBackgroundColor(getColor(R.color.player));
        } else if (input.equals("B")) {
            result = "Banker";
            txtPrediction.setText(result);
            txtPrediction.setBackgroundColor(getColor(R.color.banker));
        } else {
            result = "Wait";
            txtPrediction.setText(result);
            txtPrediction.setBackgroundColor(getColor(R.color.wait));
        }


    }

    private static List<String> getLastNItems(List<String> list, int n) {
        int size = list.size();
        List<String> lastFiveItems = list.subList(size - n, size);

        return lastFiveItems;
    }

    private void StarBlaze(List<Card> list) {

        Card lastItem = list.stream().reduce((first, second) -> second).orElse(null);
        if (lastItem != null) {
            ProcessStarBlazeBrainLogic(list);
            if (!isUndo) {
                getTrackerView(lastItem);
            }

        }

    }

    private void Tiamat(List<Card> list) {

        Card lastItem = list.stream().reduce((first, second) -> second).orElse(null);
        if (lastItem != null) {
            ProcessTiamatBrain(list);
            if (!isUndo) {
                getTrackerView(lastItem);
            }
        }

    }

    private void BodyGuard(List<Card> list) {

        Card lastItem = list.stream().reduce((first, second) -> second).orElse(null);

        if (lastItem != null) {
            ProcessBodyGuard(list);
            if (!isUndo) {
                getTrackerView(lastItem);
            }
        }

    }


    private void ChopStreak(List<Card> list) {


        Card lastItem = list.stream().reduce((first, second) -> second).orElse(null);
        ProcessChopStreak(list);
        if (!isUndo) {
            getTrackerView(lastItem);
        }


    }


    private String getFirstLetterFromString(String input) {

        char firstLetter = input.isEmpty() ? '\0' : input.charAt(0);
        return String.valueOf(firstLetter);

    }

    private static String getLastCard(List<String> stringList) {
        // Check if the list is not empty
        if (!stringList.isEmpty()) {
            // Get the last string using the size of the list
            return stringList.get(stringList.size() - 1);
        } else {
            // Handle the case where the list is empty
            return "List is empty";
        }
    }


    private void ZigZagBrain(List<Card> list) {

        Card card = list.stream().reduce((first, second) -> second).orElse(null);
        ProcessZigZagBrainLogic(card);
        if (!isUndo) {
            getTrackerView(card);
        }


    }

    private void processMoneyMangement(String r) {

        String selectedMM = txtMoneyManagementName.getText().toString();
        if (selectedMM.equals("")) {
            System.out.println("selectedMM is null. No money management strategy applied.");
        } else {
            switch (selectedMM) {
                case MoneyManagement.OSCAR:
                    OscarsGrind(r);
                    break;
                case MoneyManagement.ORC:
                    OrcMoneyManagement(r);
                    break;
                case MoneyManagement.MOON:
                    // MoonGrind(r);
                    break;
                case MoneyManagement.Mang_B:
                    // MangB(r);
                    break;
                case MoneyManagement.KOI:
                    // MangB(r);
                    break;

                default:
                    // Handle the case where selectedMM doesn't match any known type
                    // Add appropriate error handling or default behavior
                    // For now, let's assume you want to do nothing in this case
                    System.out.println("Unknown MoneyManagement type. No money management strategy applied.");
                    break;
            }
        }

    }

    private static String getLastString(List<String> stringList) {
        // Check if the list is not empty
        if (!stringList.isEmpty()) {
            // Get the last string using the size of the list
            return stringList.get(stringList.size() - 1);
        } else {
            // Handle the case where the list is empty
            return "List is empty";
        }
    }


    private void OscarsGrind(String r) {

        winLoseList.add(r);
        String lastResult = getLastString(winLoseList);
        System.out.println("lastResult: " + lastResult);

        if (lastResult.equalsIgnoreCase("W")) {


            betsList.add(roundToOneDecimalPlace(currentBetAmount));


            double nextBetAmount = currentBetAmount + baseBetAmount;
            double refinedTotal = Double.parseDouble(String.valueOf(sumOfDoubles(betsList)).replace("-", ""));

            if (refinedTotal < nextBetAmount) {
                nextBetAmount = refinedTotal + baseBetAmount;
            }

            currentBetAmount = nextBetAmount;
            txtBetAmount.setText(String.valueOf(roundToOneDecimalPlace(nextBetAmount)).replace("-", ""));
            setBetsView();


        } else if (lastResult.equalsIgnoreCase("L")) {

//                betsList.add(Double.parseDouble("-" + txtBetAmount.getText()));

            betsList.add(roundToOneDecimalPlace(Double.parseDouble("-" + currentBetAmount)));

            double lastBetAmount = currentBetAmount;

//            double refinedLastBetAmount = (lastBetAmount > 0) ? -1 * lastBetAmount : lastBetAmount;

//            currentBetAmount = refinedLastBetAmount;
            txtBetAmount.setText(String.valueOf(roundToOneDecimalPlace(currentBetAmount)).replace("-", ""));
            setBetsView();
        }
        System.out.println("lastResult: " + lastResult);


    }


    private void setViewForStopProfitStopLoss() {


        txtStopProfit.setBackgroundColor(getColor(R.color.white));
        txtStopLoss.setBackgroundColor(getColor(R.color.white));

        double totalUnits = Double.parseDouble(txtProfitByUnit.getText().toString());
        double stopProfit = Double.parseDouble(txtStopProfit.getText().toString());
        double stopLoss = -1 * Double.parseDouble(txtStopLoss.getText().toString());

        if (totalUnits >= stopProfit) {

            txtStopProfit.setBackgroundColor(getColor(R.color.win));
            txtStopLoss.setBackgroundColor(getColor(R.color.white));

        } else if (totalUnits <= stopLoss) {


            txtStopProfit.setBackgroundColor(getColor(R.color.white));
            txtStopLoss.setBackgroundColor(getColor(R.color.banker));
        }


    }


    public static double sumOfDoubles(List<Double> doubleList) {
        double sum = 0.0;

        if (doubleList != null) {
            // Start the loop from the second element (index 1)
            for (double d : doubleList) {

                sum += d;
            }
        }


        return roundToOneDecimalPlace(sum);
    }

    public double calculateProfitByUnit() {

        double profit = Double.parseDouble(txtProfit.getText().toString().replace("-", ""));
        if (profit <= 0) {
            return 0;
        }
        return roundToOneDecimalPlace(profit / baseBetAmount);
    }

    private static Double getLastDouble(List<Double> doubleList) {
        // Check if the list is not empty
        if (!doubleList.isEmpty()) {
            // Get the last double using the size of the list
            return doubleList.get(doubleList.size() - 1);
        } else {
            // Handle the case where the list is empty
            // You can customize this behavior based on your requirements
            return 0.0; // or throw an exception, or return a default value, etc.
        }
    }


    public static double roundToOneDecimalPlace(double number) {
        // Create a DecimalFormat object with the pattern "#.#" to format to one decimal place
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        // Format the number using the DecimalFormat object
        String formattedNumber = decimalFormat.format(number);
        // Parse the formatted number back to a double
        return Double.parseDouble(formattedNumber);
    }

    private void ProcessZigZagBrainLogic(Card card) {

        String cardName = card.getName();

        String prediction = "";
        if (cardName.equalsIgnoreCase("P")) {
            prediction = "Banker";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.banker));
        } else if (cardName.equalsIgnoreCase("B")) {
            prediction = "Player";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.player));
        } else {
            prediction = "Wait";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.wait));
        }

        if (cardName.equalsIgnoreCase("Reset")) {
            prediction = "Wait";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.wait));
        }

        txtPrediction.setText(prediction);


    }

    private void ProcessStarBlazeBrainLogic(List<Card> list) {

        List<String> nameList = new ArrayList<>();
        for (Card c : list) {
            nameList.add(c.getName());
        }

        //Note: Do not move these code to any line
        String outcome = "";
        if (list.size() == 3) {

            // Get the last 3 items
            List<String> lastThreeItems = getLastNItems(nameList, 3);

            String pattern = lastThreeItems.stream()
                    .skip(Math.max(0, lastThreeItems.size() - 3))
                    .collect(Collectors.joining(""));


            outcome = StarBlaze.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        } else if (list.size() == 4) {

            // Get the last 5 items
            List<String> lastFiveItems = getLastNItems(nameList, 4);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 4))
                    .collect(Collectors.joining(""));
            outcome = StarBlaze.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        } else if (list.size() >= 5) {

            // Get the last 5 items
            List<String> lastFiveItems = getLastNItems(nameList, 5);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 5))
                    .collect(Collectors.joining(""));
            outcome = StarBlaze.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        }
    }


    private void ProcessTiamatBrain(List<Card> list) {

        List<String> nameList = new ArrayList<>();
        for (Card c : list) {
            nameList.add(c.getName());
        }

        //Note: Do not move these code to any line
        String outcome = "";
        if (list.size() == 1) {


            List<String> lastThreeItems = getLastNItems(nameList, 1);

            String pattern = lastThreeItems.stream()
                    .skip(Math.max(0, lastThreeItems.size() - 1))
                    .collect(Collectors.joining(""));


            outcome = Tiamat.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        } else if (list.size() == 2) {


            List<String> lastFiveItems = getLastNItems(nameList, 2);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 2))
                    .collect(Collectors.joining(""));
            outcome = Tiamat.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        } else if (list.size() == 3) {


            List<String> lastFiveItems = getLastNItems(nameList, 3);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 3))
                    .collect(Collectors.joining(""));
            outcome = Tiamat.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        } else if (list.size() == 4) {


            List<String> lastFiveItems = getLastNItems(nameList, 4);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 4))
                    .collect(Collectors.joining(""));
            outcome = Tiamat.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        } else if (list.size() >= 5) {


            List<String> lastFiveItems = getLastNItems(nameList, 5);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 5))
                    .collect(Collectors.joining(""));
            outcome = Tiamat.patternMap.getOrDefault(pattern, "Invalid pattern");
            SetPredictionView(outcome);

        }
    }

    private void ProcessBodyGuard(List<Card> list) {


        List<String> nameList = list.stream()
                .map(Card::getName)
                .collect(Collectors.toList());


        //Note: Do not move these code to any line
        String outcome = "";
        if (list.size() >= 2) {

            // Get the last 3 items
            List<String> lastThreeItems = getLastNItems(nameList, 2);

            String pattern = lastThreeItems.stream()
                    .skip(Math.max(0, lastThreeItems.size() - 2))
                    .collect(Collectors.joining(""));

            outcome = BodyGuard.patternMap.getOrDefault(pattern, "Wait");
            SetPredictionView(outcome);

        }

    }


    private void ProcessChopStreak(List<Card> list) {


        List<String> nameList = list.stream()
                .map(Card::getName)
                .collect(Collectors.toList());


        //Note: Do not move these code to any line
        String outcome = "";
        if (list.size() >= 3) {

            // Get the last 3 items
            List<String> lastThreeItems = getLastNItems(nameList, 3);

            String pattern = lastThreeItems.stream()
                    .skip(Math.max(0, lastThreeItems.size() - 3))
                    .collect(Collectors.joining(""));

            outcome = ChopStreak.patternMap.getOrDefault(pattern, "Wait");
            SetPredictionView(outcome);

        }

    }

    private TextView createTextView(int size) {
        TextView textView = new TextView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(60, 60);
        params.setMargins(1, 1, 1, 1);

        textView.setLayoutParams(params);
        textView.setTextAppearance(android.R.style.TextAppearance_Medium);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextColor(getColor(R.color.white));
        textView.setTextSize(15);


        return textView;
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void addSeparator(RadioGroup radioGroup) {
        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        radioGroup.addView(separator);
    }

    private void addRadioButtonWithSeparator(RadioGroup radioGroup, String text, int id, boolean isSelected) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(text);
        radioButton.setId(id);
        radioButton.setTextSize(22);
        radioButton.setChecked(isSelected);
        // Set padding for the RadioButton (left, top, right, bottom)
        radioButton.setPadding(0, 10, 0, 10);
        radioGroup.addView(radioButton);
    }

    private void GetMoneyManagementList() {


        radioGroup = findViewById(R.id.radioGroup);
        moneyManagementList = moneyManagementDatabaseHelper.getAllSortedByName();

        if (moneyManagementList.size() == 0) {
            addAllMoneyManagementItemsToDaTabase();
            moneyManagementList = moneyManagementDatabaseHelper.getAllSortedByName();
        }

        MoneyManagementModel moneyManagementModelSelectedTrue = moneyManagementDatabaseHelper.getSelectedItem();
        if (moneyManagementModelSelectedTrue == null) {
            int id = preferences.getInt(MM_ID, 1);
            updateMoneyManagementListStatus(id);
            moneyManagementList = moneyManagementDatabaseHelper.getAllSortedByName();
        }


        radioGroup.removeAllViews();
        for (MoneyManagementModel item : moneyManagementList) {
            if (!item.getName().isEmpty()) {

                addRadioButtonWithSeparator(radioGroup, item.getName(), item.getId(), item.isSelected());
                addSeparator(radioGroup);

            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null) {
                    // Handle radio button click event
                    int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    preferences.edit().putInt(MM_ID, selectedRadioButtonId).apply();
                    updateMoneyManagementListStatus(selectedRadioButtonId);

//                    viewPager.setCurrentItem(1, true);
//                    setMoneyManagementView();
//                    hideKeyboard();


                }
            }
        });


    }


    private void GetBrainList() {


        radioGroupBrains = findViewById(R.id.radioGroupBrains);
        brainsList = brainsDatabaseHelper.getAllSortedByName();


        if (brainsList.size() == 0) {
            addAllBrainsItemsToDaTabase();
            brainsList = brainsDatabaseHelper.getAllSortedByName();
        }

        Brains brainsSelectedTrue = brainsDatabaseHelper.getSelectedItem();
        if (brainsSelectedTrue == null) {
            int id = preferences.getInt(MM_ID, 1);
            updateBrainsListStatus(id);
            brainsList = brainsDatabaseHelper.getAllSortedByName();
        }

        radioGroupBrains.removeAllViews();
        for (Brains item : brainsList) {
            if (!item.getName().isEmpty()) {
                System.out.println("item-> " + item.toString());
                addRadioButtonWithSeparator(radioGroupBrains, item.getName(), item.getId(), item.getSelected());
                addSeparator(radioGroupBrains);
            }
        }

        // Set a listener for when a RadioButton is selected
        radioGroupBrains.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                selectBrainFromBrainPage = checkedId;
//                String selectedOption = selectedRadioButton.getText().toString();
                brainsDatabaseHelper.updateAllIsSelected(false);
                brainsDatabaseHelper.updateIsSelected(checkedId, true);


            }
        });
    }

    private void addAllMoneyManagementItemsToDaTabase() {

        moneyManagementDatabaseHelper.save(new MoneyManagementModel(MoneyManagement.ORC, MoneyManagement.ORC_DESCRIPTION, false));
        moneyManagementDatabaseHelper.save(new MoneyManagementModel(MoneyManagement.OSCAR, MoneyManagement.OSCAR_DESCRIPTION, true));
//        moneyManagementDatabaseHelper.save(new MoneyManagementModel(MoneyManagement.MOON, MoneyManagement.MOON_DESCRIPTION, false));
//        moneyManagementDatabaseHelper.save(new MoneyManagementModel(MoneyManagement.Mang_B, MoneyManagement.Mang_B_DESCRIPTION, false));
//        moneyManagementDatabaseHelper.save(new MoneyManagementModel(MoneyManagement.KOI, MoneyManagement.KOI_DESCRIPTION, false));
    }

    private void addAllBrainsItemsToDaTabase() {

        brainsDatabaseHelper.save(new Brains(Brains.STAR_BLAZE, false));
        brainsDatabaseHelper.save(new Brains(Brains.CHOP_STREAK, false));
        brainsDatabaseHelper.save(new Brains(Brains.ZIGZAG_STREAK, false));
        brainsDatabaseHelper.save(new Brains(Brains.TIAMAT, false));
//        brainsDatabaseHelper.save(new Brains(Brains.BODY_GUARD, false));
    }


    private void updateMoneyManagementListStatus(int selectedRadioButtonId) {
        moneyManagementDatabaseHelper.updateAllIsSelected(false);
        moneyManagementDatabaseHelper.updateIsSelected(selectedRadioButtonId, true);
    }

    private void updateBrainsListStatus(int selectedRadioButtonId) {

        brainsDatabaseHelper.updateAllIsSelected(false);
        brainsDatabaseHelper.updateIsSelected(selectedRadioButtonId, true);


    }

    int winRequirement = 2;
    String[] levels = OrcMoneyManagement.levels;
    int consecutiveWins = 0;
    int consecutiveLose = 0;

    private static boolean winLoseCondition(String response) {
        return response.equalsIgnoreCase("W");
    }

    String currentPosition = "Base";

    private void setStepsHighlight(TextView textView) {

        TextView[] txtviewsArray = {txtPos1, txtBase, txtStep2, txtStep3, txtStep4, txtStep5, txtStep6};
        for (TextView txt : txtviewsArray) {
            txt.setBackgroundColor(getColor(R.color.white));
            txt.setTextColor(getColor(R.color.gray));


        }
        textView.setTextColor(getColor(R.color.white));
        textView.setBackgroundColor(getColor(R.color.blue_dark));

    }

    List<String> orcCurrentLevelList = new ArrayList<>();

    private void OrcMoneyManagement(String response) {


        switch (currentPosition) {
            case "Pos1":
                if (winLoseCondition(response)) {
                    currentPosition = levels[OrcMoneyManagement.BASE];
                    consecutiveLose = 0;
                } else {
                    currentPosition = levels[OrcMoneyManagement.STEP1];


                }

                break;

            case "Base":
                consecutiveWins = 0;
                consecutiveLose = 0;
                if (winLoseCondition(response)) {
                    currentPosition = levels[OrcMoneyManagement.POS1];


                } else {
                    currentPosition = levels[OrcMoneyManagement.STEP1];


                    consecutiveLose++;
                }
                break;

            case "Step1":
                if (winLoseCondition(response)) {
                    currentPosition = levels[OrcMoneyManagement.BASE];


                    consecutiveLose = 0;
                } else {
                    currentPosition = levels[OrcMoneyManagement.STEP2];


                    consecutiveLose++;
                }
                break;

            case "Step2":
                if (winLoseCondition(response)) {
                    consecutiveWins++;
                    consecutiveLose = 0;
                    if (consecutiveWins == winRequirement) {
                        currentPosition = levels[OrcMoneyManagement.BASE];


                    } else {
                        currentPosition = levels[OrcMoneyManagement.STEP1];


                    }
                } else {
                    currentPosition = levels[OrcMoneyManagement.STEP3];

                    consecutiveLose++;
                }
                break;

            case "Step3":
                if (winLoseCondition(response)) {
                    consecutiveWins++;
                    consecutiveLose = 0;
                    if (consecutiveWins == winRequirement) {
                        currentPosition = levels[OrcMoneyManagement.BASE];


                    } else {
                        currentPosition = levels[OrcMoneyManagement.STEP2];


                    }
                } else {
                    currentPosition = levels[OrcMoneyManagement.STEP4];


                    if (consecutiveWins > 0) {
                        consecutiveWins--;
                    }
                    consecutiveLose++;
                }
                break;

            case "Step4":
                if (winLoseCondition(response)) {
                    consecutiveWins++;
                    consecutiveLose = 0;
                    if (consecutiveWins == winRequirement) {
                        currentPosition = levels[OrcMoneyManagement.BASE];

                    } else {
                        currentPosition = levels[OrcMoneyManagement.STEP3];

                    }
                } else {
                    currentPosition = levels[OrcMoneyManagement.STEP5];


                    if (consecutiveWins > 0) {
                        consecutiveWins--;
                    }
                    consecutiveLose++;
                }
                break;

            case "Step5":
                if (winLoseCondition(response)) {
                    currentPosition = levels[OrcMoneyManagement.STEP4];
                    consecutiveWins++;
                    consecutiveLose = 0;
                } else {
                    currentPosition = levels[OrcMoneyManagement.BASE];
                    consecutiveLose++;
                }
                break;

        }
        orcCurrentLevelList.add(currentPosition);
        setupORCStepsView(orcCurrentLevelList);


    }

    private void removelastItemFromOrcLevel() {
        if (!orcCurrentLevelList.isEmpty()) {
            orcCurrentLevelList.remove(orcCurrentLevelList.size() - 1);
            if (!orcCurrentLevelList.isEmpty()) {
                setupORCStepsView(orcCurrentLevelList);
            } else {
                setStepsHighlight(txtBase);
            }
        }
    }

    private void setupORCStepsView(List<String> orcCurrentLevelList) {

        String currentLevel = orcCurrentLevelList.get(orcCurrentLevelList.size() - 1);
        switch (currentLevel) {
            case "Pos1":
                // Remove statements
                setStepsHighlight(txtPos1);
                break;

            case "Base":
                // Remove statements
                setStepsHighlight(txtBase);
                break;

            case "Step1":
                // Remove statements
                setStepsHighlight(txtStep2);
                break;

            case "Step2":
                // Remove statements
                setStepsHighlight(txtStep3);
                break;

            case "Step3":
                // Remove statements
                setStepsHighlight(txtStep4);
                break;

            case "Step4":
                // Remove statements
                setStepsHighlight(txtStep5);
                break;

            case "Step5":
                // Remove statements
                setStepsHighlight(txtStep6);
                break;
        }


    }


}