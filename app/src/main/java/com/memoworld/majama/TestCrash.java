package com.memoworld.majama;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.memoworld.majama.AllModals.EachTag;
import com.memoworld.majama.AllModals.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCrash extends AppCompatActivity {

    private static final String TAG = "TestCrash";
    private final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Tag").document("tag");
    private Tag tag;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<EachTag> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_crash);

        tags = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_test_crash);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tag = documentSnapshot.toObject(Tag.class);
                if (tag == null) {
                    Log.d(TAG, "onSuccess: NULL");
                    return;
                }
                for (Map.Entry<String, String> entry : tag.getTagmap().entrySet()) {
                    tags.add(new EachTag(entry.getKey(), entry.getValue()));
                }
                adapter = new RecyclerViewAdapter(TestCrash.this, tags);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private final Context context;
        private final List<EachTag> tags;

        public RecyclerViewAdapter(Context context, List<EachTag> tags) {
            this.context = context;
            this.tags = tags;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_post_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Glide.with(context).load(tags.get(position).getImageUrl()).placeholder(R.drawable.coder)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return tags.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_post_image_single_view);
        }
    }
}