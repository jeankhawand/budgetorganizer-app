package com.example.budgetorganizer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetorganizer.data.DbHelper;
import com.example.budgetorganizer.data.Person;

import java.util.ArrayList;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private Context mContext;
    private ArrayList<Person> persons;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    final private ListItemClickListener mOnClickListener;
    public PersonAdapter(Context context, ListItemClickListener mOnClickListener){
        this.mContext = context;
        this.mOnClickListener = mOnClickListener;
        openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
        persons = DbHelper.getPersonsWithGiftBought(db);
    }
    @Override
    public PersonViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = persons.get(position);
        holder.mPersonTextView.setText(person.getName());
        holder.mPersonBudgetTextView.setText(person.getBudget()+"$");
        holder.mPersonGiftBoughtTextView.setText(person.getTotalGifBought()+" gift(s) bought");
        holder.itemView.setTag(person.getId());
    }
//    public void swapCursor(Cursor newCursor){
//        if (mCursor != null) mCursor.close();
//        mCursor = newCursor;
//        if(newCursor != null) this.notifyDataSetChanged();
//    }
    public int getItemCount() {
        return persons.size();
    }
    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView mPersonTextView, mPersonBudgetTextView, mPersonGiftBoughtTextView;
    public PersonViewHolder(View itemView){
        super(itemView);
        mPersonTextView = itemView.findViewById(R.id.tv_gift_name);
        mPersonBudgetTextView = itemView.findViewById(R.id.tv_gift_price);
        mPersonGiftBoughtTextView = itemView.findViewById(R.id.tv_person_gifts_bought);
        itemView.setOnClickListener(this);
    }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(view,clickedPosition);
        }
    }
}
