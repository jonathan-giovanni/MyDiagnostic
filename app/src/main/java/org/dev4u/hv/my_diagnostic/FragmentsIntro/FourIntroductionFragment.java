package org.dev4u.hv.my_diagnostic.FragmentsIntro;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.dev4u.hv.my_diagnostic.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourIntroductionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourIntroductionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CheckBox check;
    private SharedPreferences savedData;
    private SharedPreferences.Editor editSavedData;


    public FourIntroductionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourIntroductionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FourIntroductionFragment newInstance(String param1, String param2) {
        FourIntroductionFragment fragment = new FourIntroductionFragment();
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
        View view = inflater.inflate(R.layout.fragment_four_introduction, container, false);

        savedData = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editSavedData = savedData.edit();

        return view;
    }

}
