package org.dev4u.hv.my_diagnostic.Fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.R;
import org.dev4u.hv.my_diagnostic.UserDataActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import db.MedicalHistory;
import db.Symptom;
import db.User;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.DiseaseUtilitesSingleton;
import utils.HistoryAdapter;
import utils.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment {

    private View view;
    private ImageView hearth;
    private AnimatedVectorDrawable hearthAnim;
    private Bitmap activePicture;
    private ImageView imageFlag;
    private CircleImageView circleImageView;
    private User user;
    private TextView lblUsername;
    private TextView lblBirtday;
    private TextView lblBloodtype;
    private TextView lblRecordCount;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;
    private RecyclerView recyclerView;
    private Fragment fragment ;

    private FloatingActionButton btnAddHistory;

    private ActionMode mActionMode;
    private Menu context_menu;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view            = inflater.inflate(R.layout.fragment_history, container, false);

        //remove home button
        setHasOptionsMenu(true);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //animation
        hearth          = (ImageView)view.findViewById(R.id.pulse);
        hearthAnim      = ((AnimatedVectorDrawable) ((ImageView) view.findViewById(R.id.pulse)).getDrawable());
        //controls

        activePicture   = ((BitmapDrawable) getActivity().getBaseContext().getDrawable(R.drawable.ic_profile)).getBitmap();
        circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
        imageFlag       = (ImageView) view.findViewById(R.id.imgFlag);
        lblUsername     = (TextView) view.findViewById(R.id.lblFullname);
        lblBirtday      = (TextView) view.findViewById(R.id.lblBirthday);
        lblBloodtype    = (TextView) view.findViewById(R.id.lblBloodtype);
        lblRecordCount  = (TextView) view.findViewById(R.id.lblMedicalHistoryCount);
        recyclerView    = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        btnAddHistory   = (FloatingActionButton) view.findViewById(R.id.btnAddHistory);


        DiseaseUtilitesSingleton.getInstance().historyAdapter = new HistoryAdapter(getContext(),DiseaseUtilitesSingleton.getInstance().getMedicalHistoryList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(DiseaseUtilitesSingleton.getInstance().historyAdapter);

        lblRecordCount.setText(""+DiseaseUtilitesSingleton.getInstance().historyAdapter.getItemCount());

        DiseaseUtilitesSingleton.getInstance().historyAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                lblRecordCount.setText(""+DiseaseUtilitesSingleton.getInstance().historyAdapter.getItemCount());
            }
        });

        AnimatedVectorDrawable d = (AnimatedVectorDrawable) getContext().getDrawable(R.drawable.hearth_pulse_animation); // Insert your AnimatedVectorDrawable resource identifier
        hearth.setImageDrawable(d);
        d.start();


        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartCursiveAnimation();
            }
        });


        //update values
        preferences = getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences = preferences.edit();

        updateUserInfo();

        btnAddHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mActionMode != null) {
                    mActionMode.finish();
                }

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());

                MedicalHistory medicalHistory = new MedicalHistory(null,getString(R.string.no_title),
                        getString(R.string.no_description)
                        ,"NULL"
                        ,DiseaseUtilitesSingleton.getInstance().getActiveUser().getUsername()
                        ,date);
                Log.d("Username ","Antes de guardar ["+DiseaseUtilitesSingleton.getInstance().getActiveUser().getUsername()+"]");
                medicalHistory.setName_disease(null);
                DiseaseUtilitesSingleton.getInstance().saveOrUpdateHistory(true,medicalHistory);
                DiseaseUtilitesSingleton.getInstance().historyAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Long long_id = DiseaseUtilitesSingleton.getInstance().historyAdapter.getItemId(position);
                String id_history  = Long.toString(long_id);


                if(long_id<0) return;
                fragment = new HistoryDetailFragment();
                Bundle args = new Bundle();
                args.putString("ID_HISTORY",id_history);
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


    private void restartCursiveAnimation() {
        hearthAnim.stop();
        hearthAnim.start();
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_find, menu);
        MenuItem item = menu.findItem(R.id.action_search);

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
                DiseaseUtilitesSingleton.getInstance().historyAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    /*

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
    }*/
    private Bitmap loadImageFromStorage(String path,String name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public void updateUserInfo(){
        Bitmap b = loadImageFromStorage("Profile","preview_profile.png");
        if(b!=null) {
            activePicture = b;
            circleImageView.setImageBitmap(activePicture);
        }

        String status = preferences.getString("USERNAME","null");
        if(!status.equals("null")){
            user = DiseaseUtilitesSingleton.getInstance().getUser(status);
            lblUsername.setText(user.getFullname());
            String parts[] = user.getBirthday().split("-");
            String bday = parts[2]+"/"+parts[1]+"/"+parts[0];
            lblBloodtype.setText(getString(R.string.blood_type)+user.getName_bloodtype());
            lblBirtday.setText(getString(R.string.birth_day)+bday);
            imageFlag.setImageResource(preferences.getInt("FLAG",R.drawable.flag_sv));
        }
    }

}
