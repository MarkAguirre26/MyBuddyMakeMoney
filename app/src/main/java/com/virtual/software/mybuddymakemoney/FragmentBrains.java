package com.virtual.software.mybuddymakemoney;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBrains#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBrains extends Fragment {

    private static final String PREF_NAME = "your_preference_name";
    SharedPreferences preferences;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Brains> brains;

    public FragmentBrains() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBrains.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBrains newInstance(String param1, String param2) {
        FragmentBrains fragment = new FragmentBrains();
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
        View view = inflater.inflate(R.layout.fragment_strategies, container, false);


        GetMoneyManagementList(view);
        return view;
    }

    private void GetMoneyManagementList(View view) {

        ListView listView = view.findViewById(R.id.listViewStrategy);
        brains = new Brains().getBrains();
        try {
            updateBrainsListStatus(new MainActivity().getBrainName());
        } catch (Exception e){

        }


        BrainsAdapter adapter = new BrainsAdapter(getContext(), brains);
        listView.setAdapter(adapter);
        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle the item click
                Brains brain = brains.get(position);
//                boolean clickedItemState = !brain.getSelected();
                String bname = brain.getName();
                MainActivity mainActivity =   new MainActivity();
                mainActivity.setBrainName(bname);
                updateBrainsListStatus(bname);


                // For example, you can do something with the clicked item
                // (e.g., display a message, start a new activity, etc.)
                // Toast.makeText(MainActivity.this, "Clicked: " + clickedItem.getFieldName(), Toast.LENGTH_SHORT).show();
                adapter.setSelectedPosition(position);
            }
        });

    }

    private void updateBrainsListStatus(String mm) {

        for (int i = 0; i < brains.size(); i++) {
            Brains item = brains.get(i);

            if (item.getName().equals(mm)) {

                brains.set(i, new Brains(item.getId(), item.getName(), true));
            } else {

                brains.set(i, new Brains(item.getId(), item.getName(), false));
            }
        }
    }


}