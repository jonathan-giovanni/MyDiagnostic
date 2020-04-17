package org.dev4u.hv.my_diagnostic.Fragments;


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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.R;

import utils.DiseaseAdapter;
import utils.DiseaseUtilitesSingleton;
import utils.RecyclerItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiseaseFragment extends BaseFragment {

    private View view;
    private RecyclerView recyclerView;
    private DiseaseAdapter diseaseAdapter;
    private MenuItem item;
    private Fragment fragment ;
    public DiseaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_disease, container, false);
        setHasOptionsMenu(true);

        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_disease);

        diseaseAdapter = new DiseaseAdapter(getContext(),DiseaseUtilitesSingleton.getInstance().getDiseasesList(),false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(diseaseAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id_disease  = Long.toString(diseaseAdapter.getItemId(position));


                if(diseaseAdapter.getItemId(position)<0) return;
                fragment = new DiseaseDetailFragment();
                Bundle args = new Bundle();
                args.putString("ID_DISEASE",id_disease);
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
                System.out.println("search query submit");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
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
            case R.id.action_search:
                //handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchTap(String s){
        diseaseAdapter.getFilter().filter(s);
    }

}
