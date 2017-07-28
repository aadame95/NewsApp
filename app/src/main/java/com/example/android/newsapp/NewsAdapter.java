package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.data.Contract;
import com.squareup.picasso.Picasso;


/**
 * Created by Andres on 6/28/17.
 */



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder>{

    private Cursor cursor;
    private ItemClickListener listener;
    private Context context;
    public static final String TAG = "newsAdapter";


    public NewsAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Cursor cursor, int clickedItemIndex);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.item_list, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    //since its no longer a array we dont use .size instead we use .getCount() to get the size
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView publish;
        ImageView image;

        ItemHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            description = (TextView)view.findViewById(R.id.description);
            publish = (TextView) view.findViewById(R.id.publish);
            image = (ImageView) view.findViewById(R.id.image);

            view.setOnClickListener(this);
        }

        //sets all the data per row to its specific location on the recycler view
        public void bind(int pos){
            cursor.moveToPosition(pos);

            title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION)));
            publish.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED_DATE)));

            String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URL_TO_IMAGE));

            //uses picasso to set up image received from json
            if(url != null){
                Picasso.with(context).load(url).into(image);
            }

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor, pos);
        }
    }



}




