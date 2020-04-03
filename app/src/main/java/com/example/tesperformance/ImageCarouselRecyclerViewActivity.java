package com.example.tesperformance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ImageCarouselRecyclerViewActivity extends AppCompatActivity {

    public static long startTime;

    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_recycler_view);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        endTime = System.currentTimeMillis();
                        Log.d("DURATION_MS", String.valueOf(endTime - startTime));

                        //if below line is commented, you will see that every View is called 3 times for onGlobalLayout
                        //also. view will not get displayed before the third round finished
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        //to slow-motion this effect, uncomment sleep method call below
                        //sleep();
                    }
                });

        ImageCarouselAdapter adapter = new ImageCarouselAdapter();

        recyclerView.setAdapter(adapter);

    }

    static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class ImageCarouselAdapter extends RecyclerView.Adapter<ImageCarouselViewHolder> {

        AdapterListener adapterListener;

        @NonNull
        @Override
        public ImageCarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_carousel_view_holder, parent, false);
            adapterListener = new AdapterListener() {
                @Override
                public void onClick() {
                    notifyDataSetChanged();
                }
            };
            return new ImageCarouselViewHolder(view, adapterListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageCarouselViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    interface AdapterListener {
        void onClick();
    }

    static class ImageCarouselViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;
        AdapterListener adapterListener;

        public ImageCarouselViewHolder(@NonNull View itemView, AdapterListener adapterListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.carouselTitleTextView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            this.adapterListener = adapterListener;
        }

        public void bind() {
            textView.setText(itemView.getContext().getResources().getString(R.string.title_carousel));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTime = System.currentTimeMillis();
                    adapterListener.onClick();
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(new ImageAdapter());
        }
    }

    static class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_view_holder, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.bind();
            //when uncomment, it will increase 18 seconds in page load time
            //calculation: 3 visible horizontal carousel bind x 3 visible vertical parent carousel viewHolder x sleep 2 seconds
            // = 3 x 3 x 2s = 18s
            //thus, blocking code in nested viewHolder onBind() have multiplier effect to add page load time
            //sleep();
        }

        @Override
        public int getItemCount() {
            return 6;
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        String imageUrl = "https://icons-for-free.com/iconfiles/png/512/instagram+new+design+social+media+square+icon-1320184017120651958.png";

        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind() {
            Glide.with(itemView.getContext()).load(imageUrl).into(imageView);
        }
    }
}
