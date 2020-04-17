package org.dev4u.hv.my_diagnostic.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.dev4u.hv.my_diagnostic.MyPlacesUI.Parser;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.Place;
import org.dev4u.hv.my_diagnostic.R;

import java.util.ArrayList;

import db.Disease;
import db.Symptom;
import utils.DiseaseAdapter;
import utils.DiseaseUtilitesSingleton;
import utils.RecyclerItemClickListener;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDiseaseFragment extends BaseFragment {

    private View view;

    private TextView lblMinMatching;
    private Button btnRefresh;
    private RangeBar rngMatch;
    private RecyclerView recyclerView;
    private MenuItem item;
    private DiseaseAdapter diseaseAdapter;
    private ArrayList<String> inputs;
    private ArrayList<Disease> diseasesFound;
    private Fragment fragment;
    private int percentage;

    private SharedPreferences savedData;

    public SearchDiseaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_disease, container, false);
        setHasOptionsMenu(true);

        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        savedData       = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);

        percentage      = savedData.getInt("PERCENTAGE",20);

        rngMatch        = (RangeBar) view.findViewById(R.id.searchRangeBar);
        lblMinMatching  = (TextView) view.findViewById(R.id.lblMinMatchingSearch);
        btnRefresh      = (Button)   view.findViewById(R.id.btnRefreshSearch);
        recyclerView    = (RecyclerView) view.findViewById(R.id.recycler_view_disease_search);

        //set default value
        rngMatch.setRangePinsByValue(1,percentage);
        lblMinMatching.setText(getString(R.string.min_matching)+percentage+"%");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        inputs = new ArrayList<>();
        for (Symptom s:DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms()) {
            inputs.add(s.getSymptom());
        }

        diseasesFound = DiseaseUtilitesSingleton.getInstance().getDiseasesMatches(inputs);
        diseaseAdapter = new DiseaseAdapter(
                getContext(),
                diseasesFound,
                true
        );
        recyclerView.setAdapter(diseaseAdapter);

        rngMatch.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                lblMinMatching.setText(getString(R.string.min_matching)+rightPinValue+"%");
            }
        });
        //on click update the search
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMatching();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id_disease  = Long.toString(diseaseAdapter.getItemId(position));

                if(diseaseAdapter.getItemId(position)<0) return;
                fragment = new DiseaseDetailFragment();
                Bundle args = new Bundle();
                args.putString("ID_DISEASE",id_disease);
                args.putBoolean("SEARCH",true);
                fragment.setArguments(args);
                if (mFragmentNavigation != null) {
                    mFragmentNavigation.pushFragment(fragment);
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));
        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_find, menu);
        item = menu.findItem(R.id.action_search);

        Drawable drawable = menu.findItem(R.id.action_search).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        SearchView sv = new SearchView(((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchTap(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchTap(String s){
        diseaseAdapter.getFilter().filter(s);
    }

    public void searchMatching(){
        diseasesFound.clear();
        DiseaseUtilitesSingleton.getInstance().minimunPercentage = Float.parseFloat(rngMatch.getRightPinValue());
        Log.d("Valor float "," "+DiseaseUtilitesSingleton.getInstance().minimunPercentage );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                diseasesFound.addAll(DiseaseUtilitesSingleton.getInstance().getDiseasesMatches(inputs));
                diseaseAdapter.notifyDataSetChanged();
            }
        });

    }

}
