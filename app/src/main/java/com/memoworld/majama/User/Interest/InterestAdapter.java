package com.memoworld.majama.User.Interest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.memoworld.majama.AllModals.EachTag;
import com.memoworld.majama.R;

import java.util.List;

import static com.memoworld.majama.MainActivityFragments.Suggestion.TAG;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.MyViewHolder> {

    private final Context context;
    private final List<EachTag> tags;
    private final InterestTagItemListener interestTagItemListener;

    public InterestAdapter(Context context, List<EachTag> tags) {
        this.context = context;
        this.tags = tags;
        interestTagItemListener = (Interest) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_single_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int index=position*3;
        if(index<tags.size()) {
            holder.interestTagText.setText(tags.get(index).getTagName());
            Glide.with(context).load(tags.get(index).getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView);
        }else{
            holder.imageView.setVisibility(View.GONE);
            holder.interestTagText.setVisibility(View.GONE);
            holder.layout1.setClickable(false);
        }
//        Log.i(TAG, "onBindViewHolder: after 1 : "+index);
        if(index+1<tags.size()) {
            holder.interestTagText1.setText(tags.get(index + 1).getTagName());
            Glide.with(context).load(tags.get(index + 1).getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView1);
        }
        else{
            holder.imageView1.setVisibility(View.GONE);
            holder.interestTagText1.setVisibility(View.GONE);
            holder.layout2.setClickable(false);
        }
//        Log.i(TAG, "onBindViewHolder:  after 2: "+index);
        if(index+2<tags.size())
        {
            holder.interestTagText2.setText(tags.get(index + 2).getTagName());
            Glide.with(context).load(tags.get(index + 2).getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView2);
        }
        else{
            holder.imageView2.setVisibility(View.GONE);
            holder.interestTagText2.setVisibility(View.GONE);
            holder.layout3.setClickable(false);
        }
        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestTagItemListener.onItemSelected(tags.get(index).getTagName());
            }
        });
        holder.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestTagItemListener.onItemSelected(tags.get(index + 1).getTagName());
            }
        });
        holder.layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestTagItemListener.onItemSelected(tags.get(index + 2).getTagName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size()/3+1;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView interestTagText;
        private final TextView interestTagText1;
        private final TextView interestTagText2;
        private final ImageView imageView;
        private final ImageView imageView1;
        private final ImageView imageView2;
        private final ConstraintLayout layout1;
        private final ConstraintLayout layout2;
        private final ConstraintLayout layout3;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            interestTagText = itemView.findViewById(R.id.name_text_box_singleItem);
            interestTagText1 = itemView.findViewById(R.id.name_text_box_singleItem1);
            interestTagText2 = itemView.findViewById(R.id.name_text_box_singleItem2);
            imageView = itemView.findViewById(R.id.Image);
            imageView1 = itemView.findViewById(R.id.Image1);
            imageView2 = itemView.findViewById(R.id.Image2);

            layout1 = itemView.findViewById(R.id.constraint1);
            layout2 = itemView.findViewById(R.id.constraint2);
            layout3 = itemView.findViewById(R.id.constraint3);

        }
    }

    public void updateList(List<EachTag> newTags) {
        tags.clear();
        tags.addAll(newTags);
        notifyDataSetChanged();
    }

}
