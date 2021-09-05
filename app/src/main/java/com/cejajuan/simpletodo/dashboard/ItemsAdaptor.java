package com.cejajuan.simpletodo.dashboard;

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

    // Provide a reference to the type of views that you are using (custom ViewHolder).
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);

            // onclick listener used by the Main activity to check when the user
            // needs to update the text of a list item with items position
            tvItem.setOnClickListener(view -> {
                clickListener.onItemClick(getAdapterPosition());
            });

            // notify the listener which position was long pressed this is
            // a callback used to remove list item from the MainActivity class
            tvItem.setOnLongClickListener(view -> {
                longClickLister.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }

    // interfaces are implemented by the MainActivity class
    public interface OnClickListener {
        void onItemClick(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickLister;
    OnClickListener clickListener;

    public ItemsAdaptor(List<String> items, OnLongClickListener longClickListener,
                        OnClickListener clickListener) {
        this.items = items;
        this.longClickLister = longClickListener;
        this.clickListener = clickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // use a layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        // wrap the view in a ViewHolder and return it
        return new ViewHolder(todoView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the item into the specified view holder
        holder.bind(items.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
