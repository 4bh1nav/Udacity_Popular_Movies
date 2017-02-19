package abhi.com.popularmovies.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.Review;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ron on 10/02/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private final Fragment mFragment;
    private final List<Review> mItems;

    public ReviewsAdapter(Fragment fragment, List<Review> reviews) {
        mFragment = fragment;
        mItems = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);

        return new ReviewsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        Review review = mItems.get(position);
        holder.review.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.review) TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
