package com.example.tesperformance;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SingleTextViewActivity extends AppCompatActivity {

    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_text_view);

        final TextView textView = findViewById(R.id.textView);

        textView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        endTime = System.currentTimeMillis();
                        Log.d("DURATION_MS", String.valueOf(endTime - startTime));
                        textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }
}
