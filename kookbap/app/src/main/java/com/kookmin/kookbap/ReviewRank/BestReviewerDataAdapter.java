package com.kookmin.kookbap.ReviewRank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kookmin.kookbap.R;

import java.util.ArrayList;

public class BestReviewerDataAdapter extends RecyclerView.Adapter<BestReviewerDataAdapter.BestReviewerViewHolder>{
    private ArrayList<RankData> bestReviewerData;
    Context context;

    public BestReviewerDataAdapter(ArrayList<RankData> bestReviewerData, Context context){
        this.bestReviewerData = bestReviewerData;
        this.context = context;
    }

    @NonNull
    @Override
    public BestReviewerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_reviewer_card,parent,false);
        return new BestReviewerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestReviewerViewHolder holder, int position) {
        holder.rank.setText(bestReviewerData.get(position).getNickname()+"");
        holder.name.setText(bestReviewerData.get(position).getNickname()+"");
        holder.reviewCount.setText(bestReviewerData.get(position).getTotal()+"");
    }

    @Override
    public int getItemCount() {
        return this.bestReviewerData.size();
    }
//    public void setBestReviewerData(ArrayList<RankData> bestReviewerData){
//        this.bestReviewerData = bestReviewerData;
//        notifyDataSetChanged();
//    }


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
    }
}
