package com.example.budgetorganizer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetorganizer.data.DbHelper;
import com.example.budgetorganizer.data.Gift;

import java.io.File;
import java.util.ArrayList;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {
    private Context mContext;
    private ArrayList<Gift> gifts;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    File file;
    final private ListItemClickListener mOnClickListener;
    public GiftAdapter(Context context, ListItemClickListener mOnClickListener, long person_id){
        this.mContext = context;
        this.mOnClickListener = mOnClickListener;
        openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
        gifts = DbHelper.getPersonsGift(db,person_id);
    }
    @Override
    public GiftAdapter.GiftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.person_item, parent, false);
        return new GiftAdapter.GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftAdapter.GiftViewHolder holder, int position) {
       if(gifts == null){
            Log.v("GiftRV","is empty");
       }else{
           Gift gift = gifts.get(position);
           holder.mGiftNameTextView.setText(gift.getName());
           holder.mGiftPriceTextView.setText(gift.getPrice()+"$");
           if(gift.getPhotoPath() == null){
               Drawable giftDrawable = mContext.getResources().getDrawable(R.drawable.gift);
               holder.mGiftImageView.setImageDrawable(giftDrawable);
           }else{
               file = new File(gift.getPhotoPath());
           }

           if(file != null && file.exists()){
               holder.mGiftImageView.setImageBitmap(BitmapFactory.decodeFile(gift.getPhotoPath()));
           }else{
               Drawable giftDrawable = mContext.getResources().getDrawable(R.drawable.gift);
               holder.mGiftImageView.setImageDrawable(giftDrawable);
           }

//           holder.mGiftDateTextView.setText(String.valueOf(gift.getDate()));
           holder.itemView.setTag(gift.getId());
       }
    }
    public int getItemCount() {
        if(gifts != null){
            return gifts.size();
        }
        return 0;
    }
    class GiftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mGiftNameTextView, mGiftPriceTextView, mGiftDateTextView;
               ImageView mGiftImageView;
        public GiftViewHolder(View itemView){
            super(itemView);
            mGiftDateTextView = itemView.findViewById(R.id.tv_gift_date);
            mGiftNameTextView = itemView.findViewById(R.id.tv_gift_name);
            mGiftPriceTextView = itemView.findViewById(R.id.tv_gift_price);
            mGiftImageView = itemView.findViewById(R.id.iv_gift_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(view,clickedPosition);
        }
    }
}
