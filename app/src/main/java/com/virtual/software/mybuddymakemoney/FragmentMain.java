package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txtPrediction;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TableLayout tableLayout;
    double baseBetAmount = 0.1;
    double currentBetAmount = 0;
    List<Double> betsListm;
    List<String> winLoseList;
    boolean isUndo = false;
    TextView txtTitle, txtHand, txtPlayerHandCount, txtbankerHandCount, txtBetAmount, txtMessage, txtProfit, txtProfitByUnit, txtStopProfit, txtStopLoss;
    LinearLayout profitPanelLayout;
    private final String BET_AMOUNT = "BetAmount";
    LinearLayout trackerPanel;
    HorizontalScrollView trackerPanelHorizontalScroller;
    TextView txtSkip;
    CardDataSource cardDatabaseHelper;


    private int win = 0;
    private int lose = 0;
    private final String STOP_LOSS = "StopLoss";
    private final String STOP_PROFIT = "StopProfit";

    private final String SHIELD_LOSE = "ShieldLoss";
    private final String SHIELD_WIN = "ShieldWin";

    private SharedPreferences preferences;
    private final String PREF_NAME = "your_preference_name";

    List<Double> betsList;

    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public interface UpdateSetting {

        void onUpdateBaseBetAmount(String newText);

        void onUpdateShield(int NoOfLoss, int NoOfWin);

        void onUpdateStopLoss(int unit);

        void onUpdateStopProfit(int unit);


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

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        cardDatabaseHelper = new CardDataSource(getActivity());


        betsList = new ArrayList<>();
        winLoseList = new ArrayList<>();

        trackerPanel = view.findViewById(R.id.trackerPanel);
        trackerPanelHorizontalScroller = view.findViewById(R.id.trackerPanelHorizontalScroller);
        tableLayout = view.findViewById(R.id.tableLayoutBeadRoad);
        txtPrediction = view.findViewById(R.id.txtPrediction);
        txtSkip = view.findViewById(R.id.txtSkip);
        txtPlayerHandCount = view.findViewById(R.id.txtPlayerHandCount);
        txtbankerHandCount = view.findViewById(R.id.txtbankerHandCount);
        txtHand = view.findViewById(R.id.txtHand);
        txtBetAmount = view.findViewById(R.id.txtBetAmount);
        txtMessage = view.findViewById(R.id.txtMessage);
        txtProfit = view.findViewById(R.id.txtProfit);
        txtProfitByUnit = view.findViewById(R.id.txtProfitByUnit);
        profitPanelLayout = view.findViewById(R.id.winAndLossLinearLayout);


        txtStopLoss = view.findViewById(R.id.txtStopLoss);
        txtStopProfit = view.findViewById(R.id.txtStopProfit);
        TextView txtShield = view.findViewById(R.id.txtShield);


        preferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String BaseUnitAmount = preferences.getString(BET_AMOUNT, "0.1");
        int stopProfit = preferences.getInt(STOP_PROFIT, 0);
        int stopLoss = preferences.getInt(STOP_LOSS, 0);
        int ShieldLoss = preferences.getInt(SHIELD_LOSE, 0);
        int ShieldWin = preferences.getInt(SHIELD_WIN, 0);


        txtBetAmount.setText(BaseUnitAmount);
        currentBetAmount = Double.parseDouble(BaseUnitAmount);

        txtStopProfit.setText(String.valueOf(stopProfit));
        txtStopLoss.setText(String.valueOf(stopLoss));
        txtShield.setText(ShieldLoss + "-" + ShieldWin);


        AppCompatButton btnPlayer = view.findViewById(R.id.btnPlayer);
        AppCompatButton btnReset = view.findViewById(R.id.btnReset);
        AppCompatButton btnBanker = view.findViewById(R.id.btnBanker);
        AppCompatButton btnSkip = view.findViewById(R.id.btnSkip);
        AppCompatButton btnUndo = view.findViewById(R.id.btnUndo);

        //get the previous data

        setBeadRoadView(6, 20, 50);


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
                setSkip(true);
                isUndo = false;
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isUndo = true;
                playClickedSound();

//                cardDatabaseHelper.open();
                List<Card> allCards = cardDatabaseHelper.getAllCards();
//                cardDatabaseHelper.close();


                //this is to reset the view
                ProcessZigZagBrainLogic(new Card(0, "Reset", "Reset", "Reset", "Reset", "Reset", "Reset"));
                //then re establish

                // Example usage: Deleting the last row (assuming there's at least one row in the database)
                if (!allCards.isEmpty()) {
                    Card lastCard = allCards.get(allCards.size() - 1);
//                    cardDatabaseHelper.open();
                    cardDatabaseHelper.deleteCard(lastCard.getId());
//                    cardDatabaseHelper.close();

// Get the index of the last child view
                    int lastIndex = trackerPanel.getChildCount() - 1;

                    if (lastIndex >= 0) {
                        // Remove the last child view
                        trackerPanel.removeViewAt(lastIndex);
                    }

                } else {
                    ResetAll();
                }

//                    ---------------------------------------------------------------------------------------------


                setBeadRoadView(6, 20, 20);


            }
        });

        return view;
    }

    private void ResetAll() {

//        preferences.edit().putInt(STOP_PROFIT, 0).apply();
//        preferences.edit().putInt(STOP_LOSS, 0).apply();
        betsList = new ArrayList<>();
        winLoseList = new ArrayList<>();

//        cardDatabaseHelper.open();
        cardDatabaseHelper.deleteAllCards();
//        cardDatabaseHelper.close();


        betsList.clear();
        trackerPanel.removeAllViews();
        profitPanelLayout.removeAllViews();
        txtMessage.setText("");
        txtBetAmount.setText(String.valueOf(baseBetAmount));
        txtProfitByUnit.setText("0");
        txtProfit.setText("0");

        txtStopProfit.setBackgroundColor(getContext().getColor(R.color.white));
        txtStopLoss.setBackgroundColor(getContext().getColor(R.color.white));
        txtProfit.setBackgroundColor(getContext().getColor(R.color.white));

        setBeadRoadView(6, 20, 50);
        ProcessZigZagBrainLogic(new Card(0, "Reset", "Reset", "Reset", "Reset", "Reset", "Yes"));
    }

    private void saveCardItem(String cardItem) {

//        cardDatabaseHelper.open();
        int count = cardDatabaseHelper.getAllCardNames().size();
//        cardDatabaseHelper.close();
//-------------------------------------------------------------------


        String selectedBrain = MainActivity.getBrainName();
        if (selectedBrain == null || selectedBrain.isEmpty()) {
            System.out.println("selectedBrain is null or empty. No brain applied.");
        } else {

            String isSkip = txtSkip.getText().toString();
            String isWait = txtPrediction.getText().toString();

            isSkip = isSkip.equalsIgnoreCase("Yes") ? "Yes" : "No";
            isWait = isWait.equalsIgnoreCase("Wait") ? "Yes" : "No";


            switch (selectedBrain) {
                case Brains.ZIGZAG_STREAK:

//                    cardDatabaseHelper.open();
                    if (count == 0) {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), MainActivity.getBrainName(), "Yes", "Yes", "Yes"));
                    } else {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), MainActivity.getBrainName(), "No", isSkip, isWait));
                    }
