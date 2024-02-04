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
import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {

    private UpdateBaseBetAmount updateBaseBetAmount;

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


    public interface UpdateBaseBetAmount {
        void onUpdateBaseBetAmount(String newText);
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

        trackerPanel = view.findViewById(R.id.trackerPanel);
        trackerPanelHorizontalScroller = view.findViewById(R.id.trackerPanelHorizontalScroller);
        tableLayout = view.findViewById(R.id.tableLayoutBeadRoad);
        txtPrediction = view.findViewById(R.id.txtPrediction);
        txtSkip = view.findViewById(R.id.txtSkip);
        txtPlayerHandCount = view.findViewById(R.id.txtPlayerHandCount);
        txtbankerHandCount = view.findViewById(R.id.txtbankerHandCount);
        txtHand = view.findViewById(R.id.txtHand);
        txtBaseBet = view.findViewById(R.id.txtBaseBet);

        preferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String BaseUnitAmount = preferences.getString(BASE_UNIT, String.valueOf(baseBetAmount));
        txtBaseBet.setText(BaseUnitAmount);


        AppCompatButton btnPlayer = view.findViewById(R.id.btnPlayer);
        AppCompatButton btnReset = view.findViewById(R.id.btnReset);
        AppCompatButton btnBanker = view.findViewById(R.id.btnBanker);
        AppCompatButton btnSkip = view.findViewById(R.id.btnSkip);
        AppCompatButton btnUndo = view.findViewById(R.id.btnUndo);

        //get the previous data
        List<String> list = MainActivity.getCards();
        setBeadRoadView(6, 20, 50, list);


        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickedSound();
                isUndo = false;
                MainActivity.setCards("P");
                List<String> list = MainActivity.getCards();
                setBeadRoadView(6, 20, 50, list);


            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickedSound();
                isUndo = false;

                MainActivity.clearCards();

                List<String> list = MainActivity.getCards();
                setBeadRoadView(6, 20, 50, list);
                trackerPanel.removeAllViews();
                setPredictionResource("wait");

            }
        });

        btnBanker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playClickedSound();
                isUndo = false;

                MainActivity.setCards("B");
                List<String> list = MainActivity.getCards();
                setBeadRoadView(6, 20, 50, list);


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
                List<String> list = MainActivity.getCards();
                if (list.size() > 0) {
                    isUndo = true;
                    playClickedSound();
                    removeLastItemFromRoad();

                }
            }
        });

        return view;
    }

    public void setBaseBetAmount(String amount) {
        if (txtBaseBet != null) {
            txtBaseBet.setText(amount);
        }
    }

    private void removeLastItemFromRoad() {

        int childCount = trackerPanel.getChildCount();
        if (childCount > 0) {
            try {
                // Remove the last child view
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.removeLastItemFromTrackerList();

// Check if there is at least one child
//                                if (trackerPanel.getChildCount() > 0) {
//                                    // Remove the last child
//                                    trackerPanel.removeViewAt(trackerPanel.getChildCount() - 1);
//                                }


                                MainActivity.removeLastCard();
//                                // Now that the view is removed, execute the other operations
                                List<String> list = MainActivity.getCards();
                                // Ensure setBeadRoadView method handles the list correctly
                                setBeadRoadView(6, 20, 50, list);


                            }
                        });
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//-----------------------------------------------------------------


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

    private void setTrackerView(String input) {


        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                60, // width
                60);
        layoutParams.setMargins(1, 1, 1, 1);
        textView.setLayoutParams(layoutParams);

        if (input.equals("W")) {
            textView.setBackground(getResources().getDrawable(R.drawable.win));
        } else {
            textView.setBackground(getResources().getDrawable(R.drawable.button_banker));
        }

//
//        if (MainActivity.getCards().size() == 1) {
//            textView.setText("*");
//            textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
//        } else {
        textView.setText(input);
//        }

        if (txtSkip.getText().equals("Yes")) {
            textView.setBackground(getResources().getDrawable(R.drawable.button_skip));
            setSkip(false);
        }


        textView.setGravity(android.view.Gravity.CENTER);

        textView.setTextColor(getResources().getColor(R.color.white)); // Replace with your color resource
        textView.setTextSize(15);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);

        trackerPanel.addView(textView);
        scrollToEnd();


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

    public void updateSelectedFragment(String name) {
        String selectedFragmentName = name;
        if (txtTitle != null) {
            txtTitle.setText(selectedFragmentName);
        }
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

    public void setBeadRoadView(int numRows, int numColumns, int textViewSize, List<String> listOfItemFromRoad) {
        String[][] table = new String[numRows][numColumns];
        fillTable(table, listOfItemFromRoad);

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

        validateResultBaseOnBrain(listOfItemFromRoad);

        countThePlayerBanker(listOfItemFromRoad);
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

    private void validateResultBaseOnBrain(List<String> list) {

        String currentBrain = MainActivity.getBrainName();
        if (currentBrain.equalsIgnoreCase(Brains.ZIGZAG_STREAK)) {
            ZigZagBrain(list);
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

    private void ZigZagBrain(List<String> list) {

        Optional<List<String>> optionalList = Optional.ofNullable(list);
        if (optionalList.isPresent() && !optionalList.get().isEmpty()) {


            String item = getLastCard(list);

            String status = "";
            String prediction = getFirstLetterFromString(txtPrediction.getText().toString());

            // -------------Logic of ZigZag---------------------
            if (item.equalsIgnoreCase(prediction)) {
                status = "W";
            } else {
                status = "L";
            }
            //-------------------------------------------------
            if (!isUndo) {
                MainActivity.setTrackerList(status);
            }

            List<String> trackerList = MainActivity.getTrackerList();

            setPredictionResource(item);

            trackerPanel.removeAllViews();
            for (String t : trackerList) {
                setTrackerView(t);
            }

            View firstChild = trackerPanel.getChildAt(0);
//            // Check if the first child is a TextView
            if (firstChild instanceof TextView) {
                // Update the text of the TextView
                ((TextView) firstChild).setText("*");
                ((TextView) firstChild).setBackground(getResources().getDrawable(R.drawable.button_skip));
            }
        }

    }

    private void setPredictionResource(String item) {

        String prediction = "";
        if (item.equalsIgnoreCase("P")) {
            prediction = "Banker";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.banker_button));
        } else if (item.equalsIgnoreCase("B")) {
            prediction = "Player";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.player_button));
        } else {
            prediction = "Wait";
            txtPrediction.setBackgroundColor(getResources().getColor(R.color.wait));
        }


        txtPrediction.setText(prediction);

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