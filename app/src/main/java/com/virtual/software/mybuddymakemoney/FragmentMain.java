package com.virtual.software.mybuddymakemoney;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TableLayout tableLayout;

    AppCompatButton btnWin;
    TextView txtTitle;

    List<String> list;

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

        
        tableLayout = view.findViewById(R.id.tableLayoutBeadRoad);
        AppCompatButton btn = view.findViewById(R.id.btnPlayer);
        AppCompatButton btnReset = view.findViewById(R.id.btnReset);
        AppCompatButton btnBanker = view.findViewById(R.id.btnBanker);

        //get the previous data
        List<String> list = MainActivity.getCards();
        setBeadRoadView(6, 20, 50, list);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickedSound();

                MainActivity.setCards("P");
                List<String> list = MainActivity.getCards();
                setBeadRoadView(6, 20, 50, list);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickedSound();

                MainActivity.clearCards();

                List<String> list = MainActivity.getCards();
                setBeadRoadView(6, 20, 50, list);


            }
        });

        btnBanker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playClickedSound();
                MainActivity.setCards("B");
                List<String> list = MainActivity.getCards();
                setBeadRoadView(6, 20, 50, list);

            }
        });

        return view;
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


}