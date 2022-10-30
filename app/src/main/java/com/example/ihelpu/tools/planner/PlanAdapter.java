package com.example.ihelpu.tools.planner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihelpu.R;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    ArrayList<Plan> planArrayList;
    Context context;

    public PlanAdapter(ArrayList<Plan> planArrayList, Context context) {
        this.planArrayList = planArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlanAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_item,parent,false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.PlanViewHolder holder, int position) {
        Plan user = planArrayList.get(position);

        holder.title.setText(user.titleField);
        holder.description.setText(user.descriptionField);
        holder.date.setText(user.dateField);
        holder.time.setText(user.timeField);
    }

    @Override
    public int getItemCount() {
        return planArrayList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, date, time;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name_plan);
            description = itemView.findViewById(R.id.description_plan);
            date = itemView.findViewById(R.id.date_plan);
            time = itemView.findViewById(R.id.time_plan);
        }
    }
}
