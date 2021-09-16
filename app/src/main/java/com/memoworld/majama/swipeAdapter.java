package com.memoworld.majama;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class swipeAdapter extends RecyclerView.Adapter<swipeAdapter.swipeViewHolder>{

    private final Context context;
    private ArrayList<String> name;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public swipeAdapter(Context context, ArrayList<String> name) {
        this.context = context;
        this.name = name;
    }

    public void setName(ArrayList<String> name){
        this.name = new ArrayList<>();
        this.name = name;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public swipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_single , parent , false);

        return new swipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull swipeAdapter.swipeViewHolder holder, int position) {

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout , String.valueOf(name.get(position)));
        viewBinderHelper.closeLayout(String.valueOf(name.get(position)));
        holder.bindData(name.get(position));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    class swipeViewHolder extends RecyclerView.ViewHolder{
        private TextView page;
        private TextView swipePage;
        private TextView confirm;
        private TextView delete;
        private SwipeRevealLayout swipeRevealLayout;

        public swipeViewHolder(@NonNull View itemView) {
            super(itemView);

            page = itemView.findViewById(R.id.pages);
            swipePage = itemView.findViewById(R.id.pagesSwipe);
            confirm = itemView.findViewById(R.id.confirm);
            delete = itemView.findViewById(R.id.reject);
            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);



        }

        void bindData(String s){
        }
    }


}
