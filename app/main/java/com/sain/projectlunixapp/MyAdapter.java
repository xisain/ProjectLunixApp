package com.sain.projectlunixapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sain.projectlunixapp.model.Agent;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Agent> dataList;

    public MyAdapter(Context context, List<Agent> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImg()).into(holder.recImage);
        holder.recAgent.setText(dataList.get(position).getName());
        holder.recDesc.setText(dataList.get(position).getDeskripsi());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id",dataList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImg());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDeskripsi());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Skill1", dataList.get(holder.getAdapterPosition()).getSkill().getSkill_1());
                intent.putExtra("Skill2", dataList.get(holder.getAdapterPosition()).getSkill().getSkill_2());
                intent.putExtra("Skill3", dataList.get(holder.getAdapterPosition()).getSkill().getSkill_3());
                intent.putExtra("ultimate", dataList.get(holder.getAdapterPosition()).getSkill().getUltimate());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Agent> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recDesc, recLang, recAgent;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recAgent = itemView.findViewById(R.id.recAgent);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
    }
}