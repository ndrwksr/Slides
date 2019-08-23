package com.ndrwksr.slides_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ndrwksr.slides_app.slides.CustomSlide;
import com.ndrwksr.slideslib.SlideLoader;
import com.ndrwksr.slideslib.exceptions.BadSlideTypeException;
import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;
import com.ndrwksr.slideslib.exceptions.BadSlidesException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SlideDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SlideListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private final SlideLoader mSlideLoader = new SlideLoader();

    public SlideListActivity() {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = new HashMap<>();
        typeTokenMap.put(CustomSlide.TYPE_STRING, new TypeToken<CustomSlide>() {
        });
        try {
            mSlideLoader.registerSlideTypes(typeTokenMap);
        } catch (BadSlideTypeException e) {
            Log.e(SlideLoader.PARSE_EXCEPTION_TAG, "Couldn't register custom slide type", e);
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.slide_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(getApplicationContext().getAssets().open("slides.json"));
            recyclerView.setAdapter(new SlideRecyclerViewAdapter(this, mSlideLoader.getSlides(reader), mTwoPane));
        } catch (IOException | BadSlidesException e) {
            Log.e(SlideLoader.PARSE_EXCEPTION_TAG, "Couldn't open slides file", e);
        }
    }

    public static class SlideRecyclerViewAdapter
            extends RecyclerView.Adapter<SlideRecyclerViewAdapter.ViewHolder> {

        private final List<Slide> mValues;
        private final SlideListActivity mParentActivity;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(@NonNull final View view) {
                Slide slide = (Slide) view.getTag();
                if (mTwoPane) {
                    SlideFragment fragment = slide.getFragment();
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.slide_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SlideDetailActivity.class);
                    intent.putExtra(Slide.INTENT_NAME, slide);
                    context.startActivity(intent);
                }
            }
        };

        SlideRecyclerViewAdapter(
                @NonNull final SlideListActivity parent,
                @NonNull final List<Slide> items,
                final boolean twoPane
        ) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(
                @NonNull final ViewGroup parent,
                final int viewType
        ) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slide_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(
                @NonNull final ViewHolder holder,
                final int position
        ) {
            holder.mIdView.setText(mValues.get(position).getTitle());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(@NonNull final View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
            }
        }
    }
}
