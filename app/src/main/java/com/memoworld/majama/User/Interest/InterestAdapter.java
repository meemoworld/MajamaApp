package com.memoworld.majama.User.Interest;

import android.content.Context;
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

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.MyViewHolder> {

    private final Context context;
    private final List<EachTag> tags;
    private final InterestTagItemListener interestTagItemListener;
    private static int index = 0;

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
        int currentIndex=index;
        holder.interestTagText.setText(tags.get(currentIndex).getTagName());
        Glide.with(context).load(tags.get(index).getImageUrl()).into(holder.imageView);

        holder.interestTagText1.setText(tags.get(currentIndex + 1).getTagName());
        Glide.with(context).load(tags.get(index + 1).getImageUrl()).into(holder.imageView1);

        holder.interestTagText2.setText(tags.get(currentIndex + 2).getTagName());
        Glide.with(context).load(tags.get(index + 2).getImageUrl()).into(holder.imageView2);

        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestTagItemListener.onItemSelected(tags.get(currentIndex).getTagName());
            }
        });
        holder.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestTagItemListener.onItemSelected(tags.get(currentIndex + 1).getTagName());
            }
        });
        holder.layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interestTagItemListener.onItemSelected(tags.get(currentIndex + 2).getTagName());
            }
        });
        index+=3;
    }

    @Override
    public int getItemCount() {
        return tags.size()/3;
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
