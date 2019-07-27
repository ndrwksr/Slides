package com.ndrwksr.slides_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ndrwksr.slides.SlideListItem;
import com.ndrwksr.slides.SlideLoader;
import com.ndrwksr.slides.slideFragments.SlideFragment;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        SlideLoader slideLoader = new SlideLoader(getApplicationContext());
        recyclerView.setAdapter(new SlideListItemRecyclerViewAdapter(this, slideLoader.getSlideList(), mTwoPane));
    }

    public static class SlideListItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SlideListItemRecyclerViewAdapter.ViewHolder> {

        private final List<SlideListItem> mValues;
        private final SlideListActivity mParentActivity;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlideListItem slideListItem = (SlideListItem) view.getTag();
                if (mTwoPane) {
                    SlideFragment fragment = SlideFragment.createFromSlideListItem(slideListItem);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.slide_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SlideDetailActivity.class);
                    intent.putExtra(SlideListItem.INTENT_NAME, slideListItem);
                    context.startActivity(intent);
                }
            }
        };

        SlideListItemRecyclerViewAdapter(SlideListActivity parent,
                                         List<SlideListItem> items,
                                         boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slide_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getSlideTitle());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
            }
        }
    }
}
