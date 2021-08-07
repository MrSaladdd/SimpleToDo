package com.example.mysimpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Responsible for displaying data from the model into a row in the recycler view
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    //---Interfaces---
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    //---Variables---
    private List<String> items;
    private ItemAdapter.OnLongClickListener longClickListener;
    private ItemAdapter.OnClickListener clickListener;

    //---Constructor---
    public ItemAdapter(List<String> items, ItemAdapter.OnLongClickListener longClickListener, ItemAdapter.OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    //---Methods---

    // inflate the item layout and create the holder
    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the android layout
        View todoView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        // Return a new holder instance
        // wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }


    // responsible for binding data to a particular view holder
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        // Grab the item at the position
        // Get the data model based on position
        String item = items.get(position);
        // Bind the item at the specified view holder
        holder.bind(item);
        holder.removeItem();
        holder.attachActivity();
    }

    // Tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each row of the list
    public class ViewHolder extends RecyclerView.ViewHolder {

        //---Variables---
        public TextView tvItem;

        //---Constructor---
        public ViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //---Methods---
        // Update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
        }

        // Removing an item from the container
        public void removeItem() {
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notify the listener which position was long pressed.
                    longClickListener.onItemLongClicked(getBindingAdapterPosition());
                    return true;
                }
            });
        }

        public void attachActivity() {
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Return the position of the item clicked on the view
                    clickListener.onItemClicked(getBindingAdapterPosition());
                }
            });
        }
    }
}
