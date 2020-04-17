package org.dev4u.hv.my_diagnostic.FragmentsIntro;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.dev4u.hv.my_diagnostic.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_disclaimer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_disclaimer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Fragment_disclaimer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondIntroductionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_disclaimer newInstance(String param1, String param2) {
        Fragment_disclaimer fragment = new Fragment_disclaimer();
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

    View view;
    public static CheckBox chkAgree=null;
    private Button btnShowTerms;
    public static boolean isAgreeChecked=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view         = inflater.inflate(R.layout.fragment_disclaimer, container, false);
        chkAgree     = (CheckBox) view.findViewById(R.id.checkTerms);
        btnShowTerms = (Button) view.findViewById(R.id.lblViewAll);
        btnShowTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        chkAgree.setChecked(isAgreeChecked);
        chkAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgreeChecked = isChecked;
            }
        });
        return view;
    }


    private void dialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(R.string.terms_and_conditions);
        WebView wv = new WebView(getContext());
        wv.loadUrl("file:///android_asset/terms.html");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
