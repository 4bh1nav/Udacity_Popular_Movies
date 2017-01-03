package abhi.com.popularmovies.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.MovieData;
import abhi.com.popularmovies.ui.activity.MovieDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ron on 16/10/16.
 */

public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.ViewHolder> {

    private static String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    private final List<MovieData> mItems;
    private final Fragment mFragment;

    public MoviesGridAdapter(Fragment fragment, List<MovieData> movies) {
        mFragment = fragment;
        mItems = movies;
    }

    @Override
    public MoviesGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesGridAdapter.ViewHolder holder, final int position) {

        final MovieData data = mItems.get(position);

        String title = data.getTitle();
        holder.title.setText(title);

        String posterPath = mItems.get(position).getPosterPath();
        String posterUrl = MOVIE_POSTER_BASE_URL.concat(posterPath);

        Picasso.with(mFragment.getContext()).load(posterUrl).into(holder.poster);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mFragment.getContext(),MovieDetailActivity.class);
                i.putExtra("data",data);
                mFragment.startActivity(i);
            }
        });
    }

    @NonNull
    public List<MovieData> getItems() {
        return mItems;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movies_poster) ImageView poster;
        @BindView(R.id.movie_title) TextView title;
        private final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);

        }
    }
}
