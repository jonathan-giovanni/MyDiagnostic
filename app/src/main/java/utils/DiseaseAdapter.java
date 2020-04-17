package utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.Fragments.DiseaseDetailFragment;
import org.dev4u.hv.my_diagnostic.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import db.Disease;
import db.Symptom;

/**
 * Created by admin on 23/6/17.
 */

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> implements Filterable {
    //private FragmentActivity myContext;
    Context mContext;
    public ArrayList<Disease> diseaseArrayList = new ArrayList<>();
    private ArrayList<Disease> mOriginalValues = new ArrayList<>();
    boolean isSearch = false;
    String textSearch = "";
    static ArrayList<Integer> lstIcons;


    public static ArrayList<Integer> getIcons(){
        lstIcons = new ArrayList<>();
        lstIcons.add(R.drawable.ic_cat_1);
        lstIcons.add(R.drawable.ic_cat_2);
        lstIcons.add(R.drawable.ic_cat_3);
        lstIcons.add(R.drawable.ic_cat_4);
        lstIcons.add(R.drawable.ic_cat_5);
        lstIcons.add(R.drawable.ic_cat_6);
        lstIcons.add(R.drawable.ic_cat_7);
        lstIcons.add(R.drawable.ic_cat_8);
        lstIcons.add(R.drawable.ic_cat_9);
        lstIcons.add(R.drawable.ic_cat_10);
        lstIcons.add(R.drawable.ic_cat_11);
        lstIcons.add(R.drawable.ic_cat_12);
        lstIcons.add(R.drawable.ic_cat_13);
        lstIcons.add(R.drawable.ic_cat_14);
        lstIcons.add(R.drawable.ic_cat_15);
        lstIcons.add(R.drawable.ic_cat_16);
        lstIcons.add(R.drawable.ic_cat_17);
        lstIcons.add(R.drawable.ic_cat_18);
        return lstIcons;
    }

    public DiseaseAdapter(Context mContext, ArrayList<Disease> diseaseArrayList, boolean isSearch) {
        this.mContext = mContext;
        this.diseaseArrayList = diseaseArrayList;
        this.mOriginalValues = diseaseArrayList;
        this.isSearch = isSearch;
        getIcons();
    }
    @Override
    public long getItemId(int position) {
        if(position>-1) return Long.parseLong(diseaseArrayList.get(position).getId_disease());
        return -1;
    }

    @Override
    public DiseaseAdapter.DiseaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiseaseAdapter.DiseaseViewHolder holder, int position) {
        final Disease disease = diseaseArrayList.get(position);

        final SpannableStringBuilder sb = new SpannableStringBuilder(disease.getName_disease());
        final SpannableStringBuilder descriptionBuild = new SpannableStringBuilder(disease.getDescription());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(mContext.getResources().getColor(R.color.title_disease));
        sb.setSpan(fcs, 0, disease.getName_disease().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        if (disease.getName_disease().toLowerCase().startsWith(textSearch)) {
            final ForegroundColorSpan highlight = new ForegroundColorSpan(Color.CYAN);
            sb.setSpan(highlight, 0, textSearch.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        holder.diseaseName.setText(sb);
        holder.description.setText(descriptionBuild);
        holder.symptomsCount.setText(mContext.getString(R.string.symptoms) + disease.getSymptoms().size());
        holder.category.setText(mContext.getString(R.string.category) + disease.getCategory_name());
        if (isSearch) {
            holder.percentage.setText(new DecimalFormat("###.##").format(disease.getMatchPercentage())+"%");
            holder.symptomsMatch.setText(mContext.getString(R.string.matches) + disease.getSymptoms_match());
        }else{
            holder.percentage.setVisibility(View.INVISIBLE);
            holder.symptomsMatch.setVisibility(View.INVISIBLE);
        }

        int id_category = Integer.parseInt(disease.getId_disease_category())-1;
        //Log.d("id guardado : ", "======== " + id_category);
        //if(id_categoriy > -1) este es el cambio tony
        if (id_category > 0 && id_category < lstIcons.size()) {
            holder.imgDisease.setImageResource(lstIcons.get(id_category));
          //  Log.d("id guardado : ","mostrado ======== "+id_category+" w "+lstIcons.get(id_category));
        }
    }

    @Override
    public int getItemCount() {
        return diseaseArrayList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Disease> FilteredArrList = new ArrayList<Disease>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Disease>(diseaseArrayList); // saves the original data in mOriginalValues
                }
                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                    textSearch = "";
                } else {
                    constraint = constraint.toString().toLowerCase();
                    textSearch = constraint.toString();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getName_disease();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(mOriginalValues.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                diseaseArrayList = (ArrayList<Disease>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }
        };
        return filter;
    }

    public class DiseaseViewHolder extends RecyclerView.ViewHolder  {
        public TextView diseaseName;
        public TextView symptomsCount;
        public TextView symptomsMatch;
        public TextView percentage;
        public TextView description;
        public TextView category;
        public CardView cardView;
        public ImageView imgDisease;
        public DiseaseViewHolder(View view) {
            super(view);
            diseaseName = (TextView) view.findViewById(R.id.lblDiseaseName);
            symptomsCount = (TextView) view.findViewById(R.id.lblSymptomsCount);
            percentage = (TextView) view.findViewById(R.id.lblPercentage);
            description = (TextView) view.findViewById(R.id.lblDescription);
            symptomsMatch = (TextView) view.findViewById(R.id.lblSymptomsMatches);
            category = (TextView) view.findViewById(R.id.lblCategory);
            cardView = (CardView) view.findViewById(R.id.item_symptom);
            imgDisease = (ImageView) view.findViewById(R.id.img_disease);
        }
    }
}



