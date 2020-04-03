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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

public class NestedPagerRecyclerViewActivity extends AppCompatActivity {

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
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        PageListener pageListener = new PageListener() {
            @Override
            public FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }
        };

        ImageCarouselAdapter adapter = new ImageCarouselAdapter(pageListener);

        recyclerView.setAdapter(adapter);

    }

    interface PageListener {
        FragmentManager getFragmentManager();
    }

    public static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class ImageCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int ITEM_COUNT = 3;

        PageListener pageListener;

        public ImageCarouselAdapter(PageListener pageListener) {
            this.pageListener = pageListener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == R.layout.layout_view_pager_view_holder) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_pager_view_holder, parent, false);
                return new ViewPagerViewHolder(view, pageListener);
            }

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_carousel_view_holder, parent, false);
            return new ImageCarouselViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewPagerViewHolder) {
                ((ViewPagerViewHolder) holder).bind();
            } else if (holder instanceof ImageCarouselViewHolder) {
                ((ImageCarouselViewHolder) holder).bind();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == ITEM_COUNT - 1) {
                return R.layout.layout_view_pager_view_holder;
            } else {
                return super.getItemViewType(position);
            }
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

    public static class ImageCarouselViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;

        public ImageCarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.carouselTitleTextView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }

        public void bind() {
            textView.setText(itemView.getContext().getResources().getString(R.string.title_carousel));
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(new ImageAdapter());
        }
    }

    public static class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_view_holder, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
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

    public static class ViewPagerViewHolder extends RecyclerView.ViewHolder {

        ViewPager viewPager;
        PageListener pageListener;

        public ViewPagerViewHolder(@NonNull View itemView, PageListener pageListener) {
            super(itemView);
            this.pageListener = pageListener;
            viewPager = itemView.findViewById(R.id.viewPager);
        }

        public void bind() {
            viewPager.setAdapter(new PagerAdapter(pageListener.getFragmentManager()));
        }
    }

    public static class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new PagerFragment();
        }

        @Override
        public int getCount() {
            return 10;
        }
    }

    public static class PagerFragment extends Fragment {
        RecyclerView recyclerView;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_pager, container, false);
            return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(new ImageAdapter());
        }
    }
}
