package com.zybooks.projecttwo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

    public final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<WeightLog> log;


    // This MyAdapter class is used in recyclerView.setAdapter() in the displayDatabaseInfo.java file to properly
    // show each element in the weight log array in a neat format
    //MyAdapter Constructor that receives the context and data structure arguments
    public MyAdapter(Context context, List<WeightLog> log, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.log = log;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    //
    // This creates the Viewholder along with view.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
        //return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false, recyclerViewInterface));
    }

    // This binds the data to the viewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.commentView.setText(log.get(position).getComment());
        holder.dateView.setText(log.get(position).getDate());

        String currentWeightString = String.valueOf(log.get(position).getCurrentWeight()) + " lbs";
        holder.weightView.setText(currentWeightString);



    }
    // this function is utilized to check whether there are items in a list.
    @Override
    public int getItemCount() {
        return log.size();
    }
}
