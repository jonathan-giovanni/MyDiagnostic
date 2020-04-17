package utils;

/**
 * Created by admin on 22/6/17.
 */


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.R;

import java.util.ArrayList;

import db.Symptom;


/**
 * Created by Jaison on 08/10/16.
 */

public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.MyViewHolder> {

    public ArrayList<Symptom> symptomArrayList =new ArrayList<>();
    public ArrayList<Symptom> selected_symptom =new ArrayList<>();
    Context mContext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CardView ll_listitem;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.lblSymptom);
            ll_listitem=(CardView)view.findViewById(R.id.item_symptom);

        }
    }


    public MultiSelectAdapter(Context context,ArrayList<Symptom> symptomArrayList,ArrayList<Symptom> selectedList) {
        this.mContext=context;
        this.symptomArrayList = symptomArrayList;
        this.selected_symptom = selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_symptom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Symptom symptom = symptomArrayList.get(position);
        holder.name.setText(symptom.getSymptom());

        if(selected_symptom.contains(symptomArrayList.get(position)))
            holder.ll_listitem.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.ll_listitem.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

    }

    @Override
    public int getItemCount() {
        return symptomArrayList.size();
    }
}