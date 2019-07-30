package edu.wit.mobileapp.wentwealth;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class WishlistItemViewHolder extends RecyclerView.ViewHolder {

    public WishlistItemViewHolder(@NonNull View itemView) {
        super(itemView);

    }
    public void init(final WishlistItemAdapter.OnItemClickListener listener, final int index,WishlistItemObject itemObject){
        itemView.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(index, WishlistItemViewHolder.this);
            }
        });

        TextView name = itemView.findViewById(R.id.name);
        TextView price = itemView.findViewById(R.id.price);
        name.setText(itemObject.getItemName());
        price.setText(String.format("%d/%d", itemObject.getrBalance(), itemObject.getValue()));
    }
}
