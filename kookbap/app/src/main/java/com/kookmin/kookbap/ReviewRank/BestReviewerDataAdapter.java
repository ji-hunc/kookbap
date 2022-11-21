package com.kookmin.kookbap.ReviewRank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kookmin.kookbap.R;

import java.util.ArrayList;

public class BestReviewerDataAdapter extends RecyclerView.Adapter<BestReviewerDataAdapter.BestReviewerViewHolder>{
    private ArrayList<BestReviewerData> bestReviewerData;

    @NonNull
    @Override
    public BestReviewerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_reviewer_card,parent,false);
        return new BestReviewerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestReviewerViewHolder holder, int position) {
        holder.onBind(bestReviewerData.get(position));
    }

    @Override
    public int getItemCount() {
        return bestReviewerData.size();
    }
    public void setBestReviewerData(ArrayList<BestReviewerData> bestReviewerData){
        this.bestReviewerData = bestReviewerData;
        notifyDataSetChanged();
    }


     class BestReviewerViewHolder extends RecyclerView.ViewHolder{
        TextView rank;
        TextView name;
        TextView reviewCount;
        public BestReviewerViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.reviewRank);
            name = itemView.findViewById(R.id.reviewerName);
            reviewCount = itemView.findViewById(R.id.reviewCount);
        }
        void onBind(BestReviewerData bestReviewerData){
            rank.setText(bestReviewerData.getRank()+"");
            name.setText(bestReviewerData.getName());
            reviewCount.setText(bestReviewerData.getCount()+"");
        }
    }
}
