package utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.R;

import java.util.ArrayList;

import db.Symptom;

/**
 * Created by admin on 7/7/17.
 */

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.SymptomViewHolder> {

    private ArrayList<String> symptomsWrite;
    private ArrayList<Symptom> symptomArrayList;
    private Context mContext;

    public SymptomAdapter(ArrayList<String> symptomsWrite, ArrayList<Symptom> symptomArrayList, Context mContext) {
        this.symptomsWrite = symptomsWrite;
        this.symptomArrayList = symptomArrayList;
        this.mContext = mContext;
    }

    @Override
    public SymptomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_symptom, parent, false);
        return new SymptomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SymptomViewHolder holder, int position) {
        final Symptom disease = symptomArrayList.get(position);
        holder.lblSymptom.setText(disease.getSymptom());
        if(symptomsWrite!=null){
            for (String s:symptomsWrite) {
                if(disease.getSymptom().equals(s))
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, android.R.color.holo_green_dark));
                else
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));
            }
        }
    }
    @Override
    public int getItemCount() {
        return symptomArrayList.size();
    }

    public class SymptomViewHolder extends RecyclerView.ViewHolder  {
        public TextView lblSymptom;
        public CardView cardView;
        public SymptomViewHolder(View view) {
            super(view);
            lblSymptom = (TextView) view.findViewById(R.id.lblSymptom);
            cardView = (CardView) view.findViewById(R.id.item_symptom);
        }
    }
}
