package com.memoworld.majama.User;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.memoworld.majama.R;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.MyViewHolder>  {

    private Context context;
    private List<String > tags;
    private InterestTagItemListener interestTagItemListener;


    public InterestAdapter(Context context, List<String> tags) {
        this.context = context;
        this.tags = tags;
        interestTagItemListener=(Interest)context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_single_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.interestTagText.setText(tags.get(position));
            holder.interestTagText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interestTagItemListener.onItemSelected(tags.get(position));
                }
            });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView interestTagText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            interestTagText=itemView.findViewById(R.id.name_text_box_singleItem);

        }
    }
    public void updateList(List<String> newTags){
        tags.clear();
        tags.addAll(newTags);
        notifyDataSetChanged();
    }
}
