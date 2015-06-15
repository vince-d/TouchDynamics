package com.teco.vindi.touchdynamics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ScrollViewAdapter extends RecyclerView.Adapter<ScrollViewAdapter.ViewHolder> {
    private ScrollViewData[] itemsData;


    /**
     * Creates the adapter with the given set of Data.
     * @param itemsData the data.
     */
    public ScrollViewAdapter(ScrollViewData[] itemsData) {
        this.itemsData = itemsData;
    }

    /**
     * Called, when the RecyclerView needs a new ViewHolder to display data of the given type. Inflate view of
     * given type, set references and return it ViewHolder containing the references. We only have one type right now.
     * @param parent The parent of the view to be inflated.
     * @param viewType The type of the view to be inflated.
     * @return The ViewHolder holding the references to the interesting parts of the inflated views.
     */
    @Override
    public ScrollViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate view from XML.
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_item_layout, null);

        // Create ViewHolder and set references.
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    /**
     * Called when the data at the given array position should be displayed. Get the data and update the views by
     * using the references contained in the given ViewHolder.
     * @param viewHolder the ViewHolder with the references.
     * @param position the position of the data in the data array.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Replace data.
        viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
        viewHolder.imgViewIcon.setImageResource(itemsData[position].getImageUrl());
    }

    /**
     * Returns the number of items in this recycler.
     * @return the number of items.
     */
    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    /**
     * The ViewHolder. Sets all references when one is created.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // The type. Not used, as we only have one type of item.
        public static final int TYPE = 0;

        // The references to the text and image.
        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }

        public int getType() {
            return this.TYPE;
        }
    }

}