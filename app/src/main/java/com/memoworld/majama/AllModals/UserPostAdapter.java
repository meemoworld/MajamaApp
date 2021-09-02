//package com.memoworld.majama.AllModals;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.memoworld.majama.R;
//
//import java.util.List;
//
//public class UserPostAdapter {
//    private final Context context;
//    private final List<Post> tags;
////    private final InterestTagItemListener interestTagItemListener;
//
//    public UserPostAdapter(Context context, List<Post> posts) {
//        this.context = context;
//        this.posts = posts;
////        interestTagItemListener = (Interest) context;
//
//    }
//
//    @NonNull
//    public UserPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_single_item_layout, parent, false);
//        return new UserPostAdapter.MyViewHolder(view);
//    }
//
//    public void onBindViewHolder(@NonNull UserPostAdapter.MyViewHolder holder, int position) {
//        final int index = position * 3;
//        if (index < posts.size()) {
//            holder.interestTagText.setText(posts.get(index).getTagName());
//            Glide.with(context).load(posts.get(index).getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView);
//        } else {
//            holder.imageView.setVisibility(View.INVISIBLE);
//            holder.interestTagText.setVisibility(View.INVISIBLE);
//            holder.layout1.setVisibility(View.INVISIBLE);
//        }
////        Log.i(TAG, "onBindViewHolder: after 1 : "+index);
//        if (index + 1 < posts.size()) {
//            holder.interestTagText1.setText(posts.get(index + 1).getTagName());
//            Glide.with(context).load(posts.get(index + 1).getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView1);
//        } else {
//            holder.imageView1.setVisibility(View.INVISIBLE);
//            holder.interestTagText1.setVisibility(View.INVISIBLE);
//            holder.layout2.setVisibility(View.INVISIBLE);
//        }
////        Log.i(TAG, "onBindViewHolder:  after 2: "+index);
//        if (index + 2 < tags.size()) {
//            holder.interestTagText2.setText(tags.get(index + 2).getTagName());
//            Glide.with(context).load(tags.get(index + 2).getImageUrl()).placeholder(R.drawable.ruko_jara).into(holder.imageView2);
//        } else {
//            holder.imageView2.setVisibility(View.INVISIBLE);
//            holder.interestTagText2.setVisibility(View.INVISIBLE);
//            holder.layout3.setVisibility(View.INVISIBLE);
//        }
////        holder.layout1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                interestTagItemListener.onItemSelected(tags.get(index).getTagName());
////            }
////        });
////        holder.layout2.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                interestTagItemListener.onItemSelected(tags.get(index + 1).getTagName());
////            }
////        });
////        holder.layout3.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                interestTagItemListener.onItemSelected(tags.get(index + 2).getTagName());
////            }
////        });
//    }
//
//    public int getItemCount() {
//        return tags.size() / 3 + 1;
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView interestTagText;
//        private final TextView interestTagText1;
//        private final TextView interestTagText2;
//        private final ImageView imageView;
//        private final ImageView imageView1;
//        private final ImageView imageView2;
//        private final ConstraintLayout layout1;
//        private final ConstraintLayout layout2;
//        private final ConstraintLayout layout3;
//
//        public MyViewHolder(@NonNull View itemView) {
//
//            super(itemView);
//            interestTagText = itemView.findViewById(R.id.name_text_box_singleItem);
//            interestTagText1 = itemView.findViewById(R.id.name_text_box_singleItem1);
//            interestTagText2 = itemView.findViewById(R.id.name_text_box_singleItem2);
//            imageView = itemView.findViewById(R.id.Image);
//            imageView1 = itemView.findViewById(R.id.Image1);
//            imageView2 = itemView.findViewById(R.id.Image2);
//
//            layout1 = itemView.findViewById(R.id.constraint1);
//            layout2 = itemView.findViewById(R.id.constraint2);
//            layout3 = itemView.findViewById(R.id.constraint3);
//
//        }
//    }
//
//
//}