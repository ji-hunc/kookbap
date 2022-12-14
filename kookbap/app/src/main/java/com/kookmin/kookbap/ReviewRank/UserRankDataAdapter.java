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

public class UserRankDataAdapter extends RecyclerView.Adapter<UserRankDataAdapter.BestReviewerViewHolder>{
    private ArrayList<UserRankData> bestReviewerData;
    Context context;

    public UserRankDataAdapter(ArrayList<UserRankData> bestReviewerData, Context context){
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
        holder.rank.setText(position+1+"등");
        holder.name.setText(bestReviewerData.get(position).getNickname()+"");
        holder.reviewCount.setText(bestReviewerData.get(position).getTotal()+"개");
    }

    @Override
    public int getItemCount() {
        return this.bestReviewerData.size();
    }
//    public void setBestReviewerData(ArrayList<UserRankData> bestReviewerData){
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
