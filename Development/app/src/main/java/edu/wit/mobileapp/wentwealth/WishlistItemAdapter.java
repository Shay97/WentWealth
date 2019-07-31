package edu.wit.mobileapp.wentwealth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class WishlistItemAdapter extends RecyclerView.Adapter<WishlistItemViewHolder> {

    private List<WishlistItemObject> mList;
    Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        public void onItemClicked(int index, WishlistItemViewHolder viewHolder);

    }

    public WishlistItemAdapter(Context context, List<WishlistItemObject> list,OnItemClickListener listener){
        mListener=listener;
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public WishlistItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        mInflater= (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.wishlist_view_item, viewGroup,false);

        return new WishlistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistItemViewHolder itemViewHolder, int i) {
        itemViewHolder.init(mListener,i,mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
