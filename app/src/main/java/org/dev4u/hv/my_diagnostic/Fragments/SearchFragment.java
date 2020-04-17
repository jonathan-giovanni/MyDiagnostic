package org.dev4u.hv.my_diagnostic.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.dev4u.hv.my_diagnostic.R;
import org.dev4u.hv.my_diagnostic.UserDataActivity;

import db.Symptom;
import utils.AutoCompleteAdapter;
import utils.DiseaseUtilitesSingleton;
import utils.MultiSelectAdapter;
import utils.RecyclerItemClickListener;

import android.view.ActionMode;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {


    //widgets
    private ViewGroup container;
    private CoordinatorLayout coordinatorLayout;
    private View view;
    private Button btnAddSymptom;
    private AutoCompleteTextView txtSymptom;
    private AutoCompleteAdapter adapter;
    private FloatingActionButton btnSearch;
    private RecyclerView recyclerView;

    //for multi select
    private ActionMode mActionMode;
    private Menu context_menu;
    private MultiSelectAdapter multiSelectAdapter;
    boolean isMultiSelect = false;
    private ArrayList<Symptom> multiselect_list = new ArrayList<>();


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.container = container;
        //adding option menu
        setHasOptionsMenu(true);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        view = inflater.inflate(R.layout.fragment_search, container, false);
        //init widgets
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.frm_search);
        btnAddSymptom = (Button) view.findViewById(R.id.btnAddSymptom);
        txtSymptom = (AutoCompleteTextView) view.findViewById(R.id.txtSymptom);
        btnSearch = (FloatingActionButton) view.findViewById(R.id.btnSearchDisease);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDisease();
            }
        });
        btnAddSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSymptom();
            }
        });

        //update the list

        multiSelectAdapter = new MultiSelectAdapter(getContext(),DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms(),multiselect_list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(multiSelectAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else
                {

                }
            }
            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Symptom>();
                    isMultiSelect = true;
                    if (mActionMode == null) {
                        mActionMode =  getActivity().startActionMode(mActionModeCallback);
                    }
                }
                multi_select(position);
            }
        }));

        updateFragment();


        return view;
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms().get(position)))
                multiselect_list.remove(DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms().get(position));
            else
                multiselect_list.add(DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms().get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else {
                mActionMode.setTitle("");
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }

            refreshAdapter();

        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_search_fragment,menu);
    }

    public void refreshAdapter()
    {
        multiSelectAdapter.selected_symptom=multiselect_list;
        multiSelectAdapter.symptomArrayList=DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms();
        multiSelectAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_delete_symptoms:
                deleteAllSymptoms();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.delete_menu, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    showDialogPicture();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Symptom>();
            refreshAdapter();
        }
    };

    // AlertDialog Callback Functions


    private void deleteAllSymptoms(){
        DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms().clear();
        multiSelectAdapter.notifyDataSetChanged();
    }


    private void addSymptom(){

        if(DiseaseUtilitesSingleton.getInstance().getSymptomsNames().contains(txtSymptom.getText().toString())){
            Symptom temporary = new Symptom("",txtSymptom.getText().toString());
            hideKeyBoard();
            for (Symptom s:DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms()) {
                if(s.getSymptom().equals(txtSymptom.getText().toString())){
                    Snackbar.make(coordinatorLayout,getString(R.string.symptom_is_already_added)+txtSymptom.getText().toString(),Snackbar.LENGTH_LONG).show();
                    txtSymptom.setText("");
                    return;
                }
            }
            txtSymptom.setText("");
            Snackbar.make(coordinatorLayout,getString(R.string.symptom_added)+txtSymptom.getText().toString(),Snackbar.LENGTH_LONG).show();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms().add(temporary);
            multiSelectAdapter.notifyDataSetChanged();
        }else{
            hideKeyBoard();
            Snackbar.make(coordinatorLayout,getString(R.string.do_not_exit_this_symptom)+txtSymptom.getText().toString(),Snackbar.LENGTH_LONG).show();
        }
    }
    public void updateFragment(){
        if(txtSymptom!=null) {
            adapter = new AutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                    DiseaseUtilitesSingleton.getInstance().getSymptomsNames());
            txtSymptom.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void hideKeyBoard(){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    private void showDialogPicture(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(R.string.warning);
        dialogBuilder.setMessage(R.string.delete_this_symptom);
        dialogBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(multiselect_list.size()>0)
                {
                    for(int i=0;i<multiselect_list.size();i++)
                        DiseaseUtilitesSingleton.getInstance().getTemporarySymptoms().remove(multiselect_list.get(i));
                    multiSelectAdapter.notifyDataSetChanged();
                    if (mActionMode != null) {
                        mActionMode.finish();
                    }

                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void searchDisease(){
        if (mFragmentNavigation != null) {
            mFragmentNavigation.pushFragment(new SearchDiseaseFragment());
        }
    }

}
