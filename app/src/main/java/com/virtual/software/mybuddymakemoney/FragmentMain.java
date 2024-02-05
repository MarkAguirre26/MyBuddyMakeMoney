package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.List;

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
    boolean isUndo = false;
    TextView txtTitle, txtHand, txtPlayerHandCount, txtbankerHandCount, txtBaseBet;

    private final String BASE_UNIT = "BaseUnit";
    LinearLayout trackerPanel;
    HorizontalScrollView trackerPanelHorizontalScroller;
    TextView txtSkip;
    CardDataSource cardDatabaseHelper;


    private final String STOP_LOSS = "StopLoss";
    private final String STOP_PROFIT = "StopProfit";

    private final String SHIELD_LOSE = "ShieldLoss";
    private final String SHIELD_WIN = "ShieldWin";

    private SharedPreferences preferences;
    private final String PREF_NAME = "your_preference_name";


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
//        trackerDatabaseHelper = new TrackerDatabaseHelper(getActivity());


        trackerPanel = view.findViewById(R.id.trackerPanel);
        trackerPanelHorizontalScroller = view.findViewById(R.id.trackerPanelHorizontalScroller);
        tableLayout = view.findViewById(R.id.tableLayoutBeadRoad);
        txtPrediction = view.findViewById(R.id.txtPrediction);
        txtSkip = view.findViewById(R.id.txtSkip);
        txtPlayerHandCount = view.findViewById(R.id.txtPlayerHandCount);
        txtbankerHandCount = view.findViewById(R.id.txtbankerHandCount);
        txtHand = view.findViewById(R.id.txtHand);
        txtBaseBet = view.findViewById(R.id.txtBaseBet);

        TextView txtStopLoss = view.findViewById(R.id.txtStopLoss);
        TextView txtStopProfit = view.findViewById(R.id.txtStopProfit);
        TextView txtShield = view.findViewById(R.id.txtShield);


        preferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String BaseUnitAmount = preferences.getString(BASE_UNIT, String.valueOf(baseBetAmount));
        int stopProfit = preferences.getInt(STOP_PROFIT, 0);
        int stopLoss = preferences.getInt(STOP_LOSS, 0);
        int ShieldLoss = preferences.getInt(SHIELD_LOSE, 0);
        int ShieldWin = preferences.getInt(SHIELD_WIN, 0);


        txtBaseBet.setText(BaseUnitAmount);
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

                ResetSAll();

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

                cardDatabaseHelper.open();
                List<Card> AllCards = cardDatabaseHelper.getAllCards();
                cardDatabaseHelper.close();

                //this is to reset the view
                setPredictionResource(new Card(0, "Reset", "Reset", "Reset", "Reset", "Reset"));
                //then re establish

                // Example usage: Deleting the last row (assuming there's at least one row in the database)
                if (!AllCards.isEmpty()) {
                    Card lastCard = AllCards.get(AllCards.size() - 1);
                    cardDatabaseHelper.open();
                    cardDatabaseHelper.deleteCard(lastCard.getId());
                    cardDatabaseHelper.close();
                } else {
//                    ResetSAll();
                }
                setBeadRoadView(6, 20, 20);


            }
        });

        return view;
    }

    private void ResetSAll() {

        cardDatabaseHelper.open();
        cardDatabaseHelper.deleteAllCards();
        cardDatabaseHelper.close();


        trackerPanel.removeAllViews();
        setBeadRoadView(6, 20, 50);
        setPredictionResource(new Card(0, "Reset", "Reset", "Reset", "Reset", "Reset"));
    }

    private void saveCardItem(String cardItem) {

        cardDatabaseHelper.open();
        int count = cardDatabaseHelper.getAllCardNames().size();
        cardDatabaseHelper.close();

        cardDatabaseHelper.open();
        if (count == 0) {
            cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), MainActivity.getBrainName(), "Yes", "Yes"));
        } else {
            cardDatabaseHelper.insertCard(new Card(0, cardItem, getFirstLetterFromString(txtPrediction.getText().toString()), MainActivity.getBrainName(), "No", txtSkip.getText().toString()));
        }
        cardDatabaseHelper.close();

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

    private void setTrackerView(Card card) {

        String cardName = card.getName();
        String prediction = card.getPrediction();
        String isInitialize = card.getInitialize();
        String isWait = card.getWait();


        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                60, // width
                60);
        layoutParams.setMargins(1, 1, 1, 1);
        textView.setLayoutParams(layoutParams);

        if (cardName.equalsIgnoreCase(prediction)) {
            textView.setText("W");
            textView.setBackground(getResources().getDrawable(R.drawable.win));
        } else {
            textView.setText("L");
            textView.setBackground(getResources().getDrawable(R.drawable.button_banker));
        }


        if (isWait.equalsIgnoreCase("Yes")) {
            textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
            setSkip(false);
        }
        if (isInitialize.equalsIgnoreCase("yes")) {
            textView.setText("*");
            textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
        }


//        textView.setText(cardName);
        textView.setGravity(android.view.Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource
        textView.setTextSize(15);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);

        trackerPanel.addView(textView);
        scrollToEnd();

//        }
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


        cardDatabaseHelper.open();
        List<String> list = cardDatabaseHelper.getAllCardNames();
        cardDatabaseHelper.close();

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

        String currentBrain = MainActivity.getBrainName();
        if (currentBrain.equalsIgnoreCase(Brains.ZIGZAG_STREAK)) {
            ZigZagBrain();
        }


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

    private void ZigZagBrain() {

        cardDatabaseHelper.open();
        List<Card> list = cardDatabaseHelper.getAllCards();
        cardDatabaseHelper.close();

        trackerPanel.removeAllViews();

        for (Card card : list) {
            System.out.println(card.toString());
            setPredictionResource(card);
            setTrackerView(card);
        }
    }

    private void setPredictionResource(Card card) {

        String cardName = card.getName();

        String prediction = "";
        if (cardName.equalsIgnoreCase("P")) {
            prediction = "Banker";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.banker_button));
        } else if (cardName.equalsIgnoreCase("B")) {
            prediction = "Player";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.player_button));
        } else {
            prediction = "Wait";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.wait));
        }

        if (card.getWait().equalsIgnoreCase("Yes")) {
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.wait));
        }

        if (cardName.equalsIgnoreCase("Reset")) {
            prediction = "Wait";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.wait));
        }

        txtPrediction.setText(prediction);
//        }


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