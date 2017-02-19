package abhi.com.popularmovies.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.Favorite;
import abhi.com.popularmovies.data.model.Movie;
import abhi.com.popularmovies.rest.RetrofitService;
import abhi.com.popularmovies.ui.activity.FavDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ron on 12/02/17.
 */



public class FavoriteAdapter extends RealmRecyclerViewAdapter<Favorite,FavoriteAdapter.ViewHolder>{

    private static String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private final Context mContext;
    private final RetrofitService.movieApiInterface mMovieApi;
    private static final String API_KEY = "" ;
    private final OrderedRealmCollection<Favorite> mData;
    private Movie mMovieData;


    public FavoriteAdapter(@NonNull Context context, OrderedRealmCollection<Favorite> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
        mContext = context;
        mMovieApi = RetrofitService.getClient();
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item,parent,false);
        return new FavoriteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Favorite favorite = mData.get(position);
        long id = favorite.getId();

        mMovieApi.getMovie(id,API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                mMovieData = response.body();

                String title = mMovieData.getOriginalTitle();
                holder.title.setText(title);

                String posterPath = mMovieData.getPosterPath();
                String posterUrl = MOVIE_POSTER_BASE_URL.concat(posterPath);

                Picasso.with(mContext).load(posterUrl).into(holder.poster);


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext,FavDetailActivity.class);
                        i.putExtra("data",mMovieData);
                        mContext.startActivity(i);
                    }
                });

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    public OrderedRealmCollection<Favorite> getAllIteams(){
        return mData;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.movies_poster)
        ImageView poster;
        @BindView(R.id.movie_title)
        TextView title;
        private final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);

        }
    }
}