//                    cardDatabaseHelper.close();

                    break;
                case Brains.CHOP_STREAK:
                case Brains.STAR_BLAZE:

//                    cardDatabaseHelper.open();
                    if (count < 3) {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), MainActivity.getBrainName(), "Yes", "Yes", "Yes"));
                    } else {
                        cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), MainActivity.getBrainName(), "No", isSkip, isWait));
                    }
//                    cardDatabaseHelper.close();

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
            txtSkip.setBackgroundColor(getResources().getColor(R.color.wait));
        } else {
            txtSkip.setText("No");
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



        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                50, // width
                50);
        layoutParams.setMargins(1, 1, 1, 1);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource
//        textView.setTypeface(null, Typeface.NORMAL);


        if (!isInitialize.equalsIgnoreCase("Yes")) {

            if (cardName.equalsIgnoreCase(prediction) && isInitialize.equalsIgnoreCase("No") && isWait.equalsIgnoreCase("No") && isSkip.equalsIgnoreCase("No")) {

                textView.setText("W");
                textView.setBackground(getResources().getDrawable(R.drawable.win));
                result = "W";

            } else if (!cardName.equalsIgnoreCase(prediction) && isInitialize.equalsIgnoreCase("No") && isWait.equalsIgnoreCase("No") && isSkip.equalsIgnoreCase("No")) {
                textView.setText("L");
                textView.setBackground(getResources().getDrawable(R.drawable.button_banker));
                result = "L";

            } else if (cardName.equalsIgnoreCase(prediction) && isInitialize.equalsIgnoreCase("No") && isSkip.equalsIgnoreCase("Yes")) {

                textView.setText("W");
                textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
                result = "W";
                System.out.println("here1");
                setSkip(false);
            } else if (!cardName.equalsIgnoreCase(prediction) && isInitialize.equalsIgnoreCase("No") && isSkip.equalsIgnoreCase("Yes")) {
                textView.setText("L");
                textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
                result = "L";
                setSkip(false);
                System.out.println("here2");
            }

            if (isWait.equalsIgnoreCase("Yes")) {
                textView.setText("*");
                textView.setBackground(getResources().getDrawable(R.drawable.button_player));
                System.out.println("here3");
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

            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // width
                    60);
            layoutParams.setMargins(1, 1, 1, 1);
            textView.setLayoutParams(layoutParams);


            if (betsList.get(i) > 0) {

                textView.setBackgroundColor(getContext().getColor(R.color.win));
            } else {
                textView.setBackgroundColor(getContext().getColor(R.color.banker));
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

        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.clicked_sound);
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
                tableRow = new TableRow(getContext());
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

//        cardDatabaseHelper.open();
        List<Card> list = cardDatabaseHelper.getAllCards();
//        cardDatabaseHelper.close();


        String selectedBrain = MainActivity.getBrainName();
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
                default:
                    // Handle the case where selectedBrain doesn't match any known type
                    // Add appropriate error handling or default behavior
                    // For now, let's assume you want to log an error message
                    System.out.println("Unknown Brain type '" + selectedBrain + "'. No Brain applied.");
                    break;
            }
        }


    }

    private void generateResult(String input) {

        String result;
        if (input.equals("P")) {
            result = "Player";
            txtPrediction.setText(result);
            txtPrediction.setBackgroundColor(getContext().getColor(R.color.player));
        } else if (input.equals("B")) {
            result = "Banker";
            txtPrediction.setText(result);
            txtPrediction.setBackgroundColor(getContext().getColor(R.color.banker));
        } else {
            result = "Wait";
            txtPrediction.setText(result);
            txtPrediction.setBackgroundColor(getContext().getColor(R.color.wait));
        }


    }

    private static List<String> getLastNItems(List<String> list, int n) {
        int size = list.size();
        List<String> lastFiveItems = list.subList(size - n, size);

        return lastFiveItems;
    }

    private void StarBlaze(List<Card> list) {


//        betsList.clear();
//        winLoseList.clear();
//        trackerPanel.removeAllViews();

        Card lastItem = list.stream().reduce((first, second) -> second).orElse(null);
        if (lastItem != null) {
            ProcessStarBlazeBrainLogic(list);
            if(!isUndo){
                getTrackerView(lastItem);
            }

        }
//        for (Card card : list) {
//            ProcessStarBlazeBrainLogic(list);
//            getTrackerView(lastItem);
//        }


    }


    private void ChopStreak(List<Card> list) {

//        trackerPanel.removeAllViews();
//        for (Card card : list) {
        Card lastItem = list.stream().reduce((first, second) -> second).orElse(null);
        ProcessChopStreak(list);
        if(!isUndo){
            getTrackerView(lastItem);
        }
//            OscarsGrind(r);


//        }


    }


    private String getFirstLetterFromString(String input) {
//        // Get the first letter using charAt(0)
        char firstLetter = input.isEmpty() ? '\0' : input.charAt(0);
        return String.valueOf(firstLetter);
//        return String.valueOf(input);
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

//        betsList.clear();
//        winLoseList.clear();
//        trackerPanel.removeAllViews();

//        for (Card card : list) {
        Card card = list.stream().reduce((first, second) -> second).orElse(null);
        ProcessZigZagBrainLogic(card);
        if(!isUndo){
            getTrackerView(card);
        }



//        }


    }

    private void processMoneyMangement(String r) {
        String selectedMM = MainActivity.getMoneyManagementName();
        if (selectedMM == null || selectedMM.equals("")) {
            System.out.println("selectedMM is null. No money management strategy applied.");
        } else {
            switch (selectedMM) {
                case MoneyManagement.OSCAR:
                    OscarsGrind(r);
                    break;
                case MoneyManagement.ORC:
                    // OrcMoneyManagement(r);
                    break;
                case MoneyManagement.MOON:
                    // MoonGrind(r);
                    break;
                case MoneyManagement.Mang_B:
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


        txtStopProfit.setBackgroundColor(getContext().getColor(R.color.white));
        txtStopLoss.setBackgroundColor(getContext().getColor(R.color.white));

        double totalUnits = Double.parseDouble(txtProfitByUnit.getText().toString());
        double stopProfit = Double.parseDouble(txtStopProfit.getText().toString());
        double stopLoss = -1 * Double.parseDouble(txtStopLoss.getText().toString());

        if (totalUnits >= stopProfit) {

            txtStopProfit.setBackgroundColor(getContext().getColor(R.color.win));
            txtStopLoss.setBackgroundColor(getContext().getColor(R.color.white));

        } else if (totalUnits <= stopLoss) {


            txtStopProfit.setBackgroundColor(getContext().getColor(R.color.white));
            txtStopLoss.setBackgroundColor(getContext().getColor(R.color.banker));
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
            generateResult(outcome);

        } else if (list.size() == 4) {

            // Get the last 5 items
            List<String> lastFiveItems = getLastNItems(nameList, 4);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 4))
                    .collect(Collectors.joining(""));
            outcome = StarBlaze.patternMap.getOrDefault(pattern, "Invalid pattern");
            generateResult(outcome);

        } else if (list.size() >= 5) {

            // Get the last 5 items
            List<String> lastFiveItems = getLastNItems(nameList, 5);
            String pattern = lastFiveItems.stream()
                    .skip(Math.max(0, lastFiveItems.size() - 5))
                    .collect(Collectors.joining(""));
            outcome = StarBlaze.patternMap.getOrDefault(pattern, "Invalid pattern");
            generateResult(outcome);

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
            generateResult(outcome);

        }

    }

    private TextView createTextView(int size) {
        TextView textView = new TextView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(60, 60);
        params.setMargins(1, 1, 1, 1);

        textView.setLayoutParams(params);
        textView.setTextAppearance(android.R.style.TextAppearance_Medium);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextColor(getContext().getColor(R.color.white));
        textView.setTextSize(15);


        return textView;
    }


};