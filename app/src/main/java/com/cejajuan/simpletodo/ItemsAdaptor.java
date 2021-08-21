package com.cejajuan.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/* Implementing the adapter and view holder for recycler view

 * The ViewHolder is a wrapper around a View that contains the layout for
 * an individual item in the list.
 * The Adapter creates ViewHolder objects as needed, and also sets the data
 * for those views.
 * The process of associating views to their data is called binding.
 *
 *  # onCreateViewHolder(): RecyclerView calls this method whenever it needs to
 * create a new ViewHolder. The method creates and initializes the ViewHolder
 * and its associated View, but does not fill in the view's contents—the
 * ViewHolder has not yet been bound to specific data.
 *
 * # onBindViewHolder(): RecyclerView calls this method to associate a ViewHolder
 * with data. The method fetches the appropriate data and uses the data to fill
 * in the view holder's layout. For example, if the RecyclerView displays a list
 * of names, the method might find the appropriate name in the list and fill in
 * the view holder's TextView widget.
 *
 * # getItemCount(): RecyclerView calls this method to get the size of the data set.
 * For example, in an address book app, this might be the total number of addresses.
 * RecyclerView uses this to determine when there are no more items that can be displayed.
 * */

// Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdaptor extends RecyclerView.Adapter<ItemsAdaptor.ViewHolder> {
    public interface OnLongClickLister {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickLister longClickLister;

    public ItemsAdaptor(List<String> items, OnLongClickLister longClickListener) {
        this.items = items;
        this.longClickLister = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // use a layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        // wrap the view in a holder and return it
        return new ViewHolder(todoView);
    }

    // Responsible for binding to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grab the item at the position
        String item = items.get(position);

        // bind the item into the specified view holder
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provided easy access to views that represent each row of
    // the list
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // notify the listener which position was long pressed
                    longClickLister.onItemLongClicked(getAdapterPosition());
                    return false;
                }
            });
        }
    }

}
